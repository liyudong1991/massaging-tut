# activemq

## 配置默认数据库为mysql

找到activemq.xml中的persistenceAdapter属性, 更改为mysql, 添加bean和jdbc等.

参考http://jason-sherman.blogspot.com/2012/02/activemq-jdbc-master-slave-with-mysql.html

```$xslt
CREATE USER 'activemq'@'%'localhost' IDENTIFIED BY 'activemq'; 
GRANT ALL ON activemq.* TO 'activemq'@'localhost';  
```