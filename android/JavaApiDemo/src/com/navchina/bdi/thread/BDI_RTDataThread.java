package com.navchina.bdi.thread;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.navchina.bdi.BDI_Daemon;
import com.navchina.bdi.BDI_Util;
import com.navchina.bdi.event.BDI_ActiveTest;
import com.navchina.bdi.event.BDI_Event;
import com.navchina.bdi.event.rtdata.BDI_RTData;

public class BDI_RTDataThread extends BDI_Thread
{
	private Socket sock = null;
	private static final int RT_FULLHEALTH = 3;

	public BDI_RTDataThread(BDI_Daemon d)
	{
		super(d);
	}

	@Override
	public void run()
	{
		int health = RT_FULLHEALTH;
		while (health >= 0) {
			// connect
			try {
				sock = d.doLogin(BDI_Thread.RT_PEER);
			}
			catch (UnknownHostException e) {
				health = -1; // thread quit
			}
			catch (IOException e) {
				health = 0;
			}
			

			// update health
			if (null != sock)
				health = RT_FULLHEALTH;

			// listen from fos
			while (health > 0) {

				// set read timeout
				int timeout = (health == RT_FULLHEALTH ? 55 : 10) * 1000;
				try {
					sock.setSoTimeout(timeout);
				}
				catch (SocketException e) {
					health = 0;
				}
				BDI_Event eRecv = null;

				// recv a event
				try {
					eRecv = d.recvEvent(sock);
				}
				catch (IOException e) {
					// timeout to check link
					if (--health > 0) {
						BDI_ActiveTest linkHold = new BDI_ActiveTest();
						linkHold.setSn(BDI_Util.getSerialNo());
						d.sendEvent(linkHold, sock);
					}
				}

				if (null != eRecv) {
					// make a response and send it
					BDI_Event ack = null;
					boolean responseDone = false;
					boolean isRedirectInd = false;
					switch ((int) eRecv.getType()) {
						case BDI_Event.REDIRECT_IND:
							ack = new BDI_Event(BDI_Event.REDIRECT_RESP);
							isRedirectInd = true;
							break;
						case BDI_Event.RTDATA:
							ack = new BDI_RTData(eRecv.encode()).ack();
						default:
							break;
					}
					if (null != ack) {
						ack.setSn(eRecv.getSn());
						responseDone = d.sendEvent(ack, sock);
					}
					if (responseDone || isRedirectInd) {
						d.getRtdq().add(eRecv);
					}
					health = isRedirectInd ? -1 : RT_FULLHEALTH;
				}// if(null != eRecv)

			}// while(health>0)

			// prepare to relogin
			if (null != sock) {
				try {
					sock.close();
					BDI_Util.getLogger().info("sock.close()");
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				sock = null;
			}

			if (health == 0) {
				try {
					sleep(30 * 1000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}// while(health>=0)
		
	}

}
