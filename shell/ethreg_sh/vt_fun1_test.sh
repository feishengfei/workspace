#!/bin/sh
. $topdir/t_util.sh
. $topdir/vt_fun1.sh

#on/off port VID_MEM
vt_fun1_testcase1() 
{
	assert_fail
	vt_fun1_vlan_on_port
	vt_fun1_vlan_on_port -1
	vt_fun1_vlan_on_port 7

	assert_success
	i_list="1 2 3 4 5 6" 
	for i in $i_list
		do
			vt_fun1_vlan_on_port $i
			readReg $VT_FUNCTION1 show
	done

	assert_fail
	vt_fun1_vlan_off_port
	vt_fun1_vlan_off_port -1
	vt_fun1_vlan_off_port 7

	assert_success
	i_list="1 2 3 4 5 6" 
	for i in $i_list
		do
			vt_fun1_vlan_off_port $i
			readReg $VT_FUNCTION1 show
	done
}

#set VT_VALID
vt_fun1_testcase2() 
{
	assert_fail

	vt_fun1_set_VT_VALID
	vt_fun1_set_VT_VALID on
	vt_fun1_set_VT_VALID off 

	assert_success	

	vt_fun1_set_VT_VALID 0 
	readReg $VT_FUNCTION1 show
	vt_fun1_set_VT_VALID 1
	readReg $VT_FUNCTION1 show
}

#get VT_VALID
vt_fun1_testcase3()
{
	vt_fun1_set_VT_VALID 1 
	vt_fun1_get_VT_VALID
	echo get:$?
	vt_fun1_set_VT_VALID 0 
	vt_fun1_get_VT_VALID
	echo get:$?
	vt_fun1_set_VT_VALID 1 
	vt_fun1_get_VT_VALID
	echo get:$?
}

vt_fun1_testsuite()
{
#	vt_fun1_testcase1;echo
#	vt_fun1_testcase2;echo
	vt_fun1_testcase3;echo
}
