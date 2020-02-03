SELECT 'Upgrading MetaStore schema from 3.1.2000 to 3.1.3000';

USE SYS;

CREATE EXTERNAL TABLE IF NOT EXISTS `SCHEDULED_QUERIES` (
  `SCHEDULED_QUERY_ID` bigint,
  `SCHEDULE_NAME` string,
  `ENABLED` boolean,
  `CLUSTER_NAMESPACE` string,
  `SCHEDULE` string,
  `USER` string,
  `QUERY` string,
  `NEXT_EXECUTION` bigint,
  CONSTRAINT `SYS_PK_SCHEDULED_QUERIES` PRIMARY KEY (`SCHEDULED_QUERY_ID`) DISABLE
)
STORED BY 'org.apache.hive.storage.jdbc.JdbcStorageHandler'
TBLPROPERTIES (
"hive.sql.database.type" = "METASTORE",
"hive.sql.query" =
"SELECT
  \"SCHEDULED_QUERY_ID\",
  \"SCHEDULE_NAME\",
  \"ENABLED\",
  \"CLUSTER_NAMESPACE\",
  \"SCHEDULE\",
  \"USER\",
  \"QUERY\",
  \"NEXT_EXECUTION\"
FROM
  \"SCHEDULED_QUERIES\""
);

CREATE EXTERNAL TABLE IF NOT EXISTS `SCHEDULED_EXECUTIONS` (
  `SCHEDULED_EXECUTION_ID` bigint,
  `SCHEDULED_QUERY_ID` bigint,
  `EXECUTOR_QUERY_ID` string,
  `STATE` string,
  `START_TIME` int,
  `END_TIME` int,
  `ERROR_MESSAGE` string,
  `LAST_UPDATE_TIME` int,
  CONSTRAINT `SYS_PK_SCHEDULED_EXECUTIONS` PRIMARY KEY (`SCHEDULED_EXECUTION_ID`) DISABLE
)
STORED BY 'org.apache.hive.storage.jdbc.JdbcStorageHandler'
TBLPROPERTIES (
"hive.sql.database.type" = "METASTORE",
"hive.sql.query" =
"SELECT
  \"SCHEDULED_EXECUTION_ID\",
  \"SCHEDULED_QUERY_ID\",
  \"EXECUTOR_QUERY_ID\",
  \"STATE\",
  \"START_TIME\",
  \"END_TIME\",
  \"ERROR_MESSAGE\",
  \"LAST_UPDATE_TIME\"
FROM
  \"SCHEDULED_EXECUTIONS\""
);

CREATE EXTERNAL TABLE IF NOT EXISTS `COMPACTION_QUEUE` (
  `CQ_ID` bigint,
  `CQ_DATABASE` string,
  `CQ_TABLE` string,
  `CQ_PARTITION` string,
  `CQ_STATE` string,
  `CQ_TYPE` string,
  `CQ_TBLPROPERTIES` string,
  `CQ_WORKER_ID` string,
  `CQ_START` bigint,
  `CQ_RUN_AS` string,
  `CQ_HIGHEST_WRITE_ID` bigint,
  `CQ_HADOOP_JOB_ID` string,
  `CQ_ERROR_MESSAGE` string
)
STORED BY 'org.apache.hive.storage.jdbc.JdbcStorageHandler'
TBLPROPERTIES (
"hive.sql.database.type" = "METASTORE",
"hive.sql.query" =
"SELECT
  \"COMPACTION_QUEUE\".\"CQ_ID\",
  \"COMPACTION_QUEUE\".\"CQ_DATABASE\",
  \"COMPACTION_QUEUE\".\"CQ_TABLE\",
  \"COMPACTION_QUEUE\".\"CQ_PARTITION\",
  \"COMPACTION_QUEUE\".\"CQ_STATE\",
  \"COMPACTION_QUEUE\".\"CQ_TYPE\",
  \"COMPACTION_QUEUE\".\"CQ_TBLPROPERTIES\",
  \"COMPACTION_QUEUE\".\"CQ_WORKER_ID\",
  \"COMPACTION_QUEUE\".\"CQ_START\",
  \"COMPACTION_QUEUE\".\"CQ_RUN_AS\",
  \"COMPACTION_QUEUE\".\"CQ_HIGHEST_WRITE_ID\",
  \"COMPACTION_QUEUE\".\"CQ_HADOOP_JOB_ID\",
  \"COMPACTION_QUEUE\".\"CQ_ERROR_MESSAGE\"
FROM \"COMPACTION_QUEUE\"
"
);

