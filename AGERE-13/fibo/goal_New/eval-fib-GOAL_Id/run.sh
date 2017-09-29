#!/bin/bash
pathname=`dirname "$0"`
cd "$pathname"
export LD_LIBRARY_PATH=../../../lib/goalLibsNew/swifiles/libs/
export SWI_HOME_DIR=../../../lib/goalLibsNew/swifiles/
java -cp ../../../lib/goalLibsNew/goal.jar -Djava.library.path=../../../lib/goalLibs/swifiles/libs/ goal.tools.Run ./fib.mas2g
./prepareData < output.txt > data.csv
rm output.txt
