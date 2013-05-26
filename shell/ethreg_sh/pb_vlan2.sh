#!/bin/sh

#reg relative function/param
. $topdir/reg.sh			

#9.32.4 Port-Based VLAN 2
PB_VLAN2_0='0x010C'
PB_VLAN2_1='0x020C'
PB_VLAN2_2='0x030C'
PB_VLAN2_3='0x040C'
PB_VLAN2_4='0x050C'
PB_VLAN2_5='0x060C'

DOT1Q_MODE=30			#Bit 31:30
CORE_PORT_EN=29			#Bit 29
ING_VLAN_MODE=27		#Bit 28:27
#RES					#Bit 26:24
VLAN_PRI_PRO_EN=23		#Bit 23
PORT_VID_MEM=16			#Bit 22:16
#RES					#Bit 15
UNI_LEAKY_EN=14			#Bit 14
MULTI_LEAKY_EN=13		#Bit 13
#RES					#Bit 12:0

pb_vlan2_switchPort()
{
	if [ $1 ]
		then
			case $1 in
			0)
				port=$PB_VLAN2_0
				return 0;;
			1)
				port=$PB_VLAN2_1
				return 0;;
			2)
				port=$PB_VLAN2_2
				return 0;;
			3)
				port=$PB_VLAN2_3
				return 0;;
			4)
				port=$PB_VLAN2_4
				return 0;;
			5)
				port=$PB_VLAN2_5
				return 0;;
			*)
				echo 'usage:pb_vlan2_switchPort [0~5]'
				return 1;;
			esac
		else
			echo 'usage:pb_vlan2_switchPort [0~5]'
			return 1
	fi
}

pb_vlan2_readReg_show()
{
	if pb_vlan2_switchPort $1	
		then
			readReg $port show
	fi
}

pb_vlan2_dumpPort()
{
	if pb_vlan2_switchPort $1	
		then

		getBits $port $DOT1Q_MODE 2
		case $bits in
		0)
		echo -e "\tDOT1Q_MODE\t\t802.1Q DISABLE";;
		1)
		echo -e "\tDOT1Q_MODE\t\tFallback";;
		2)
		echo -e "\tDOT1Q_MODE\t\tCheck";;
		3)
		echo -e "\tDOT1Q_MODE\t\tSecure";;
		esac

		getBits $port $ING_VLAN_MODE 2
		case $bits in
		0)
		echo -e "\tING_VLAN_MODE\t\tAll untag/tag can be recved";;
		1)
		echo -e "\tING_VLAN_MODE\t\tOnly tag can be recved";;
		2)
		echo -e "\tING_VLAN_MODE\t\tOnly untag can be recved(NO Vlan or Priority Vlan)";;
		3)
		echo -e "\tING_VLAN_MODE\t\tReserved";;
		esac

		getBits $port $PORT_VID_MEM 7
		echo -e "\tPORT_VID_MEM\t\t$bits"
		getBit $port $CORE_PORT_EN
		echo -e "\tCORE_PORT_EN\t\t$?"
		getBit $port $VLAN_PRI_PRO_EN
		echo -e "\tVLAN_PRI_PRO_EN\t\t$?"
		getBit $port $UNI_LEAKY_EN
		echo -e "\tUNI_LEAKY_EN\t\t$?"
		getBit $port $MULTI_LEAKY_EN
		echo -e "\tMULTI_LEAKY_EN\t\t$?"

	fi
}

#############################################
pb_vlan2_set_DOT1Q_MODE()
{
	if pb_vlan2_switchPort $1	
		then
		if [ $2 ]
			then
				if [ $2 -ge 0 -a $2 -le 3 ]
					then
						setBits $port $DOT1Q_MODE 2 $2
				else
					echo "usage:pb_vlan2_set_DOT1Q_MODE PORT MODE(0~3)"
				fi
			else
				echo "usage:pb_vlan2_set_DOT1Q_MODE PORT MODE(0~3)"
		fi
	fi
}

pb_vlan2_get_DOT1Q_MODE()
{
	if pb_vlan2_switchPort $1	
		then
		getBits $port $DOT1Q_MODE 2
	fi
}

pb_vlan2_set_ING_VLAN_MODE()
{
	if pb_vlan2_switchPort $1	
		then
		if [ $2 ]
			then
				if [ $2 -ge 0 -a $2 -le 3 ]
					then
						setBits $port $ING_VLAN_MODE 2 $2
				else
					echo "usage:pb_vlan2_set_ING_VLAN_MODE PORT MODE(0~3)"
				fi
			else
				echo "usage:pb_vlan2_set_ING_VLAN_MODE PORT MODE(0~3)"
		fi
	fi
}

pb_vlan2_get_ING_VLAN_MODE()
{
	if pb_vlan2_switchPort $1	
		then
		getBits $port $ING_VLAN_MODE 2
	fi
}

pb_vlan2_set_PORT_VID_MEM()
{
	if pb_vlan2_switchPort $1	
		then
		if [ $2 ]
			then
				if [ $2 -ge 0 -a $2 -le 127 ]
					then
						setBits $port $PORT_VID_MEM 7 $2
				else
					echo "usage:pb_vlan2_set_PORT_VID_MEM PORT MEM(0~3)"
				fi
			else
				echo "usage:pb_vlan2_set_PORT_VID_MEM PORT MEM(0~3)"
		fi
	fi
}

pb_vlan2_get_PORT_VID_MEM()
{
	if pb_vlan2_switchPort $1	
		then
		getBits $port $PORT_VID_MEM 7
	fi
}





pb_vlan2_set_CORE_PORT_EN()
{
	if pb_vlan2_switchPort $1	
		then
			setBit $port $CORE_PORT_EN $2					
	fi
}

pb_vlan2_get_CORE_PORT_EN()
{
	if pb_vlan2_switchPort $1	
		then
			getBit $port $CORE_PORT_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pb_vlan2_set_VLAN_PRI_PRO_EN()
{
	if pb_vlan2_switchPort $1	
		then
			setBit $port $VLAN_PRI_PRO_EN $2					
	fi
}

pb_vlan2_get_VLAN_PRI_PRO_EN()
{
	if pb_vlan2_switchPort $1	
		then
			getBit $port $VLAN_PRI_PRO_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pb_vlan2_set_UNI_LEAKY_EN()
{
	if pb_vlan2_switchPort $1	
		then
			setBit $port $UNI_LEAKY_EN $2					
	fi
}

pb_vlan2_get_UNI_LEAKY_EN()
{
	if pb_vlan2_switchPort $1	
		then
			getBit $port $UNI_LEAKY_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pb_vlan2_set_MULTI_LEAKY_EN()
{
	if pb_vlan2_switchPort $1	
		then
			setBit $port $MULTI_LEAKY_EN $2					
	fi
}

pb_vlan2_get_MULTI_LEAKY_EN()
{
	if pb_vlan2_switchPort $1	
		then
			getBit $port $MULTI_LEAKY_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}
