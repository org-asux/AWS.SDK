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

echo "!!!  !!! The OLD script is deprecated.   Pay attention!!! instructions coming below .."; echo ''
sleep 3

echo "We now will take advantage of the STANDARDIZATION that AWS now has finalized."
echo "We lookup EC2 AMIs for AMZN2Linux using the LookupKey=amzn2-ami-hvm-x86_64-gp2"
echo ''
echo "https://aws.amazon.com/blogs/compute/query-for-the-latest-amazon-linux-ami-ids-using-aws-systems-manager-parameter-store/"
echo "WEB PAGE TITLE: Query for the latest Amazon Linux AMI IDs using AWS Systems Manager Parameter Store"
echo ''
		### !! Note !! That value of this variable is __CONFIRMED__ by another script.
		###			AWS/CFN/bin/AWS-AMI-list-by-Region.sh         <AWSRegion>
		###	!! Note !! In the output of the above command .. !!!!!!! Remove the VERSION#.DATE !!!!!!!! from the middle of the AMI-TYPE, to get the EC2AMI_LookupKey
		###	Example:  amzn2-ami-hvm-2.0.20190618-x86_64-gp2   --> becomes --> amzn2-ami-hvm-x86_64-gp2






exit 1





###=============================================================
###@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###=============================================================

CACHEDOUTPUT="${AWSCFNHOME}/config/inputs/AMZNLinuxAMI-${AWSLocation}.txt"
if [ -e ${CACHEDOUTPUT} ] && [ -s ${CACHEDOUTPUT} ]; then  ### if [ -z "$var" ]    <-- does NOT distinguish between 'unset var' & var=""
	cat ${CACHEDOUTPUT}
	exit 0 ### !!!!!!!!!! ATTENTION !!!!!!!!!! Script exits here.
else
	unset NOTHING; # Do nothing.
fi

###-------------------
### On Non-Linux (Non-GNU) UNIX systems, like MacOS/Solaris, 'gdate' is GNU's sophisticated implementation of 'date-manipulation'
command -v gdate > /dev/null
if [ $? -ne 0 ]; then
	DATECMD=date
else
	DATECMD=gdate
fi

YYYY=`${DATECMD} -d "-31 days" +"%Y"`
MM=`${DATECMD} -d "-31 days" +"%m"`
YYYYMM=${YYYY}${MM}
#____ echo $YYYYMM
### Now it should point to last month's 

###=============================================================
###@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###=============================================================

declare -a arr
arr=()
declare -a arrEcho
arrEcho=()

function addAWSParams() {
	arr=( "${arr[@]}" "Name=$1,Values=$2" );
	arrEcho=( "${arrEcho[@]}" "'Name=$1,Values=$2'" );
}
addAWSParams 'name' 'amzn*'
addAWSParams 'image-type' 'machine'
addAWSParams 'is-public' 'true'
addAWSParams 'owner-alias' 'amazon'
addAWSParams 'state' 'available'
addAWSParams 'virtualization-type' 'hvm'
#___ addAWSParams 'platform' 'linux' ### Platform -> The value is Windows for Windows AMIs; otherwise blank.
#___ echo "${arr[@]}"

###=============================================================
###@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###=============================================================

### https://aws.amazon.com/blogs/compute/query-for-the-latest-amazon-linux-ami-ids-using-aws-systems-manager-parameter-store/
### aws ec2 describe-images --owners amazon --filters "Name=name,Values=amzn*" --query 'sort_by(Images, &CreationDate)[].Name'

### echo aws ec2 describe-images --filters ${arrEcho[@]} --owners amazon --profile ${AWSprofile} --region ${AWSRegion} --query "'sort_by(Images, &CreationDate)[].Name'" 
### echo "tr '\\" "t' '\\""\n' | \\grep ${YYYYMM} | \\grep -v 'ecs' | \\grep -v nat | \\grep -v ".-arm" | \\grep 'gp2$' | \\grep 'amzn2'"

aws ec2 describe-images --filters ${arr[@]} --owners amazon --profile ${AWSprofile} --region ${AWSRegion} --query 'sort_by(Images, &CreationDate)[].Name' | tr '\t' '\n' | \grep ${YYYYMM} | \grep -v 'ecs' | \grep -v nat | \grep -v ".-arm" | \grep 'gp2$' | \grep 'amzn2' > /tmp/$$
sed -e "s/^/${AWSRegion}LinuxAMI=/" < /tmp/$$
sed -e "s/^/${AWSLocation}LinuxAMI=/" < /tmp/$$
\rm /tmp/$$

### NOTE: ebs -vs- gp2
### AMI's ending in 'gp2' implies General Purpose SSD (gp2) 
### AMI's ending in 'ebs' implies traditional Magnetic Storage Disk-drives
### AMI's ending in 'io1' implies Provisioned IOPS SSD (io1)	
### AMI's ending in 'st1': traditional Magnetic Storage Disk-drives based volumes .. but, Throughput Optimized HDD (st1) for frequently accessed, throughput intensive workloads 
### AMI's ending in 'sc1': The lowest-cost Cold HDD (sc1) for less frequently accessed data.

#EoF
