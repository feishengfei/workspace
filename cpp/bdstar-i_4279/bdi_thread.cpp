#include <stdio.h>
#include <unistd.h>
#include <errno.h>
#include <assert.h>
#include <sys/sysinfo.h> 

#include "rbglobal.h"
#include "debug_alloc.h"
#include "bdi_event.h"
#include "bdi_event_list.h"
#include "bdi_unix_stream.h"
#include "bdi_daemon.h"
#include "bdi_thread.h"

using namespace BDI;

#define BDI_RT_FULLHEALTH 3

void *bdi_thread_func(void *arg)
{
    // set thread cancellation
    pthread_setcancelstate(PTHREAD_CANCEL_ENABLE, NULL);
	pthread_setcanceltype(PTHREAD_CANCEL_DEFERRED, NULL);

	// waiting for the status flags be ready
	usleep(100 * 1000); 

	BDI_Thread *thread = reinterpret_cast<BDI_Thread *>(arg);
	thread->run();
	thread->_running = 0;
	thread->_finished = 0;

	pthread_exit(NULL);
}

// BDI_Thread members

void BDI_Thread::onStopped()
{
	// by default do nothing
}

BDI_Thread::BDI_Thread(BDI_Daemon *d)
{
	_tid = static_cast<pthread_t>(0);
	_d = d;
	_running = 0;
	_finished = 1;
    _busy = 0;
}

BDI_Thread::~BDI_Thread()
{
}

void BDI_Thread::start()
{
	if (_running || !_finished)
		wait();

	if (pthread_create(&_tid, NULL, bdi_thread_func, this) != 0) {
		_tid = static_cast<pthread_t>(0);
		_running = 0;
		_finished = 1;
		RB_WARNING("bdi|pthread_create(): %s", strerror(errno));
	}
	else {
		_running = 1;
		_finished = 0;
	}
}

void BDI_Thread::terminate()
{
	if (_running) {
		pthread_cancel(_tid);
		_running = 0;
	}
}

void BDI_Thread::wait()
{
	if (!_finished) {
		assert(_tid != static_cast<pthread_t>(0));
        RB_DEBUG("Waiting bdi-thread... (%ld)", static_cast<long>(_tid));
		pthread_join(_tid, NULL);
		onStopped();
		RB_DEBUG("bdi-thread stopped! (%ld)", static_cast<long>(_tid));
        _tid = static_cast<pthread_t>(0);
        _finished = 1;
        _busy = 0;
    }
}

// BDI_RTDataThread members

BDI_RTDataThread::BDI_RTDataThread(BDI_Daemon *d)
    : BDI_Thread(d)
{
    _sockfd = -1;
}

void BDI_RTDataThread::run()
{
	int health = BDI_RT_FULLHEALTH;

	RB_DEBUG("rtdata-thread start running... (%ld)", static_cast<long>(_tid));

	while (health >= 0) {
		int ret = _d->doLogin(BDI_Thread::RT_PEER, &_sockfd);
		switch (ret) {
		case 0:
			health = BDI_RT_FULLHEALTH;
			break;
		case LOGIN_SER_REDIRECT:
		case LOGIN_INVALID_USR:
		case LOGIN_AUTHENTICATION_FAIL:
        case LOGIN_TIME_OUTERSYNC:
			health = -1; // thread quit
			break;
		default:
			health = 0; // relogin
			break;
		}

        while (health > 0) {
            struct timeval timeout;
            timeout.tv_sec = (health == BDI_RT_FULLHEALTH ? 55 : 10);
            timeout.tv_usec = 0;
            if (_d->dlink()->wait(_sockfd, &timeout)) {
                _busy = 1;
                // receiving a event
                BDI_EventRef eRcv = _d->recvEvent(_sockfd);
                if (eRcv.isNull()) {
                    health = 0;
                    break;
                }

                // makes a response event and send it
                BDI_EventRef acke;
                bool responseDone = false;
                bool isRedirectInd = false;
                switch (eRcv->type()) {
                case BDI_Event::RedirectInd:
                    acke = NEW BDI_Event(BDI_Event::RedirectResp);
                    isRedirectInd = true;
                    break;
                case BDI_Event::RTData:
                    acke = reinterpret_cast<BDI_RTData *>(eRcv.getPtr())->ack();
                    break;
                default:
                    break;
                }
                
                if (!acke.isNull()) {
                    acke->setSn(eRcv->sn());
                    responseDone = _d->sendEvent(_sockfd, acke.getPtr());
                }

				if (isRedirectInd || responseDone)
					_d->rtdataList()->addEvent(eRcv);

				health = isRedirectInd ? -1 : BDI_RT_FULLHEALTH;
				_busy = 0;
			}
			// timeout to check link
			else if (--health > 0) {
				BDI_ActiveTest linkHold;
				linkHold.setSn(getNextSerialNo());
				if (!_d->sendEvent(_sockfd, &linkHold))
					health = 0;
			}
		}

        _busy = 0;

        // prepare to relongin
        if (_sockfd != -1) {
            _d->dlink()->close(_sockfd);
            _sockfd = -1;
        }

		if (health == 0)
			sleep(30);
	}
}

