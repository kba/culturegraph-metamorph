#!/bin/bash

for FILE in $*
do
    echo visualizing $FILE
    java org.culturegraph.metamorph.Visualize $FILE | dot -Tsvg > $FILE.svg
done