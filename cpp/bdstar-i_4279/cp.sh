#!/bin/sh

TARGET=bdi-daemon

arm-linux-strip $MQSDK/runtime/$MQARCH/bin/$TARGET
cp $MQSDK/runtime/$MQARCH/bin/$TARGET /tmp
