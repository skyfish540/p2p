var myReferre=document.referrer; //火狐浏览器的跳转到原来的页面
if(myReferre==null||myReferre==""){
    myReferre=window.opener.location.href; //IE浏览器跳转到原来的页面
}

$(function () {
    //初始化登录页面的动态数据
    initLoanData();
    $("#phone").blur(function () {
        checkPhone();
    });

    $("#loginPassword").blur(function () {
        checkLoginPassword();
    });

    $("#loginId").click(function () {
        login();
    });

    //支持回车键登录
    //keydown，keypress，keyup分别是按下，按着没上抬，上抬键盘 ,推荐：keyup，防止笔记本键盘不小心触摸到
    $(document).keyup(function(event){
        if(event.keyCode ==13){
           login();
        }
    });
});

//初始化登录页面的动态数据
function initLoanData() {
    $.ajax({
        url:'user/initLoanState',
        type:'post',
        success:function (data) {
            $("#historyAvgRate").html(data.historyRate);
            $("#countOfAllUsers").html(data.allUsers);
            $("#allBidMoney").html(data.allBidMoney);
        }
    });
}

//验证手机号是否合法
function checkPhone() {
    var phone=$.trim($(":text[name=phone]").val());
    if (phone==null||phone==""){
        $("#showId").html("请输入账号")
        return false;
    }else if (!/^1[3-9]\d{9}$/.test(phone)){
        $("#showId").html("请输入正确的账号")
        return false;
    }
    $("#showId").html("")
    return true;
}

//验证密码是否正确
function checkLoginPassword() {
    var loginPassword=$.trim($("#loginPassword").val());
    if (loginPassword==null||loginPassword==""){
        $("#showId").html("请输入密码")
        return false;
    } else {
        $("#showId").html("")
        return true;
    }
}

//登录验证
function login() {
    var phone=$.trim($(":text[name=phone]").val());
    var loginPassword=$.trim($("#loginPassword").val());
    if (checkPhone()&&checkLoginPassword()) {
        $.ajax({
            url:'user/login',
            type:'post',
            data:{
                phone:phone,
                loginPassword:loginPassword
            },
            success:function (data) {
                if (data.code=="1000"){
                    $("#showId").html(data.message);
                    return false;
                } else {
                   //登录成功后，跳转到原来的页面
                    window.location.href=myReferre;
                }
            },
            error:function () {
                $("#showId").html("系统繁忙，请稍后重试...");
            }
        });
    }

}