pathname=`dirname "$0"`
cd "$pathname"
cd ../../../lib/2aplLibsNew
java -jar 2apl.jar -nogui -nojade ../../fibo/2apl_New/eval-fib-2apl_Id/fib.mas > ../../fibo/2apl_New/eval-fib-2apl_Id/output.txt
cd ../../fibo/2apl_New/eval-fib-2apl_Id/
./prepareData < output.txt > data.csv
rm output.txt
