<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>动力金融网 - 专业的互联网金融信息服务平台</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/fund-guanli.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/cashbox-share.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/base.css"/>
    <script type="text/javascript" language="javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/trafficStatistics.js"></script>
    <script type="text/javascript">
        $(function(){
            $("#myCash").attr("class","on");

            $(".selectBox").hover(function(){
                $("ul",this).show();
            },function(){
                $("ul",this).hide();
            });
            $(".selectBox li a").click(function(){
                var selectName=$(this).text();
                $(".select-selected h3").text(selectName);
                $(".selectBox ul").hide();
            });
            $(".select-selected").hover(function(){
                $(this).addClass('select-hover');
            },function(){
                $(this).removeClass('select-hover');
            });
        })
        $(document).ready(function() {
            $("#overView").removeClass("on");
            $("#myAccount").addClass("on");
        });
        $(document).ready(function(){
            $("#touzijilu").addClass("on");
        });
    </script>
</head>

<body>
<!--页头start-->
<div id="header">
    <jsp:include page="commons/header.jsp"/>
</div>
<!--页头end-->

<!-- 二级导航栏start -->
<jsp:include page="commons/subNav.jsp"/>
<!-- 二级导航栏end -->

<!--页中start-->
<div class="mainBox">
    <div class="homeWap">
        <div class="fund-guanli clearfix">
            <div class="left-nav">
                <jsp:include page="commons/leftNav.jsp"/>
            </div>
            <div class="right-body">
                <div class="right-wap">
                    <div class="deal-data">
                        <dl>
                            <dt>
                                <span class="deal-time">投资时间</span>
                                <span class="deal-name">投资产品</span>
                                <span class="deal-type" style="width:120px">投资状态</span>
                                <span class="deal-money">投资金额(元)</span>
                            </dt>
                            <c:forEach items="${bidInfoList}" var="bidInfo">
                                <dd>
                                    <div class="deal-time"><fmt:formatDate value="${bidInfo.bidTime}" pattern="yyyy-MM-dd HH:mm:ss"/></div>
                                    <div class="deal-name">${bidInfo.loanInfo.productName}</div>
                                    <div class="deal-type" style="width:120px">
                                        <c:if test="${bidInfo.bidStatus eq 1}">投资成功</c:if>
                                    </div>
                                    <div class="deal-money">${bidInfo.bidMoney}</div>
                                </dd>
                            </c:forEach>
                            &nbsp;&nbsp;
                            <div class="touzi_fenye" style="width:100%;text-align:center;">
                                共${totalRows}条${totalPages}页　当前为第${pageNo}页
                                <c:choose>
                                    <c:when test="${pageNo eq 1}">
                                        首页
                                        上一页
                                    </c:when>
                                    <c:otherwise>
                                        <a id="lnkNext" href="${pageContext.request.contextPath}/bid/myBid">首页</a>
                                        <a id="lnkNext" href="${pageContext.request.contextPath}/bid/myBid?pageNo=${pageNo-1}">上一页</a>
                                    </c:otherwise>
                                </c:choose>

                                <c:choose>
                                    <c:when test="${pageNo eq totalPages}">
                                        下一页
                                        尾页
                                    </c:when>
                                    <c:otherwise>
                                        <a id="lnkNext" href="${pageContext.request.contextPath}/bid/myBid?pageNo=${pageNo+1}">下一页 </a>
                                        <a id="lnkNext" href="${pageContext.request.contextPath}/bid/myBid?pageNo=${totalPages}">尾页</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </dl>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--页中end-->

<!--页脚start-->
<jsp:include page="commons/footer.jsp"/>
<!--页脚end-->
</body>
</html>