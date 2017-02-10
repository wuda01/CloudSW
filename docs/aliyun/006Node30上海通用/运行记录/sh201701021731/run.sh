#!/bin/sh

alias cp='cp'
mkdir -p /home/hadoop/lib
osscmd --id=YZs7tB9ljH3trBEX --key=PC6ChUJYQoivwrMqu2bRziIQkcbB5g --host=oss-cn-hangzhou-internal.aliyuncs.com get oss://xubo245hangzhou/data/av1.1.1.tar.gz ./v1.1.1.tar.gz
osscmd --id=YZs7tB9ljH3trBEX --key=PC6ChUJYQoivwrMqu2bRziIQkcbB5g --host=oss-cn-hangzhou-internal.aliyuncs.com get oss://xubo245hangzhou/lib/libJNIparasail.so /home/hadoop/lib/libJNIparasail.so
osscmd --id=YZs7tB9ljH3trBEX --key=PC6ChUJYQoivwrMqu2bRziIQkcbB5g --host=oss-cn-hangzhou-internal.aliyuncs.com get oss://xubo245hangzhou/lib/libssw.so /home/hadoop/lib/libssw.so
osscmd --id=YZs7tB9ljH3trBEX --key=PC6ChUJYQoivwrMqu2bRziIQkcbB5g --host=oss-cn-hangzhou-internal.aliyuncs.com get oss://xubo245hangzhou/lib/libsswjni.so /home/hadoop/lib/libsswjni.so
osscmd --id=YZs7tB9ljH3trBEX --key=PC6ChUJYQoivwrMqu2bRziIQkcbB5g --host=oss-cn-hangzhou-internal.aliyuncs.com get oss://xubo245hangzhou/shell/dispatch.sh /home/hadoop/dispatch.sh
chmod 755 /home/hadoop/dispatch.sh
mkdir -p ./tools
tar -zxvf v1.1.1.tar.gz -C ./tools/
cd ./tools/parasail-1.1.1/
./configure
sudo make install
sudo chmod 777 /etc/profile
echo "export LD_LIBRARY_PATH=/home/hadoop/tools/parasail-1.1.1/:/home/hadoop/lib/:\$LD_LIBRARY_PATH" >> /etc/profile
source /etc/profile
cd

