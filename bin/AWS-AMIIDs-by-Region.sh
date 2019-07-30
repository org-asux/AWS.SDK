#!/bin/sh -f

###------------------------------
### You need to run this _ONCE_ for _EACH_ region!
if [ $# -eq 1 ]; then
	AWSRegion=$1
else
	>&2 echo "Usage: $0 us-east-2"
	>&2 echo ''
	>&2 echo "You need to run this _ONCE_ for _EACH_ region!"
	>&2 echo ''
	exit 1
fi

unset VERBOSE

###------------------------------

### The following line did NOT work on Windows
# CmdPathGuess="${BASH_SOURCE[0]}"

CmdPathGuess="$0"
# echo $CmdPathGuess
SCRIPTFLDR_RELATIVE="$(dirname "$CmdPathGuess")"
SCRIPTFULLFLDRPATH="$( cd "$(dirname "$0")" ; pwd -P )"
if [ "${VERBOSE}" == "1" ]; then echo SCRIPTFULLFLDRPATH=${SCRIPTFULLFLDRPATH}; fi


### Attention !!!!!!!!! SCRIPTFULLFLDRPATH must be set .. before sourcing/including '{SCRIPTFULLFLDRPATH}/../../bin.common.sh'
.   ${SCRIPTFULLFLDRPATH}/common.sh

###=============================================================
###@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###=============================================================


NEWFILE="${AWSSDKHOME}/etc/offline-downloads/AMZNLinux2_AMI_ID-${AWSLocation}".txt
TMPFILE=/tmp/t.txt
TMPFILE222=/tmp/t222.txt
# TMPFILE999=/tmp/t999.txt

###-------------------
if [ -e "${NEWFILE}" ]; then
	>&2 echo "${NEWFILE} already exists."
	>&2 echo "Please check the contents of the file and delete it if necessary"
	>&2 echo "Cntl-C now.. else, it will be overwritten !!!!!!!!!"
	sleep 10
fi


ensureTempFileDoesNotExist ${TMPFILE}		### This function is defined within ${ORGASUXHOME}/bin/common.sh
ensureTempFileDoesNotExist ${TMPFILE222}

###=============================================================
###@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###=============================================================

echo "!! If the _FOLLOWING__ __FAILS__ .. then .. Need your _MANUAL_HELP_ in Querying AWS to figure out .. what the AMI-ID for amzn2-ami-hvm-x86_64-gp2 is, in the Location ${AWSRegion}."
echo ''
sleep 5
### https://aws.amazon.com/blogs/compute/query-for-the-latest-amazon-linux-ami-ids-using-aws-systems-manager-parameter-store/
echo "aws ssm get-parameters --names '/aws/service/ami-amazon-linux-latest/amzn2-ami-hvm-x86_64-gp2' --region ${AWSRegion} --profile ${AWSprofile} --output json"
echo "asux yaml batch 'useAsInput @/tmp/o.json ; yaml --read Parameters,0,Value --delimiter ,' --no-quote -i /dev/null -o '${AWSSDKHOME}/etc/offline-downloads/AMZNLinux2_AMI_ID-${AWSLocation}'"
echo ''


###=============================================================
###@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###=============================================================

### https://aws.amazon.com/blogs/compute/query-for-the-latest-amazon-linux-ami-ids-using-aws-systems-manager-parameter-store/

aws ssm get-parameters --names '/aws/service/ami-amazon-linux-latest/amzn2-ami-hvm-x86_64-gp2' --region ${AWSRegion} --profile ${AWSprofile} --output json > /tmp/o.json
asux yaml batch 'useAsInput @/tmp/o.json ; yaml --read Parameters,0,Value --delimiter ,' --no-quote -i /dev/null -o ${TMPFILE222}

###------------------------------
### Create a new java.util.Properties compatible file containing a SINGLE line like:   AWSAMIID=ami-8c1be5f6
printf 'AWSAMIID=' > ${TMPFILE}
cat ${TMPFILE222} >> ${TMPFILE}

###------------------------------
if [ -e "${NEWFILE}" ]; then
	diff -w          "${NEWFILE}"    "${TMPFILE}" > /dev/null
	if [ $? != 0 ]; then
		>&2 echo "File already exists and __IS__ DIFFERENT !!!!!!!  Please check the contents of the ${NEWFILE} and delete it if necessary"
		>&2 echo "Cntl-C now.. OR else, it will be overwritten !!!!!!!!!";
		read -p "Cntl-C now >>" BLACKHOLEVARIABLE
		mv -i      "${TMPFILE}"     "${NEWFILE}"
	else
		>&2 echo "No changed made."
		>&2 echo "Nothing new (from AWS-APIs) to change the contents of ${NEWFILE}"
	fi
else
	mv -i      "${TMPFILE}"     "${NEWFILE}"
	cat "${NEWFILE}"
fi

###------------------------------
### Cleanup
rm -f ${TMPFILE}
rm -f ${TMPFILE222}

#EoF
