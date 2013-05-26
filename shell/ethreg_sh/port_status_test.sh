#!/bin/sh
. $topdir/t_util.sh
. $topdir/port_status.sh

#ps_switchPort
ps_testcase1()
{
	assert_fail
	ps_switchPort
	ps_switchPort -1
	ps_switchPort 6

	assert_success
	i_list="0 1 2 3 4 5" 
	for i in $i_list
		do
			ps_switchPort $i
			echo port=$port
	done
}

#ps_readReg_show()
ps_testcase2()
{
	assert_fail
	ps_readReg_show
	ps_readReg_show -1
	ps_readReg_show 6

	assert_success
	ps_readReg_show 1
	ps_readReg_show 5
}

ps_testcase3()
{
	assert_success

	i_list="0 1 2 3 4"  #XXX port 5 is used by tftp
	for i in $i_list
		do
		echo "port: $i"
		ps_readReg_show $i
		echo

		ps_set_FLOW_LINK_EN $i 1
		ps_get_FLOW_LINK_EN $i
		echo FLOW_LINK_EN:$?
		ps_set_FLOW_LINK_EN $i 0
		ps_get_FLOW_LINK_EN $i
		echo FLOW_LINK_EN:$?
		ps_set_FLOW_LINK_EN $i 1
		ps_get_FLOW_LINK_EN $i
		echo FLOW_LINK_EN:$?
		echo

		ps_get_LINK_ASYN_PAUSE_EN $i
		echo LINK_ASYN_PAUSE_EN:$?
		echo

		ps_get_LINK_PAUSE_EN $i
		echo LINK_PAUSE_EN:$?
		echo

		ps_set_LINK_EN $i 1
		ps_get_LINK_EN $i
		echo LINK_EN:$?
		ps_set_LINK_EN $i 0
		ps_get_LINK_EN $i
		echo LINK_EN:$?
		ps_set_LINK_EN $i 1
		ps_get_LINK_EN $i
		echo LINK_EN:$?
		echo

		ps_get_LINK $i
		echo LINK:$?
		echo

		ps_set_TX_HALF_FLOW_EN $i 1
		ps_get_TX_HALF_FLOW_EN $i
		echo TX_HALF_FLOW_EN:$?
		ps_set_TX_HALF_FLOW_EN $i 0
		ps_get_TX_HALF_FLOW_EN $i
		echo TX_HALF_FLOW_EN:$?
		ps_set_TX_HALF_FLOW_EN $i 1
		ps_get_TX_HALF_FLOW_EN $i
		echo TX_HALF_FLOW_EN:$?
		echo

		ps_set_DUPLEX_MODE $i 1
		ps_get_DUPLEX_MODE $i 
		echo DUPLEX_MODE:$?
		ps_set_DUPLEX_MODE $i 0
		ps_get_DUPLEX_MODE $i 
		echo DUPLEX_MODE:$?
		echo

		ps_set_RX_FLOW_EN $i 1
		ps_get_RX_FLOW_EN $i
		echo RX_FLOW_EN:$?
		ps_set_RX_FLOW_EN $i 0
		ps_get_RX_FLOW_EN $i
		echo RX_FLOW_EN:$?
		echo

		ps_set_TX_FLOW_EN $i 1
		ps_get_TX_FLOW_EN $i
		echo TX_FLOW_EN:$?
		ps_set_TX_FLOW_EN $i 0
		ps_get_TX_FLOW_EN $i
		echo TX_FLOW_EN:$?
		echo

		ps_set_RXMAC_EN $i 1
		ps_get_RXMAC_EN $i
		echo RXMAC_EN:$?
		ps_set_RXMAC_EN $i 0
		ps_get_RXMAC_EN $i
		echo RXMAC_EN:$?
		echo

		ps_set_TXMAC_EN $i 1
		ps_get_TXMAC_EN $i
		echo TXMAC_EN:$?
		ps_set_TXMAC_EN $i 0
		ps_get_TXMAC_EN $i
		echo TXMAC_EN:$?
		echo
		
		j_list="0 1 2 3 0"
		for j in $j_list
			do
				ps_set_SPEED $i $j
				ps_get_SPEED $i
				echo SPEED:$bits
		done
		echo

	done

}

ps_testsuite()
{
#	ps_testcase1;echo
#	ps_testcase2;echo
	ps_testcase3;echo
}
