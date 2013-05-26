package com.navchina.xzhou.basicsocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class TCPEchoServer implements Runnable
{
	private static final int BUF_SIZE = 32;
	private int servPort;
	private int recvMsgSize;
	private byte[] receiveBuf = new byte[BUF_SIZE];
	private ServerSocket servSock ;

	public TCPEchoServer(int servPort) 
	{
		super();
		this.servPort = servPort;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException
	{
		if (args.length != 1) {
			throw new IllegalArgumentException("Parameter(s) : <Port>");
		}
		
		new Thread(new TCPEchoServer(Integer.parseInt(args[0]))).run();
		
	}

	@Override
	public void run()
	{
		
		try {
			servSock = new ServerSocket(servPort);
		}
		catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("无法绑定端口:" + servPort);
		}
		
		
		while(true) {
			try {
				Socket clntSock = servSock.accept();
				
				SocketAddress clntAddress = clntSock.getRemoteSocketAddress();
				System.out.println("处理客户端:" + clntAddress);
				
				InputStream in = clntSock.getInputStream();
				OutputStream out = clntSock.getOutputStream();
				
				while((recvMsgSize=in.read(receiveBuf)) != -1) {
					out.write(receiveBuf, 0, recvMsgSize);
					System.out.println("服务器发送:" + new String(receiveBuf, 0, recvMsgSize));
				}
				
				clntSock.close();
			}
			catch (IOException e) {
				System.out.println("无法获取客户端");
			}
			
		}
	}

}
