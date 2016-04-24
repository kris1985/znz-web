﻿var basePath = getContextPath();
DATA={
	menu:[{//一级菜单
		menuid:'m001',
		name:'主页',
		code:'m001',
		icon:'images/home.png'
	},{
		menuid:'m002',
		name:'邮件',
		code:'m002',
		icon:'images/email.png'
	},{
		menuid:'m003',
		name:'文件夹',
		code:'m003',
		icon:'images/portfolio.png'
	},{
		menuid:'m004',
		name:'音乐',
		code:'m004',
		icon:'images/music.png'
	},{
		menuid:'m005',
		name:'视频',
		code:'m005',
		icon:'images/video.png'
	},{
		menuid:'m006',
		name:'备忘录',
		code:'m006',
		icon:'images/history.png'
	},{
		menuid:'m007',
		name:'日历',
		code:'m007',
		icon:'images/calendar.png'
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
			url:basePath+'/admin/user/listUser',
			sonMenu:"[]",
			asc:6
		},
		'disk':{
			appid:'19',
			icon:'friend.png',
			name:'景点门票',
			url:basePath+'/admin/attractions/listAttractions',
			width:'1600px',
			asc:7
		},
		'theme':{
			appid:'20',
			icon:'icon0.png',
			name:'设置主题',
            url:basePath+'/admin/file/toUpdateIndexBg',
			asc:9
		},
		'folder':{
			appid:'21',
			icon:'mangguo.png',
			name:'产品中心',
			url:basePath+'/admin/product/listProduct',
			width:'1000',
			sonMenu:"[]",
			asc:8
		},
		'tuangou':{
			appid:'22',
			icon:'tuanmap.png',
			name:'跟团游',
			url:basePath+'/admin/travel/listGty',
			width:'1000',
			sonMenu:"[]",
			asc:10
		},
		'zyx':{
			appid:'23',
			icon:'3.png',
			name:'自由行',
			url:basePath+'/admin/travel/listZyx' +
			'',
			width:'1000',
			sonMenu:"[]",
			asc:11
		},
		'hotel':{
			appid:'24',
			icon:'hotel.png',
			name:'酒店',
			url:basePath+'/admin/hotel/listHotel',
			width:'1000',
			sonMenu:"[]",
			asc:12
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
	Icon1:['folder','user','disk','theme','tuangou','zyx','hotel']
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


