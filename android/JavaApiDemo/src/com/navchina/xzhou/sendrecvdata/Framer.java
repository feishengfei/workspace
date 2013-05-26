package com.navchina.xzhou.sendrecvdata;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author xzhou
 *
 */
public interface Framer
{
	void frameMsg(byte[] msg, OutputStream out) throws IOException;

	byte[] nextMsg() throws IOException;
}
