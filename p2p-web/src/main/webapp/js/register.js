//注意：在js文件中不能使用${pageContext.request.contextPath}/
$(function(){
    //手机号验证
    $("#phone").blur(function(){
        checkPhone();
    });
    //验证密码
    $("#loginPassword").blur(function(){
        checkLoginPassword();
    });
    //验证确认密码
    $("#replayLoginPassword").blur(function(){
        checkLoginPasswordEqu();
    });
    //图形验证码
    $("#captcha").blur(function(){
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
    $("#btnRegist").click(function(){
        regist();
    });
});

/*用户输入手机号必须进行验证
a)  手机号不能为空
b)  手机号格式
c)  手机号是否已被注册
*/
function checkPhone(){
    //获取用户输入的手机号
    var phone = $.trim($("#phone").val());
    var ret=true;

    if("" == phone) {
        showError("phone","请输入手机号");
        return false;
    }else if(!/^1[1-9]\d{9}$/.test(phone)){
        showError("phone","请输入正确的手机号码");
        return false;
    }else{
        $.ajax({
            url:'user/checkPhone',
            type:'post',
            data:{
                phone:phone
            },
            async:false,
            success:function(data){
                if(data.code==0){
                    showSuccess("phone");
                    flag = true;
                }else{
                    showError("phone",data.message);
                    flag = false;
                }
            },
            error:function(){
                showError("phone","系统繁忙，请稍后重试...");
                flag = false;
            }
        });
    }

    return flag;
}

/*密码验证格式：
a)  密码不能为空
b)  密码字符只可使用数字和大小写英文字母
c)  密码应同时包含英文或数字
d)  密码应为6-16位
e)  两次输入密码必须一致*/
function checkLoginPassword(){
    //获取用户输入的登录密码
    var loginPassword = $.trim($("#loginPassword").val());
    var replayLoginPassword = $.trim($("#replayLoginPassword").val());

    if("" == loginPassword) {
        showError("loginPassword","请输入登录密码");
        return false;
    }else if(!/^[0-9a-zA-Z]+$/.test(loginPassword)){
        showError("loginPassword","密码字符只可使用数字和英文字母");
        return false;
    }else if(!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)){
        showError("loginPassword","密码应同时包含英文或数字 ");
        return false;
    }else if(loginPassword.length < 6 || loginPassword.length > 16){
        showError("loginPassword","密码长度应为6-16位");
        return false;
    }else{
        showSuccess("loginPassword");
    }
    return true;
}
//验证确认密码
/*再次验证登录密码
a)  上次验证有错，隐藏提示信息
b)  第一次密码是否为空
c)  第二次密码是否为空
d)  判断两次是否一致*/
function checkLoginPasswordEqu(){
    //获取用户登录密码
    var loginPassword = $.trim($("#loginPassword").val());
    var replayLoginPassword = $.trim($("#replayLoginPassword").val());

    if(!checkLoginPassword()) {
        hideError("replayLoginPassword");
        return false;
    }

    if("" == replayLoginPassword) {
        showError("replayLoginPassword","请输入确认登录密码");
        return false;
    }else if(replayLoginPassword != loginPassword){
        showError("replayLoginPassword","两次输入密码不一致");
        return false;
    }else{
        showSuccess("replayLoginPassword");
    }
    return true;
}

//图形验证码
/*
 * 不能空
 * 用户输入的图形验证码与系统生成保持一致
 */
function checkCaptcha(){
    var ret = true;
    //获取用户输入的图形验证码
    var captcha = $.trim($("#captcha").val());
    if("" == captcha) {
        showError("captcha","请输入图形验证码");
        return false;
    }else{
        $.ajax({
            url:"user/checkCaptcha",
            type:"post",
            data:"captcha="+captcha,
            async:false,
            success:function(data) {
                if(data.code == 0) {
                    showSuccess("captcha");
                    ret = true;
                } else {
                    showError("captcha",data.message);
                    ret = false;
                }

            },
            error:function() {
                showError("captcha","系统繁忙，请稍后重试...");
                ret = false;
            }
        });
    }
    return ret;
}
//注册用户
function regist(){
    //获取用户输入的表单内容
    var phone = $.trim($("#phone").val());
    var loginPassword = $.trim($("#loginPassword").val());
    var replayLoginPassword = $.trim($("#replayLoginPassword").val());
    var captcha = $.trim($("#captcha").val());

    //先验证所有数据
    if(checkPhone() && checkLoginPassword() && checkLoginPasswordEqu() && checkCaptcha()) {
        //密码要使用jquery.md5加密：$.md5(加密的文本内容),返回一个加密之后 的字符串
        $.ajax({
            url:"user/register",
            type:"post",
            data:{
                "phone":phone,
                "loginPassword":$.md5($.trim($("#loginPassword").val())),
                "replayLoginPassword":$.md5($.trim($("#replayLoginPassword").val())),
                "captcha":captcha
            },
            async:false,
            success:function(data) {
                if(data.code == 0) {
                    //注册成功
                    window.location.href = "realName.jsp";
                } else {
                    //注册失败
                    showError("catpcha",data.message);
                }
            },
            error:function() {
                showError("captcha","系统繁忙，请稍后重试...");
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