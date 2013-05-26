package com.navchina.xzhou.sendrecvdata;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LengthFramer implements Framer
{
	public static final int MAXMESSAGELENGTH = 65535;
	public static final int BYTEMASK = 0xff;
	public static final int SHORTMASK = 0xffff;
	public static final int BYTESHIFT = 8;

	private DataInputStream in;

	public LengthFramer(InputStream in) throws IOException
	{
		super();
		this.in = new DataInputStream(in);
	}

	@Override
	public void frameMsg(byte[] msg, OutputStream out) throws IOException
	{
		if (msg.length > MAXMESSAGELENGTH) {
			throw new IOException("message too long");
		}

		out.write((msg.length >> BYTESHIFT) & BYTEMASK);
		out.write(msg.length & BYTEMASK);

		out.write(msg);
		out.flush();

	}

	@Override
	public byte[] nextMsg() throws IOException
	{
		int length;
		try {
			length = in.readUnsignedShort();
		}
		catch (EOFException e) {
			return null;
		}
		byte[] msg = new byte[length];
		in.readFully(msg);
		return msg;
	}

}
