#!/bin/sh

#reg relative function/param
. $topdir/reg.sh			

#9.31.17 Address Table Function 2
AT_FUNCTION2='0x0058'
MIRROR_EN=13		#Bit 13

at_fun2_set_MIRROR_EN()
{
	setBit $AT_FUNCTION2 $MIRROR_EN $1
}

at_fun2_get_MIRROR_EN()
{
	getBit $AT_FUNCTION2 $MIRROR_EN
	case "$?" in
	0)
		return 0;;
	1)
		return 1;;
	esac
}
