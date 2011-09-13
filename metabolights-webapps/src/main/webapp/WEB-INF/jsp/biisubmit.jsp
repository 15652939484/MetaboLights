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
	          //showButtonPanel: true,
	          buttonText: 'Choose Date',
	          dateFormat: 'dd-M-yy',
	          minDate: '0',
	          maxDate: '+5y',
	      });
	});
	
	function toggleDate() {
        document.forms['uf'].elements['pickdate'].focus();
		return false; 
	}
	
	
</script>		
		
<div class="formbox">
	<form method="post" action="biiuploadExperiment" enctype="multipart/form-data" name="uf" onsubmit="disableSubmission()">
           <table border="0" cellpadding="5px" cellspacing="0px">
	        <tr class="formheader">
	             <th class="tableheader" colspan="2"><spring:message code="msg.upload" /> </th>
	        </tr>
	        <tr>
	            <td colspan='2'>&nbsp;</td>
	        </tr>

	        <tr>
	            <td><spring:message code="label.isatabZipFile" />:</td>
	            <td><input type="file" name="file" /></td>
	        </tr>
	       
	        <tr>
	        	<td colspan='2'> <hr/> </td>
	        </tr>
	        <tr>
	            <td colspan='2'><b><spring:message code="label.experimentMsgPublic" /> </b></td>
	        </tr>
	        <tr>
	        	<td><spring:message code="label.publicDate"/></td>
				<td>
					<input type="image" src="img/ebi-icons/16px/calendar.png" onclick="return toggleDate()" />
					<input type="text" name="pickdate" id="datepicker" readonly="readonly" size="12"/>
				</td> 
	        </tr>
	       	<tr>
	        	<td colspan='2'> <hr/> </td>
	        </tr>
	        
	        <tr>
	            <td height="100px" colspan='2'>
		             <div id="hideableButtons" style="display:none">
                     <input name="submit" type="submit" class="big_submit" value="<spring:message code="label.upload"/>">
		             &nbsp;&nbsp;
		             <a href="index"><spring:message code="label.cancel"/></a>
			         </div>
            	</td>
	        </tr>
	      
	       
	        <tr>
	            <td colspan='2'>
	            <div id="hourglass">
	            <img src="img/wait.gif"/>&nbsp; <b> <spring:message code="msg.pleaseWaitForUpload"/></b>
	            </div>
	            </td>
	        </tr>
	        
	   		<tr>
	   			<td colspan='2'>
			       <br/>
			       <a href="<spring:url value="submitHelp"/>"><spring:message code="menu.submitHelp"/></a>
			       <br>       
		    	</td>  	
	        </tr>
	    </table>
	    
	</form> 
	 <c:if test="${not empty message}">
	    <div class="error">
	       <c:out value="${message}" />
	    </div>
	 </c:if>
</div>

