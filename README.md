mvn tomcat7:run -Dmaven.tomcat.port=8888

add mysql user

insert into mysql.user(Host,User,Password) values("localhost","znz",password("znz"));
grant all privileges on *.* to znz@'%' identified by '123';
flush privileges;