$(function(){
	$("#realName").blur(function(){
		checkRealName();
	});
	
	$("#idCard").blur(function(){
		checkIdCard();
	});
	
	$("#replayIdCard").blur(function(){
		checkReplayIdCard();
	});
	
	$("#captcha").blur(function(){
		checkCaptcha();
	});
	
	$("#btnRegist").click(function(){
		verifyRealName();
	});
});
//检查姓名验证
function checkRealName() {
	var realName = $.trim($("#realName").val());
	if (null==realName || realName=="") {
		showError("realName", "真实姓名不能为空");
		return false;
	} else if (!/^[\u4e00-\u9fa5][\u4e00-\u9fa5]*$/.test(realName)) {
		showError("realName", "请输入真实的中文姓名");
		return false;
	} else {
		showSuccess('realName');
		return true;
	}
}
//身份证号码验证
function checkIdCard() {
	var idCard = $.trim($("#idCard").val());
	//var replayIdCard = $.trim($("#replayIdCard").val());
	if (idCard=="") {
		showError('idCard','身份证号码不能为空');
		return false;
	}
	//身份证号码为15位或18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
	var reg = /(^\d{15}$)|(^\d{17}(\d|X|x)$)/;
	if (!(reg.test(idCard))) {
		showError('idCard','身份证号码不合法');
		return false;
	} else {
		showSuccess('idCard');
	}
	/*if (replayIdCard!=null && replayIdCard!=null&& idCard==replayIdCard) {
		showSuccess('replayIdCard');
	}*/
	return true;
}

//验证确认身份证号码
function checkReplayIdCard() {
	var idCard=$.trim($("#idCard").val());//身份证
	var replayIdCard=$.trim($("#replayIdCard").val());//确认身份证
	if (!checkIdCard()) {
		hideError('replayIdCard');
		return false;
	}
	if(replayIdCard=="") {
		showError('replayIdCard','确认身份证号码不能为空');
		return false;
	} else if(idCard!=replayIdCard) {
		showError('replayIdCard','两次输入身份证号码不一致');
		return false;
	} else {
		showSuccess('replayIdCard');
		return true;
	}
}
//验证码验证
function checkCaptcha() {
	var captcha = $.trim($("#captcha").val());
	var ret=false;
	if (captcha == "") {
		showError('captcha','请输入图形验证码');
		return false;
	} else {
		$.ajax({
			type:"POST",
			url:"user/checkCaptcha",
			async: false,
			data:"captcha="+captcha,
			success: function(data) {
			    if (data.code=="0") {
			    	showSuccess('captcha');
			    	ret = true;
			    } else {
			    	showError('captcha', data.message);
			    	ret = false;
			    }
			},
		    error:function() {
				showError('captcha','网络错误');
				rtn = false;
			}
		});
	}
	
	return ret;
} 
//错误提示
function showError(id,msg) {
	$("#"+id+"Ok").hide();
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	$("#"+id+"Err").show();
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}
//实名认证提交
function verifyRealName () {
	
	var idCard = $.trim($("#idCard").val());
	var replayIdCard = $.trim($("#replayIdCard").val());//确认身份证号
	var realName = $.trim($("#realName").val());
	var captcha = $.trim($("#captcha").val());
	if (checkRealName() && checkIdCard() && checkReplayIdCard()&&checkCaptcha()) {
		$.ajax({
			type:"POST",
			url:"user/verifyRealName",
			data:{
				"realName":realName,
				"idCard":idCard,
				"replayIdCard":replayIdCard,
				"captcha":captcha
			},
			success: function(data) {
				if (data.code == "0") {
					window.location.href = "loan/myCenter";
				} else {
					showError('captcha', data.message);
				}
			},
		    error:function() {
				 showError('captcha','网络错误');
				 rtn = false;
			}
		});
	}
}
//同意实名认证协议
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
});
//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}