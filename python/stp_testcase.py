#!/usr/bin/python
tc_list= [(hellotime, maxage, forwarddelay) \
			for hellotime in range(1,11) \
			for maxage in range(6,41) \
			for forwarddelay in range(4,31) \
			if (2*(forwarddelay-1)>=maxage)&(maxage>=2*( hellotime +1)) ]

#for tc in tc_list:
#    print tc[0], tc[1], tc[2]

file = open("stp.csv", 'w')
for tc in tc_list:
    file.write( str(tc[0]) + "," + str(tc[1]) + "," + str(tc[2]))
    file.write( '\r\n' )

file.close()
