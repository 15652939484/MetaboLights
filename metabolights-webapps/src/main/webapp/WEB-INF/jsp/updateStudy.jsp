<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<script>
	
	function showWait(){

	}

</script>

<script type="text/javascript">

	$(document).ready(function() {
		
		$("#hourglass").dialog({
		    create: function(){
		    	$('.ui-dialog-titlebar-close').removeClass('ui-dialog-titlebar-close');
		    },
		    width: 400,
		    height: 150,
		    modal: true,
		    autoOpen: false
		});

		
	});
	
	function disableSubmission() {
	    document.body.style.cursor = "wait";
	    $('.ui-dialog-titlebar').hide();
		$( "#hourglass" ).dialog( "open" );
	}

	$(function() {
		$( "#datepicker" ).datepicker( {
	          changeMonth: true,
	          changeYear: true,
	          showOtherMonths: true,
	          buttonText: 'Choose Date',
	          dateFormat: 'dd-M-yy',
	          minDate: '0',
	          maxDate: '+1y'
	      });
	});
	
	function toggleDate() {
        document.forms['uf'].elements['pickdate'].focus();
		return false; 
	}
	
</script>


<c:if test="${not empty title}">
    <h3><c:out value="${title}"/></h3>
</c:if>
<c:if test="${not empty message}">
	<p>${message}</p><br/>
</c:if>

<c:if test="${not empty ftpLocation}">
	<div class="grid_8 prefix_1 alpha omega">
		<div class="bigbutton seccolorI">
			<a href="${ftpLocation}">
				<div class="ebiicon download seccolorI"></div>&nbsp;
				<span class="bigfont"> <spring:message
						code="label.ftpDownload" />
				</span>
			</a>
		</div>
	</div>
	<p/>
</c:if>

<c:if test="${not empty searchResult}">
    <br/>
	<c:set var="nopublish" value="true"/>
	<div class="grid_23 prefix_1 alpha omega">
		<%@include file="entrySummary.jsp" %>
	</div>
	</p>
</c:if>

<c:if test="${empty updated}">
	<form method="post" action="${action}" enctype="multipart/form-data" name="uf" onsubmit="disableSubmission()">
		&nbsp;<br/>	
	    <input type="hidden" value="${study}" name="study"/>

		<c:if test="${isUpdateMode}">
			<div class="grid_6 alpha prefix_1"><spring:message code="label.isatabZipFile" />:</div>
			<div class="grid_17 omega">
				<input type="file" name="file" />
		    </div>
		</c:if>
		<br/>
		<div class="grid_6 alpha prefix_1"><spring:message code="label.publicDate"/>:</div>
		<div class="grid_17 omega">
			<input type="image" src="img/ebi-icons/16px/calendar.png" onclick="return toggleDate()" />
			<input type="text" name="pickdate" id="datepicker" readonly="readonly" size="12" value="<fmt:formatDate pattern="dd-MMM-yyyy" value="${defaultDate}"/>"/>
	    </div>
		<div id="hideableButtons" class="grid_17 prefix_7 alpha omega">
			&nbsp;<br/>
			<input name="submit" type="submit" class="submit" value="${submitText}">		
			<input name="cancel" type="button" class="submit cancel" value="<spring:message code="label.cancel"/>" onclick="location.href='index'">
	    </div>
	
	   	<div id="hourglass">
	   		<img src="img/wait.gif" alt="Please wait"/>&nbsp;<b><spring:message code="msg.pleaseWaitForUpload"/></b>
	   	</div>
	</form> 
	<c:if test="${not empty validationmsg}">
		<div class="grid_24">
			<span class="error">${validationmsg}</span>
		</div>
	</c:if>

</c:if>
