#!/bin/sh

#reg relative function/param
. $topdir/reg.sh			

#9.32.2 Port Control
PORT_CONTROL_0='0x0104'
PORT_CONTROL_1='0x0204'
PORT_CONTROL_2='0x0304'
PORT_CONTROL_3='0x0404'
PORT_CONTROL_4='0x0504'
PORT_CONTROL_5='0x0604'

#RES				#Bit 31:24
EAPOL_EN=23			#Bit 23
ARP_LEAKY_EN=22		#Bit 22
IGMP_LEAVE_EN=21	#Bit 21
IGMP_JOIN_EN=20		#Bit 20
DHCP_EN=19			#Bit 19
IPG_DEC_EN=18		#Bit 18
ING_MIRROR_EN=17	#Bit 17
EG_MIRROR_EN=16		#Bit 16
#RES				#Bit 15
LEARN_EN=14			#Bit 14
#RES				#Bit 13
MAC_LOOP_BACK=12	#Bit 12
HEAD_EN=11			#Bit 11
IGMP_MLD_EN=10		#Bit 10
EG_VLAN_MODE=8		#Bit 9:8
LEARN_ONE_LOCK=7	#Bit 7
PORT_LOCK_EN=6		#Bit 6
LOCK_DROP_EN=5		#Bit 5
#RES				#Bit 4:3
PORT_STATE=0		#Bit 2:0


pc_switchPort()
{
	if [ $1 ]
		then
			case $1 in
			0)
				port=$PORT_CONTROL_0
				return 0;;
			1)
				port=$PORT_CONTROL_1
				return 0;;
			2)
				port=$PORT_CONTROL_2
				return 0;;
			3)
				port=$PORT_CONTROL_3
				return 0;;
			4)
				port=$PORT_CONTROL_4
				return 0;;
			5)
				port=$PORT_CONTROL_5
				return 0;;
			*)
				echo 'usage:pc_switchPort [0~5]'
				return 1;;
			esac
		else
			echo 'usage:pc_switchPort [0~5]'
			return 1
	fi
}

pc_readReg_show()
{
	if pc_switchPort $1	
		then
			readReg $port show
	fi
}

pc_dumpPort()
{
	if pc_switchPort $1	
		then
		getBit $port $EAPOL_EN
		echo -e "\tEAPOL_EN\t\t$?"
		getBit $port $ARP_LEAKY_EN
		echo -e "\tARP_LEAKY_EN\t\t$?"
		getBit $port $IGMP_LEAVE_EN
		echo -e "\tIGMP_LEAVE_EN\t\t$?"
		getBit $port $IGMP_JOIN_EN
		echo -e "\tIGMP_JOIN_EN\t\t$?"
		getBit $port $DHCP_EN
		echo -e "\tDHCP_EN\t\t\t$?"
		getBit $port $IPG_DEC_EN
		echo -e "\tIPG_DEC_EN\t\t$?"
		getBit $port $ING_MIRROR_EN
		echo -e "\tING_MIRROR_EN\t\t$?"
		getBit $port $EG_MIRROR_EN
		echo -e "\tEG_MIRROR_EN\t\t$?"
		getBit $port $LEARN_EN
		echo -e "\tLEARN_EN\t\t$?"
		getBit $port $MAC_LOOP_BACK
		echo -e "\tMAC_LOOP_BACK\t\t$?"
		getBit $port $HEAD_EN
		echo -e "\tHEAD_EN\t\t\t$?"
		getBit $port $IGMP_MLD_EN
		echo -e "\tIGMP_MLD_EN\t\t$?"
		getBits $port $EG_VLAN_MODE 2

		case $bits in
		0)
		echo -e "\tEG_VLAN_MODE\t\tEgress trans frames UNMODIFIED";;
		1)
		echo -e "\tEG_VLAN_MODE\t\tEgress trans frames WITHOUT VLAN";;
		2)
		echo -e "\tEG_VLAN_MODE\t\tEgress trans frames WITH VLAN";;
		esac
		getBit $port $LEARN_ONE_LOCK
		echo -e "\tLEARN_ONE_LOCK\t\t$?"
	fi

}

#############################################
pc_set_EAPOL_EN()
{
	if pc_switchPort $1	
		then
			setBit $port $EAPOL_EN $2					
	fi
}

pc_get_EAPOL_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $EAPOL_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_ARP_LEAKY_EN()
{
	if pc_switchPort $1
		then
			setBit $port $ARP_LEAKY_EN $2					
	fi
}

