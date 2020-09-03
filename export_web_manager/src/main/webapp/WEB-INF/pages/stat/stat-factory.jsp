<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../base.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
</head>
<body>
<div id="frameContent" class="content-wrapper" style="margin-left:0px;">
    <section class="content-header">
        <h1>
            统计分析
            <small>厂家销量统计</small>
        </h1>
    </section>
    <section class="content">
        <div class="box box-primary">
            <div id="main" style="width: 1200px;height:400px;"></div>
        </div>
    </section>
</div>
</body>
<script src="../plugins/jQuery/jquery-2.2.3.min.js"></script>
<script src="../../plugins/echarts/echarts.min.js"></script>
<script type="text/javascript">
    // 窗体加载事件中初始化图表
    $(function () {
        // 异步请求，返回json格式
        $.ajax({
            url:"/stat/factorySale",
            type:"get",
            dataType:"json",
            success:function (results) {
                initChart(results);
            }
        })
    });
    
    // 初始化图表
    function initChart(results) {//results=[{name=众鑫, value=489.00}, {name=会龙, value=26201.40}]
        // 存储厂家的数组
        var titles = [];
        for(var i=0; i<results.length; i++){
            // results[i] = {name=众鑫, value=489.00}
            titles[i] = results[i].name;
        }

        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));

        option = {
            title: {
                text: '某站点用户访问来源',
                subtext: '纯属虚构',
                left: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: titles
            },
            series: [
                {
                    name: '访问来源',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: results,      // 异步返回的结果
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };


        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
</script>
</html>