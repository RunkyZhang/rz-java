nacos=========
启动：
./bin sh startup.sh -m standalone
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

vf-user=======
Dubbo启动：dubbo->redis->mysql
 telnet localhost 15511
 invoke com.vf.user.base.api.service.DemoService.sayHelloByName({name:"111"})
Http启动：http->redis->mysql
  http://localhost:8080/hello
配置中心
  需要使用bootstrap.yml配置文件。使用该文件必须必须，使用启动参数加入-Dspring.cloud.bootstrap.enabled=true
  http://localhost:8080


vf-video======
dubbo启动：dubbo->redis->mysql
  telnet localhost 16622
  invoke com.vf.video.base.api.service.SomeService.sayHelloByName({name:"111"})
nacos注册中心调用：http->redis->mysql
                     ->dubbo(vf-user)->redis->mysql
  http://localhost:9090/hello
配置中心
  需要使用bootstrap.yml配置文件。使用该文件必须必须，使用启动参数加入-Dspring.cloud.bootstrap.enabled=true
  http://localhost:9090


