package com.navchina.bdi;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

class MyLogHander extends Formatter
{
	@Override
	public String format(LogRecord record)
	{
		/*
		return record.getSourceMethodName() + "@" + record.getSourceClassName()
				+ "<" + record.getLevel() + ">:\n" + record.getMessage() + "\n";
		*/
		return record.getMessage() + "\n";
	}
}