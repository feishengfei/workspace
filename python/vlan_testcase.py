#!/usr/bin/python

port_list = [ (port_mod, port_vid, port_tag, port_al1, port_al2) \
			for port_mod in range(2) \
			for port_vid in range(1, 4) \
			for port_tag in range(2) \
			for port_al1 in range(4) \
			for port_al2 in range(4) \
			if ( (port_mod == 0) & (port_tag == 0) & (port_al1 == 0) & (port_al2 == 0) ) \
			 | (port_mod == 1) ]

tc_list = [ (tc_lan1[0], tc_lan1[1], tc_lan1[2], tc_lan1[3], tc_lan1[4], \
			tc_lan2[0], tc_lan2[1], tc_lan2[2], tc_lan2[3], tc_lan2[4], \
			tc_up[0], tc_up[1], tc_up[2], tc_up[3], tc_up[4]) \
			for tc_lan1 in port_list \
			for tc_lan2 in port_list \
			for tc_up in port_list ]
lan1_al1 = ''
lan1_al2 = ''

lan2_al1 = ''
lan2_al2 = ''

up_al1 = ''
up_al2 = ''

file = open("vlan.csv", 'w')
for tc in tc_list:

#	lan 1
    if tc[3] == 0 :
        lan1_al1 = ''
    else :
        lan1_al1 = str(tc[3])
    if tc[4] == 0 :
        lan1_al2 = ''
    else :
        lan1_al2 = str(tc[4])

#	lan 2
    if tc[8] == 0 :
        lan2_al1 = ''
    else :
        lan2_al1 = str(tc[8])
    if tc[9] == 0 :
        lan2_al2 = ''
    else :
        lan2_al2 = str(tc[9])

#	lan 2
    if tc[13] == 0 :
        up_al1 = ''
    else :
        up_al1 = str(tc[13])
    if tc[14] == 0 :
        up_al2 = ''
    else :
        up_al2 = str(tc[14])

    file.write( str(tc[0]) + "," + str(tc[1]) + "," + str(tc[2]) + "," + lan1_al1 + "," + lan1_al2 + "," \
				+ str(tc[5]) + "," + str(tc[6]) + "," + str(tc[7]) + "," + lan2_al1 + "," + lan2_al2 + "," \
    			+ str(tc[10]) + "," + str(tc[11]) + "," + str(tc[12]) + "," + up_al1 + "," + up_al2 )
    file.write( '\r\n' )

file.close()
