package com.navchina.xzhou.basicsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class TCPEchoClient
{

	/**
	 * @param args
	 *            TCPEchoClient: <Server> <Words> [<port>]
	 */
	public static void main(String[] args) throws IOException
	{
		if ((args.length < 2) || args.length > 3) {
			throw new IllegalArgumentException(
					"TCPEchoClient: <Server> <Word> [<port>]");
		}

		String server = args[0];

		byte[] data = args[1].getBytes();
		int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 1000;

		// Create socket
		Socket socket = new Socket(server, servPort);
		System.out.println("连接服务器...");

		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();

		// send data
		out.write(data);
		System.out.println("发送:" + new String(data));

		// receive
		int totalBytesRcvd = 0;
		int bytesRcvd;
		while (totalBytesRcvd < data.length) {
			if ((bytesRcvd = in.read(data, totalBytesRcvd, data.length
					- totalBytesRcvd)) == -1) {
				throw new SocketException("连接过早关闭");
			}
			totalBytesRcvd += bytesRcvd;
		}

		System.out.println("接收:" + new String(data));
		socket.close();
	}

}
