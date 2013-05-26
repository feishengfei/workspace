package com.navchina.bdi;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.navchina.bdi.event.BDI_Event;
import com.navchina.bdi.event.BDI_Login;
import com.navchina.bdi.event.BDI_LoginResp;
import com.navchina.bdi.thread.BDI_RTDataThread;
import com.navchina.bdi.thread.BDI_SubmitThread;
import com.navchina.bdi.thread.BDI_Thread;

public class BDI_Daemon implements Runnable, IDaemon
{
	private BlockingQueue<BDI_Event> rtdq = null;
	private BlockingQueue<BDI_Event> subq = null;
	private String usr = "";
	private String pwd = "";
	private BDI_RTDataThread rtdThread = null;

	// private BDI_SubmitThread subThread = null;

	public BDI_Daemon()
	{
		super();
		rtdq = new LinkedBlockingQueue<BDI_Event>();
		subq = new LinkedBlockingQueue<BDI_Event>();
		rtdThread = new BDI_RTDataThread(this);
	}

	@Override
	public boolean matchResponse(BDI_Event e, BDI_Event e_resp)
	{
		if (e.getType() == BDI_Event.REGISTER
				&& e_resp.getType() == BDI_Event.REDIRECT_RESP) {
			return true;
		}

		if (e.getType() == BDI_Event.LOGIN
				&& e_resp.getType() == BDI_Event.LOGIN_RESP) {
			return true;
		}

		if (e.getType() == BDI_Event.LOGOUT
				&& e_resp.getType() == BDI_Event.LOGIN_RESP) {
			return true;
		}

		if (e.getType() == BDI_Event.RTDATA
				&& e_resp.getType() == BDI_Event.RTDATA_RESP) {
			return true;
		}

		return false;
	}

	@Override
	public synchronized boolean sendEvent(BDI_Event e, Socket sock)
	{
		if (null == e || null == sock)
			return false;

		byte[] send_buf = e.encode();

		BDI_Util.getLogger().info(
				"[" + BDI_Util.nowStr() + sock.getInetAddress() + "<--]");
		BDI_Util.getLogger().info(e.toString());

		try {
			OutputStream out = sock.getOutputStream();
			out.write(send_buf);
			//BDI_Util.dumpByteArray(send_buf);
			return true;
		}
		catch (IOException e1) {
			System.err.println(e1.getMessage());
			return false;
		}
	}

	@Override
	public synchronized BDI_Event recvEvent(Socket sock) throws IOException
	{
		if (null == sock) {
			return null;
		}

		InputStream in = null;
		in = sock.getInputStream();

		// read header
		byte[] headBuf = new byte[BDI.FRAME_HEADER_LEN];
		in.read(headBuf);

		// get frlen
		DataInputStream din = new DataInputStream(new ByteArrayInputStream(
				headBuf));
		BDI_Decoder dec = new BDI_Decoder(din);
		long frlen = dec.readUInt();

		// new buf(length = frlen )
		byte[] frame = new byte[(int) frlen];
		System.arraycopy(headBuf, 0, frame, 0, BDI.FRAME_HEADER_LEN);

		// read remaining part
		for (int i = 0; i < frlen-BDI.FRAME_HEADER_LEN; i++) {
			frame[i + BDI.FRAME_HEADER_LEN] = (byte) in.read();
		}

		BDI_Event e = BDI_Event.decode(frame);
		
		// log
		BDI_Util.getLogger().info(
				"[" + BDI_Util.nowStr() + sock.getInetAddress() + "-->]");
		BDI_Util.getLogger().info(e.toString());
		//BDI_Util.dumpByteArray(frame);

		return e;
	}

	@Override
	public BDI_Event doSubmit(Socket sock, BDI_Event req) throws IOException
	{
		if (!sendEvent(req, sock)) {
			return null;
		}
		boolean replyDone = false;

		BDI_Event eRcv = null;
		while (!replyDone) {
			eRcv = recvEvent(sock);

			if (matchResponse(req, eRcv)) {
				replyDone = true;
			}
		}
		return eRcv;
	}

