//初始化jquery函数
$(function () {
    //验证手机号
    $(":text[name=phone]").blur(function () {
        checkPhone();
    });
    //验证密码
    $("#loginPassword").blur(function () {
        checkLoginPassword();
    });
    //验证再次输入密码
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
        //var ischeck=$("#agree").prop("checked")
        if (ischeck) {
            $("#btnRegist").attr("disabled", false);
            $("#btnRegist").removeClass("fail");
        } else {
            $("#btnRegist").attr("disabled","disabled");
            $("#btnRegist").addClass("fail");
        }
    });
    //注册
    $("#btnRegist").blur(function () {
        register();
    });
});

/*用户输入手机号必须进行验证
a)  手机号不能为空
b)  手机号格式
c)  手机号是否已被注册
*/
function checkPhone() {
    //获得手机号,并去空格
    var phone=$.trim($("#phone").val());
    var flag;
    //判断手机号是否为空
    if (phone==null||phone==""){
        showError("phone","请输入手机号");
        return false;
        //判断手机号是否合法
    }else if (!/^1[1-9]\d{9}$/.test(phone)) {
        showError("phone","请输入正确的手机号");
        return false;
    }else {
        //验证手机号在数据库里是否存在，发送异步请求
        $.ajax({
            url:'user/checkPhone',
            type:'post',
            data:{
                phone:phone
            },
            async:false,
            success:function (data) {
                if (data.code=="1000"){
                    showError("phone",data.message);
                    flag= false;
                }else if (data.code=="1001"){
                    showSuccess("phone");
                    flag=true
                }
            },
            error:function () {
                showError("phone","系统繁忙，请稍后重试...")
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
function checkLoginPassword() {
    var loginPassword=$.trim($("#loginPassword").val());

    if (loginPassword==null||loginPassword==""){
        showError("loginPassword","请输入密码");
        return false;
    }else if (!/^[0-9a-zA-Z]+$/.test(loginPassword)){
        showError("loginPassword","密码只能使用数字和英文字母");
        return false;
    }else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*$/.test(loginPassword)){
        showError("loginPassword","密码不合规，必须包含英文和数字");
        return false;
    }else if (loginPassword.length>=6 && loginPassword.length<=16) {
        showSuccess("loginPassword");
        return true;
    }else {
        showError("loginPassword","密码长度应为6-16位");
        return false;
    }
}
//验证两次密码输入是否一致
function checkReplayLoginPassword() {
    //先判断第一次输入密码是否合法
    if (!checkLoginPassword()) {
        //showError("loginPassword","请输入密码");
        checkLoginPassword();
        return false;
    }
    var loginPassword=$.trim($("#loginPassword").val());
    var replayLoginPassWord=$.trim($("#replayLoginPassword").val());

    if (replayLoginPassWord==null||replayLoginPassWord==""){
        showError("replayLoginPassword","请输入确认密码");
        return false;
    } else if (replayLoginPassWord!=loginPassword){
        showError("replayLoginPassword","两次输入的密码不一样");
        return false;
    } else {
        showSuccess("replayLoginPassword");
        return true;
    }
}

/*形验证码
 * 不能空
 * 用户输入的图形验证码与系统生成保持一致
 */
function checkCaptcha() {
    var captcha=$.trim($("#captcha").val());
    var ret=true;
    if (captcha==null||captcha==""){
        showError("captcha","请输入图形验证码")
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
                    showError("captcha",data.message)
                    ret=false;
                } else {
                    showSuccess("captcha")
                    ret=true;
                }
            },
            error:function () {
                showError("captcha","系统繁忙，请稍后重试...")
            }
        });
    }
    return ret;
}

//注册用户，再次验证上面输入是否正确
function register() {
    var phone=$.trim($("#phone").val());
    var loginPassword=$.trim($("#loginPassword").val());
    var replayLoginPassWord=$.trim($("#replayLoginPassword").val());
    var captcha=$.trim($("#captcha").val());
    if (checkPhone&&checkLoginPassword&&checkReplayLoginPassword&&checkCaptcha) {
        $.ajax({
            url: 'user/register',
            type: 'post',
            data: {
                phone: phone,
                loginPassword: $.md5(loginPassword),
                replayLoginPassWord: $.md5(replayLoginPassWord),
                captcha: captcha
            },
            sync: false,
            success: function (data) {
                if (data.code == "1001") {
                    //跳转到登录页面
                    window.location.href = "login.jsp"
                } else {
                    showError("captcha", data.message)
                }
            },
            error: function () {
                showError("captcha", "系统繁忙，请稍后重试====")
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
//显示成功信息
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