void BDI_RTDataThread::onStopped()
{
    BDI_Thread::onStopped();

    if (_sockfd != -1) {
        //_d->doLogout(_sockfd);
        _d->dlink()->close(_sockfd);
        _sockfd = -1;
    }
}

// BDI_SubmitThread members

BDI_SubmitThread::BDI_SubmitThread(BDI_Daemon *d)
    : BDI_Thread(d)
{
    _sockfd = -1;
}

void BDI_SubmitThread::run()
{
	int ret = 0;
	const BDI_EventListItem *item = NULL;

	RB_DEBUG("submit-thread start running... (%ld)", static_cast<long>(_tid));

	for (;;) {
        BDI_EventRef sube;
		BDI_EventRef repe;
		DBusMessage *msg = NULL;

		item = _d->submitList()->getFreeEvent(_tid);
		if (item == NULL)
			continue;

        sube = item->event();
        msg = reinterpret_cast<DBusMessage *>(item->data());

        /* TODO: What to do with the obsoleted event?
		if (msg != NULL && time(NULL) - item->bornTime() > 180) {
            ret = SES_EVENT_OBSOLETED;
            goto CLEAR_AND_NEXT;
        }
        */

		_busy = 1;
		
		if ( BDI_Event::Register == sube.getPtr()->type() ) {	//register mode
			//connect
			ret = _d->doConnect(BDI_Thread::RR_PEER, &_sockfd);	
			if (ret != 0) {
				goto CLEAR_AND_NEXT;
			}
			//submit	
			ret = _d->doSubmit(_sockfd, sube.getPtr(), repe);
			if (ret == LOGIN_SER_REDIRECT)
				break; // to exit thread
			else if (ret != 0)
				goto CLEAR_AND_NEXT;
		}
		else {													//normal mode
			//login
			ret = _d->doLogin(BDI_Thread::RR_PEER, &_sockfd);

			if (ret == LOGIN_SER_REDIRECT
					|| ret == LOGIN_INVALID_USR 
					|| ret == LOGIN_AUTHENTICATION_FAIL 
					|| ret == LOGIN_TIME_OUTERSYNC)
				break; // to exit thread
			else if (ret != 0)
				goto CLEAR_AND_NEXT;
			//submit
			ret = _d->doSubmit(_sockfd, sube.getPtr(), repe);
			if (ret == LOGIN_SER_REDIRECT)						//XXX is redirect possible?
				break; // to exit thread
			else if (ret != 0)
				goto CLEAR_AND_NEXT;

			// logout
			_d->doLogout(_sockfd);
		}


CLEAR_AND_NEXT:
        // close connection
        if (_sockfd != -1) {
            _d->dlink()->close(_sockfd);
            _sockfd = -1;
        }

		if ( BDI_Event::Register == sube.getPtr()->type() ) {	//register mode
			if (repe.isNull() || repe->type() != BDI_Event::RegisterResp){
				repe = NEW BDI_ErrorEvent(ret, sube->sn(), sube->bid(), sube->pid());
			}
		}
		else {													//normal mode
			if (repe.isNull() || repe->type() != BDI_Event::RTDataResp){
				repe = NEW BDI_ErrorEvent(ret, sube->sn(), sube->bid(), sube->pid());
			}
        }

        if (!repe.isNull()) {
            RB_DEBUG("+++ %s", repe->toString().c_str());
		    _d->rtdataList()->addEvent(repe, msg);
        }

		_d->submitList()->removeEvent(_tid, &item);

        _busy = 0;

		if (ret != 0 && ret != SES_EVENT_OBSOLETED)
			sleep(2);
	}

	_busy = 0;
}

void BDI_SubmitThread::onStopped()
{
    BDI_Thread::onStopped();

    if (_sockfd != -1) {
        _d->dlink()->close(_sockfd);
        _sockfd = -1;
    }
}
