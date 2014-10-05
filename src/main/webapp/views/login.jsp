<%@ page import="org.springframework.security.core.AuthenticationException" %>
<%@ page import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<authz:authorize ifAllGranted="ROLE_USER">
  <c:redirect url="/views/index.jsp"/>
</authz:authorize>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>DevOps Platform</title>
		<meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<link rel="stylesheet" href="/assets/css/bootstrap.min.css" />
        <link rel="stylesheet" href="/assets/css/font-awesome.css" />
        <link rel="stylesheet" href="/assets/css/unicorn-login.css" />
    	<script type="text/javascript" src="/assets/js/respond.min.js"></script>
	</head>    
	<body>
        <div id="container">
            <authz:authorize ifNotGranted="ROLE_USER">
	            <div id="logo">
	                <img src="/assets/img/logo.png" alt="" />
	            </div>

	            <div id="loginbox">    
	                   
				         
	                <form action="<c:url value="/login.do"/>" method="post">
	    				<p>Enter username and password to continue.</p>
	    				<c:if test="${!empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION}">
					      <div class="error">
					      	<p><font color=red>Your login attempt was not successful. (<%= ((AuthenticationException) session.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY)).getMessage() %>)</font></p>
					      </div>
					    </c:if>
					    <c:remove scope="session" var="SPRING_SECURITY_LAST_EXCEPTION"/>
	                    <div class="input-group input-sm">
	                        <span class="input-group-addon"><i class="fa fa-user"></i></span><input class="form-control" type="text" id="j_username" name="j_username" placeholder="Username" />
	                    </div>
	                    <div class="input-group">
	                        <span class="input-group-addon"><i class="fa fa-lock"></i></span><input class="form-control" type="password" id="j_password"name='j_password' placeholder="Password" />
	                    </div>
	                    <div class="form-actions clearfix">
	                        <input name="login"  value="Login" type="submit" class="btn btn-block btn-primary btn-default" />
	                    </div>
	                </form>
	            </div>
            </authz:authorize>
        </div>
        
        <script src="/assets/js/jquery.min.js"></script>  
        <script src="/assets/js/jquery-ui.custom.min.js"></script>
        <script src="/assets/js/unicorn.login.js"></script> 
    </body>
</html>


