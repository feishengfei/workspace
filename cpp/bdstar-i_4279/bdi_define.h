#ifndef BDI_DEFINE_H
#define BDI_DEFINE_H

typedef unsigned long uint32;
typedef unsigned short uint16;
typedef unsigned char uint8;

typedef signed long int32;
typedef signed short int16;
typedef signed char int8;

namespace BDI
{
    const uint32 FRAME_HEADER_LEN = 16;
    const uint32 DATA_HEADER_LEN = 8;

    // Login error code
    const int LOGIN_SER_REDIRECT = 0x1;
    const int LOGIN_SER_BUSY = 0x20; //32
    const int LOGIN_SER_TOOMANY = 0x21;//33
    const int LOGIN_UNKNOWN_VER = 0x22;//34
    const int LOGIN_INVALID_USR = 0x23;//35
    const int LOGIN_AUTHENTICATION_FAIL = 0x24;//36
    const int LOGIN_TIME_OUTERSYNC = 0x25;//37
    const int LOGIN_UNKNOWN_MODE = 0x26;//38
    const int LOGIN_RELOGIN = 0X27;

    // System error
    const int SYS_SYSTEM_FAULT = 100;
    const int SYS_SERVER_NOT_FOUND = 101;
    const int SYS_SERVER_NO_ANSWER = 102;
    const int SYS_NETWORK_UNREACH = 103;

    // Session error
    const int SES_DBUS_FAULT = 200;
    const int SES_EVENT_OBSOLETED = 201;
    const int SES_REQUEST_UNEXEC = 202;

    // Priority level
    const int PRIORITY_LOW = 0;
    const int PRIORITY_NOMAL = 1;
    const int PRIORITY_HIGH = 2;
    const int PRIORITY_EMERGENCY = 3;

    // Invalid cell signal 
    const int CELL_NSIGV = 99;
    const int CELL_NSIGL = 255;

    enum IPFamily {
    NamedHost = 0, 
    IPv4 = 4, 
    IPv6 = 6
    };

    enum MessageType {
    MT_HanZiMessage = 0, 
    MT_CodedMessage = 1, 
    MT_Notice = 2, 
    MT_Warnning = 3, 
    MT_Ad = 4, 
    MT_SMS = 255
    };

    enum TextCodec {
    Unicode, 
    GB2312_SBC,
    ANSI,
    };

    // Event data type (businuss type)
    const uint32 TermListEvent = 0x02012001;

    const uint32 InviteTermJoinGroup = 0x82012080;
    const uint32 InviteTermJoinGroupReply = 0x02012080;

    const uint32 GetPosReport = 0x82012020;
    const uint32 TermPosReply = 0x02011020;
    const uint32 TermPosReport = 0x02011023;

    const uint32 TermSosEvent = 0x02011030;

    const uint32 SubmitMessageEvent = 0x82012050;
    const uint32 DeliverMessageEvent = 0x02011052;
    const uint32 ReceiptMessageEvent = 0x02011001;	//TODO
	
	/*
	*	following types are not decided yet!!!
	*/
    const uint32 TermSosCanceledEvent = 0x02010142;	//TODO canceled
    const uint32 LocateTermEvent = 0x02010131;
    const uint32 ImportOutportEvent = 0x02010134;
    const uint32 TrackQueryEvent = 0x02010205;

    const uint32 PosReplyEvent = 0x82010131;
    const uint32 TrackReplyEvent = 0x82010205;

    // Mobile business type
    enum MobileEventType {
    Mobile_CallingIn = 1, 
    Mobile_NewMessageInd, 
    Mobile_CallingUp, 
    Mobile_Hungup, 
    Mobile_HandsetTrigger, 
    Mobile_Signal,
	Mobile_DialOk,
	Mobile_DialErr,
	Mobile_MsgSendOk,
	Mobile_MsgSendErr,
	PPP_Up,
	PPP_Down
    };

    // GPS business type
    enum GPSEventType {
    GPS_Position = 1, 
    GPS_SateView
    };
};

#endif
