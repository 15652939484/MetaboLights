<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--
  ~ EBI MetaboLights - http://www.ebi.ac.uk/metabolights
  ~ Cheminformatics and Metabolism group
  ~
  ~ European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
  ~
  ~ Last modified: 2/10/14 4:54 PM
  ~ Modified by:   conesa
  ~
  ~
  ~ ©, EMBL, European Bioinformatics Institute, 2014.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~    http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
  --%>

<!-- If there isn't any result and it is due to the freetext, filter will not be printed -->
<c:if test="${!((totalHits==0) && filters.isFilterLoadNeeded)}">

	<script>
		function fillAutocomplete(id, availableTags) {
			$( "#"+id ).autocomplete({
				source: availableTags,
//				change: function(event, ui)
//						{
//							console.log("change triggered");
//						},
				select: function(event, ui)
						{
							console.log("select triggered");
//							var checkedItem = $("input[value='" + $( "#"+id ).val()+ "']");
                            var checkedItem = $("input[value='" + ui.item.value + "']");
							checkedItem.prop("checked", true);
							$("#filterForm").submit();
						}
			});
			// Prevent submitting the form when keypress
			$( "#"+id ).bind('keypress', function(e) {
	            if (e.keyCode == 13) {
	            	//$("input[value='" + $( "#"+id ).val()+ "']").prop("checked", true);
	            	$("input[value='" + $( "#"+id ).val()+ "']").click();
	           }
	        });
		};
	</script>

   	<c:if test="${!empty welcomemessage}">
		<h2><spring:message code="menu.myStudiesCap" /></h2>
	</c:if>
    
    <c:if test="${empty welcomemessage}">
        <div class="topSpacerFilter noText"></div>  <!-- Add second top spacer if no heading displayed -->
    </c:if>

	<form name="searchFilter" id="filterForm" action="${action}" method="post" accept-charset="utf-8">


		<h3><spring:message code="label.searchFilter"/></h3>

		<c:forEach var="filterset" items="${filters.fss}">
			<c:if test="${filterset.value.isEnabled}">

                    <c:set var="caption">
                        <c:choose><c:when test="${filterset.key=='organism'}"><spring:message code="label.organism"/></c:when>
                            <c:when test="${filterset.key=='technology'}"><spring:message code="label.technology"/></c:when>
                            <c:when test="${filterset.key=='status'}"><spring:message code="label.status"/></c:when>
                            <c:when test="${filterset.key=='metabolite'}"><spring:message code="label.metabolite"/></c:when>
                            <c:otherwise>${filterset.key}</c:otherwise>
                        </c:choose>
                    </c:set>
					<h4>${caption}</h4>
					<c:if test="${fn:length(filterset.value.filterItems) gt 5}">
						<div class="ui-widget">
							<input
                                    class="inputDiscrete resizable"
                                    id="autocomplete_${filterset.key}"
                                    placeholder= "Find your ${caption}"
                            />
							<script>var availableTags = new Array();</script>
						</div>
					</c:if>
							
					<ul class="filterset"  id="${filterset.key}">
						<c:forEach var="times" begin="0" end="1" step="1">
							<c:set var="checkedItems" value="0"/>							
							<c:forEach var="filter" items="${filterset.value.filterItems}">
								<c:if test='${(filter.value.isChecked and (times == 0)) or (!filter.value.isChecked and (times == 1))}'>									
									<c:if test='${(filter.value.isChecked and (times == 0))}'>
										<c:set var="checkedItems" value="${checkedItems + 1}"/>
									</c:if>
									<input 	type="checkbox"
										 	name="${filter.value.name}" 
										  	value="${filter.value.value}"
										  	<c:if test='${filter.value.isChecked}'>CHECKED</c:if>
                                            onclick="this.form.submit();">
									<c:if test="${filter.value.number<1}"><span class="dimmed">${filter.value.text}</span> </c:if>
									<c:if test="${filter.value.number>0}">${filter.value.text}</c:if>
									<br/>
									<c:if test="${fn:length(filterset.value.filterItems) gt 5}">
										<script>availableTags.push("${filter.value.value}")</script>
									</c:if>
								</c:if>	
							</c:forEach>
							<%--<c:if test='${(times == 0) and (checkedItems gt 0)}'><hr/></c:if>--%>
						</c:forEach>
						<c:if test="${fn:length(filterset.value.filterItems) gt 5}">
							<script>fillAutocomplete('autocomplete_${filterset.key}', availableTags);</script>
						</c:if> 
					</ul>

			</c:if>
		</c:forEach>
		<input type="hidden" name="freeTextQuery" value="<c:out value="${freeTextQuery}"/>"/>
	    <input type="hidden" name="pageNumber" value="1"/>
	</form>
</c:if>
&nbsp;