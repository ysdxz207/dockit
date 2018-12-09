
    
**描述** 

- ${description}

**请求URL** 
> ` ${requestUrl}`
  
**请求方式**
> ${requestMethod}

**参数** 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|username |是  |String |用户名   |
|password |是  |String |密码     |

 **返回示例**

``` 
  {
    "responseCode": "SUCCESS",
    "responseMsg": "成功",
    "data": {
      "userId": "12345",
      "username": "feixiangdeyun",
      "name": "李飞翔",
      "lastLoginTime": "2018-12-08 23:43:29",
    }
  }
```

 **返回参数** 

|参数名|类型|说明|
|:-----  |:-----|-----                           |
|userId |Integer   |用户ID  |
