#!/bin/sh
. $topdir/t_util.sh
. $topdir/pb_vlan.sh

pb_vlan_testcase1()
{
	assert_success
	i_list="0 1 2 3 4 5"  #XXX port 5 is used by tftp

	for i in $i_list
	do
		echo "port: $i"
		pb_vlan_readReg_show $i
		echo

		j_list="0 1 2 3 4 5 6 7 0"
		for j in $j_list
		do
			pb_vlan_set_ING_PORT_PRI $i $j
			pb_vlan_get_ING_PORT_PRI $i
			echo ING_PORT_PRI:$bits
		done
		echo

		j_list="0 1 3 7 15 31 63 127 255 1023 4094 0"
		for j in $j_list
		do
			pb_vlan_set_PORT_DEFAULT_CVID $i $j
			pb_vlan_get_PORT_DEFAULT_CVID $i
			echo PORT_DEFAULT_CVID:$bits
		done
		echo

		j_list="0 1 3 7 15 31 63 127 255 1023 4094 0"
		for j in $j_list
		do
			pb_vlan_set_PORT_DEFAULT_SVID $i $j
			pb_vlan_get_PORT_DEFAULT_SVID $i
			echo PORT_DEFAULT_SVID:$bits
		done
		echo

		pb_vlan_set_FORCE_PORT_VLAN_EN $i 1
		pb_vlan_get_FORCE_PORT_VLAN_EN $i
		echo FORCE_PORT_VLAN_EN:$?
		pb_vlan_set_FORCE_PORT_VLAN_EN $i 0
		pb_vlan_get_FORCE_PORT_VLAN_EN $i
		echo FORCE_PORT_VLAN_EN:$?
		echo

		pb_vlan_set_PORT_CLONE_EN $i 1
		pb_vlan_get_PORT_CLONE_EN $i
		echo PORT_CLONE_EN:$?
		pb_vlan_set_PORT_CLONE_EN $i 0
		pb_vlan_get_PORT_CLONE_EN $i
		echo PORT_CLONE_EN:$?
		echo
		
		pb_vlan_set_PORT_VLAN_PROP_EN $i 1
		pb_vlan_get_PORT_VLAN_PROP_EN $i
		echo PORT_VLAN_PROP_EN:$?
		pb_vlan_set_PORT_VLAN_PROP_EN $i 0
		pb_vlan_get_PORT_VLAN_PROP_EN $i
		echo PORT_VLAN_PROP_EN:$?
		echo

		pb_vlan_set_PORT_TLS_MODE $i 1
		pb_vlan_get_PORT_TLS_MODE $i
		echo PORT_TLS_MODE:$?
		pb_vlan_set_PORT_TLS_MODE $i 0
		pb_vlan_get_PORT_TLS_MODE $i
		echo PORT_TLS_MODE:$?
		echo

		pb_vlan_set_FORCE_DEFAULT_VID_EN $i 1
		pb_vlan_get_FORCE_DEFAULT_VID_EN $i
		echo FORCE_DEFAULT_VID_EN:$?
		pb_vlan_set_FORCE_DEFAULT_VID_EN $i 0
		pb_vlan_get_FORCE_DEFAULT_VID_EN $i
		echo FORCE_DEFAULT_VID_EN:$?
		echo

	done
}

pb_vlan_testsuite()
{
	pb_vlan_testcase1
}
