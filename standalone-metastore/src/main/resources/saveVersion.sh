#!/usr/bin/env bash

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


# This file is used to generate the package-info.java class that
# records the version, revision, branch, user, timestamp, and url
unset LANG
unset LC_CTYPE
unset LC_TIME
version=$1
shortversion=$2
src_dir=$3
revision=$4
branch=$5
url=$6
user=`whoami`
date=`date`
dir=`pwd`
cwd=`dirname $dir`
if [ "$revision" = "" ]; then
    if git rev-parse HEAD 2>/dev/null > /dev/null ; then
        revision=`git log -1 --pretty=format:"%H"`
        hostname=`hostname`
        branch=`git branch | sed -n -e 's/^* //p' | tr -d '"'`
        url="git://${hostname}${cwd}"
    elif [ -d .svn ]; then
        revision=`svn info ../ | sed -n -e 's/Last Changed Rev: \(.*\)/\1/p'`
        url=`svn info ../ | sed -n -e 's/^URL: \(.*\)/\1/p'`
  # Get canonical branch (branches/X, tags/X, or trunk)
        branch=`echo $url | sed -n -e 's,.*\(branches/.*\)$,\1,p' \
            -e 's,.*\(tags/.*\)$,\1,p' \
            -e 's,.*trunk$,trunk,p'`
    else
        revision="Unknown"
        branch="Unknown"
        url="file://$cwd"
    fi
fi
if [ "$branch" = "" ]; then
    branch="Unknown"
fi
if [ "$url" = "" ]; then
    url="file://$cwd"
fi

if [ -x /sbin/md5 ]; then
  md5="/sbin/md5"
else
  md5="md5sum"
fi

srcChecksum=`find ../ -name '*.java' | grep -v generated-sources | LC_ALL=C sort | xargs $md5 | $md5 | cut -d ' ' -f 1`

mkdir -p $src_dir/gen/version/org/apache/hadoop/hive/metastore/annotation

# In Windows, all the following string ends with \r, need to get rid of them
branch=`echo $branch | tr -d '\r'`
user=`echo $user | tr -d '\r'`
date=`echo $date | tr -d '\r'`
url=`echo $url | tr -d '\r'`
srcChecksum=`echo $srcChecksum | tr -d '\r'`

cat << EOF | \
  sed -e "s/VERSION/$version/" -e "s/SHORTVERSION/$shortversion/" \
      -e "s/USER/$user/" -e "s/DATE/$date/" \
      -e "s|URL|$url|" -e "s/REV/$revision/" \
      -e "s|BRANCH|$branch|" -e "s/SRCCHECKSUM/$srcChecksum/" \
      > $src_dir/gen/version/org/apache/hadoop/hive/metastore/annotation/package-info.java
/*
 * Generated by saveVersion.sh
 */
@MetastoreVersionAnnotation(version="VERSION", shortVersion="SHORTVERSION",
                         revision="REV", branch="BRANCH",
                         user="USER", date="DATE", url="URL",
                         srcChecksum="SRCCHECKSUM")
package org.apache.hadoop.hive.metastore.annotation;
EOF
