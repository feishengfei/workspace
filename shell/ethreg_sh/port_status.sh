#!/bin/sh

#reg relative function/param
. $topdir/reg.sh			

#9.32.1 Port Status
PORT_STATUS_0='0x0100'
PORT_STATUS_1='0x0200'
PORT_STATUS_2='0x0300'
PORT_STATUS_3='0x0400'
PORT_STATUS_4='0x0500'
PORT_STATUS_5='0x0600'
#RES				#Bit 31:13
FLOW_LINK_EN=12		#Bit 12
LINK_ASYN_PAUSE_EN=11 #Bit 11
LINK_PAUSE_EN=10	#Bit 10
LINK_EN=9			#Bit 9
LINK=8				#Bit 8
TX_HALF_FLOW_EN=7	#Bit 7
DUPLEX_MODE=6		#Bit 6
RX_FLOW_EN=5		#Bit 5
TX_FLOW_EN=4		#Bit 4
RXMAC_EN=3			#Bit 3
TXMAC_EN=2			#Bit 2
SPEED=0				#Bit 1:0

ps_switchPort()
{
	if [ $1 ]
		then
			case $1 in
			0)
				port=$PORT_STATUS_0
				return 0;;
			1)
				port=$PORT_STATUS_1
				return 0;;
			2)
				port=$PORT_STATUS_2
				return 0;;
			3)
				port=$PORT_STATUS_3
				return 0;;
			4)
				port=$PORT_STATUS_4
				return 0;;
			5)
				port=$PORT_STATUS_5
				return 0;;
			*)
				echo 'usage:ps_switchPort [0~5]'
				return 1;;
			esac
		else
			echo 'usage:ps_switchPort [0~5]'
			return 1
	fi
}

ps_readReg_show()
{
	if ps_switchPort $1	
		then
			readReg $port show
	fi
}

ps_dumpPort()
{
	if ps_switchPort $1	
		then
		getBit $port $FLOW_LINK_EN
		echo -e "\tFLOW_LINK_EN\t\t$?"
		getBit $port $LINK_ASYN_PAUSE_EN
		echo -e "\tLINK_ASYN_PAUSE_EN\t$?"
		getBit $port $LINK_PAUSE_EN
		echo -e "\tLINK_PAUSE_EN\t\t$?"
		getBit $port $LINK_EN
		echo -e "\tLINK_EN\t\t\t$?"

		getBit $port $LINK
		case $? in
		0)
		echo -e "\tLINK\t\t\tPHY link down";;
		1)
		echo -e "\tLINK\t\t\tPHY link up";;
		esac

		getBit $port $TX_HALF_FLOW_EN
		echo -e "\tTX_HALF_FLOW_EN\t\t$?"

		getBit $port $DUPLEX_MODE
		case $? in
		0)
		echo -e "\tDUPLEX_MODE\t\tHalf-duplex mode";;
		1)
		echo -e "\tDUPLEX_MODE\t\tFull-duplex mode";;
		esac

		getBit $port $RX_FLOW_EN
		echo -e "\tRX_FLOW_EN\t\t$?"
		getBit $port $TX_FLOW_EN
		echo -e "\tTX_FLOW_EN\t\t$?"
		getBit $port $RXMAC_EN
		echo -e "\tRXMAC_EN\t\t$?"
		getBit $port $TXMAC_EN
		echo -e "\tTXMAC_EN\t\t$?"

		getBits $port $SPEED 2
		case $bits in
		0)
		echo -e "\tSPEED\t\t\t10Mbps";;
		1)
		echo -e "\tSPEED\t\t\t100Mbps";;
		2)
		echo -e "\tSPEED\t\t\t1000Mbps";;
		3)
		echo -e "\tSPEED\t\t\tError speed mode";;
		esac
	fi
}

#############################################

ps_set_FLOW_LINK_EN()
{
	if ps_switchPort $1	
		then
			setBit $port $FLOW_LINK_EN $2					
	fi
}

ps_get_FLOW_LINK_EN()
{
	if ps_switchPort $1	
		then
			getBit $port $FLOW_LINK_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

ps_get_LINK_ASYN_PAUSE_EN()
{
	if ps_switchPort $1	
		then
			getBit $port $LINK_ASYN_PAUSE_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

ps_get_LINK_PAUSE_EN()
{
	if ps_switchPort $1	
		then
			getBit $port $LINK_PAUSE_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

ps_set_LINK_EN()
{
	if ps_switchPort $1	
		then
			setBit $port $LINK_EN $2					
	fi
}

ps_get_LINK_EN()
{
	if ps_switchPort $1	
		then
			getBit $port $LINK_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

ps_get_LINK()
{
	if ps_switchPort $1	
		then
			getBit $port $LINK
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

ps_set_TX_HALF_FLOW_EN()
{
	if ps_switchPort $1	
		then
			setBit $port $TX_HALF_FLOW_EN $2					
	fi
}

ps_get_TX_HALF_FLOW_EN()
{
	if ps_switchPort $1	
		then
			getBit $port $TX_HALF_FLOW_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

ps_set_DUPLEX_MODE()
{
	if ps_switchPort $1	
		then
			setBit $port $DUPLEX_MODE $2					
	fi
}

ps_get_DUPLEX_MODE()
{
	if ps_switchPort $1	
		then
			getBit $port $DUPLEX_MODE
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

ps_set_RX_FLOW_EN()
{
	if ps_switchPort $1	
		then
			setBit $port $RX_FLOW_EN $2
	fi
}

ps_get_RX_FLOW_EN()
{
	if ps_switchPort $1	
		then
			getBit $port $RX_FLOW_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

ps_set_TX_FLOW_EN()
{
	if ps_switchPort $1	
		then
			setBit $port $TX_FLOW_EN $2
	fi
}

ps_get_TX_FLOW_EN()
{
	if ps_switchPort $1	
		then
			getBit $port $TX_FLOW_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

ps_set_RXMAC_EN()
{
	if ps_switchPort $1	
		then
			setBit $port $RXMAC_EN $2
	fi
}

ps_get_RXMAC_EN()
{
	if ps_switchPort $1	
		then
			getBit $port $RXMAC_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

ps_set_TXMAC_EN()
{
	if ps_switchPort $1	
		then
			setBit $port $TXMAC_EN $2
	fi
}

ps_get_TXMAC_EN()
{
	if ps_switchPort $1	
		then
			getBit $port $TXMAC_EN
			case "$?" in
			0)
				return 0;;
			1)
				return 1;;
			esac
	fi
}

ps_set_SPEED()
{
	if ps_switchPort $1	
		then
		if [ $2 ]
			then
				if [ $2 -ge 0 -a $2 -le 3 ]
					then
						setBits $port $SPEED 2 $2
				else
					echo "usage:ps_set_SPEED PORT SPEED(0~3)"
				fi
			else
				echo "usage:ps_set_SPEED PORT SPEED(0~3)"
		fi
	fi
}

ps_get_SPEED()
{
	if ps_switchPort $1	
		then
		getBits $port $SPEED 2
	fi
}

