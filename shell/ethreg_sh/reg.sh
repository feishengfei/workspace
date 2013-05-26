#!/bin/sh

nval=0			# reg val to write
val=0			# read reg val
port=''
mask=''
bits=''

readReg()
{
	val=`ethreg -i eth0 $1 | cut -d ' ' -f 5`
	if [ "$2" -a "$2" = "show" ]
		 then
		 echo -e "Read  Reg:     $1\t\t\t\t    $val"
	fi
}

genmask()
{
	ret=1
	if [ "$1" ]
		then
			if [ "$2" ]
				then
					if [ $(($1+$2)) -le 32 ]
					then
						local ii=$(($2-1))
						while [ $ii -gt 0 ];
						do
							ret=$(($ret<<1))
							ret=$(($ret + 1))
							ii=$(($ii-1))	
						done
					else
						echo "usage:genmask OFFSET <LEN>. (OFFSET+LEN < 32)"
						return 2
					fi
			fi
		else
			echo "usage:genmask OFFSET <LEN>. (OFFSET+LEN < 32)"
			return 1
	fi

	ret=$(($ret<<$1))
	mask=$ret
	return 0
}

setBits()
{
	#REGISTER($1) OFFSET($2) LEN($3) VAL($4)
	if [ "$1" -a "$2" -a "$3" -a "$4" ]
		then
			genmask $2 $3
			if [ $? -eq 0 ]
				then
					readReg $1
					val=$(($val & ~$mask))
					nval=$(($val | $4<<$2))
					ethreg -i eth0 $1=$nval
					return 0
			fi
		else
			echo "usage:setBits REGISTER OFFSET LEN VAL"
			return 1
	fi
}

getBits()
{
	#REGISTER($1) OFFSET($2) LEN($3) 
	if [ "$1" -a "$2" -a "$3" ]
		then
			genmask $2 $3
			if [ $? -eq 0 ]
				then
				readReg $1
				bits=$(($val & $mask))
				bits=$(($bits >> $2))
				return 0
			else
				echo "usage:getBits REGISTER OFFSET LEN"
				return 1
		fi
	else
		echo "usage:getBits REGISTER OFFSET LEN"
		return 1
	fi
}

setBit()
{
	#REGISTER($1) OFFSET($2) VAL($3)
	if [ "$1" -a "$2" -a "$3" ]
		then
			if [ $3 = 1 ]
				then
				readReg $1
				nval=$(($val | 1<<$2))
				ethreg -i eth0 $1=$nval
			elif [ $3 = 0 ]
				then
				readReg $1
				nval=$(( $val & ~(1<<$2) ))
				ethreg -i eth0 $1=$nval
			else
				echo "usage:setBit REGISTER OFFSET VAL(0/1)"
			fi
		else
			echo "usage:setBit REGISTER OFFSET VAL(0/1)"
	fi
}

getBit()
{
	#REGISTER($1) OFFSET($2)
	if [ "$1" -a "$2" ]
		then
			readReg $1
			ret=$(( $val & 1<<$2 ))
			if [ $(($ret>>$2)) -eq 1 ]
				then	
				return 1
			elif [ $ret = 0 ]
				then
				return 0
			fi
		else
			echo "usage:getBit REGISTER OFFSET"
			return 2
	fi
}
