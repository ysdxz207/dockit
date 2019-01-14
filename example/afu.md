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

#### 返回示例
``` json
{
	"service":{
		"subObject":{
			"name":"QmWApOU",
			"id":7973
		},
		"subArray":[
			{
				"name":"icYG"
			},
			{
				"name":"I4"
			},
			{
				"name":"681DozY8V9"
			}
		]
	},
	"list":[
		{
			"arr":[
				{
					"arrValue":"Y5w",
					"subArr":[
						{
							"subArrId":"9dm4vW",
							"subArrName":"f93iG"
						},
						{
							"subArrId":"kiRtRr",
							"subArrName":"Oexe"
						}
					],
					"arrName":"JOyE0navNv",
					"subObj":{
						"objId":"7ec",
						"objName":"V5fgtrGa"
					}
				},
				{
					"arrValue":"2R8imYGW",
					"subArr":[
						{
							"subArrId":"aTEek",
							"subArrName":"r6"
						},
						{
							"subArrId":"1IUzl",
							"subArrName":"8"
						},
						{
							"subArrId":"ct2bEO",
							"subArrName":"smqzT"
						}
					],
					"arrName":"Hj6i8h8",
					"subObj":{
						"objId":"cXSsG",
						"objName":"dmwvmc"
					}
				}
			],
			"createTime":"un",
			"name":"EGzZVyb",
			"id":"SK",
			"type":"nSBZop4um",
			"content":"3hmW01l"
		},
		{
			"arr":[
				{
					"arrValue":"ILwvb",
					"subArr":[
						{
							"subArrId":"KxA6skRND",
							"subArrName":"updG9uItvo"
						},
						{
							"subArrId":"yiFAcwJ3h",
							"subArrName":"8q2"
						},
						{
							"subArrId":"YvYuPfhrz",
							"subArrName":"l41L7xbIYl"
						}
					],
					"arrName":"BoIt5qY5",
					"subObj":{
						"objId":"iUIC8J0",
						"objName":"CuAv9"
					}
				},
				{
					"arrValue":"8icKnhPSV",
					"subArr":[
						{
							"subArrId":"OMe2",
							"subArrName":"S"
						},
						{
							"subArrId":"LXHj",
							"subArrName":"soI9"
						},
						{
							"subArrId":"kJCgal",
							"subArrName":"ojMdBLw7s8"
						}
					],
					"arrName":"p9uj3kKB8",
					"subObj":{
						"objId":"c1pU3f94T",
						"objName":"dDTIXeP4k"
					}
				},
				{
					"arrValue":"NnsZ",
					"subArr":[
						{
							"subArrId":"V",
							"subArrName":"cbVz1Koyd"
						},
						{
							"subArrId":"MqCQ",
							"subArrName":"ty9JEp"
						}
					],
					"arrName":"UEf8iZge9C",
					"subObj":{
						"objId":"pdyU",
						"objName":"KLYWGSc"
					}
				}
			],
			"createTime":"fabelWM",
			"name":"tne",
			"id":"T4c2ZwLC",
			"type":"Pq2FdfZZ",
			"content":"hOjSkw"
		}
	]
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

#### 备注
- 
