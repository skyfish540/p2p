<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
//我要投资下拉
$(function(){
	$(".headBody .main-nav li:eq(1)").hover(function(){
	    $(this).children(".menu-drop").stop(false,true).fadeIn();
	},function(){
	    $(this).children(".menu-drop").stop(false,true).hide();
	});
});
</script>
<!--页头start-->
<a id="top"></a>
<div class="headBody" id="headBody">
    <div class="header">
    <div class="mainBox clearfix">
        <div class="head_l"><span>客服电话：400-8080-105</span><em></em><a href="http://weibo.com/bjpowernode" target="_blank" class="weibo"></a></div>
        <div class="head_r"><a href="javascript:void(0)">帮助中心</a><em></em><a href="javascript:void(0)" class="phone_jinxin" style="margin-right:15px;"><i></i>手机客户端</a></div>
    </div>
    </div>
    <div class="headCnt clearfix">
        <div class="logo"><a href="${pageContext.request.contextPath}"><img src="${pageContext.request.contextPath}/images/logo.jpg"></a></div>
        <ul class="main-nav">
            <li><h2><a id="jinxin" href="${pageContext.request.contextPath}">首页<i></i></a></h2></li><!--current为当前状态-->
            <li>
            	<h2><a id="wytz" href="javascript:void(0)">我要投资<i></i></a></h2>
            	<div class="menu-drop" style="display:none;">
	               <a href="${pageContext.request.contextPath}/loan/queryListLoanByTypeForPage?productType=1">优选类产品</a>
	               <a href="${pageContext.request.contextPath}/loan/queryListLoanByTypeForPage?productType=2">散标类产品</a>
	            </div>
            </li>
            <li><h2><a id="borrowers" href="javascript:void(0)">借款人信息<i></i></a></h2></li>
            <li><h2><a id="information" href="javascript:void(0)">信息披露<i></i></a></h2></li>
            <li><h2><a id="insurance" href="javascript:void(0)">安全计划<i></i></a></h2></li>
        </ul>
        <div class="login-right">
            <c:choose>
        	<c:when test="${empty sessionScope.sessionUser}">
            	<!--未登录begin-->
            	<div class="no-login"><a href="${pageContext.request.contextPath}/register.jsp" class="btn-reg">注册</a><a href="${pageContext.request.contextPath}/login.jsp" class="btn-login">登录</a></div>
            	<!--未登录end-->
            </c:when>
	        <c:otherwise>
                <!--登录后begin-->
                <div class="logged">
                    <div class="userinfo-up">
                        <a href="${pageContext.request.contextPath}/user/myCenter" class="user-phone">${sessionScope.sessionUser.phone}</a>
                        <a href="${pageContext.request.contextPath}/user/myCenter" class="user-vip" id="member">VIP</a><i></i>
                    </div>
                    <div class="userinfo-drop-down" style="height: 0px;">
                        <div class="down-bk">
                            <div class="user-balance">余额：<span id="frame_top">${sessionScope.sessionUser.financeAccount.availableMoney == null ? "0" : sessionScope.sessionUser.financeAccount.availableMoney}</span>元</div>
                            <ul class="quick-entry clearfix">
                                <li class="icon-recharge"><a href="${pageContext.request.contextPath}/recharge/toRecharge"><i></i><span>充值</span></a></li>
                                <li class="icon-invest" style="margin-left:85px;"><a href="${pageContext.request.contextPath}/"><i></i><span>投资</span></a></li>
                            </ul>
                            <div class="enter">进入“<a href="${pageContext.request.contextPath}/user/myCenter">我的小金库</a>”</div>
                            <div class="exit"><a href="${pageContext.request.contextPath}/user/logout">退出</a></div>
                        </div>
                    </div>
                </div>
                <!--登录后end-->
	        </c:otherwise>
	        </c:choose>
        </div>
    </div>
</div>
<!--页头end-->