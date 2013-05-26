#!/bin/sh
. $topdir/t_util.sh
. $topdir/port_control.sh
pc_testcase1()
{
	assert_success
	i_list="0 1 2 3 4 5"  #XXX port 5 is used by tftp

	for i in $i_list
	do
		echo "port: $i"
		pc_readReg_show $i
		echo

		pc_set_EAPOL_EN $i 1
		pc_get_EAPOL_EN $i
		echo EAPOL_EN:$?
		pc_set_EAPOL_EN $i 0
		pc_get_EAPOL_EN $i
		echo EAPOL_EN:$?
		echo

		pc_set_ARP_LEAKY_EN $i 1
		pc_get_ARP_LEAKY_EN $i
		echo ARP_LEAKY_EN:$?
		pc_set_ARP_LEAKY_EN $i 0
		pc_get_ARP_LEAKY_EN $i
		echo ARP_LEAKY_EN:$?
		echo

		pc_set_IGMP_LEAVE_EN $i 1
		pc_get_IGMP_LEAVE_EN $i
		echo IGMP_LEAVE_EN:$?
		pc_set_IGMP_LEAVE_EN $i 0
		pc_get_IGMP_LEAVE_EN $i
		echo IGMP_LEAVE_EN:$?
		echo

		pc_set_IGMP_JOIN_EN $i 1
		pc_get_IGMP_JOIN_EN $i
		echo IGMP_JOIN_EN:$?
		pc_set_IGMP_JOIN_EN $i 0
		pc_get_IGMP_JOIN_EN $i
		echo IGMP_JOIN_EN:$?
		echo

		pc_set_DHCP_EN $i 1
		pc_get_DHCP_EN $i
		echo DHCP_EN:$?
		pc_set_DHCP_EN $i 0
		pc_get_DHCP_EN $i
		echo DHCP_EN:$?
		echo

		pc_set_IPG_DEC_EN $i 1
		pc_get_IPG_DEC_EN $i
		echo IPG_DEC_EN:$?
		pc_set_IPG_DEC_EN $i 0
		pc_get_IPG_DEC_EN $i
		echo IPG_DEC_EN:$?
		echo

		pc_set_ING_MIRROR_EN $i 1
		pc_get_ING_MIRROR_EN $i
		echo ING_MIRROR_EN:$?		
		pc_set_ING_MIRROR_EN $i 0
		pc_get_ING_MIRROR_EN $i
		echo ING_MIRROR_EN:$?		
		echo

		pc_set_EG_MIRROR_EN $i 1
		pc_get_EG_MIRROR_EN $i
		echo EG_MIRROR_EN:$?		
		pc_set_EG_MIRROR_EN $i 0
		pc_get_EG_MIRROR_EN $i
		echo EG_MIRROR_EN:$?		
		echo
	
		pc_set_LEARN_EN $i 1
		pc_get_LEARN_EN $i
		echo LEARN_EN:$?
		pc_set_LEARN_EN $i 0
		pc_get_LEARN_EN $i
		echo LEARN_EN:$?
		echo

		pc_set_MAC_LOOP_BACK $i 1
		pc_get_MAC_LOOP_BACK $i
		echo MAC_LOOP_BACK:$?
		pc_set_MAC_LOOP_BACK $i 0
		pc_get_MAC_LOOP_BACK $i
		echo MAC_LOOP_BACK:$?
		echo

		pc_set_HEAD_EN $i 1
		pc_get_HEAD_EN $i
		echo HEAD_EN:$?	
		pc_set_HEAD_EN $i 0
		pc_get_HEAD_EN $i
		echo HEAD_EN:$?	
		echo
		
		pc_set_IGMP_MLD_EN $i 1
		pc_get_IGMP_MLD_EN $i
		echo IGMP_MLD_EN:$?
		pc_set_IGMP_MLD_EN $i 0
		pc_get_IGMP_MLD_EN $i
		echo IGMP_MLD_EN:$?
		echo

		j_list="0 1 2"
		for j in $j_list
		do
			pc_set_EG_VLAN_MODE $i $j
			pc_get_EG_VLAN_MODE $i
			echo EG_VLAN_MODE:$bits
		done
		echo

		pc_set_LEARN_ONE_LOCK $i 1
		pc_get_LEARN_ONE_LOCK $i 
		echo LEARN_ONE_LOCK:$?
		pc_set_LEARN_ONE_LOCK $i 0
		pc_get_LEARN_ONE_LOCK $i 
		echo LEARN_ONE_LOCK:$?
		echo

		pc_set_PORT_LOCK_EN $i 1
		pc_get_PORT_LOCK_EN $i
		echo PORT_LOCK_EN:$?
		pc_set_PORT_LOCK_EN $i 0
		pc_get_PORT_LOCK_EN $i
		echo PORT_LOCK_EN:$?
		echo

		pc_set_LOCK_DROP_EN $i 1
		pc_get_LOCK_DROP_EN $i
		echo LOCK_DROP_EN:$?
		pc_set_LOCK_DROP_EN $i 0
		pc_get_LOCK_DROP_EN $i
		echo LOCK_DROP_EN:$?
		echo

		j_list="0 1 2 3 4"
		for j in $j_list
		do
			pc_set_PORT_STATE $i $j
			pc_get_PORT_STATE $i
			echo EG_VLAN_MODE:$bits
		done
		echo

	done
}

pc_testsuite()
{
	pc_testcase1;echo
}
