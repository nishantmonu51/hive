set hive.mapred.mode=nonstrict;
set hive.explain.user=false;
SET hive.vectorized.execution.enabled=true;
SET hive.vectorized.execution.reduce.enabled=true;
set hive.fetch.task.conversion=none;

SET hive.map.groupby.sorted=true;
set hive.default.nulls.last=false;

create table dtest(a int, b int) clustered by (a) sorted by (a) into 1 buckets stored as orc;
insert into table dtest select c,b from (select array(300,300,300,300,300) as a, 1 as b from src order by a limit 1) y lateral view  explode(a) t1 as c;

explain vectorization detail
select sum(distinct a), count(distinct a) from dtest;
select sum(distinct a), count(distinct a) from dtest;

explain vectorization detail
select sum(distinct cint), count(distinct cint), avg(distinct cint), std(distinct cint) from alltypesorc;
select sum(distinct cint), count(distinct cint), avg(distinct cint), std(distinct cint) from alltypesorc;

explain vectorization detail
select ctinyint, count(distinct cint), sum(( CASE WHEN ( ( cstring1 LIKE'test%1' )
 OR ( cstring1 LIKE 'test%2' ) ) THEN 1 ELSE 0 END )) AS s,
max(( CASE WHEN ( ( cstring1 LIKE 'test%3' ) OR ( cstring1 LIKE '%test%5' ) )
THEN cstring1 ELSE 'XXXXX' END )) AS maxVal from alltypesorc group by ctinyint;

select ctinyint, count(distinct cint), sum(( CASE WHEN ( ( cstring1 LIKE
'test%1' ) OR ( cstring1 LIKE 'test%2' ) ) THEN 1 ELSE 0 END )) AS s,
max(( CASE WHEN ( ( cstring1 LIKE 'test%3' ) OR ( cstring1 LIKE '%test%5' ) )
THEN cstring1 ELSE 'XXXXX' END )) AS maxVal from alltypesorc group by ctinyint;
