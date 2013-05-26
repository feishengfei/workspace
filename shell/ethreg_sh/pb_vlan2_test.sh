#!/bin/sh
. $topdir/t_util.sh
. $topdir/pb_vlan2.sh

pb_vlan2_testcase1()
{
	assert_success
	i_list="0 1 2 3 4 5"  

	for i in $i_list
	do
		echo "port: $i"
		pb_vlan2_readReg_show $i
		echo

		j_list="0 1 2 3 0"
		for j in $j_list
		do
			pb_vlan2_set_DOT1Q_MODE $i $j
			pb_vlan2_get_DOT1Q_MODE $i
			echo DOT1Q_MODE:$bits
		done
		echo

		j_list="0 1 2 3 0"
		for j in $j_list
		do
			pb_vlan2_set_ING_VLAN_MODE $i $j
			pb_vlan2_get_ING_VLAN_MODE $i
			echo ING_VLAN_MODE:$bits
		done
		echo

		case "$i" in
		0)
			j_list="0 1 3 7 15 31 63 $((0x7e))";;
		1)
			j_list="0 1 3 7 15 31 63 $((0x7d))";;
		2)
			j_list="0 1 3 7 15 31 63 $((0x7b))";;
		3)
			j_list="0 1 3 7 15 31 63 $((0x77))";;
		4)
			j_list="0 1 3 7 15 31 63 $((0x6f))";;
		5)
			j_list="0 1 3 7 15 31 63 $((0x5f))";;
		esac

		for j in $j_list
		do
			pb_vlan2_set_PORT_VID_MEM $i $j
			pb_vlan2_get_PORT_VID_MEM $i 
			echo PORT_VID_MEM:$bits
		done
		echo

		pb_vlan2_set_CORE_PORT_EN $i 1
		pb_vlan2_get_CORE_PORT_EN $i
		echo CORE_PORT_EN:$?
		pb_vlan2_set_CORE_PORT_EN $i 0
		pb_vlan2_get_CORE_PORT_EN $i
		echo CORE_PORT_EN:$?
		echo

		pb_vlan2_set_VLAN_PRI_PRO_EN $i 1
		pb_vlan2_get_VLAN_PRI_PRO_EN $i
		echo VLAN_PRI_PRO_EN:$?
		pb_vlan2_set_VLAN_PRI_PRO_EN $i 0
		pb_vlan2_get_VLAN_PRI_PRO_EN $i
		echo VLAN_PRI_PRO_EN:$?
		echo

		pb_vlan2_set_UNI_LEAKY_EN $i 1
		pb_vlan2_get_UNI_LEAKY_EN $i
		echo UNI_LEAKY_EN:$?
		pb_vlan2_set_UNI_LEAKY_EN $i 0
		pb_vlan2_get_UNI_LEAKY_EN $i
		echo UNI_LEAKY_EN:$?
		echo

		pb_vlan2_set_MULTI_LEAKY_EN $i 1
		pb_vlan2_get_MULTI_LEAKY_EN $i
		echo MULTI_LEAKY_EN:$?
		pb_vlan2_set_MULTI_LEAKY_EN $i 0
		pb_vlan2_get_MULTI_LEAKY_EN $i
		echo MULTI_LEAKY_EN:$?
		echo

	done
}

pb_vlan2_testsuite()
{
	pb_vlan2_testcase1
}
