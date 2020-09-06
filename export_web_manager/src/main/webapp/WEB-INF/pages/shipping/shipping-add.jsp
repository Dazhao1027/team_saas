<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="../base.jsp"%>
<!DOCTYPE html>
<html>

<head>
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>数据 - AdminLTE2定制版</title>
    <meta name="description" content="AdminLTE2定制版">
    <meta name="keywords" content="AdminLTE2定制版">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <!-- 页面meta /-->
</head>
<body>
<div id="frameContent" class="content-wrapper" style="margin-left:0px;">
    <!-- 内容头部 -->
    <section class="content-header">
        <h1>
            货运管理
            <small>委托单表单</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="all-admin-index.html"><i class="fa fa-dashboard"></i> 首页</a></li>
            <li><a href="all-order-manage-list.html">委托管理</a></li>
            <li class="active">委托表单</li>
        </ol>
    </section>
    <!-- 内容头部 /-->

    <!-- 正文区域 -->
    <section class="content">

        <!--订单信息-->
        <div class="panel panel-default">
            <div class="panel-heading">委托合同信息</div>
            <form id="editForm" action="${ctx}/cargo/shipping/edit" method="post">
                <input type="hidden" name="id" value="${packing.packingListId}">
                <div class="row data-type" style="margin: 0px">
                    <div class="col-md-2 title">运输方式</div>
                    <div class="col-md-4 data">
                        <div class="form-group form-inline">
                            <div class="radio"><label><input type="radio" ${shipping.orderType=="SEA"?'checked':''} name="orderType" value="SEA">海运</label></div>
                            <div class="radio"><label><input type="radio" ${shipping.orderType=="AIR"?'checked':''} name="orderType" value="AIR">空运</label></div>
                        </div>
                    </div>
                    <div class="col-md-2 title">货主</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="传智博客" name="shipper" value="${shipping.shipper}">
                    </div>

                    <%--<div class="col-md-2 title">提单抬头</div>--%>
                    <%--<div class="col-md-4 data">--%>
                        <%--<div class="input-group date">--%>
                            <%--<div class="input-group-addon">--%>
                                <%--&lt;%&ndash;<i class="fa fa-calendar"></i>&ndash;%&gt;--%>
                            <%--</div>--%>
                            <%--&lt;%&ndash;<input type="text" placeholder="签单日期"  name="signingDate" class="form-control pull-right"&ndash;%&gt;--%>
                                   <%--&lt;%&ndash;value="<fmt:formatDate value="${contract.signingDate}" pattern="yyyy-MM-dd"/>" id="signingDate">&ndash;%&gt;--%>
                            <%--<input type="text" class="form-control" placeholder="提单抬头" name="consignee" value=""${shipping.consignee}">--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <div class="col-md-2 title">提单抬头</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="提单抬头" name="consignee" value="${shipping.consignee}">
                    </div>


                    <div class="col-md-2 title">正本通知人</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="正本通知人" name="notifyParty" value="${shipping.notifyParty}">
                    </div>

                    <div class="col-md-2 title">信用证号</div>
                    <div class="col-md-4 data">
                        <input type="text"  class="form-control" placeholder="信用证号" name="lcNo" value="${multiExport.lcno}">
                    </div>

                    <div class="col-md-2 title">唛头</div>
                    <div class="col-md-4 data">
                        <input type="text"  class="form-control" placeholder="唛头" name="marks" value="${multiExport.marks}">
                    </div>

                    <div class="col-md-2 title">装运港</div>
                    <div class="col-md-4 data">
                        <input type="text"  class="form-control" placeholder="装运港" name="portOfLoading" value="${export.shipmentPort}">
                    </div>
                    <div class="col-md-2 title">转运港</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="如无转运港,可以不填" name="portOfTrans" value="${shipping.portOfTrans}">
                    </div>

                    <%--<div class="col-md-2 title">船期</div>--%>
                    <%--<div class="col-md-4 data">--%>
                        <%--<div class="input-group date">--%>
                            <%--<div class="input-group-addon">--%>
                                <%--<i class="fa fa-calendar"></i>--%>
                            <%--</div>--%>
                            <%--<input type="text" placeholder="船期"  name="shipTime" class="form-control pull-right"--%>
                                   <%--value="<fmt:formatDate value="${contract.shipTime}" pattern="yyyy-MM-dd"/>" id="shipTime">--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <div class="col-md-2 title">卸货港</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="卸货港" name="portOfDischar" value="${export.destinationPort}">
                    </div>
                    <div class="col-md-2 title">是否分批</div>
                    <div class="col-md-4 data">
                        <div class="form-group form-inline">
                            <div class="radio"><label><input type="radio" ${shipping.isBatch==1?'checked':''} name="isBatch" value="1">是</label></div>
                            <div class="radio"><label><input type="radio" ${shipping.isBatch==0?'checked':''} name="isBatch" value="0">否</label></div>

                        </div>
                    </div>
                    <div class="col-md-2 title">是否转船</div>
                    <div class="col-md-4 data">
                        <div class="form-group form-inline">
                            <div class="radio"><label><input type="radio" ${shipping.isTrans==1?'checked':''} name="isTrans" value="1">是</label></div>
                            <div class="radio"><label><input type="radio" ${shipping.isTrans==0?'checked':''} name="isTrans" value="0">否</label></div>

                        </div>
                    </div>
                    <%--<div class="col-md-2 title">交货期限</div>--%>
                    <%--<div class="col-md-4 data">--%>
                        <%--<div class="input-group date">--%>
                            <%--<div class="input-group-addon">--%>
                                <%--<i class="fa fa-calendar"></i>--%>
                            <%--</div>--%>
                            <%--<input type="text" placeholder="交货期限"  name="deliveryPeriod" class="form-control pull-right"--%>
                                   <%--value="<fmt:formatDate value="${contract.deliveryPeriod}" pattern="yyyy-MM-dd"/>" id="deliveryPeriod">--%>
                        <%--</div>--%>
                    <%--</div>--%>

                    <div class="col-md-2 title">扼要说明</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="remark" name="remark" value="${shipping.remark}">
                    </div>

                    <%--<div class="col-md-2 title">打印版式</div>--%>
                    <%--<div class="col-md-4 data">--%>
                        <%--<input type="text" class="form-control" placeholder="打印版式" name="printStyle" value="${contract.printStyle}">--%>
                    <%--</div>--%>
                    <div class="col-md-2 title">委托总金额</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="0" name="shippingMoney" value="${shipping.shippingMoney}">
                    </div>
                    <%--<div class="col-md-2 title rowHeight2x">备注</div>--%>
                    <%--<div class="col-md-10 data rowHeight2x">--%>
                        <%--<textarea class="form-control" rows="3" name="remark">${contract.remark}</textarea>--%>
                    <%--</div>--%>
                </div>
          </form>
        </div>
        <!--订单信息/-->

        <!--工具栏-->
        <div class="box-tools text-center">
            <button type="button" onclick='document.getElementById("editForm").submit()' class="btn bg-maroon">保存</button>
            <button type="button" class="btn bg-default" onclick="history.back(-1);">返回</button>
        </div>
        <!--工具栏/-->

    </section>
    <!-- 正文区域 /-->

</div>
<!-- 内容区域 /-->
</body>
<script src="../../plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="../../plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<link rel="stylesheet" href="../../css/style.css">
<script>
    $('#signingDate').datepicker({
        autoclose: true,
        format: 'yyyy-mm-dd'
    });
    $('#deliveryPeriod').datepicker({
        autoclose: true,
        format: 'yyyy-mm-dd'
    });
    $('#shipTime').datepicker({
        autoclose: true,
        format: 'yyyy-mm-dd'
    });
</script>
</html>