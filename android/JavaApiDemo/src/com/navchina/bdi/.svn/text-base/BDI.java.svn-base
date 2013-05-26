package com.navchina.bdi;

public interface BDI
{
	public static final int FRAME_HEADER_LEN = 16;
	public static final int DATA_HEADER_LEN = 8;

	// Login error code
	public static final int LOGIN_SER_REDIRECT = 0x1;
	public static final int LOGIN_SER_BUSY = 0x20;
	public static final int LOGIN_SER_TOOMANY = 0x21;
	public static final int LOGIN_UNKNOWN_VER = 0x22;
	public static final int LOGIN_INVALID_USR = 0x23;
	public static final int LOGIN_AUTHENTICATION_FAIL = 0x24;
	public static final int LOGIN_TIME_OUTERSYNC = 0x25;
	public static final int LOGIN_UNKNOWN_MODE = 0x26;
	public static final int LOGIN_RELOGIN = 0X27;

	// System error
	public static final int SYS_SYSTEM_FAULT = 100;
	public static final int SYS_SERVER_NOT_FOUND = 101;
	public static final int SYS_SERVER_NO_ANSWER = 102;
	public static final int SYS_NETWORK_UNREACH = 103;

	// Session error
	public static final int SES_DBUS_FAULT = 200;
	public static final int SES_EVENT_OBSOLETED = 201;
	public static final int SES_REQUEST_UNEXEC = 202;

	// Priority level
	public static final int PRIORITY_LOW = 0;
	public static final int PRIORITY_NOMAL = 1;
	public static final int PRIORITY_HIGH = 2;
	public static final int PRIORITY_EMERGENCY = 3;

	// Invalid cell signal
	public static final int CELL_NSIGV = 99;
	public static final int CELL_NSIGL = 255;

	// Event data type (businuss type)
	public static final int TermListEvent = 0x02012001;

	public static final int InviteTermJoinGroup = 0x82012080;
	public static final int InviteTermJoinGroupReply = 0x02012080;

	public static final int GetPosReport = 0x82012020;
	public static final int TermPosReply = 0x02011020;
	public static final int TermPosReport = 0x02011023;

	public static final int TermSosEvent = 0x02011030;

	public static final int SubmitMessageEvent = 0x82012050;
	public static final int DeliverMessageEvent = 0x02011052;
	public static final int ReceiptMessageEvent = 0x02011001;

	/*
	 * following types are not decided yet!!!
	 */
	public static final int TermSosCanceledEvent = 0x02010142;
	public static final int LocateTermEvent = 0x02010131;
	public static final int ImportOutportEvent = 0x02010134;
	public static final int TrackQueryEvent = 0x02010205;

	public static final int PosReplyEvent = 0x82010131;
	public static final int TrackReplyEvent = 0x82010205;

	public static final double MEGA_D = 1000000.0;
	public static final int MEGA_I = 1000000;
}
