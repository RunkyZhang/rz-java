nacos=========
启动：
sh ./bin/startup.sh -m standalone
http://localhost:8848/nacos/

redis=========
安装：
brew install redis
启动：
redis-server
127.0.0.1:6379
无密码

mysql=========
安装：onedrive的setup找
User:root
Password:12345678

ww-user=======
Http启动：http->redis->mysql
  curl --request POST --url http://localhost:8080/sayHello --header 'Content-Type: application/json' --data '{"name": "houhou"}'
配置中心
  需要使用bootstrap.yml配置文件。使用该文件必须必须，使用启动参数加入-Dspring.cloud.bootstrap.enabled=true
  http://localhost:8080


ww-video======
nacos注册中心调用：http->redis->mysql
                     ->http(ww-user)->redis->mysql
  http://localhost:9090
配置中心
  需要使用bootstrap.yml配置文件。使用该文件必须必须，使用启动参数加入-Dspring.cloud.bootstrap.enabled=true
  http://localhost:9090


