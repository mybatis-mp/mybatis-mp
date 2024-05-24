# mysql
```
docker pull container-registry.oracle.com/mysql/community-server
```
```
docker run --name mysql --env=MYSQL_ROOT_HOST=% --env=MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 -d container-registry.oracle.com/mysql/community-server
```
# mariadb
```
docker pull mariadb
```
```
docker run --name mariadb --env=MARIADB_ROOT_PASSWORD=123456 -p 3307:3306 -d mariadb
```
# postgres
```
docker pull postgres
```
```
docker run --name postgres --env=POSTGRES_PASSWORD=123456 --env=POSTGRES_HOST_AUTH_METHOD=trust -p 5432:5432 -d postgres
```
# oracle
```
docker pull truevoly/oracle-12c
```
```
docker run --name oracle12c -p 1521:1521 -d truevoly/oracle-12c
```
# sql server
```
docker pull mcr.microsoft.com/mssql/server:2022-latest
```
```
docker run --name mssql --env=ACCEPT_EULA=Y --env=MSSQL_SA_PASSWORD=AbC@128723 --env=MSSQL_LCID=2052 --env=MSSQL_COLLATION=Chinese_PRC_CI_AS -p 1433:1433 -d mcr.microsoft.com/mssql/server:2022-latest
```
# db2
```
docker pull ibmcom/db2
```
```
docker run -d -p 50000:50000 --name db2 --privileged=true -e DB2INST1_PASSWORD=123456  -e DBNAME=test3 -e LICENSE=accept ibmcom/db2
```
# 达梦
```
docker pull xuxuclassmate/dameng
```
```
docker run --name dm -e INSTANCE_NAME=SYSDBA -e DSYSDBA_PWD=SYSDBA001 -p 5236:5236 -d xuxuclassmate/dameng
```

