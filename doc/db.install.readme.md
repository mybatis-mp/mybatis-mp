# mysql
> 设置 变量：
```
MYSQL_ROOT_PASSWORD
```

```
docker exec -it mysql /bin/bash
```
```
mysql -u root -p123456
```
```
CREATE USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
flush privileges;
```
# mariadb
> 设置 变量：
```
MARIADB_ROOT_PASSWORD
```

# postgres
> 设置 变量：
```
POSTGRES_PASSWORD
```
```
POSTGRES_HOST_AUTH_METHOD=trust
```

# oracle

# sql server
```
ACCEPT_EULA = Y
```
```
SA_PASSWORD
```
```
MSSQL_LCID = 2052
```
```
MSSQL_COLLATION = Chinese_PRC_CI_AS
```

# db2
```
docker pull ibmcom/db2
```

```
docker run -d -p 50000:50000 --name db2 --privileged=true -e DB2INST1_PASSWORD=123456  -e DBNAME=test3 -e LICENSE=accept ibmcom/db2
```