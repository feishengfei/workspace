package com.navchina.xzhou.sendrecvdata;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DelimFramer implements Framer
{
	private InputStream in;
	private static final byte DELIMITER = '\n';

	/**
	 * @param in
	 * 		the input stream from which messages are to be extracted
	 */
	public DelimFramer(InputStream in)
	{
		super();
		this.in = in;
	}

	@Override
	public void frameMsg(byte[] msg, OutputStream out) throws IOException
	{
		for (byte b : msg) {
			if (DELIMITER == b) {
				throw new IOException("Message contains delimiter");
			}
		}
		out.write(msg);
		out.write(DELIMITER);
		out.flush();
	}

	@Override
	public byte[] nextMsg() throws IOException
	{
		ByteArrayOutputStream msgBuf = new ByteArrayOutputStream();
		int nextByte;

		while ((nextByte = in.read()) != DELIMITER) {
			if (nextByte == -1) {
				if (msgBuf.size() == 0) {
					return null;
				}
				else {
					throw new EOFException("Non-empty message without delimiter");
				}
			}
			msgBuf.write(nextByte);
		}

		return msgBuf.toByteArray();
	}
}
