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

sentinel=======
启动：
java -jar ./sentinel-dashboard-1.8.8.jar
http://localhost:8080
user: sentinel
password: sentinel

api-gateway=======
路由：
  http://localhost:5050/yyy：跳转到https://www.baidu.com
  http://localhost:5050/getConfig: 跳转到http://localhost:7070/getConfig；通过nacos中转
  get请求http://localhost:5050/sayHello：跳转到post请求http://localhost:7070/sayHello；通过nacos中转
  delete请求http://localhost:5050/sayHello：跳转到post请求http://localhost:7070/sayHello
配置中心
  需要使用bootstrap.yml配置文件。使用该文件必须必须，使用启动参数加入-Dspring.cloud.bootstrap.enabled=true
  http://localhost:5050

ww-user=======
http调用：http->redis->mysql
  curl --request POST --url http://localhost:7070/sayHello --header 'Content-Type: application/json' --data '{"name": "houhou"}'
配置中心
  需要使用bootstrap.yml配置文件。使用该文件必须必须，使用启动参数加入-Dspring.cloud.bootstrap.enabled=true
  http://localhost:7070/getConfig


ww-video======
nacos注册中心调用：http->redis->mysql
                     ->http(ww-user)->redis->mysql
  http://localhost:9090
配置中心
  需要使用bootstrap.yml配置文件。使用该文件必须必须，使用启动参数加入-Dspring.cloud.bootstrap.enabled=true
  http://localhost:9090


