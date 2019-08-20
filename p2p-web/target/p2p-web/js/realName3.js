var myReferre=document.referrer; //火狐浏览器的跳转到原来的页面
if(myReferre==null||myReferre==""){
    myReferre=window.opener.location.href; //IE浏览器跳转到原来的页面
}
$(function () {
    $("#realName").blur(function () {
        checkRealName();
    });

    $("#idCard").blur(function () {
        checkIdCard();
    });

    $("#replayIdCard").blur(function () {
        checkReplayIdCard();
    });

    $("#captcha").blur(function () {
        checkCaptcha();
    });
    //同意实名认证协议
    $("#agree").click(function(){
        //var ischeck = document.getElementById("agree").checked;
        var ischeck = $("#agree").prop("checked");
        if (ischeck) {
            $("#btnRegist").attr("disabled", false);
            $("#btnRegist").removeClass("fail");
        } else {
            $("#btnRegist").attr("disabled","disabled");
            $("#btnRegist").addClass("fail");
        }
    });

    //实名认证
    $("#btnRegist").click(function () {
        verifyRealName();
    });

});

//验证真实姓名
function checkRealName() {
    var realName=$.trim($("#realName").val());
    if (realName==null||realName==""){
        showError("realName","请输入真实姓名");
        return false;
    }else if (!/^[\u4e00-\u9fa5][\u4e00-\u9fa5]*$/.test(realName)){
        showError("realName","真实姓名只能为中文")
        return false;
    }else {
        showSuccess("realName")
        return true;
    }
}
//验证身份证输入
function checkIdCard() {
    var idCard=$.trim($("#idCard").val());
    if (idCard==null||idCard==""){
        showError("idCard","请输入身份证");
        return false;
    } else if (!/(^\d{15}$)|(^\d{17}(\d|X|x)$)/.test(idCard)){
        showError("idCard","身份证格式不正确");
        return false;
    }else {
        showSuccess("idCard")
        return true;
    }
}

//验证再次输入身份证
function checkReplayIdCard() {
    if (!checkIdCard()){
        showError("idCard","请输入正确的身份证");
        return false;
    }
    var replayIdCard=$.trim($("#replayIdCard").val());
    var idCard=$.trim($("#idCard").val());
    if (replayIdCard==null||replayIdCard==""){
        showError("replayIdCard","请输入确认身份证");
        return false;
    } else if (replayIdCard!=idCard){
        showError("replayIdCard","两次输入的身份证不一样");
        return false;
    } else {
        showSuccess("replayIdCard");
        return true;
    }
}
//验证图形验证码
function checkCaptcha() {
    var captcha=$.trim($("#captcha").val());
    var ret;
    if (captcha==null||captcha=="") {
        showError("captcha","请输入图形验证码");
        return false;
    }else {
        $.ajax({
            url:'user/checkCaptcha',
            type:'post',
            data:{
                captcha:captcha
            },
            sync:false,
            success:function (data) {
                if (data.code=="1000"){
                    showError("captcha",data.message);
                    ret=false;
                } else {
                    showSuccess("captcha")
                    ret=true;
                }
            },
            error:function () {
                showError("captcha","网络异常，请稍后重试...");
                ret=false;
            }
        });
    }
    return ret;
}

//实名认证提交
function verifyRealName() {
    var realName=$.trim($("#realName").val());
    var idCard=$.trim($("#idCard").val());
    var replayIdCard=$.trim($("#replayIdCard").val());
    var captcha=$.trim($("#captcha").val());
    //如果上面验证都通过，则执行认证过程
    if (checkRealName()&&checkIdCard()&&checkReplayIdCard()&&checkCaptcha){
        $.ajax({
            url: 'user/verifyRealName',
            type: 'post',
            data: {
                realName:realName,
                idCard:idCard,
                replayIdCard:replayIdCard,
                captcha:captcha
            },
            success:function (data) {
                if (data.code=="1000"){
                    showError("captcha",data.message);
                } else {
                    //跳转到原来页面
                    window.location.href=myReferre;
                }
            },
            error:function () {
                showError("captcha","网络异常，请稍后重试...");
            }
        });
    }

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