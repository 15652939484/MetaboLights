#!/bin/ksh

base_dir=/net/isilonP/public/rw/homes/tc_cm01/studies
private_public="private public"

cd $base_dir
for priv_pub in $private_public
do
    #echo "priv_pub is $priv_pub"
    for archive_file in `ls $priv_pub/*.zip`
    do
        #echo "archive_file is $archive_file"
        in_use=$(/sbin/fuser -u $archive_file)
        if [ ${#in_use} -gt 0 ] ; then
           echo "$priv_pub file in use $archive_file"
        else
           echo "PRIVATE file $archive_file not in use, moving to /ebi/ftp/pub/databases/metabolights/studies/$priv_pub/"
           mv $archive_file /ebi/ftp/pub/databases/metabolights/studies/$priv_pub/
        fi
    done
done