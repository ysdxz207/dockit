### 阿福列表接口
####描述

1. 获取阿福列表

####版本

> 1.0.0

####是否可用

可用

####请求URL 

> ` afu.list`

####请求方式

> `POST`

####参数 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|pageNum |是  |Integer |页码   |
|pageSize |否  |Integer |分页大小   |

####返回示例

``` 
{
	"service":{
		"subObject":{
			"name":"sCZm1nI03O",
			"id":8647
		},
		"subArray":[
			{
				"name":"jdsYATCqWh"
			},
			{
				"name":"fuA"
			},
			{
				"name":"ST"
			}
		]
	},
	"list":[
		{
			"arr":[
				{
					"arrValue":"SvN",
					"subArr":[
						{
							"subArrId":"IbB26VzO",
							"subArrName":"7tZlg"
						},
						{
							"subArrId":"7drVzBttO2",
							"subArrName":"7XxSdfqxf"
						},
						{
							"subArrId":"x5P",
							"subArrName":"G3Vl"
						}
					],
					"arrName":"8zQQdB",
					"subObj":{
						"objId":"x8KoZ",
						"objName":"qVRMryH8"
					}
				},
				{
					"arrValue":"5",
					"subArr":[
						{
							"subArrId":"fVm",
							"subArrName":"L"
						},
						{
							"subArrId":"8YL",
							"subArrName":"s"
						},
						{
							"subArrId":"yElaq8BN",
							"subArrName":"hB8yK"
						}
					],
					"arrName":"zTzUuw",
					"subObj":{
						"objId":"3Rmi",
						"objName":"Fl"
					}
				}
			],
			"createTime":"UBx",
			"name":"uf7xuoFT2",
			"id":"epGfHrip",
			"type":"TkR9HAwCZ",
			"content":"dxnP9oS"
		},
		{
			"arr":[
				{
					"arrValue":"yt91D0rOP",
					"subArr":[
						{
							"subArrId":"k",
							"subArrName":"ZBA"
						},
						{
							"subArrId":"qEPyUF",
							"subArrName":"ScyCxy"
						}
					],
					"arrName":"Qmo",
					"subObj":{
						"objId":"YqzRkuRGMB",
						"objName":"fZhwV"
					}
				},
				{
					"arrValue":"ftEM",
					"subArr":[
						{
							"subArrId":"fAp2U",
							"subArrName":"m"
						},
						{
							"subArrId":"X1VC5l",
							"subArrName":"LeQY"
						},
						{
							"subArrId":"4v4b0RsT7",
							"subArrName":"U"
						}
					],
					"arrName":"lqR",
					"subObj":{
						"objId":"A34",
						"objName":"NftbiLUh"
					}
				}
			],
			"createTime":"Lcn",
			"name":"kzoeGPk",
			"id":"EOK5F",
			"type":"MMnVELG3as",
			"content":"Tf"
		},
		{
			"arr":[
				{
					"arrValue":"Dusar3UjI",
					"subArr":[
						{
							"subArrId":"LyVvMWja",
							"subArrName":"VMa8"
						},
						{
							"subArrId":"D1o8ow94Z",
							"subArrName":"AXQGlTDOD"
						},
						{
							"subArrId":"ACB",
							"subArrName":"OAAM0i"
						}
					],
					"arrName":"8ae6h3",
					"subObj":{
						"objId":"VdMQ",
						"objName":"zfc"
					}
				},
				{
					"arrValue":"n",
					"subArr":[
						{
							"subArrId":"PyI2",
							"subArrName":"qrg2RV"
						},
						{
							"subArrId":"3tX",
							"subArrName":"oT946cE4"
						}
					],
					"arrName":"DYX",
					"subObj":{
						"objId":"2qT2",
						"objName":"KTE"
					}
				},
				{
					"arrValue":"gsJfkYzSU8",
					"subArr":[
						{
							"subArrId":"y",
							"subArrName":"u3cgYE1yDe"
						},
						{
							"subArrId":"Og",
							"subArrName":"wX25gmzXZr"
						},
						{
							"subArrId":"QDfdb",
							"subArrName":"ATCC"
						}
					],
					"arrName":"ymu",
					"subObj":{
						"objId":"0KxrZEY",
						"objName":"Jdr4K4mw"
					}
				}
			],
			"createTime":"WdH",
			"name":"otv",
			"id":"CjVLP",
			"type":"z1rkNYx",
			"content":"RVgslJ29Lw"
		}
	]
}
```
####返回参数 

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|service |是  |Object |请求服务，原样返回   |
| - subObject |是  |Object |service子对象   |
| -- id |是  |Integer |service子对象ID   |
| -- name |是  |String |service子对象名   |
| - subArray |是  |Array |service子数组   |
| -- name |是  |String |service子数组名   |
|list |是  |Array |阿福列表   |
| - id |是  |String |ID   |
| - name |是  |String |名称   |
| - type |是  |String |类型   |
| - createTime |是  |String |创建时间   |
| - content |是  |String |内容   |
| - arr |是  |Array |数组   |
| -- arrName |是  |String |数组名   |
| -- arrValue |是  |String |数组值   |
| -- subArr |是  |Array |子数组   |
| --- subArrId |是  |String |子数组ID   |
| --- subArrName |是  |String |子数组名   |
| -- subObj |是  |Object |子对象   |
| --- objId |是  |String |子对象ID   |
| --- objName |是  |String |子对象名   |

####备注

- 
