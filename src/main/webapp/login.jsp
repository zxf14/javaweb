<%@ page contentType="text/html; charset=utf-8" language="java" pageEncoding="UTF-8" errorPage="" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>注册</title>
    <meta charset="utf-8" />
</head>
<body>
<h2>登录</h2>
<form method="post" action="/login">
    <div class="form-group has-feedback">
        <input type="text" class="form-control" name="name" placeholder="姓名"  value="<% out.print(request.getAttribute("login")); %>">
        <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
    </div>
    <div class="form-group has-feedback">
        <input type="password" class="form-control" name="psw" placeholder="密码">
        <span class="glyphicon glyphicon-lock form-control-feedback"></span>
    </div>
    <button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
</form>
Click点击 <a href=" <% response.encodeURL(request.getRequestURI());%> ">here</a> to reload this page.<br>
<p>在线总人数 <%out.print(request.getAttribute("counter"));%></p>
<p>已登录用户 <%out.print(request.getAttribute("userCounter"));%></p>
<p>游客总人数 <%out.print((int)request.getAttribute("counter")-(int)request.getAttribute("userCounter"));%></p>
</body>
</html>
