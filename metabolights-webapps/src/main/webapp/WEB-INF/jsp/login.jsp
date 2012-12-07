<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>


	<h3>
    	<c:if test="${empty source}"><spring:message code="msg.credentials" /></c:if>
        <c:if test="${not empty source}"><spring:message code="msg.submCredentials" /></c:if>
    </h3>
    
    <c:if test="${not empty source}">
    	<p><strong><spring:message code="msg.submHeader"/></strong></p>
    </c:if>

    <form name="loginForm" action="<c:url value='j_spring_security_check'/>" method="post">
		
		<c:if test="${not empty param.login_error}">
            <p class="error">
            <!-- Your login attempt was not successful, try again.<br/>-->
            <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
            </p>
    	</c:if>
    	
        <div class="grid_4 alpha"><spring:message code="label.email" />:</div>
        <div class="grid_20 omega"><input type='text' name='j_username'/></div>
		
		<div class="grid_4 alpha"><spring:message code="label.password" />:</div>
        <div class="grid_20 omega">
           	<input type='password' name='j_password'/>
           	<a href="forgotPassword"><spring:message code="label.oopsForgot" /></a>
        </div>
		
		<div class="grid_20 prefix_4 omega">
			<input name="submit" type="submit" class="submit" value="<spring:message code="label.login"/>">		
			<input name="cancel" type="button" class="submit cancel" value="<spring:message code="label.cancel"/>" onclick="location.href='index'">
        </div>


    </form>

		<div class="grid_24 alpha omega">
			&nbsp;
			<br/><br/>
			<a href="newAccount" class="noLine"><img class="ebiicon user-add"/></a>
			<a href="newAccount"><spring:message code="label.needNewAccount"/></a>
            
        </div>

<script type="text/javascript" language="javascript">
    document.loginForm.j_username.focus();
</script>