	@Override
	public Socket doConnect(int channel) throws UnknownHostException,
			IOException
	{
		switch (channel) {
			case BDI_Thread.RT_PEER:
				return new Socket(IDaemon.RTS_HOST, IDaemon.RTS_PORT);
			case BDI_Thread.RR_PEER:
				return new Socket(IDaemon.RRS_HOST, IDaemon.RRS_PORT);
			default:
				BDI_Util.getLogger().warning("doConnect fail!!!");
				return null;
		}
	}

	@Override
	public Socket doLogin(int channel) throws UnknownHostException, IOException
	{
		int errCode;
		BDI_Event resp;
		Socket sock = doConnect(channel);

		BDI_Login login = new BDI_Login();
		synchronized (usr) {
			login.setUsr(usr);
		}
		synchronized (pwd) {
			login.setPwd(pwd);
		}
		login.setSn(BDI_Util.getSerialNo());
		login.setTs(new Date().getTime() / 1000);

		while (true) {
			resp = doSubmit(sock, login);

			if (BDI_Event.LOGIN_RESP != resp.getType()) {
				sock.close();
				continue;
			}

			BDI_LoginResp loginResp = new BDI_LoginResp(resp.encode());
			try {
				rtdq.put(loginResp);
			}
			catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
			errCode = loginResp.getErrCode();
			break;
		}

		// buffer a man made response event
		if (null == resp) {
			resp = new BDI_LoginResp(errCode);
			System.err.println("+++ " + resp.toString());
			try {
				rtdq.put(resp);
			}
			catch (InterruptedException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}

		return sock;
	}

	@Override
	public void doLogout(Socket sock) throws IOException
	{
		BDI_Event resp;
		BDI_Event logout = new BDI_Event(BDI_Event.LOGOUT);
		logout.setSn(BDI_Util.getSerialNo());
		doSubmit(sock, logout);
	}

	public synchronized void setUsr(String usr)
	{
		synchronized (this.usr) {
			this.usr = usr;
		}
	}

	public synchronized void setPwd(String pwd)
	{
		synchronized (this.pwd) {
			this.pwd = pwd;
		}
	}

	public synchronized BDI_RTDataThread getRtdThread()
	{
		return rtdThread;
	}

	public synchronized void setRtdThread(BDI_RTDataThread rtdThread)
	{
		this.rtdThread = rtdThread;
	}

	public synchronized BlockingQueue<BDI_Event> getRtdq()
	{
		return rtdq;
	}

	public synchronized BlockingQueue<BDI_Event> getSubq()
	{
		return subq;
	}
	
	@Override
	public void run()
	{
		rtdThread.start();
	}
	
	public BDI_Event getRTData(){
		BDI_Event res = null;
		try {
			res = rtdq.poll(100, TimeUnit.MILLISECONDS);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return res;
	}
	

	public static void main(String[] args)
	{
		// testRegister();
		// testLogin01();
		// testLogin02();
		BDI_Daemon d = new BDI_Daemon();
		d.setUsr("98000827");
		d.setPwd("1");
		new Thread(d).start();
	}

	/**
	 * 测试分别从长连接和短连接处登录
	 */
	private static void testLogin02()
	{
		BDI_Daemon d = new BDI_Daemon();
		d.setUsr("98000001");
		d.setPwd("123456");
		try {
			d.doLogin(BDI_Thread.RT_PEER);
			d.doLogin(BDI_Thread.RR_PEER);

			BDI_LoginResp resp = new BDI_LoginResp(d.getRtdq().take().encode());
			// BDI_Util.dumpByteArray(resp.encode());
			resp = new BDI_LoginResp(d.getRtdq().take().encode());
			// BDI_Util.dumpByteArray(resp.encode());
		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testRegister()
	{

	}

	public static void testLogin01()
	{
		try {
			Socket sock = new Socket("124.192.56.253", 9020);

			BDI_Login e = new BDI_Login();
			e.setUsr("98000827");
			e.setPwd("1");
			e.setTs(new Date().getTime() / 1000);

			BDI_Daemon daemon = new BDI_Daemon();
			daemon.sendEvent(e, sock);
			BDI_Event re = daemon.recvEvent(sock);
		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
