#!/bin/tcsh -f

###------------------------------
if (  !   $?ORGASUXFLDR ) then
        which asux >& /dev/null
        if ( $status == 0 ) then
                set ORGASUXFLDR=`which asux`
                set ORGASUXFLDR=$ORGASUXFLDR:h
                if ( "${ORGASUXFLDR}" == "." ) set ORGASUXFLDR=$cwd
                setenv ORGASUXFLDR "${ORGASUXFLDR}"
                echo "ORGASUXFLDR=$ORGASUXFLDR"
        else
                foreach FLDR ( ~/org.ASUX   ~/github/org.ASUX   ~/github.com/org.ASUX  /mnt/development/src/org.ASUX     /opt/org.ASUX  /tmp/org.ASUX  )
                        set ORIGPATH=( $path )
                        if ( -x "${FLDR}/asux" ) then
                                set ORGASUXFLDR="$FLDR"
                                set path=( $ORIGPATH "${ORGASUXFLDR}" )
                                rehash
                        endif
                end
                setenv ORGASUXFLDR "${ORGASUXFLDR}"
        endif
endif

###------------------------------
source ${ORGASUXFLDR}/test/testAll-common.csh-source

echo OFFLINE=${OFFLINE}

###------------------------------
set PROJECTNAME=AWS-SDK
set PROJECTPATH="${ORGASUXFLDR}/AWS/${PROJECTNAME}"

set TESTSRCFLDR=${PROJECTPATH}/test
chdir ${TESTSRCFLDR}
if ( "$VERBOSE" != "" ) pwd

set TEMPLATEFLDR=${TESTSRCFLDR}/outputs${OFFLINE}
set OUTPUTFLDR=/tmp/test-output-${PROJECTNAME}${OFFLINE}

\rm -rf ${OUTPUTFLDR}
mkdir -p ${OUTPUTFLDR}

###------------------------------
set TESTNUM=0

# 1
@ TESTNUM = $TESTNUM + 1
set OUTPFILE=${OUTPUTFLDR}/test-${TESTNUM}
echo $OUTPFILE
asux ${VERBOSE} ${OFFLINE} aws sdk delete-key-pair ap-south-1 testSSHKeyPair2 --no-quote  >&! ${OUTPFILE}.stdout  ### Ignore this output.
asux ${VERBOSE} ${OFFLINE} aws.sdk create-key-pair ap-south-1 testSSHKeyPair2 --no-quote  >&! ${OUTPFILE}.stdout
diff ${OUTPFILE}.stdout ${TEMPLATEFLDR}/test-${TESTNUM}.stdout

# 2
@ TESTNUM = $TESTNUM + 1
set OUTPFILE=${OUTPUTFLDR}/test-${TESTNUM}
echo $OUTPFILE
asux ${VERBOSE} ${OFFLINE} aws.sdk describe-key-pairs ap-northeast-1 null --no-quote  >&! ${OUTPFILE}.stdout
diff ${OUTPFILE}.stdout ${TEMPLATEFLDR}/test-${TESTNUM}.stdout

# 3
@ TESTNUM = $TESTNUM + 1
set OUTPFILE=${OUTPUTFLDR}/test-${TESTNUM}
echo $OUTPFILE
asux ${VERBOSE} ${OFFLINE} aws sdk delete-key-pair ap-south-1 testSSHKeyPair2 --no-quote  >&! ${OUTPFILE}.stdout
diff ${OUTPFILE}.stdout ${TEMPLATEFLDR}/test-${TESTNUM}.stdout

# 4
@ TESTNUM = $TESTNUM + 1
set OUTPFILE=${OUTPUTFLDR}/test-${TESTNUM}
echo $OUTPFILE
asux ${VERBOSE} ${OFFLINE} aws sdk describe-key-pairs ap-northeast-1 Tokyo-org-ASUX-Playground-LinuxSSH.pem --no-quote  >&! ${OUTPFILE}.stdout
diff ${OUTPFILE}.stdout ${TEMPLATEFLDR}/test-${TESTNUM}.stdout

# 5
@ TESTNUM = $TESTNUM + 1
set OUTPFILE=${OUTPUTFLDR}/test-${TESTNUM}
echo $OUTPFILE
asux ${VERBOSE} ${OFFLINE} aws.sdk get-vpc-id ap-northeast-1 --no-quote  >&! ${OUTPFILE}.stdout
diff ${OUTPFILE}.stdout ${TEMPLATEFLDR}/test-${TESTNUM}.stdout

###---------------------------------

# @ TESTNUM = $TESTNUM + 1
# set OUTPFILE=${OUTPUTFLDR}/test-${TESTNUM}
# echo $OUTPFILE
#     \
        # -o ${OUTPFILE} >! ${OUTPFILE}.stdout
# diff ${OUTPFILE} ${TEMPLATEFLDR}/test-${TESTNUM}
# diff ${OUTPFILE}.stdout ${TEMPLATEFLDR}/test-${TESTNUM}.stdout

# @ TESTNUM = $TESTNUM + 1
# set OUTPFILE=${OUTPUTFLDR}/test-${TESTNUM}
# echo $OUTPFILE
#     \
        # -o ${OUTPFILE} >! ${OUTPFILE}.stdout
# diff ${OUTPFILE} ${TEMPLATEFLDR}/test-${TESTNUM}
# diff ${OUTPFILE}.stdout ${TEMPLATEFLDR}/test-${TESTNUM}.stdout

###---------------------------------

set noglob
echo 'foreach fn ( ${OUTPUTFLDR}/* )'
echo '       diff ./outputs/$fn:t $fn'
echo end
###---------------------------------
#EoScript
