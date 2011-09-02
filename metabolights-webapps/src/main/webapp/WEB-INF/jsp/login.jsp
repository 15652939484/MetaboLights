<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="formbox">

    <form name="loginForm" action="<c:url value='j_spring_security_check'/>" method="POST">
    <table cellpadding="5px" cellspacing="0px">

        <tr class="formheader">
             <th class="tableheader" colspan="3">
            	<c:if test="${empty source}"><spring:message code="msg.credentials" /></c:if> 
             	<c:if test="${not empty source}"><spring:message code="msg.submCredentials" /></c:if> 
             </th>
        </tr>
        <tr>
            <td colspan='3'><c:if test="${not empty source}"><br/><br/><spring:message code="msg.submHeader"/><br/><br/></c:if>&nbsp;</td>
        </tr>
	
		<tr>
			<td><spring:message code="label.email" />:</td>
			<td colspan='2'><input type='text' name='j_username'/></td>
		</tr>
		<tr>
			<td><spring:message code="label.password" />:</td>
			<td><input type='password' name='j_password'></td>
            <td><a href="forgotPassword"><spring:message code="label.oopsForgot" /></a></td>
		</tr>

		<!-- tr>
			<td></td>
			<td><input type="checkbox"
				name="_spring_security_remember_me">&nbsp;<spring:message code="label.rememberme" />
			</td>
		</tr-->
		<tr>
            <td></td>
			<td colspan='2'><input name="submit" type="submit" class="big_submit" value="<spring:message code="label.login"/>">
            &nbsp;&nbsp;<a href="index"><spring:message code="label.cancel"/></a>
			</td>
		</tr>
        <tr >
            <td valign="top" align="right" style="padding-top:30px">
                 <a href="newAccount" >
                   <!-- img src="img/newUser.png" border="0px"/-->
                   <img src="img/ebi-icons/32px/user-add.png" class="img_alignment_green"/>
                 </a>
            </td>
            <td colspan='2' valign="top" style="padding-top:30px">
                 <a href="newAccount" ><spring:message code="label.needNewAccount" /></a>
            </td>
        </tr>       
	</table>
    </form>

    <c:if test="${not empty param.login_error}">
      <span class="error">
        <br> 
        <!-- Your login attempt was not successful, try again.<br/>--> 
        <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
        <br>
      </span>
    </c:if>
    

</div>


<script type="text/javascript" language="javascript">
   document.loginForm.j_username.focus();
</script>
