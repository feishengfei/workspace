package com.cookbook.launch_activity.testcase;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTest
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for Contacts");
		suite.addTestSuite(ContactTest.class);
		//suite.addTestSuite(TestInsertBD.class);
		return suite;
		
	}
}