CREATE EXTERNAL TABLE IF NOT EXISTS `COMPLETED_COMPACTIONS` (
  `CC_ID` bigint,
  `CC_DATABASE` string,
  `CC_TABLE` string,
  `CC_PARTITION` string,
  `CC_STATE` string,
  `CC_TYPE` string,
  `CC_TBLPROPERTIES` string,
  `CC_WORKER_ID` string,
  `CC_START` bigint,
  `CC_END` bigint,
  `CC_RUN_AS` string,
  `CC_HIGHEST_WRITE_ID` bigint,
  `CC_HADOOP_JOB_ID` string,
  `CC_ERROR_MESSAGE` string
)
STORED BY 'org.apache.hive.storage.jdbc.JdbcStorageHandler'
TBLPROPERTIES (
"hive.sql.database.type" = "METASTORE",
"hive.sql.query" =
"SELECT
  \"COMPLETED_COMPACTIONS\".\"CC_ID\",
  \"COMPLETED_COMPACTIONS\".\"CC_DATABASE\",
  \"COMPLETED_COMPACTIONS\".\"CC_TABLE\",
  \"COMPLETED_COMPACTIONS\".\"CC_PARTITION\",
  \"COMPLETED_COMPACTIONS\".\"CC_STATE\",
  \"COMPLETED_COMPACTIONS\".\"CC_TYPE\",
  \"COMPLETED_COMPACTIONS\".\"CC_TBLPROPERTIES\",
  \"COMPLETED_COMPACTIONS\".\"CC_WORKER_ID\",
  \"COMPLETED_COMPACTIONS\".\"CC_START\",
  \"COMPLETED_COMPACTIONS\".\"CC_END\",
  \"COMPLETED_COMPACTIONS\".\"CC_RUN_AS\",
  \"COMPLETED_COMPACTIONS\".\"CC_HIGHEST_WRITE_ID\",
  \"COMPLETED_COMPACTIONS\".\"CC_HADOOP_JOB_ID\",
  \"COMPLETED_COMPACTIONS\".\"CC_ERROR_MESSAGE\"
FROM \"COMPLETED_COMPACTIONS\"
"
);

CREATE OR REPLACE VIEW `COMPACTIONS`
(
  `C_ID`,
  `C_CATALOG`,
  `C_DATABASE`,
  `C_TABLE`,
  `C_PARTITION`,
  `C_TYPE`,
  `C_STATE`,
  `C_HOSTNAME`,
  `C_WORKER_ID`,
  `C_START`,
  `C_DURATION`,
  `C_HADOOP_JOB_ID`,
  `C_RUN_AS`,
  `C_HIGHEST_WRITE_ID`
) AS
SELECT
  CC_ID,
  'default',
  CC_DATABASE,
  CC_TABLE,
  CC_PARTITION,
  CASE WHEN CC_TYPE = 'i' THEN 'minor' WHEN CC_TYPE = 'a' THEN 'major' ELSE 'UNKNOWN' END,
  CASE WHEN CC_STATE = 'f' THEN 'failed' WHEN CC_STATE = 's' THEN 'succeeded' WHEN CC_STATE = 'a' THEN 'attempted' ELSE 'UNKNOWN' END,
  CASE WHEN CC_WORKER_ID IS NULL THEN cast (null as string) ELSE split(CC_WORKER_ID,"-")[0] END,
  CASE WHEN CC_WORKER_ID IS NULL THEN cast (null as string) ELSE split(CC_WORKER_ID,"-")[1] END,
  CC_START,
  CASE WHEN CC_END IS NULL THEN cast (null as string) ELSE CC_END-CC_START END,
  CC_HADOOP_JOB_ID,
  CC_RUN_AS,
  CC_HIGHEST_WRITE_ID
