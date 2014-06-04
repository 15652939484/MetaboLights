<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>

	<%--
  ~ EBI MetaboLights - http://www.ebi.ac.uk/metabolights
  ~ Cheminformatics and Metabolism group
  ~
  ~ Last modified: 6/4/14 12:35 PM
  ~ Modified by:   kenneth
  ~
  ~ Copyright 2014 - European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
  --%>

<form:form name="mySubmForm" action="mysubmissions">
    	<h2><spring:message code="menu.myAccountCaps" /></h2>

        <div class="grid_6 prefix_6">
			<input type="submit" name="submit" class="submit bigfont" value=" <spring:message code="label.viewMySubmissions"/> ">
			<br/>&nbsp;
		</div>

    </form:form>
	<hr/>
    <form:form name="accountForm" action="updateAccount" method="post" commandName="metabolightsUser">
        <form:hidden path="userId" />
        <form:hidden path="userName" />
        <form:hidden path="email"/>
        <form:hidden path="apiToken"/>
        <form:hidden path="role"/>
        <sec:authorize ifNotGranted="ROLE_SUPER_USER" >
        	<form:hidden path="status" />
        </sec:authorize>

        <div class="grid_24">
               <strong><spring:message code="msg.updateAccount" /></strong>
               <br/>&nbsp;
		</div>

		<div class="grid_24">
			<div class="grid_6 alpha">
				<spring:message code="label.email" />:
			</div>
			<div class="grid_18 omega">
				<c:out value="${metabolightsUser.email}" />
			</div>
		</div>

        <jsp:include page="accountFormFields.jsp" />

        <div class="grid_24">
            <div class="grid_6 alpha">
                <spring:message code="label.apiToken" />:
            </div>
            <div class="grid_18 omega">
                <c:out value="${metabolightsUser.apiToken}" />
            </div>
        </div>


		<div class="grid_24">
			<div class="grid_18 prefix_6 alpha omega">
				<input name="submit" type="submit" class="submit" value="<spring:message code="label.update"/>">
				<input name="cancel" type="button" class="submit cancel" value="<spring:message code="label.cancel"/>" onclick="location.href='index'">
	         </div>
		</div>

    	<div class="grid_24 alpha omega">
			<p><strong><spring:message code="msg.starRequired"/></strong></p>
		</div>

    </form:form>

    <script type="text/javascript" language="javascript">
       document.accountForm.firstName.focus();
    </script>

    <c:if test="${not empty message}">
        <span class="error"> <c:out value="${message}" /> </span>
        <br/>
    </c:if>
