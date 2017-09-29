#!/bin/bash
pathname=`dirname "$0"`
cd "$pathname"
export LD_LIBRARY_PATH=../../../lib/goalLibsNew2/swifiles/libs/
export SWI_HOME_DIR=../../../lib/goalLibsNew2/swifiles/
java -cp ../../../lib/goalLibsNew2/goal.jar -Djava.library.path=../../../lib/goalLibsNew2/swifiles/libs/ goal.tools.Run ./fib.mas2g
./prepareData < output.txt > data.csv
rm output.txt
