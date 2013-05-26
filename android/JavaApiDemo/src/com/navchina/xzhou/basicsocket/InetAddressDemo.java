package com.navchina.xzhou.basicsocket;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Locale;

public class InetAddressDemo
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try {
			Enumeration<NetworkInterface> interfaceList = NetworkInterface
					.getNetworkInterfaces();
			if (null == interfaceList) {
				System.out.println("未发现网络接口");
			}
			else {
				while (interfaceList.hasMoreElements()) {
					NetworkInterface networkInterface = (NetworkInterface) interfaceList
							.nextElement();

					System.out.println("接口" + networkInterface.getName() + ":");

					Enumeration<InetAddress> addrList = networkInterface
							.getInetAddresses();
					if (!addrList.hasMoreElements()) {
						System.out.println("\t (本接口无网络地址)");
					}
					while (addrList.hasMoreElements()) {
						InetAddress inetAddress = (InetAddress) addrList
								.nextElement();
						String type;
						if (inetAddress instanceof Inet4Address) {
							type = "(v4)";
						}
						else if (inetAddress instanceof Inet6Address) {
							type = "(v6)";
						}
						else {
							type = "(?)";
						}
						System.out.print("\t 地址" + type);
						System.out.println(": " + inetAddress.getHostAddress());

					}
				}
			}
		}
		catch (SocketException e) {
			System.out.println("获取网络接口异常:" + e.getMessage());
		}

		// test parse
		for (String host : args) {
			parse(host);
		}
		parse("blah.blah");
		parseLocal();

	}

	/**
	 * 解析本地地址
	 */
	private static void parseLocal()
	{
		try {
			InetAddress local = InetAddress.getLocalHost();
			System.out.println("local: \t " + local.getHostName() + "/"
					+ local.getHostAddress());

		}
		catch (UnknownHostException e) {
			System.out.println("获取本机地址异常");
		}

	}

	/**
	 * @param host
	 *            name to be parsed
	 * 
	 */
	private static void parse(String host)
	{
		try {
			System.out.println(host + ":");
			InetAddress[] addressList = InetAddress.getAllByName(host);
			for (InetAddress addr : addressList) {
				System.out.println("\t " + addr.getHostName() + "/"
						+ addr.getHostAddress());
				if (addr.isReachable(3 * 1000)) {
					System.out.println("\t " + "ok");
				}
				else {
					System.out.println("\t " + "xxx");
				}
			}
		}
		catch (Exception e) {
			System.out.println("\t 无法找到地址" + host);
		}
	}
}
