#!/bin/sh
. $topdir/t_util.sh
. $topdir/vt_fun0.sh

#VT_PRI
vt_fun0_testcase1()
{
	assert_fail
	vt_fun0_set_VT_PRI 
	vt_fun0_set_VT_PRI -1
	vt_fun0_set_VT_PRI 8

	assert_success
	i_list="1 2 3 4 5 6 7"
	for i in $i_list
		do
			vt_fun0_set_VT_PRI $i
			readReg $VT_FUNCTION0 show
	done
}

#VID
vt_fun0_testcase2()
{
	assert_fail
	vt_fun0_set_VID
	vt_fun0_set_VID -1
	vt_fun0_set_VID 0
	vt_fun0_set_VID 4095
	vt_fun0_set_VID 4096

	assert_success
	i_list="1 3 7 15 31 63 127 255 511 1023 2047 4094"
	for i in $i_list
		do
			vt_fun0_set_VID $i
			readReg $VT_FUNCTION0 show
	done
}

#VT_PORT_NUM
vt_fun0_testcase3()
{
	assert_fail
	vt_fun0_set_VT_PORT_NUM
	vt_fun0_set_VT_PORT_NUM -1
	vt_fun0_set_VT_PORT_NUM 16

	assert_success
	i_list="1 3 7 15" 
	for i in $i_list
		do
			vt_fun0_set_VT_PORT_NUM $i
			readReg $VT_FUNCTION0 show
	done
}

#VT_FUNC
vt_fun0_testcase4()
{
	assert_fail
	vt_fun0_set_VT_FUNC
	vt_fun0_set_VT_FUNC -1
	vt_fun0_set_VT_FUNC 8

	assert_success
	i_list="1 3 7" 
	for i in $i_list
		do
			vt_fun0_set_VT_FUNC $i
			readReg $VT_FUNCTION0 show
	done
}


vt_fun0_testsuite()
{
#	vt_fun0_testcase1;echo
	vt_fun0_testcase2;echo
#	vt_fun0_testcase3;echo
#	vt_fun0_testcase4;echo
}
