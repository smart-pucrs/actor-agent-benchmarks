#!/bin/bash
pathname=`dirname "$0"`
cd "$pathname"
export LD_LIBRARY_PATH=../../../lib/goalLibs/swifiles/libs/
export SWI_HOME_DIR=../../../lib/goalLibs/swifiles/
java -cp ../../../lib/goalLibs/goal.jar -Djava.library.path=../../../lib/goalLibs/swifiles/libs/ goal.tools.SingleRun ./token1.mas2g
