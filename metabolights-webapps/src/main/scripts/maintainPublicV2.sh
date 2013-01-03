#!/bin/ksh
##########################################################################
#
#   File    : maintainPublic.sh
#   Type    : Korn Shell Script
#
#   Purpose : Check the database for recently modified studies and moving
#             archive files to the public ftp location
#
# 20110831  : Ken Haug - Created script
# 20110912  : Ken Haug - Simplified and more comprehensive file checks
# 20110914  : Ken Haug - Changed email notifications to Metabolights-dev@ebi.ac.uk
# 20120328  : Ken Haug - Removed "clear" as this is dependent on a TERM set, like xterm
# 20121115  : Ken Haug - Move folder as we no longer store the studies as zip files
# 20130103  : Ken Haug - Simplified to use the new queue system
#
##########################################################################

. /nfs/production/panda/metabolights/source/metabolights/metabolights-webapps/src/main/scripts/logging_functions
source /homes/oracle/ora11setup.sh

#################################
#  Configurable Options Follow  #
#################################

EMAILTO=metabolights-dev@ebi.ac.uk
PROPS_FILE=/nfs/production/panda/metabolights/source/metabolights/metabolights-webapps/src/main/scripts/hibernate.properties

#################################
#  End of Configurable Options  #
#################################

SHELL_LOG_FILE=/nfs/production/panda/metabolights/logs/maintainPublic.log.`date +"%Y-%m-%d %H:%M:%S"` 
DB_CONNECTION=`grep hibernate.connection.username ${PROPS_FILE} | grep -v '!' | grep -v '#' |cut -f2 -d=`  
DB_CONNECTION=$DB_CONNECTION'/'`grep hibernate.connection.password ${PROPS_FILE} | grep -v '!' | grep -v '#' |cut -f2 -d=`  
DB_CONNECTION=$DB_CONNECTION'@'`grep hibernate.connection.url ${PROPS_FILE} | grep -v '!' | grep -v '#' |cut -f6 -d:`
QUEUE_LOCATION=`grep queuelocation ${PROPS_FILE} | grep -v '!' | grep -v '#' |cut -f2 -d=`
SQL_BASIC_STR="whenever sqlerror exit failure;\n set feedback off head off pagesize 0;\n "
# Get private studies that are passed the release date
GET_STUDIES_SQL="${SQL_BASIC_STR} select acc from study where status = 1 AND trunc(releasedate)<=trunc(sysdate);"

Info ------------------------------------------------------------------------------------------ 
Info Settings:
Info '  Using properties file:             '$PROPS_FILE 
Info '  Shell script log file:             '$SHELL_LOG_FILE
Info '  Queue folder:                      '$QUEUE_LOCATION
Info ------------------------------------------------------------------------------------------

Info "Testing required parameters"
[ -z $PROPS_FILE ] && Error "Properties file not found, exiting"
[ -z $DB_CONNECTION ] && Error "Database connection string is not set, exiting"
[ -z $GET_STUDIES_SQL ] && Error "GET_STUDIES_SQL is not set, exiting"

Info ------------------------------------------------------------------------------------------
Info "Start"

PUBLIC_STUDIES=`echo -e ${GET_STUDIES_SQL} | sqlplus -s ${DB_CONNECTION}`
 
for studies in $PUBLIC_STUDIES
do
    GET_STUDIES_SQL="${SQL_BASIC_STR} \n
	    SELECT DISTINCT ud.username||'~'||s.ACC||'~'||to_char(s.releasedate,'YYYYMMDD')||'~'||'PRDupdatedate.zip' \n
            FROM user_detail ud, study2user su, study s WHERE ud.id = su.user_id AND su.study_id = s.id AND trunc(s.releasedate)<=trunc(sysdate) AND s.status = 1 AND acc ='${studies}';"

    QUEUE_FILES=`echo -e $GET_STUDIES_SQL | sqlplus -s ${DB_CONNECTION}`

    for qfiles in $QUEUE_FILES
    do
       touch $QUEUE_LOCATION${qfiles}
       Info "Study ${studies} is now submitted to the public queue as file ${QUEUE_LOCATION}${qfiles}"
    done

done

[ -z $PUBLIC_STUDIES ] ||  mailx -s 'MetaboLights Public File Maintenance' ${EMAILTO} < ${SHELL_LOG_FILE}
