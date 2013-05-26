#!/bin/sh

#reg relative function/param
. $topdir/reg.sh			

#9.32.3 Port-Based VLAN 
PB_VLAN_0='0x0108'
PB_VLAN_1='0x0208'
PB_VLAN_2='0x0308'
PB_VLAN_3='0x0408'
PB_VLAN_4='0x0508'
PB_VLAN_5='0x0608'

ING_PORT_PRI=29				#Bit 31:29
FORCE_PORT_VLAN_EN=28		#Bit 28
PORT_DEFAULT_CVID=16		#Bit 27:16
PORT_CLONE_EN=15			#Bit 15
PORT_VLAN_PROP_EN=14		#Bit 14
PORT_TLS_MODE=13			#Bit 13
FORCE_DEFAULT_VID_EN=12		#Bit 12
PORT_DEFAULT_SVID=0			#Bit 11:0

pb_vlan_switchPort()
{
	if [ $1 ]
		then
			case $1 in
			0)
				port=$PB_VLAN_0
				return 0;;
			1)
				port=$PB_VLAN_1
				return 0;;
			2)
				port=$PB_VLAN_2
				return 0;;
			3)
				port=$PB_VLAN_3
				return 0;;
			4)
				port=$PB_VLAN_4
				return 0;;
			5)
				port=$PB_VLAN_5
				return 0;;
			*)
				echo 'usage:pb_vlan_switchPort [0~5]'
				return 1;;
			esac
		else
			echo 'usage:pb_vlan_switchPort [0~5]'
			return 1
	fi
}

pb_vlan_readReg_show()
{
	if pb_vlan_switchPort $1	
		then
			readReg $port show
	fi
}

pb_vlan_dumpPort()
{
	if pb_vlan_switchPort $1
		then
		getBits $port $ING_PORT_PRI 3
		echo -e "\tING_PORT_PRI\t\t$bits"
		getBits $port $PORT_DEFAULT_CVID 12
		echo -e "\tPORT_DEFAULT_CVID\t$bits"
		getBits $port $PORT_DEFAULT_SVID 12
		echo -e "\tPORT_DEFAULT_SVID\t$bits"
		getBit $port $FORCE_PORT_VLAN_EN
		echo -e "\tFORCE_PORT_VLAN_EN\t$?"
		getBit $port $PORT_CLONE_EN
		echo -e "\tPORT_CLONE_EN\t\t$?"
		getBit $port $PORT_VLAN_PROP_EN
		echo -e "\tPORT_VLAN_PROP_EN\t$?"

		getBit $port $PORT_TLS_MODE
		case $? in
		0)
		echo -e "\tPORT_TLS_MODE\t\tPort works in TLS mode";;
		1)
		echo -e "\tPORT_TLS_MODE\t\tPort works in NON-TLS mode";;
		esac

		getBit $port $FORCE_DEFAULT_VID_EN
		echo -e "\tFORCE_DEFAULT_VID_EN\t$?"
	fi
}

#############################################
pb_vlan_set_ING_PORT_PRI()
{
	if pb_vlan_switchPort $1	
		then
		if [ $2 ]
			then
				if [ $2 -ge 0 -a $2 -le 7 ]
					then
						setBits $port $ING_PORT_PRI 3 $2
				else
					echo "usage:pb_vlan_set_ING_PORT_PRI PORT PRIORITY(0~7)"
				fi
			else
				echo "usage:pb_vlan_set_ING_PORT_PRI PORT PRIORITY(0~7)"
		fi
	fi
}

pb_vlan_get_ING_PORT_PRI()
{
	if pb_vlan_switchPort $1	
		then
		getBits $port $ING_PORT_PRI 3
	fi
}

pb_vlan_set_PORT_DEFAULT_CVID()
{
	if pb_vlan_switchPort $1	
		then
		if [ $2 ]
			then
				if [ $2 -ge 0 -a $2 -le 4094 ]
					then
						setBits $port $PORT_DEFAULT_CVID 12 $2
				else
					echo "usage:pb_vlan_set_PORT_DEFAULT_CVID PORT VID(0~4094)"
				fi
			else
				echo "usage:pb_vlan_set_PORT_DEFAULT_CVID PORT VID(0~4094)"
		fi
	fi
}

pb_vlan_get_PORT_DEFAULT_CVID()
{
	if pb_vlan_switchPort $1	
		then
		getBits $port $PORT_DEFAULT_CVID 12
	fi
}

pb_vlan_set_PORT_DEFAULT_SVID()
{
	if pb_vlan_switchPort $1	
		then
		if [ $2 ]
			then
				if [ $2 -ge 0 -a $2 -le 4094 ]
					then
						setBits $port $PORT_DEFAULT_SVID 12 $2
				else
					echo "usage:pb_vlan_set_PORT_DEFAULT_SVID PORT VID(0~4094)"
				fi
			else
				echo "usage:pb_vlan_set_PORT_DEFAULT_SVID PORT VID(0~4094)"
		fi
	fi
}

pb_vlan_get_PORT_DEFAULT_SVID()
{
	if pb_vlan_switchPort $1	
		then
		getBits $port $PORT_DEFAULT_SVID 12
	fi
}




pb_vlan_set_FORCE_PORT_VLAN_EN()
{
	if pb_vlan_switchPort $1	
		then
			setBit $port $FORCE_PORT_VLAN_EN $2					
	fi
}

pb_vlan_get_FORCE_PORT_VLAN_EN()
{
	if pb_vlan_switchPort $1	
		then
			getBit $port $FORCE_PORT_VLAN_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pb_vlan_set_PORT_CLONE_EN()
{
	if pb_vlan_switchPort $1	
		then
			setBit $port $PORT_CLONE_EN $2					
	fi
}

pb_vlan_get_PORT_CLONE_EN()
{
	if pb_vlan_switchPort $1	
		then
			getBit $port $PORT_CLONE_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pb_vlan_set_PORT_VLAN_PROP_EN()
{
	if pb_vlan_switchPort $1	
		then
			setBit $port $PORT_VLAN_PROP_EN $2					
	fi
}

pb_vlan_get_PORT_VLAN_PROP_EN()
{
	if pb_vlan_switchPort $1	
		then
			getBit $port $PORT_VLAN_PROP_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pb_vlan_set_PORT_TLS_MODE()
{
	if pb_vlan_switchPort $1	
		then
			setBit $port $PORT_TLS_MODE $2					
	fi
}

pb_vlan_get_PORT_TLS_MODE()
{
	if pb_vlan_switchPort $1	
		then
			getBit $port $PORT_TLS_MODE
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pb_vlan_set_FORCE_DEFAULT_VID_EN()
{
	if pb_vlan_switchPort $1	
		then
			setBit $port $FORCE_DEFAULT_VID_EN $2					
	fi
}

pb_vlan_get_FORCE_DEFAULT_VID_EN()
{
	if pb_vlan_switchPort $1	
		then
			getBit $port $FORCE_DEFAULT_VID_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}
