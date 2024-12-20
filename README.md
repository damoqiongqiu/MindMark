# MindMark(心印)

🚀🚀🚀MindMark（心印）是一款基于 SpringAI 和 AIGC 的问答系统， 采用 RAG 架构，可以和基于 Spring 体系的业务系统进行无缝集成。

TODO:文档补充完整

## 0.注意

SpringAI 项目整体上处于预览阶段，并没有正式发布版本，请勿把本项目的代码用于实际业务系统。

## 1.主要依赖

| 模块 | 版本 | 说明 |
| --- | --- | --- |
| OpenJDK 20 | JDK >=18 | - |
| 智谱大模型 | - | [https://open.bigmodel.cn/](https://open.bigmodel.cn/) |
| SpringAI | 1.0.0-SNAPSHOT | [https://docs.spring.io/spring-ai/reference/index.html](https://docs.spring.io/spring-ai/reference/index.html) |
| ElasticSearch | 8.17.0 | [https://www.elastic.co/elasticsearch](https://www.elastic.co/elasticsearch) |
| MariaDB | >=10.0 | [https://mariadb.org/](https://mariadb.org/) |

## 2.准备工作

### 2.1 申请智谱大模型 api-key

在智谱大模型注册并完成实名认证，然后获得一个 api-key ，[https://open.bigmodel.cn/](https://open.bigmodel.cn/) 。

把获得的 api-key 配置到 mindmark-llm-connector/src/main/resources/application.yml 中。

### 2.2 ElasticSearch 安装配置

官方文档： https://hub.docker.com/_/elasticsearch/

拉取镜像：

```

docker pull docker.elastic.co/elasticsearch/elasticsearch:8.17.0

```

启动容器：

```
  docker run -d --name elasticsearch \
  -e "discovery.type=single-node" \
  -e "xpack.security.enabled=false" \
  -e "xpack.security.transport.ssl.enabled=false" \
  -e "xpack.security.http.ssl.enabled=false" \
  -e "ES_JAVA_OPTS=-Xms8g -Xmx8g" \
  -p 9200:9200 \
  -p 9300:9300 \
  docker.elastic.co/elasticsearch/elasticsearch:8.17.0
```

**请注意：以上启动方式禁用了 SSL ，这是为了本地开发方便，对于生产系统，请启用 SSL 。**

观察启动日志

```
docker logs -f elasticsearch
```

打开浏览器，测试 ElasticSearch 是否正常运行：

http://192.168.0.105:9200/

安装 Kibana 图形界面并连接 ElasticSearch

```
docker run -d --name kibana -p 5601:5601 --link elasticsearch:elasticsearch docker.elastic.co/kibana/kibana:8.17.0
```

观察启动日志

```
docker logs -f kibana
```

打开浏览器，测试 Kibana 是否正常运行： http://192.168.0.105:5601/

其它安装配置方式请参考 ElasticSearch 官方文档。

### 2.3 MariaDB 安装配置（可选）

省略 MariaDB 安装配置过程， MySQL 也可以。

TODO:文档补充完整

### 3. 启动项目

- 拉取本项目
- 修改配置文件（application.yml 和 application-druid.yml 中有一些配置项需要改成你自己的配置）
- 启动 MindMarkApplication.java

**备注：在启动和运行时，如果看到异常信息可以无视，因为日志级别配置成了 TRACE ，只要能够正常访问即可。**

TODO:文档补充完整

### 4.准备数据

找一些文件，放到被监控的目录中，定时任务会自动扫描并解析这些文件的内容，然后进行向量化，并存储到 ElasticSearch 中去。

监控目录的配置项是 application.yml 的 watch-file.file-path 。

目前支持的文件格式有：pdf/txt/markdown/doc/docx/ppt/pptx/xls/xlsx/json 。

TODO:文档补充完整

## 5.测试接口

使用 Postman 或者直接使用浏览器访问接口。

TODO:文档补充完整

## 6.系统架构

TODO:文档补充完整

## 7.License

MIT

（补充声明：您可以随意使用此项目，但是本人不对您使用此项目造成的任何损失承担责任。）

## 8.联系我

VX: lanxinshuma
