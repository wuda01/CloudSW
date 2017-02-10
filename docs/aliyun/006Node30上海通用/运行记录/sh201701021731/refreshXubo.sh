rm -r  xubo
hadoop fs -get /xubo .
tar -zcvf 'xubo201701'$1'.tar.gz' xubo
sz 'xubo201701'$1'.tar.gz'
