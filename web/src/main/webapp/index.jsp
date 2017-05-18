<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.1/moment.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<c:if test="${pageContext.request.isUserInRole('USER')}">
    <script src="${pageContext.request.contextPath}/resources/js/user.js"></script>
</c:if>
<c:if test="${pageContext.request.isUserInRole('ADMIN')}">
    <script src="${pageContext.request.contextPath}/resources/js/admin.js"></script>
</c:if>
<title>Images</title>
</head>
<body>
<div class="container">
<h1>Hello, <%=request.getUserPrincipal() %></h1>
<c:if test="${pageContext.request.isUserInRole('USER')}">
<div class="alert alert-info" role="alert">
<h2>Add new project</h2>
<form action="rest/project/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file" required/>
    <input type="submit" />
</form>
</div>
</c:if>
<table id="myProject" class="table  table-bordered">
   <thead>
     <tr><th><h2>Projects</h2></th></tr>
   </thead>
   <tbody>
   </tbody>
</table>
<c:if test="${pageContext.request.isUserInRole('ADMIN')}">
<div class="alert alert-info" role="alert">
<h2>Add user</h2>
<form id="addUserForm" method="post">
    Username: <input type="text" name="username" id="username" required><br>
    Password: <input type="password" name="password" id="password" required><br>
    <input type="radio" name="role" value="ADMIN">Admin
    <input type="radio" name="role" value="USER">User
    <button type="submit" id="addUserBtn">Submit</button>
</form>
</div>
<table id="users" class="table table-bordered">
   <thead>
     <tr><th><h2>Users</h2></th></tr>
   </thead>
   <tbody>
   </tbody>
</table>

</c:if>
</div>
</body>
</html>
