#!/bin/bash
pathname=`dirname "$0"`
cd "$pathname"
export LD_LIBRARY_PATH=../../../lib/goalLibsNew/swifiles/libs/
export SWI_HOME_DIR=../../../lib/goalLibsNew/swifiles/
java -cp ../../../lib/goalLibsNew/goal.jar -Djava.library.path=../../../lib/goalLibsNew/swifiles/libs/ goal.tools.Run ./token1.mas2g
