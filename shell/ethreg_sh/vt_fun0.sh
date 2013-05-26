#!/bin/sh

#reg relative function/param
. $topdir/reg.sh			

#9.31.13 VLAN Table Function 0
VT_FUNCTION0='0x0040'
VT_PRI_EN=31		#Bit 31
VT_PRI=28			#Bit 30:28
VT_PRI_MASK=0x70000000	
VID=16				#Bit 27:16
VID_MASK=0x0FFF0000
#RES				#Bit 15:12
VT_PORT_NUM=8		#Bit 11:8
VT_PORT_NUM_MASK=0x00000F00
VT_BUSY=3			#Bit 3
VT_FUNC=0			#Bit 2:0
VT_FUNC_MASK=0x00000007

vt_fun0_dump()
{
	getBit $VT_FUNCTION0 $VT_PRI_EN
	echo -e "\tVT_PRI_EN\t\t$?"
	getBits $VT_FUNCTION0 $VT_PRI 3
	echo -e "\tVT_PRI\t\t\t$bits"
	getBits $VT_FUNCTION0 $VID 12
	echo -e "\tVID\t\t\t$bits"
	getBits $VT_FUNCTION0 $VT_PORT_NUM 4
	echo -e "\tVT_PORT_NUM\t\t$bits"
	getBit $VT_FUNCTION0 $VT_BUSY
	echo -e "\tVT_BUSY\t\t\t$?"
	
	getBits $VT_FUNCTION0 $VT_FUNC 3
	case $bits in
	0)
		echo -e "\tVT_FUNC\t\t\tNo operation";;
	1)
		echo -e "\tVT_FUNC\t\t\tFlush all entries";;
	2)
		echo -e "\tVT_FUNC\t\t\tLoad an entry";;
	3)
		echo -e "\tVT_FUNC\t\t\tPurge an entry";;
	4)
		echo -e "\tVT_FUNC\t\t\tRemove a port from table";;
	5)
		echo -e "\tVT_FUNC\t\t\tGet the next VID";;
	6)
		echo -e "\tVT_FUNC\t\t\tRead one entry";;
	esac
}

vt_fun0_set_VT_BUSY()
{
	nval=$(($nval | 1<<$VT_BUSY))
}

vt_fun0_get_VT_BUSY()
{
	getBit $VT_FUNCTION0 $VT_BUSY
		case "$?" in
		0)
			return 0;;
		1)
			return 1;;
		esac
}

vt_fun0_set_VT_PRI()
{
	if [ $1 ] 
		then
			if [ $1 -ge 0 -a $1 -le 7 ]
				then
					readReg $VT_FUNCTION0
					val=$(($val & ~VT_PRI_MASK))
					nval=$(($val | $1<<$VT_PRI))
					nval=$(($nval | 1<<$VT_PRI_EN))

					vt_fun0_set_VT_BUSY
					ethreg -i eth0 $VT_FUNCTION0=$nval
			else
				echo usage:vt_fun0_set_VT_PRI PRIORITY[0~7]
			fi
		else
			echo usage:vt_fun0_set_VT_PRI PRIORITY[0~7]
	fi
}

vt_fun0_get_VT_PRI()
{
	getBits $VT_FUNCTION0 $VT_PRI 3
}

vt_fun0_get_VT_PRI_EN()
{
	getBit $VT_FUNCTION0 $VT_PRI_EN
	case "$?" in
		0)
			return 0;;
		1)
			return 1;;
		esac
}

vt_fun0_set_VID()
{
	if [ $1 ]
		then
			if [ $1 -ge 1 -a $1 -le 4094 ]
				then
					readReg $VT_FUNCTION0
					val=$(($val & ~VID_MASK))
					nval=$(($val | $1<<$VID))

					vt_fun0_set_VT_BUSY
					ethreg -i eth0 $VT_FUNCTION0=$nval
			else
				echo usage:vt_fun0_set_VID [1~4094]
			fi
		else
			echo usage:vt_fun0_set_VID [1~4094]
	fi
}

vt_fun0_get_VID()
{
	getBits $VT_FUNCTION0 $VID 12
}

##FIXME max port num is unknown!!!###
vt_fun0_set_VT_PORT_NUM()
{
	if [ $1 ]
		then
			if [ $1 -ge 0 -a $1 -le 15 ]
				then
					readReg $VT_FUNCTION0
					val=$(($val & ~VT_PORT_NUM_MASK))
					nval=$(($val | $1<<$VT_PORT_NUM))

					vt_fun0_set_VT_BUSY
					ethreg -i eth0 $VT_FUNCTION0=$nval
			else
				echo usage:vt_fun0_set_VT_PORT_NUM PORT_NUM[0~15]
			fi
		else
			echo usage:vt_fun0_set_VT_PORT_NUM PORT_NUM[0~15]
	fi
}

vt_fun0_get_VT_PORT_NUM()
{
	getBits $VT_FUNCTION0 $VT_PORT_NUM 4
}

vt_fun0_set_VT_FUNC()
{
	if [ $1 ]
		then
			if [ $1 -ge 0 -a $1 -le 7 ]
				then
					readReg $VT_FUNCTION0
					val=$(($val & ~VT_FUNC_MASK))
					nval=$(($val | $1<<$VT_FUNC))

					vt_fun0_set_VT_BUSY
					ethreg -i eth0 $VT_FUNCTION0=$nval
			else
				echo usage:vt_fun0_set_VT_FUNC [0~7]
			fi
		else
			echo usage:vt_fun0_set_VT_FUNC [0~7]
	fi
}

vt_fun0_get_VT_FUNC()
{
	getBits $VT_FUNCTION0 $VT_FUNC 3
}
