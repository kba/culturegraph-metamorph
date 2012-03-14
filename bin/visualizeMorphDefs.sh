#!/bin/bash

if [ $# -lt 2 ]
then
  echo "Usage: `basename $0` OUTPUT_FORMAT DOT_FILES+"
  exit 65
fi


FORMAT=$1
shift
for FILE in $@
do
    echo visualizing $FILE
    java org.culturegraph.metamorph.Visualize $FILE | dot -T$FORMAT > $FILE.$FORMAT
done