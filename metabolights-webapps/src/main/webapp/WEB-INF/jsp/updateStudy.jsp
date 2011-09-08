<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="javascript/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="javascript/jquery-ui-1.8.15.custom.min.js"></script>

<script type="text/javascript">
function onloadAction() {
	//standard call from layout.jsp
	enableSubmission();		
}


function disableSubmission() {
    document.body.style.cursor = "wait";
	var hglass = document.getElementById("hourglass");
	hglass.style.display = "block";		
	var buttons = document.getElementById("hideableButtons");
	buttons.style.display = "none";
}

function enableSubmission() {
    document.body.style.cursor = "default";
	var hglass = document.getElementById("hourglass");
	hglass.style.display = "none";
	var buttons = document.getElementById("hideableButtons");
	buttons.style.display = "block";
}

$(function() {
	$( "#datepicker" ).datepicker( {
          changeMonth: true,
          changeYear: true,
          showOtherMonths: true,
          showButtonPanel: true,
          //showAnim: 'fold',
          //showOn: 'both',
          buttonText: 'Choose Date',
          dateFormat: 'dd-M-yy',
          minDate: '+1d',
          maxDate: '+5y',
          hideIfNoPrevNext: true
      });
});

function togglePublic() {
	document.forms['uf'].elements['public'].checked=false;
	document.forms['uf'].elements['pickdate'].disabled=false;
	document.forms['uf'].elements['pickdate'].focus();
	return false; 
}	

function syncDateBox() {
	
	document.forms['uf'].elements['pickdate'].disabled=document.forms['uf'].elements['public'].checked;
	
	if (document.forms['uf'].elements['public'].checked){
		//$("#datepicker").datepicker("setDate", new Date());
		document.forms['uf'].elements['pickdate'].value = "";
	}
}
</script>


<div class="formbox">
	<table border="0px" "cellpadding="15px" cellspacing="0px" width="90%">
	    <tr class="formheader">
	        <th colspan=2 class="tableheader">
	        	<c:if test="${not empty title}">
					<c:out value="${title}"/>
				</c:if>
	        </th>
	    </tr>
		<c:if test="${not empty message}">
			<tr>
				<td colspan=2><br/>${message}<br/><br/><br/></td>
			</tr>
		</c:if>
		
		<c:if test="${not empty ftpLocation}">
            <tr><td class="big_submit" width="80px">
            
            	<IMG src="img/ebi-icons/16px/download.png" class="img_alignment_dark"> <a href="${ftpLocation}" style="color:white; font-size:14px"> <spring:message code="label.ftpDownload"/></a>
            <br>
			</td></tr>
		</c:if>
		<c:if test="${not empty searchResult}">
			<tr>
				<td colspan=2>
					<br/>
					<c:set var="nopublish" value="true"/>
					<%@include file="entrySummary.jsp" %>
				</td>
			</tr>
		</c:if>
	</table>
	
	<c:if test="${empty updated}">
		<br/><br/>
		<form method="post" action="${action}" enctype="multipart/form-data" name="uf" onsubmit="disableSubmission()">
	    	<input type="hidden" value="${study}" name="study"/>
		    <table cellpadding="0px" cellspacing="15px" width="90%">
				<c:if test="${isUpdateMode}">
					<tr>
					    <td colspan='2'>&nbsp;</td>
					</tr>
					<tr>
					    <td><spring:message code="label.isatabZipFile" />:</td>
					    <td><input type="file" name="file" /></td>
					</tr>
				</c:if>
			    <tr>
		            <td width="33%"><spring:message code="label.experimentstatuspublic" />:</td>
		            <td><input type="checkbox" name="public" onClick='syncDateBox()'/></td>
		        </tr>
		        <tr>
		        	<td><spring:message code="label.publicDate"/></td>
					<td>
						<input type="image" src="img/ebi-icons/16px/calendar.png" onclick="return togglePublic()" style="vertical-align: middle" size=10/>
						<input type="text" name="pickdate" id="datepicker" readonly="readonly" size="12"/>
					</td> 
		        </tr>
		        <tr>
		        	<td>
			            <div id="hideableButtons" style="display:none">
						<input type="submit" name="submit" class="big_submit" value="${submitText}">
						&nbsp;&nbsp;<a href="index" name="cancel"><spring:message code="label.cancel"/></a>
						</div>
		        	</td>
		        	<td>
		        		<c:if test="${not empty validationmsg}">
		        			<span class="error">${validationmsg}</span>
		        		</c:if>
		        	</td>
		        </tr>
		        <tr>
		            <td colspan='2'>
		            <div id="hourglass">
		            <img src="img/wait.gif"/>&nbsp; <b> <spring:message code="msg.pleaseWaitForUpload"/></b>
		            </div>
		            </td>
		        </tr>
			</table>
		</form>   
	</c:if>
</div>
<br/><br/>
