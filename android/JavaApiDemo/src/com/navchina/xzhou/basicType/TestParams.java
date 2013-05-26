package com.navchina.xzhou.basicType;

public class TestParams
{
	private String s1;
	private String s2;
	
	
	
	public TestParams(String s1, String s2)
	{
		super();
		this.s1 = s1;
		this.s2 = s2;
	}
	
	@Override
	public String toString()
	{
		return "s1 = " + s1 + "\n" 
				+ "s2 = " + s2;
	}


	public void test01(String s1, String s2)
	{
		s1 = new String("abc");
		s2 = null;
	}
	
	
	public static void main(String[] args)
	{
		TestParams tp = new TestParams(null, "apple");
		
		String s1 = null;
		String s2 = "banana";
		System.out.println(s1 + s2);
		tp.test01(s1, s2);
		System.out.println(s1 + s2);
		
	}
}
