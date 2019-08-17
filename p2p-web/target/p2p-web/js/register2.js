$(function () {
    //验证手机号
    $("#phone").blur(function () {
        checkPhone();
    });

    //验证密码
    $("#loginPassword").blur(function () {
        checkLoginPassword();
    });

    //验证确认密码
    $("#replayLoginPassword").blur(function () {
        checkReplayLoginPassword();
    });

    //验证图形验证码
    $("#captcha").blur(function () {
        checkCaptcha();
    });

    //同意协议
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

    //注册用户
    $("#btnRegist").click(function () {
        register();
    });
});

function checkPhone() {
    var phone=$.trim($("#phone").val());

    var ret;

    if(phone==null||phone==""){
        //提示错误信息
        showError("phone","请输入手机号");
        return false;
    }else if(!/^1[1-9]\d{9}$/.test(phone)){
        showError("phone","请输入正确的手机号");
        return false;
    }else{
        //发送异步请求
        $.ajax({
            url:'user/checkPhone',
            type:'post',
            data:{
                phone:phone
            },
            async:false,//把ajax请求设置成同步请求
            success:function (data) {
                if(data.code=='1000'){
                    showSuccess("phone");
                    //return true;
                    ret=true;
                }else{
                    showError("phone",data.message);
                    //return false;
                    ret=false;
                }
            },
            error:function () {
                showError("phone","系统忙，请稍后....");
            }
        });
    }

    return ret;
}

function checkLoginPassword() {
    var loginPassword=$.trim($("#loginPassword").val());

    if(loginPassword==null||loginPassword==""){
        showError("loginPassword","请输入密码");
        return false;
    }else if(!/^[0-9a-zA-Z]+$/.test(loginPassword)){
        showError("loginPassword","密码只能由数字和字母组成");
        return false;
    }else if(!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)){
        showError("loginPassword","密码必须同时包含数字和字母");
        return false;
    }else if(!(loginPassword.length>=6&&loginPassword.length<=20)){
        showError("loginPassword","密码应为 6-20 位");
        return false;
    }else{
        showSuccess("loginPassword");
        return true;
    }
}

function checkReplayLoginPassword() {
    var replayLoginPassword=$.trim($("#replayLoginPassword").val());
    var loginPassword=$.trim($("#loginPassword").val());

    if(!checkLoginPassword()){
        return false;
    }

    if(replayLoginPassword==null||replayLoginPassword==""){
        showError("replayLoginPassword","请输入确认密码");
        return false;
    }else if(replayLoginPassword!=loginPassword){
        showError("replayLoginPassword","两次输入密码不一致");
        return false;
    }else{
        showSuccess("replayLoginPassword");
        return true;
    }
}

function checkCaptcha() {
    var captcha=$.trim($("#captcha").val());

    var ret;

    if(captcha==null||captcha==""){
        showError("captcha","请输入验证码");
        return false;
    }else{
        //向后台发送异步请求
        $.ajax({
            url:'user/checkCaptcha',
            type:'post',
            data:{
                captcha:captcha
            },
            async:false,
            success:function (data) {
                if(data.code=='1000'){
                    showSuccess("captcha");
                    ret=true;
                }else{
                    showError("captcha",data.message);
                    ret=false;
                }
            }
        });
    }

    return ret;
}

function register() {
    var phone=$.trim($("#phone").val());
    var loginPassword=$.trim($("#loginPassword").val());
    var replayLoginPassword=$.trim($("#replayLoginPassword").val());
    var captcha=$.trim($("#captcha").val());

    if(checkPhone()&&checkLoginPassword()&&checkReplayLoginPassword()&&checkCaptcha()){
        $.ajax({
            url:'user/register',
            type:'post',
            data:{
                phone:phone,
                loginPassword:$.md5(loginPassword),
                replayLoginPassword:$.md5(replayLoginPassword),
                captcha:captcha
            },
            success:function (data) {
                if(data.code=='1000'){
                    window.location.href="login.jsp";
                }else{
                    showError("captcha",data.message);
                }
            },
            error:function () {
                showError("captcha","系统忙，请稍后....");
            }
        });
    }
}

//显示错误信息
function showError(id,msg){
    $("#"+id+"Ok").hide();
    $("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
    $("#"+id+"Err").show();
    $("#"+id).addClass("input-red");
}
//显示成功
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