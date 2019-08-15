var myReferre=document.referrer;
if(myReferre==null||myReferre==""){
    myReferre=window.opener.location.href;
}

$(function () {
    //初始化平台状态数据
    loadLoanState();

    //验证手机号
    $("#phone").blur(function () {
        checkPhone();
    });

    //验证密码
    $("#loginPassword").blur(function () {
        checkLoginPassword();
    });

    //登录
    $("#loginBtn").click(function () {
        login();
    });

    //回车键提交表单
    $(document).keyup(function (event) {
        if(event.keyCode==13){
            login();
        }
    });
});

function loadLoanState() {
    $.ajax({
        url:'loan/loadLoanState',
        type:'post',
        success:function (data) {
            //{historyAvgRate:xxx,countOfAllUsers:xxx,allBidMoney:xxx}
            $("#historyAvgRate").html(data.historyAvgRate);
            $("#countOfAllUsers").html(data.countOfAllUsers);
            $("#allBidMoney").html(data.allBidMoney);
        }
    });
}

function checkPhone() {
    var phone=$.trim($("#phone").val());

    if(phone==null||phone==""){
        $("#showId").html("请输入账号");
        return false;
    }else if(!/^1[1-9]\d{9}$/.test(phone)){
        $("#showId").html("请输入正确的账号");
        return false;
    }else{
        $("#showId").html("");
        return true;
    }
}

function checkLoginPassword() {
    var loginPassword=$.trim($("#loginPassword").val());

    if(loginPassword==null||loginPassword==""){
        $("#showId").html("请输入密码");
        return false;
    }else{
        $("#showId").html("");
        return true;
    }
}

function login() {
    var phone=$.trim($("#phone").val());
    var loginPassword=$.trim($("#loginPassword").val());

    if(checkPhone()&&checkLoginPassword()){
        $.ajax({
            url:'user/login',
            type:'post',
            data:{
                phone:phone,
                loginPassword:$.md5(loginPassword)
            },
            success:function (data) {
                if(data.code=='1000'){
                    //跳转到原来所在页面
                    //window.location.href='index.jsp';

                    window.location.href=myReferre;
                }else{
                    $("#showId").html(data.message);
                }
            },
            error:function () {
                $("#showId").html("系统忙，请稍后....");
            }
        });
    }
}