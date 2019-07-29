#!/bin/false


## This must be sourced within other BASH files




### !!!!!!!!!! NOTE:  $0 is _NOT_ === this-file /aka/  {AWSSDKHOME}/bin/common.sh)"

###=============================================================
###@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###=============================================================

### Attention !!!!!!!!! SCRIPTFULLFLDRPATH must be set .. before sourcing/including '{SCRIPTFULLFLDRPATH}/../../bin.common.sh'
if [ -z ${SCRIPTFULLFLDRPATH+x} ]; then  ### if [ -z "$var" ]    <-- does NOT distinguish between 'unset var' & var=""
	>&2 echo "The topmost script that 'includes (a.k.a.) sources' common.sh must define SCRIPTFULLFLDRPATH ${SCRIPTFULLFLDRPATH}"
	kill -SIGUSR1 `ps --pid $$ -oppid=`
	exit $1
fi

ERRMSG1="Coding-ERROR: BEFORE sourcing common.sh, you MUST set variables like AWSRegion (=${AWSRegion}) and ORGASUXHOME (=${ORGASUXHOME}).   The best way is to run using 'asux.js aws .. .. ..' commands."
if [ -z ${ORGASUXHOME+x} ]; then  ### if [ -z "$var" ]    <-- does NOT distinguish between 'unset var' & var=""
	#__ echo SCRIPTFULLFLDRPATH=${SCRIPTFULLFLDRPATH}
	#__ ls -la ${SCRIPTFULLFLDRPATH}/../../..
	if [ -e ${SCRIPTFULLFLDRPATH}/../../../asux.js ] && [ -e ${SCRIPTFULLFLDRPATH}/../../../bin/common.sh ]; then
		export ORGASUXHOME=`cd ${SCRIPTFULLFLDRPATH}/../../..; pwd`		
		.  ${ORGASUXHOME}/bin/common.sh  ### <<--- This should be sourcing {ORGASUXHOME}/bin/common.sh, which will automatically set value of ORGASUXHOME
	else
		>&2 echo $ERRMSG1
		kill -SIGUSR1 `ps -p $$ -oppid=`	### On MacOS Shell, 'ps --pid' does _NOT_ work.  Instead using 'ps -p'
		exit $1
	fi
fi

###-------------------------------------

if [ -z ${AWSHOME+x} ]; then
	export AWSHOME=${ORGASUXHOME}/AWS
	if [ ! -d ${AWSHOME} ]; then
		>&2 echo "Uh! Oh! Looks like you have CUSTOMIZED the folder heirarchy of the ASUX.org projects.  Was expected the folder ${ORGASUXHOME}/AWS.  It is MISSING !!!!!!!"
		terminateEntireScriptProcess 10		### This function is defined within ${ORGASUXHOME}/bin/common.sh
	fi
fi

if [ -z ${AWSSDKHOME+x} ]; then
	export AWSSDKHOME=${ORGASUXHOME}/AWS/AWS-SDK
fi

###-------------------------------------

if [ -z ${AWSRegion+x} ]; then  ### if [ -z "$var" ]    <-- does NOT distinguish between 'unset var' & var=""
	>&2 echo $ERRMSG1
	terminateEntireScriptProcess 10			### This function is defined within ${ORGASUXHOME}/bin/common.sh
fi

###=============================================================
###@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###=============================================================

export PATH=${PATH}:${ORGASUXHOME}

if [ ! -e "${ORGASUXHOME}/asux.js" ]; then
	>&2 echo "Please edit this file $0 to set the correct value of 'ORGASUXHOME'"
	>&2 echo "	This command will fail until correction is made from current value of ${ORGASUXHOME}"
	terminateEntireScriptProcess 5			### This function is defined within ${ORGASUXHOME}/bin/common.sh
fi

###-------------------
if [ ! -e ${AWSprofile} ]; then
	if [ -z ${AWSprofile+x} ]; then  ### if [ -z "$AWSprofile" ]    <-- does NOT distinguish between 'unset AWSprofile' & AWSprofile=""
        echo "AWS login credentials missing in a file, and also AWSprofile is NOT set"
        terminateEntireScriptProcess 7		### This function is defined within ${ORGASUXHOME}/bin/common.sh
	fi
fi

###=============================================================
###@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###=============================================================

grep ${AWSRegion} ${AWSSDKHOME}/config/AWSRegionsLocations.properties | \sed -e 's/.*=/AWSLocation=/' > /tmp/$$
. /tmp/$$
\rm /tmp/$$

#_____ read -p "ATTENTION! Need manual help to convert AWS-REGION ${AWSRegion} into a Location.  Enter Location:>" AWSLocation
if [ "${VERBOSE}" == "1" ]; then
	echo "AWSRegion=${AWSRegion}"
	read -p "The official Location of AWS-REGION ${AWSRegion} is ${AWSLocation}.  Correct?" USERRESPONSE
fi

###=============================================================
###@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
###=============================================================

#EoF
