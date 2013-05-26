package com.navchina.bdi;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.navchina.bdi.event.BDI_Event;

/**
 * @author xzhou
 * 
 */
public interface IDaemon
{
	public static final String RTS_HOST = "124.192.56.253";
	public static final int RTS_PORT = 9020;

	public static final String RRS_HOST = "124.192.56.253";
	public static final int RRS_PORT = 9021;

	/**
	 * @param e
	 * @param e_resp
	 * @return whether e_resp matches e
	 */
	public boolean matchResponse(BDI_Event e, BDI_Event e_resp);

	/**
	 * @param e
	 *            event e
	 * @param sock
	 *            through which socket to send the event e
	 * @return true on success, false on failure
	 */
	public boolean sendEvent(BDI_Event e, Socket sock);

	/**
	 * @param sock
	 *            through which socket
	 * @return We got the event
	 * @throws IOException
	 */
	public BDI_Event recvEvent(Socket sock) throws IOException;

	/**
	 * @param sock
	 * @param req
	 * @return
	 * @throws IOException
	 */
	public BDI_Event doSubmit(Socket sock, BDI_Event req) throws IOException;

	/**
	 * @param channel
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Socket doConnect(int channel) throws UnknownHostException,
			IOException;

	/**
	 * @param channel
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Socket doLogin(int channel) throws UnknownHostException, IOException;

	/**
	 * @param sock
	 */
	public void doLogout(Socket sock) throws IOException;
}
