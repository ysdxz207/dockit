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
			"name":"gpde",
			"id":8799
		},
		"subArray":[
			{
				"name":"O"
			},
			{
				"name":"Ag"
			}
		]
	},
	"list":[
		{
			"arr":[
				{
					"arrValue":"IL0Wz",
					"subArr":[
						{
							"subArrId":"chE",
							"subArrName":"onfHIEMp"
						},
						{
							"subArrId":"jPHV3jc6XM",
							"subArrName":"9"
						},
						{
							"subArrId":"Es5lZo",
							"subArrName":"lQr1edEE"
						}
					],
					"arrName":"XkVaF3u",
					"subObj":{
						"objId":"LqPG7q75",
						"objName":"bwZbCKbn"
					}
				},
				{
					"arrValue":"X",
					"subArr":[
						{
							"subArrId":"a",
							"subArrName":"gKyhkez"
						},
						{
							"subArrId":"fNAeGd",
							"subArrName":"PLeC2o1"
						},
						{
							"subArrId":"17R8VNvZ",
							"subArrName":"94Wnnv"
						}
					],
					"arrName":"V9KX61Gzk5",
					"subObj":{
						"objId":"f",
						"objName":"ktdQ"
					}
				},
				{
					"arrValue":"tuFulLrtJV",
					"subArr":[
						{
							"subArrId":"aqL",
							"subArrName":"RTDzu6"
						},
						{
							"subArrId":"yr8jd",
							"subArrName":"GUVgelp"
						},
						{
							"subArrId":"5VIcy",
							"subArrName":"cYCIau"
						}
					],
					"arrName":"jPHkM",
					"subObj":{
						"objId":"Tur",
						"objName":"42S02xE1d"
					}
				}
			],
			"createTime":"P3qH",
			"name":"LZSLNpn",
			"id":"9Fo8",
			"type":"8Y1ryGLp",
			"content":"tA8yJdhV"
		},
		{
			"arr":[
				{
					"arrValue":"ekgJkw",
					"subArr":[
						{
							"subArrId":"qh2Ld",
							"subArrName":"1r"
						},
						{
							"subArrId":"Z1GRNuy8k",
							"subArrName":"cxZKQ"
						},
						{
							"subArrId":"h7ytvqgvI",
							"subArrName":"VcyLKLMV"
						}
					],
					"arrName":"xLh",
					"subObj":{
						"objId":"oo",
						"objName":"jrdCGS"
					}
				},
				{
					"arrValue":"g",
					"subArr":[
						{
							"subArrId":"jnqWSmM",
							"subArrName":"O6UfHWLqgb"
						},
						{
							"subArrId":"rMpbiZAG",
							"subArrName":"C8"
						},
						{
							"subArrId":"P8",
							"subArrName":"YmCEm8vYg"
						}
					],
					"arrName":"4i6Vs5XB",
					"subObj":{
						"objId":"qklN",
						"objName":"f"
					}
				}
			],
			"createTime":"HStR",
			"name":"wPSf9CB",
			"id":"VXObuGWOjn",
			"type":"o8cKg18",
			"content":"s7EgYlSO"
		},
		{
			"arr":[
				{
					"arrValue":"GugNlkM",
					"subArr":[
						{
							"subArrId":"kj3ph9Pp",
							"subArrName":"Fu7i"
						},
						{
							"subArrId":"4b9aBw",
							"subArrName":"8"
						}
					],
					"arrName":"cVwO9fmVj",
					"subObj":{
						"objId":"Zu20qB",
						"objName":"w1WqeR"
					}
				},
				{
					"arrValue":"106yyF",
					"subArr":[
						{
							"subArrId":"fckYfuo0ns",
							"subArrName":"Npau"
						},
						{
							"subArrId":"g9VX",
							"subArrName":"TS4"
						},
						{
							"subArrId":"DbI6lmGi",
							"subArrName":"NU"
						}
					],
					"arrName":"jto",
					"subObj":{
						"objId":"buyFbvDW",
						"objName":"4qmkyK28"
					}
				}
			],
			"createTime":"mxjBe",
			"name":"LmBBCSV",
			"id":"5wO",
			"type":"K9ijogMX",
			"content":"rUB"
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
