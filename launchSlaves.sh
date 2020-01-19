#!/bin/bash
master=$0
i=0
echo $0
echo $1
for node in "$@"
do
  oarsh node
  java Slave $i
done