pc_get_ARP_LEAKY_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $ARP_LEAKY_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_IGMP_LEAVE_EN()
{
	if pc_switchPort $1
		then
			setBit $port $IGMP_LEAVE_EN $2					
	fi
}

pc_get_IGMP_LEAVE_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $IGMP_LEAVE_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_IGMP_JOIN_EN()
{
	if pc_switchPort $1
		then
			setBit $port $IGMP_JOIN_EN $2					
	fi
}

pc_get_IGMP_JOIN_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $IGMP_JOIN_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_DHCP_EN()
{
	if pc_switchPort $1
		then
			setBit $port $DHCP_EN $2					
	fi
}

pc_get_DHCP_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $DHCP_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_IPG_DEC_EN()
{
	if pc_switchPort $1
		then
			setBit $port $IPG_DEC_EN $2					
	fi
}

pc_get_IPG_DEC_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $IPG_DEC_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_ING_MIRROR_EN()
{
	if pc_switchPort $1
		then
			setBit $port $ING_MIRROR_EN $2					
	fi
}

pc_get_ING_MIRROR_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $ING_MIRROR_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_EG_MIRROR_EN()
{
	if pc_switchPort $1
		then
			setBit $port $EG_MIRROR_EN $2					
	fi
}

pc_get_EG_MIRROR_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $EG_MIRROR_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_LEARN_EN()
{
	if pc_switchPort $1
		then
			setBit $port $LEARN_EN $2					
	fi
}

pc_get_LEARN_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $LEARN_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_MAC_LOOP_BACK()
{
	if pc_switchPort $1
		then
			setBit $port $MAC_LOOP_BACK $2					
	fi
}

pc_get_MAC_LOOP_BACK()
{
	if pc_switchPort $1	
		then
			getBit $port $MAC_LOOP_BACK
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_HEAD_EN()
{
	if pc_switchPort $1
		then
			setBit $port $HEAD_EN $2					
	fi
}

pc_get_HEAD_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $HEAD_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_IGMP_MLD_EN()
{
	if pc_switchPort $1
		then
			setBit $port $IGMP_MLD_EN $2					
	fi
}

pc_get_IGMP_MLD_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $IGMP_MLD_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_EG_VLAN_MODE()
{
	if pc_switchPort $1	
		then
		if [ $2 ]
			then
				if [ $2 -ge 0 -a $2 -le 2 ]
					then
						setBits $port $EG_VLAN_MODE 2 $2
				else
					echo "usage:pc_set_EG_VLAN_MODE PORT EG_VLAN_MODE(0~2)"
				fi
			else
				echo "usage:pc_set_EG_VLAN_MODE PORT EG_VLAN_MODE(0~2)"
		fi
	fi
}

pc_get_EG_VLAN_MODE()
{
	if pc_switchPort $1	
		then
		getBits $port $EG_VLAN_MODE 2
	fi
}

pc_set_LEARN_ONE_LOCK()
{
	if pc_switchPort $1
		then
			setBit $port $LEARN_ONE_LOCK $2					
	fi
}

pc_get_LEARN_ONE_LOCK()
{
	if pc_switchPort $1	
		then
			getBit $port $LEARN_ONE_LOCK
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_PORT_LOCK_EN()
{
	if pc_switchPort $1
		then
			setBit $port $PORT_LOCK_EN $2					
	fi
}

pc_get_PORT_LOCK_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $PORT_LOCK_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_LOCK_DROP_EN()
{
	if pc_switchPort $1
		then
			setBit $port $LOCK_DROP_EN $2					
	fi
}

pc_get_LOCK_DROP_EN()
{
	if pc_switchPort $1	
		then
			getBit $port $LOCK_DROP_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

pc_set_PORT_STATE()
{
	if pc_switchPort $1	
		then
		if [ $2 ]
			then
				if [ $2 -ge 0 -a $2 -le 4 ]
					then
						setBits $port $PORT_STATE 3 $2
				else
					echo "usage:pc_set_PORT_STATE PORT PORT_STATE(0~4)"
				fi
			else
				echo "usage:pc_set_PORT_STATE PORT PORT_STATE(0~4)"
		fi
	fi
}

pc_get_PORT_STATE()
{
	if pc_switchPort $1	
		then
		getBits $port $PORT_STATE 3
	fi
}
