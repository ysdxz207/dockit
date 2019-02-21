### 阿福列表接口

#### 描述

1. 获取阿福列表

#### 版本

> 1.0.0

#### 是否可用

可用

#### 请求URL

> ` afu.list`

#### 请求方式

> `POST`

#### 参数

|参数名|必选|类型|说明|
|:----    |:---|:----- |-----   |
|pageNum |是  |Integer |页码   |
|pageSize |否  |Integer |分页大小   |
|obj |否  |Object |参数   |
| - name |否  |String |名称   |
| - createTime |否  |String |创建事件   |

#### 请求参数示例

```json
{
	"obj":{
		"createTime":"YzPr",
		"name":"JE3kB4x"
	},
	"pageSize":7701,
	"pageNum":3326
}
```

#### 返回参数

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

#### 返回示例

```json
{
	"service":{
		"subObject":{
			"name":"A3bSPPMHz",
			"id":7207
		},
		"subArray":[
			{
				"name":"bFSss"
			},
			{
				"name":"mIYcZ"
			}
		]
	},
	"list":[
		{
			"arr":[
				{
					"arrValue":"TJVsZ",
					"subArr":[
						{
							"subArrId":"Xw",
							"subArrName":"LzXGA7KB"
						},
						{
							"subArrId":"uZdvvU",
							"subArrName":"PlW9"
						}
					],
					"arrName":"R",
					"subObj":{
						"objId":"bZU",
						"objName":"sNf"
					}
				},
				{
					"arrValue":"ToK2G",
					"subArr":[
						{
							"subArrId":"D8EMIp",
							"subArrName":"GIMNvOCa"
						},
						{
							"subArrId":"cqC",
							"subArrName":"S5lE"
						}
					],
					"arrName":"s",
					"subObj":{
						"objId":"J06f",
						"objName":"U"
					}
				}
			],
			"createTime":"JC",
			"name":"Vj89tmCR",
			"id":"j",
			"type":"zToONFf",
			"content":"f"
		},
		{
			"arr":[
				{
					"arrValue":"9r5",
					"subArr":[
						{
							"subArrId":"Krr",
							"subArrName":"ci4hMycZ"
						},
						{
							"subArrId":"m",
							"subArrName":"zd1E9"
						},
						{
							"subArrId":"CmvOchrLPR",
							"subArrName":"NtI0hgvco"
						}
					],
					"arrName":"HPiR9FQTvs",
					"subObj":{
						"objId":"Lrfc",
						"objName":"OLLgtu3g"
					}
				},
				{
					"arrValue":"fh4pBmIL",
					"subArr":[
						{
							"subArrId":"B",
							"subArrName":"xP"
						},
						{
							"subArrId":"aFZIE",
							"subArrName":"hw1"
						},
						{
							"subArrId":"04",
							"subArrName":"eEGONzWhXY"
						}
					],
					"arrName":"cvLvn1IFPM",
					"subObj":{
						"objId":"zkHe49E",
						"objName":"88U0fhm"
					}
				},
				{
					"arrValue":"MW",
					"subArr":[
						{
							"subArrId":"VVW5JPhh1Z",
							"subArrName":"lZ9f"
						},
						{
							"subArrId":"rU",
							"subArrName":"36jRSS"
						},
						{
							"subArrId":"PwVVtbzfx",
							"subArrName":"gAl3xp"
						}
					],
					"arrName":"Uk",
					"subObj":{
						"objId":"tL7",
						"objName":"YsUj"
					}
				}
			],
			"createTime":"Rs",
			"name":"Ls",
			"id":"6",
			"type":"7MImPaa",
			"content":"cpyGg"
		},
		{
			"arr":[
				{
					"arrValue":"5PGnN",
					"subArr":[
						{
							"subArrId":"TVaF",
							"subArrName":"O0cWUt"
						},
						{
							"subArrId":"xAYNK",
							"subArrName":"R1i26xYt"
						}
					],
					"arrName":"FHertUgswo",
					"subObj":{
						"objId":"nFuabiZ",
						"objName":"u"
					}
				},
				{
					"arrValue":"U",
					"subArr":[
						{
							"subArrId":"yGawAssu",
							"subArrName":"Ikd1gXpDo"
						},
						{
							"subArrId":"DTb0",
							"subArrName":"13"
						},
						{
							"subArrId":"lpc1C",
							"subArrName":"ji"
						}
					],
					"arrName":"SEShl",
					"subObj":{
						"objId":"2SDh",
						"objName":"cNU6XeK"
					}
				}
			],
			"createTime":"CzOHm",
			"name":"BIxPER",
			"id":"AjbIWma42",
			"type":"zz4mXIT",
			"content":"qFhu9"
		}
	]
}
```

#### 备注

- 

