#!/bin/sh

# MAC0 RGMII
ethreg -i eth0 0x624=0x007f7f7f		#p133	Global Forward Control Register 1
ethreg -i eth0 0x10=0x40000000		#p63	Power-On Strapping Register 
ethreg -i eth0 0x4=0x07600000		#p59	Port 0 PAD MODE CTRL Regiters
ethreg -i eth0 0xc=0x01000000		#p62	Port 6 PAD MODE CTRL Regiters
ethreg -i eth0 0x7c=0x7e			#p75	Port 0 Status Register

# Recognize tag packet from CPU
ethreg -i eth0 0x620=0x000004f0		#p131	Global Forward Control Register
ethreg -i eth0 0x660=0x0014017e		#p139	PORT 0 Lookup Control Register
ethreg -i eth0 0x66c=0x0014017d		#p144	PORT 1 Lookup Control Register
ethreg -i eth0 0x678=0x0014017b		#p149	PORT 2 Lookup Control Register
ethreg -i eth0 0x684=0x00140177		#p153	PORT 3 Lookup Control Register
ethreg -i eth0 0x690=0x0014016f		#p158	PORT 4 Lookup Control Register
ethreg -i eth0 0x69c=0x0014015f		#p162	PORT 5 Lookup Control Register

# Insert PVID 1 to LAN ports
ethreg -i eth0 0x420=0x00010001		#p111	Port 0 VLAN Control Register 0
ethreg -i eth0 0x430=0x00010001		#p115	Port 2 VLAN Control Register 0
ethreg -i eth0 0x438=0x00010001		#p117	Port 3 VLAN Control Register 0
ethreg -i eth0 0x440=0x00010001		#p118	Port 4 VLAN Control Register 0
ethreg -i eth0 0x448=0x00010001		#p120	Port 5 VLAN Control Register 0

# Insert PVID 2 to WAN port 
ethreg -i eth0 0x428=0x00020001		#p113	Port 1 VLAN Control Register 0

# Egress tag packet to CPU and untagged packet to LAN port
ethreg -i eth0 0x424=0x00002040		#p112	Port 0 VLAN Control Register 1
ethreg -i eth0 0x42c=0x00001040		#p114	Port 1 VLAN Control Register 1
ethreg -i eth0 0x434=0x00001040		#p116	Port 2 VLAN Control Register 1
ethreg -i eth0 0x43c=0x00001040		#p117	Port 3 VLAN Control Register 1
ethreg -i eth0 0x444=0x00001040		#p119	Port 4 VLAN Control Register 1
ethreg -i eth0 0x44c=0x00001040		#p120	Port 5 VLAN Control Register 1

# Group port - 0,2,3,4,5 to VID 1 
ethreg -i eth0 0x610=0x001b55e0		#p128	VTU_FUNC_REG0 Register
# BUSY is changed to bit[31],need to modify register write driver
 ethreg -i eth0 0x614=0x80010002	#p129	VTU_FUNC_REG1 Register
#ethreg -i eth0 -p 0x18 0x0=0x3
#ethreg -i eth0 -p 0x10 0xa=0x0002
#ethreg -i eth0 -p 0x10 0xb=0x8001


#0000 011 10000 01010

# Group port - 0 and 1  to VID 2
ethreg -i eth0 0x610=0x0016ff60		#p128	VTU_FUNC_REG0 Register
# BUSY is changed to bit[31],need to modify register write driver
ethreg -i eth0 0x614=0x80020002		#p129	VTU_FUNC_REG1 Register
# ethreg -i eth0 -p 0x18 0x0=0x3
# ethreg -i eth0 -p 0x10 0xa=0x0002
# ethreg -i eth0 -p 0x10 0xb=0x8002

brctl delif br0 eth0
brctl delif br0 eth1
ifconfig br0 down

vconfig add eth0 1
vconfig add eth0 2
ifconfig eth0.1 up
ifconfig eth0.2 up

#ifconfig eth0.2 192.168.2.1

brctl addif br0 eth0.1
brctl addif br0 eth0.2
ifconfig br0 192.168.1.2

cfg -a AP_VLAN_MODE=1

echo 1 > /proc/sys/net/ipv4/ip_forward
