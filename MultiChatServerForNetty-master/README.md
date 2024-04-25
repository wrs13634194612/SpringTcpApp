## Netty Toy Project MultiChatServer


####系统配置图

![image](https://github.com/LeeYuHwan/MultiChatServerForNetty/assets/66478929/dfb3a2fa-2898-44de-85da-a0f4b0a29c0b)

--------------------

#### Socket 通讯规格
--------------------
JSON Data沟通使用
--------------------
## 登录
# Request Data
{
"userId": userId,
"password": password,
"task":"login"
}

# Response Data
{
"result":"登录完成",
"task":"login",
"resultCode":"0000"
}

--------------------
## 创建房间
# Request Data
{
"task":"createRoom"
}

# Response Data
{
"task":"createRoom",
"resultCode":"0000",
"roomId": roomId
}

--------------------
## 房间入口
# Request Data
{
"roomId": roomId,
"task":"enterRoom"
}

# Response Data
{"task":"enterRoom",
"resultCode":"0000",
"roomId": roomId
}

--------------------
## 离开这个房间
# Request Data
{
"roomId": roomId,
"task":"exitRoom"
}

# Response Data
{"task":"exitRoom",
"resultCode":"0000"
}

--------------------
## 发信息
# Request Data
{
"msg": message,
"task":"sendRoom"
}

# Response Data
{
"msg": message,
"task":"sendRoom",
"resultCode":"0000",
"userName": userName,
"userId": userId
}
