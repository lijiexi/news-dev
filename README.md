

# 南安温彻新闻站

​	南安温彻新闻站项目致力于打造一个可以在线阅读、搜索、发表新闻的自媒体网站，采用现阶段流行技术来实现，采用前后端分离编写。

## 项目介绍

​	南安温彻新闻站是一个可以在线阅读、搜索、发表新闻的自媒体网站，包括前台新闻系统、作家页面以及后台管理系统，基于 SpringBoot、MyBatis实现。前台新闻系统包括：网站首页、用户登录、注册、文章搜索、文章详情、文章分类、最新热文等模块。作家页面包括：内容管理、发布文章、账号设置、粉丝管理、粉丝画像等。后台管理系统包括：文章审核、用户管理、文章分类管理、设置管理员等模块。

## 项目演示

### 前台新闻系统

#### 网站首页&搜索&热文&分类

<img src="https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/%E6%88%AA%E5%B1%8F2022-02-08%2019.30.40.png" style="zoom:50%;" />

#### 用户登录注册

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/20220208201042.png)

#### 文章详情

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/20220208202825.png)

### 作家页面

#### 内容管理

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/image-20220208204034517.png)

##### 搜索文章

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/20220208204113.png)

#### 发布文章

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/image-20220208204229795.png)

#### 账号设置

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/20220208204404.png)

#### 粉丝管理

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/image-20220208204620712.png)

#### 粉丝画像

##### 男女分布

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/20220208204649.png)

##### 地域分布

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/20220208204741.png)

### 后台管理系统

#### 文章审核

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/20220208204918.png)

#### 用户管理

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/20220208205045.png)

##### 用户冻结解冻

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/20220208205202.png)

#### 文章分类管理

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/20220208205300.png)

#### 设置管理员

![](https://raw.githubusercontent.com/lijiexi/Picbed_PicGo/main/blogImg/20220208205357.png)


## 组织结构

```
news
├── news-dev-common -- 工具类及通用代码
├── news-dev-api -- 子模块的接口定义
├── news-dev-model -- 数据层包括pojo、vo对象等
├── news-dev-service-user -- 用户服务
├── news-dev-service-admin -- 管理员模块
├── news-dev-service-article -- 文章模块
├── news-dev-service-search -- 搜索服务
├── news-dev-service-files -- 文件上传服务
```

## 技术选型

### 后端技术

|     技术      |     说明     |                   官网                   |
| :-----------: | :----------: | :--------------------------------------: |
|  SpringBoot   | 容器+MVC框架 |  https://spring.io/projects/spring-boot  |
|     Redis     |   缓存数据   |             https://redis.io             |
|    MyBatis    |   ORM框架    |         https://mp.baomidou.com          |
| Elasticsearch |   搜索引擎   | https://github.com/elastic/elasticsearch |
|   RabbitMQ    |   消息队列   |         https://www.rabbitmq.com         |
| Elasticsearch |   搜索引擎   | https://github.com/elastic/elasticsearch |
|     MySQL     |    数据库    |          https://www.mysql.com           |

## 环境搭建

### 开发工具

|     工具      |        说明         |                           官网                           |
| :-----------: | :-----------------: | :------------------------------------------------------: |
|     IDEA      |    开发Java程序     |         https://www.jetbrains.com/idea/download          |
|      RDM      | redis客户端连接工具 | https://github.com/uglide/RedisDesktopManager/stargazers |
|  SwitchHosts  |    本地host管理     |            https://oldj.github.io/SwitchHosts            |
|   SecureCRT   |  Linux远程连接工具  |       https://www.vandyke.com/products/securecrt/        |
|    Navicat    |   数据库连接工具    |           http://www.formysql.com/xiazai.html            |
| PowerDesigner |   数据库设计工具    |                 http://powerdesigner.de                  |
|    Postman    |   API接口调试工具   |                 https://www.postman.com                  |
|    Typora     |   Markdown编辑器    |                    https://typora.io                     |

### 开发环境

|     工具      | 版本号 |                             下载                             |
| :-----------: | :----: | :----------------------------------------------------------: |
|      JDK      |  1.8   | https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html |
|     Mysql     |  5.7   |                    https://www.mysql.com                     |
|     Redis     | Redis  |                  https://redis.io/download                   |
| Elasticsearch | 6.8.6  |               https://www.elastic.co/downloads               |
|  SpringBoot   | 2.2.5  |               https://www.elastic.co/cn/kibana               |
|   RabbitMQ    | 3.8.5  |            http://www.rabbitmq.com/download.html             |
|     Nginx     | 1.1.6  |              http://nginx.org/en/download.html               |