FROM COMPLETED_COMPACTIONS
UNION ALL
SELECT
  CQ_ID,
  'default',
  CQ_DATABASE,
  CQ_TABLE,
  CQ_PARTITION,
  CASE WHEN CQ_TYPE = 'i' THEN 'minor' WHEN CQ_TYPE = 'a' THEN 'major' ELSE 'UNKNOWN' END,
  CASE WHEN CQ_STATE = 'i' THEN 'initiated' WHEN CQ_STATE = 'w' THEN 'working' WHEN CQ_STATE = 'r' THEN 'ready for cleaning' ELSE 'UNKNOWN' END,
  CASE WHEN CQ_WORKER_ID IS NULL THEN NULL ELSE split(CQ_WORKER_ID,"-")[0] END,
  CASE WHEN CQ_WORKER_ID IS NULL THEN NULL ELSE split(CQ_WORKER_ID,"-")[1] END,
  CQ_START,
  cast (null as string),
  CQ_HADOOP_JOB_ID,
  CQ_RUN_AS,
  CQ_HIGHEST_WRITE_ID
FROM COMPACTION_QUEUE;

USE INFORMATION_SCHEMA;

create or replace view SCHEDULED_QUERIES  as
select
  `SCHEDULED_QUERY_ID` ,
  `SCHEDULE_NAME` ,
  `ENABLED`,
  `CLUSTER_NAMESPACE`,
  `SCHEDULE`,
  `USER`,
  `QUERY`,
  FROM_UNIXTIME(NEXT_EXECUTION) as NEXT_EXECUTION
FROM
  SYS.SCHEDULED_QUERIES
;

create or replace view SCHEDULED_EXECUTIONS as
SELECT
  SCHEDULED_EXECUTION_ID,
  SCHEDULE_NAME,
  EXECUTOR_QUERY_ID,
  `STATE`,
  FROM_UNIXTIME(START_TIME) as START_TIME,
  FROM_UNIXTIME(END_TIME) as END_TIME,
  END_TIME-START_TIME as ELAPSED,
  ERROR_MESSAGE,
  FROM_UNIXTIME(LAST_UPDATE_TIME) AS LAST_UPDATE_TIME
FROM
  SYS.SCHEDULED_EXECUTIONS SE
JOIN
  SYS.SCHEDULED_QUERIES SQ
WHERE
  SE.SCHEDULED_QUERY_ID=SQ.SCHEDULED_QUERY_ID;

CREATE OR REPLACE VIEW `COMPACTIONS`
(
  `C_ID`,
  `C_CATALOG`,
  `C_DATABASE`,
  `C_TABLE`,
  `C_PARTITION`,
  `C_TYPE`,
  `C_STATE`,
  `C_HOSTNAME`,
  `C_WORKER_ID`,
  `C_START`,
  `C_DURATION`,
  `C_HADOOP_JOB_ID`,
  `C_RUN_AS`,
  `C_HIGHEST_WRITE_ID`
) AS
SELECT DISTINCT
  C_ID,
  C_CATALOG,
  C_DATABASE,
  C_TABLE,
  C_PARTITION,
  C_TYPE,
  C_STATE,
  C_HOSTNAME,
  C_WORKER_ID,
  C_START,
  C_DURATION,
  C_HADOOP_JOB_ID,
  C_RUN_AS,
  C_HIGHEST_WRITE_ID
FROM
  `sys`.`COMPACTIONS` C JOIN `sys`.`TBLS` T ON (C.`C_TABLE` = T.`TBL_NAME`)
                        JOIN `sys`.`DBS` D ON (C.`C_DATABASE` = D.`NAME`)
                        LEFT JOIN `sys`.`TBL_PRIVS` P ON (T.`TBL_ID` = P.`TBL_ID`)
WHERE
  (NOT restrict_information_schema() OR P.`TBL_ID` IS NOT NULL
  AND (P.`PRINCIPAL_NAME`=current_user() AND P.`PRINCIPAL_TYPE`='USER'
    OR ((array_contains(current_groups(), P.`PRINCIPAL_NAME`) OR P.`PRINCIPAL_NAME` = 'public') AND P.`PRINCIPAL_TYPE`='GROUP'))
  AND P.`TBL_PRIV`='SELECT' AND P.`AUTHORIZER`=current_authorizer());


CREATE OR REPLACE VIEW `VERSION` AS SELECT 1 AS `VER_ID`, '3.1.3000' AS `SCHEMA_VERSION`,
  'Hive release version 3.1.3000' AS `VERSION_COMMENT`;

SELECT 'Finished upgrading MetaStore schema from 3.1.2000 to 3.1.3000';
