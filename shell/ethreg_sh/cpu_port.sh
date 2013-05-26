#!/bin/sh

#reg relative function/param
. $topdir/reg.sh			

#9.31.22 CPU Port
CPU_PORT='0x0078'
MIRROR_PORT_NUM=4		#Bit 7:4

cpu_port_set_MIRROR_PORT_NUM()
{
	if [ $1 ]
		then
			if [ $1 -ge 0 -a $1 -le 4 ]
				then
					setBits $CPU_PORT $MIRROR_PORT_NUM 4 $1
			else
				echo "usage:cpu_port_set_MIRROR_PORT_NUM PORT(0~4)"
			fi
		else
			echo "usage:cpu_port_set_MIRROR_PORT_NUM PORT(0~4)"
	fi
}


cpu_port_get_MIRROR_PORT_NUM()
{
	getBits $CPU_PORT $MIRROR_PORT_NUM 4
}
