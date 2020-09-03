<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
    <%--授权：登陆用户有权限才能看到这里的菜单--%>
    <%--老王有权限看； jack没有权限看--%>
    <shiro:hasPermission name="用户管理">
        <a href="#">用户管理</a>
    </shiro:hasPermission>
    
    <shiro:hasPermission name="角色管理">
        <a href="#">角色管理</a>
    </shiro:hasPermission>


    <%--jack、老王都可以看下面的菜单--%>
    <shiro:hasPermission name="购销合同">
        <a href="#">购销合同...</a>
    </shiro:hasPermission>



</body>
</html>
