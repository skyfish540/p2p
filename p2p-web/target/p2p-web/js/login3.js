$(function () {
    //初始化登录页面的动态数据
    initLoanData();
    $("#phone").blur(function () {
        checkPhone();
    });

    $("#loginPassword").blur(function () {
        checkLoginPassword();
    });
});

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
    var phone=$(":text[name=phone]").val();
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
    var loginPassword=$("#loginPassword").val();
    if (loginPassword==null||loginPassword==""){
        $("#showId").html("请输入密码")
        return false;
    } else {
        $("#showId").html("")
        return true;
    }

}