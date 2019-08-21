$(function(){

	$("#bidMoney").blur(function(){
		checkMoney();
	});

    $("#investNow").click(function(){
        invest();
    });
});
//验证用户输入的投资金额
function checkMoney() {
	//获取用户输入的投资金额
	var bidMoney = $.trim($("#bidMoney").val());
	/* 
	验证用户输入投资金额 
	a)  投资金额不能为空 
	b)  投资金额应为大于0 的整数 
	c)  投资金额应为100的整数 
	d)  投资金额不能低于起投金额 
	e)  单笔投资金额不能超过最大投资限额 
	f)  投资金额不得大于产品剩余可投金额
	f)  验证通过计算预计本息收益 
	2.  计算预计本息收益，公式如下： 
	本息收益  =  投资金额  *  （年化利率/100/365）*  投资周期 
	投资周期：单位为天 */
	
	if("" == bidMoney) {
		$("#shouyi").html("");
		$(".max-invest-money").html("请输入投资金额");
		return false;
	} else if(isNaN(bidMoney)) {
		$("#shouyi").html("");
		$(".max-invest-money").html("投资金额只能为数字");
		return false;
	} else if(parseFloat(bidMoney) <= 0) {
		$("#shouyi").html("");
		$(".max-invest-money").html("投资金额应为大于0的数字");
		return false;
	} else if(parseFloat(bidMoney) % 100 != 0) {
		$("#shouyi").html("");
		$(".max-invest-money").html("投资金额应为100的整数倍");
		return false;
	} else if(parseFloat(bidMoney)<parseFloat(bidMinLimit)){
		$("#shouyi").html("");
		$(".max-invest-money").html("本产品投资金额最少不能低于"+bidMinLimit+"元");
		return false;
	}else if(parseFloat(bidMoney)>parseFloat(bidMaxLimit)){
		$("#shouyi").html("");
		$(".max-invest-money").html("本产品投资金额最多不能高于"+bidMaxLimit+"元");
		return false;
	}else if(parseFloat(bidMoney)>parseFloat(leftProductMoney)){
		$("#shouyi").html("");
		$(".max-invest-money").html("投资金额最多不能高于剩余可投金额"+leftProductMoney+"元");
		return false;
	}else{
		//清空错误提示信息
		$(".max-invest-money").html("");
	
		//计算收益 = 投资金额 * 天利率 * 投资周期（单位为天）
		var incomeMoney = "";
		//根据产品周期分类：新手宝（周期为天）和优先、散标（周期为月）
		if("0" == productType) {
			//新手宝产品(投资周期以天为单位)
			incomeMoney = bidMoney * (parseFloat(rate) /100/ 365) * cycle;
		} else {
			//优选或散标产品(投资周期以月为单位)
			incomeMoney = bidMoney * (parseFloat(rate) /100/ 365) * cycle * 30;
		}

		//Math.round(x)函数：可以把一个数字舍入为最接近的整数。
		//Math.pow(x,y)：指x的y次幂
		incomeMoney = Math.round(incomeMoney * Math.pow(10,2)) / Math.pow(10,2);
		$("#shouyi").html(incomeMoney);
	}
	return true;
}

//用户投资
function invest() {
	/*
    投资业务规则
        1.投资金额合法
        2.用户是否登录
        3.用户是否进行实名认证
        4.投资金额是否小于启投金额
        5.投资金额是否超过单笔投资限额
        6.投资金额是否超过产品剩余可投金额*/
	//获取用户的投资金额
	var bidMoney = $.trim($("#bidMoney").val());
	
	if(checkMoney()) {
		if("" == user) {
			if(confirm("您尚未登录，请登录")) {
				window.location.href = "../login.jsp";
			}
		} else if("" == userName) {
			if(confirm("您尚未实名认证，请去实名认证")) {
				window.location.href = "../realName.jsp";
			}
		} else if(parseFloat(bidMoney) > parseFloat(availableMoney)) {
			$(".max-invest-money").html("帐户余额不足，请充值");
			return;
		}else {
			$(".max-invest-money").html("");

			var url=contextPath+"bid/invest";
			$.ajax({
				url:url,
				type:"post",
				data:{
					"bidMoney":bidMoney,
					"loanId":loanId
				},
				success:function(data) {
					if(data.code == "0") {
						$("#failurePayment").show();
						$("#dialog-overlay1").show();
					} else {
						$(".max-invest-money").html(data.message);
					}
				},
				error:function() {
					$(".max-invest-money").html("系统正忙，客官请稍后再投资...");
				}
			});
		}
	}
}

function closeit() {
	$("#failurePayment").hide();
	$("#dialog-overlay1").hide();
	window.location.href=contextPath+"user/myCenter";
}