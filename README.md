mvn tomcat7:run -Dmaven.tomcat.port=8888

add mysql user

insert into mysql.user(Host,User,Password) values("%","znz",password("znz"));
grant all privileges on *.* to znz@'%' identified by 'znz';
flush privileges;

alter database name character set utf8;
alter table t_user convert to character set utf8;