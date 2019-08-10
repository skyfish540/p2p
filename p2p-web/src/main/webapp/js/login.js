var referrer = "";//登录后返回页面
referrer = document.referrer;
if (!referrer) {
	try {
		if (window.opener) {                
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;
			}  
	} catch (e) {
	}
}

//输入域失去焦点，将红色提示信息清除
$(function() {
	//加载平台信息
	loadState();
	
	$("#phone").blur(function() { 
		checkPhone();
	});
	$("#loginPassword").blur(function() { 
		checkLoginPassword();
	});
	
	//按键盘Enter键即可登录
	$(document).keyup(function(event){
		if(event.keyCode == 13){
			login();
		}
	});
	
	$("#loginBtn").click(function(){
		login();
	});
});

//验证手机号
function checkPhone(){
	var phone=$.trim($("#phone").val());
	if(""==phone||"请输入11位手机号码"==phone){
		$("#showId").html("请输入手机号码");
		return false;
	}else if(!/^1[1-9]\d{9}$/.test(phone)){
		$("#showId").html("请输入正确的手机号码");
		return false;
	}else{
		$("#showId").html("");
	}
	return true;
}

//验证登录密码
function checkLoginPassword(){
	var loginPassword=$.trim($("#loginPassword").val());
	if(""==loginPassword||"请输入登录密码"==loginPassword){
		$("#showId").html("请输入登录密码");
		return false;
	}else{
		$("#showId").html("");
	}
	return true;
}

//用户登录
function login() {
	var phone = $("#phone").val();
	var loginPassword = $("#loginPassword").val();
	var captcha = $("#captcha").val();
	
	if(!checkPhone()){
		return false;
	}
	if(!checkLoginPassword()){
		return false;
	}
	if (captcha=="" && $("#showCaptcha").css('display') == "block"){
		$("#showId").html("请输入图形验证码");
		return false;
	}
	
	$.ajax({
	   type:"POST",
	   url:"user/login",
	   data:{
		   "phone":phone,
		   "loginPassword":$.md5(loginPassword),
		   "captcha":captcha
	   },
	   success: function(data) {
		   if(data.code=='0'){//登录成功，跳转到原来页面
			   if ("" != referrer) {
		    		window.location.href = referrer;
		    	} else {
			    	window.location.href = "user/myCenter";
		    	}
		   }else{//登录失败
			   //提示信息
			   $("#showId").html(data.message);
			   //如果登录失败次数超过三次，需要输入验证码
			   if(data.errorLoginCount>=3){
				  $("#showCaptcha").css("display","block"); 
			   }
		   }
	   	}
	});
}
//用户及交易统计
function loadState () {
	$.ajax({
		url:'loan/loadState',
		type:'GET',
		success:function(data){
			$("#avg").html(data.historyAverageRate);
			$("#user").html(data.countOfAllUsers);
			$("#gold").html(data.allBidMoney);
		}
	});
}