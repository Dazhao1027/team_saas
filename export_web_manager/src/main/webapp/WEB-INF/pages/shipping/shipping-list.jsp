<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
    <script src="${ctx}/plugins/jQuery/jquery-2.2.3.min.js"></script>
</head>
<script>
    function deleteById() {
        var id = getCheckId()
        if(id) {
            if(confirm("你确认要删除此条记录吗？")) {
               //异步发送请求,判断状态是否是草稿,只有草稿才可以删除
                $.ajax({
                    url:"/cargo/shipping/delete?id="+id,
                    type:"get",
                    dataType:"json",
                    success:function (result) {
                        //result==1,删除成功,反之不成功
                        if (result=="1"){
                            alert("删除成功")
                            //刷新页面
                            location.reload();
                        } else {
                            alert("请选择状态为草稿的委托单删除")
                            location.reload();
                        }
                    },
                    error:function (e) {
                        console.log(e)
                        alert("服务器忙...")
                    }
                })
            }
        }else{
            alert("请勾选待处理的记录，且每次只能勾选一个")
        }
    }

    function submit() {
        var id = getCheckId()
        if(id) {
            location.href="${ctx}/cargo/shipping/excel?id="+id;
        }else{
            alert("请勾选待处理的记录，且每次只能勾选一个")
        }
    }

    function cancel() {
        var id = getCheckId()
        if(id) {
            location.href="${ctx}/cargo/contract/cancel.do?id="+id;
        }else{
            alert("请勾选待处理的记录，且每次只能勾选一个")
        }
    }

    function view() {
        var id = getCheckId()
        if(id) {
            location.href="${ctx}/cargo/contract/toView.do?id="+id;
        }else{
            alert("请勾选待处理的记录，且每次只能勾选一个")
        }
    }


</script>
<body>
<div id="frameContent" class="content-wrapper" style="margin-left:0px;">
<section class="content-header">
    <h1>
        委托管理
        <small>委托合同</small>
    </h1>
    <ol class="breadcrumb">
        <li><a href="all-admin-index.html"><i class="fa fa-dashboard"></i> 首页</a></li>
    </ol>
</section>
<!-- 内容头部 /-->

<!-- 正文区域 -->
<section class="content">

    <!-- .box-body -->
    <div class="box box-primary">
        <div class="box-header with-border">
            <h3 class="box-title">委托列表</h3>
        </div>

        <div class="box-body">

            <!-- 数据表格 -->
            <div class="table-box">

                <!--工具栏-->
                <div class="pull-left">
                    <div class="form-group form-inline">
                        <div class="btn-group">
                            <%--<button type="button" class="btn btn-default" title="新建" onclick='location.href="${ctx}/cargo/contract/toAdd.do"'><i class="fa fa-file-o"></i> 新建</button>--%>
                            <%--<button type="button" class="btn btn-default" title="查看" onclick='view()'><i class="fa  fa-eye-slash"></i> 查看</button>--%>
                            <button type="button" class="btn btn-default" title="删除" onclick='deleteById()'><i class="fa fa-trash-o"></i> 删除</button>
                            <button type="button" class="btn btn-default" title="刷新" onclick="window.location.reload();"><i class="fa fa-refresh"></i> 刷新</button>
                            <button type="button" class="btn btn-default" title="导出Excel" onclick="submit()"><i class="fa fa-retweet"></i> 导出Excel</button>
                            <button type="button" class="btn btn-default" title="生成发票" onclick="cancel()"><i class="fa fa-retweet"></i> 生成发票</button>
                        </div>
                    </div>
                </div>
                <div class="box-tools pull-right">
                    <div class="has-feedback">
                        <input type="text" class="form-control input-sm" placeholder="搜索">
                        <span class="glyphicon glyphicon-search form-control-feedback"></span>
                    </div>
                </div>
                <!--工具栏/-->

                <!--数据列表-->
                <table id="dataList" class="table table-bordered table-striped table-hover dataTable">
                    <thead>
                    <tr>
                        <th class="" style="padding-right:0px;">

                        </th>
                        <th class="sorting">编号</th>
                        <th class="sorting">运输方式</th>
                        <th class="sorting">货主</th>
                        <th class="sorting">提单抬头</th>
                        <th class="sorting">正本通知人</th>
                        <th class="sorting">信用证</th>
                        <th class="sorting">装运港</th>
                        <th class="sorting">转运港</th>
                        <th class="sorting">卸货港</th>
                        <th class="sorting">是否分批</th>
                        <th class="text-center">是否转船</th>
                        <th class="text-center">扼要说明</th>
                        <th class="text-center">状态</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${pageInfo.list}" var="o" varStatus="status">
                        <tr>
                            <td><input type="checkbox" name="id" value="${o.shippingOrderId}"/></td>
                            <%--编号--%>
                            <td>${o.shippingOrderId}</td>
                            <%--运输方式--%>
                            <td><c:if test="${o.orderType=='SEA'}">海运</c:if>
                                <c:if test="${o.orderType=='AIR'}">空运</c:if>
                            </td>

                                <%--货主--%>
                            <td>
                                    ${o.shipper}
                            </td>
                            <%--提单抬头--%>
                            <td>${o.consignee}</td>
                            <%--正本通知人--%>
                            <td>${o.notifyParty}</td>
                            <%--信用证--%>
                            <td>${o.lcNo}</td>
                            <%--装运港--%>
                            <td>${o.portOfLoading}</td>
                            <%--转运港--%>
                            <td>${o.portOfTrans}</td>
                            <%--卸货港--%>
                            <td>${o.portOfDischar}</td>
                            <%--是否分批--%>
                            <td>
                                <c:if test="${o.isBatch=='1'}"><font color="green">是</font></c:if>
                                <c:if test="${o.isBatch=='0'}"><font color="red">否</font></c:if>
                            </td>
                            <%--是否转船--%>
                            <td>
                                <c:if test="${o.isTrans=='1'}"><font color="green">是</font></c:if>
                                <c:if test="${o.isTrans=='0'}"><font color="red">否</font></c:if>
                            </td>
                            <%--扼要说明--%>
                            <td>${o.remark}</td>
                            <%--状态--%>
                            <td>
                                <c:if test="${o.state==0}"><font color="green">草稿</font></c:if>
                                <c:if test="${o.state==1}"><font color="red">已开票</font></c:if>
                            </td>

                        </tr>
                    </c:forEach>
                    </tbody>
                    <!--
                <tfoot>
                <tr>
                <th>Rendering engine</th>
                <th>Browser</th>
                <th>Platform(s)</th>
                <th>Engine version</th>
                <th>CSS grade</th>
                </tr>
                </tfoot>-->
                </table>
                <!--数据列表/-->

                <!--工具栏/-->

            </div>
            <!-- 数据表格 /-->


        </div>
        <!-- /.box-body -->

        <!-- .box-footer-->
        <div class="box-footer">
            <jsp:include page="../common/page.jsp">
                <jsp:param value="/cargo/shipping/list.do" name="pageUrl"/>
            </jsp:include>
        </div>
        <!-- /.box-footer-->


    </div>

</section>
</div>
</body>

</html>