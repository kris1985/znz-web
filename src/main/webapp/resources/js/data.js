﻿var basePath = getContextPath();
DATA={
	menu:[{//一级菜单
		menuid:'m001',
		name:'主页',
		code:'m001',
		icon:'images/home.png'
	},{
		menuid:'m008',
		name:'RSS',
		code:'m008',
		icon:'images/rss.png'
	}],
	app:{//桌面1
		'readGod':{
			appid:'2534',
			icon:'readGod.png',
			name:'读览天下',
			url:'http://www.jq-school.com/',
			sonMenu:"[{"+
				"'appid':'8856',"+
				"'icon':'sosomap.png',"+
				"'name':'搜搜地图',"+
				"'url':'http://www.jq-school.com/'"+
			"},{"+
				"appid:'8857',"+
				"icon:'time.png',"+
				"name:'时钟',"+
				"url:'http://www.jq-school.com/'"+
			"},{"+
				"appid:'8858',"+
				"icon:'jinshan.png',"+
				"name:'金山快盘',"+
				"url:'http://www.jq-school.com/'"+
			"}]",
			asc :1
		},
		'sosomap':{
			appid:'42',
			icon:'sosomap.png',
			name:'搜搜地图',
			url:'http://www.jq-school.com/',
			sonMenu:"[{"+
				"'appid':'10010',"+
				"'icon':'fastsearch.png',"+
				"'name':'快递查询',"+
				"'enname':'fastsearch',"+
				"'url':'http://www.jq-school.com/',"+
				"'asc' :1"+
			"},{"+
				"appid:'10011',"+
				"icon:'doudizhu.png',"+
				"enname:'doudizhu',"+
				"name:'欢乐斗地主',"+
				"url:'http://www.jq-school.com/',"+
				"asc :2"+
			"}]",
			asc :2
		},
		
		'user':{
			appid:'18',
			icon:'friendgroup.png',
			name:'用户管理',
			url:basePath+'/admin/user/users',
			sonMenu:"[]",
			asc:6
		},
		'disk':{
			appid:'20',
			icon:'wangdesk.png',
			name:'磁盘空间',
			url:'disk.html',
			width:'1600px',
			asc:7
		},
		'theme':{
			appid:'21',
			icon:'icon0.png',
			name:'设置主题',
			url:'theme.html',
			asc:9
		},
		'folder':{
			appid:'514',
			icon:'folder_o.png',
			name:'资料管理',
			url:'files.html',
			width:'1000',
			sonMenu:"[]",
			asc:8
		}
	},
	sApp:{//侧边栏应用
		'appmarket':{
			appid:'1',
			icon:'appmarket.png',
			name:'应用市场',
			url:'http://www.jq-school.com/',
			sonMenu:"[]",
			asc :1
		},
		'qq':{
			appid:'2',
			icon:'big.png',
			name:'QQ',
			url:'http://www.jq-school.com/',
			sonMenu:"[]",
			asc :2
		},
		'weibo':{
			appid:'3',
			icon:'weibo.png',
			name:'微博',
			url:'http://www.jq-school.com/',
			sonMenu:"[]",
			asc :3
		},
		'mail':{
			appid:'4',
			icon:'mail.png',			
			name:'邮箱',
			url:'http://www.jq-school.com/',
			sonMenu:"[]",
			asc :4
		},
		'zone':{
			appid:'5',
			icon:'zone.png',
			name:'空间',
			url:'http://www.jq-school.com/',
			sonMenu:"[]",
			asc :5
		},
		'internet':{
			appid:'6',
			icon:'internet.png',
			name:'浏览网页',
			url:'http://www.jq-school.com/',
			sonMenu:"[]",
			asc :6
		}
	
	}
};
ops = {//向桌面添加应用
	Icon1:['folder','user','disk','theme']
	/**Icon2:['mangguo','tuanmap','fastsearch','bianqian','wangdesk'],
	Icon3:['friend','friendnear','friendgroup','kaikai','kxjy'],
	Icon4:['qidianzhongwen','qqread','xiami','musicbox','vadio','leshi'],
	Icon5:['doudizhi',	'3366',	'qqbaby','game'],
	Icon6:['3366','friendnear','yule','kaikai','Clock'],
	Icon7:['friend','sosomap','xiami','game','kxjy'],
	Icon8:['friend','mangguo']**/
}
//初始化左边快捷菜单
var leftMenu = new Array(['appmarket','qq','weibo','mail','internet','zone']);


