#!/bin/sh

#reg relative function/param
. $topdir/reg.sh			

#9.31.14 VLAN Table Function 1
VT_FUNCTION1='0x0044'
#RES				#Bit 31:12
VT_VALID=11			#Bit 11
#RES				#Bit 10:7
VID_MEM=0			#Bit 6:0

vt_fun1_dump()
{
	getBit $VT_FUNCTION1 $VT_VALID
	echo -e "\tVT_VALID\t\t$?"
	getBits $VT_FUNCTION1 $VID_MEM 7
	echo -e "\tVID_MEM\t\t\t$bits"
}

vt_fun1_vlan_on_port()
{
	if [ $1 ] 
		then
			if [ $1 -ge 0 -a $1 -le 6 ]
				then
				echo port:$1
				readReg $VT_FUNCTION1
				nval=$(( $val | 1<<$1 ))
				ethreg -i eth0 $VT_FUNCTION1=$nval
			else
				echo 'usage:vt_fun1_vlan_on_port [0~6]'
			fi
		else
			echo 'usage:vt_fun1_vlan_on_port [0~6]'
	fi
}

vt_fun1_vlan_off_port()
{
	if [ $1 ] 
		then
			if [ $1 -ge 0 -a $1 -le 6 ]
				then
				echo port:$1
				readReg $VT_FUNCTION1
				nval=$(( $val & ~(1<<$1) ))
				ethreg -i eth0 $VT_FUNCTION1=$nval
			else
				echo 'usage:vt_fun1_vlan_off_port [0~6]'
			fi
		else
			echo 'usage:vt_fun1_vlan_off_port [0~6]'
	fi
}

vt_fun1_vlan_get_VID_MEM()
{
	getBits $VT_FUNCTION1 $VID_MEM 7
}

vt_fun1_set_VT_VALID()
{
	setBit $VT_FUNCTION1 $VT_VALID $1
}

vt_fun1_get_VT_VALID()
{
	getBit $VT_FUNCTION1 $VT_VALID
	case "$?" in
	0)
		return 0;;
	1)
		return 1;;
	esac
}
