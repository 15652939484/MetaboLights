<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<script type="text/javascript" src="javascript/jquery-imtechPager.js"></script>
<script type="text/javascript" src="javascript/jquery-highlight.js"></script>

<script type="text/javascript">
function navigate(_pageNumber) {
    filterForm = document.forms['filterForm'];
    pageNumberField = filterForm.elements["PageNumber"];
    pageNumberField.value=_pageNumber;
    filterForm.submit();
}

</script>

<c:if test="${not empty entries}">
    <div class="grid_24">
        <div class="grid_6 alpha">
            <h6>
                <b><spring:message code="ref.msg.filterResults"></spring:message></b>
            </h6>
        </div>
        <div class="grid_12">
            <div class="grid_24">
                <c:choose>
                    <c:when test="${not empty query}">
                        <h6>
                            <b>${queryResults} <spring:message code="ref.msg.searchResult">${query}</spring:message></b>
                        </h6>
                    </c:when>
                    <c:otherwise>
                        <h6>
                            <b>${queryResults} <spring:message code="ref.msg.emptyBrowse"></spring:message></b>
                        </h6>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <div>
            <c:if test="${! empty query}">
                <aside class="grid_6 omega shortcuts expander" id="search-extras">
                    <div id="ebi_search_results"><h3 class="slideToggle icon icon-functional" data-icon="u"><spring:message code="msg.otherebiresults"/></h3>
                    </div>
                </aside>
            </c:if>
        </div>
        <div class="grid_6 omega">
            <br/>
        </div>
    </div>
    <c:if test="${!empty query}">
        <script src="http://www.ebi.ac.uk/web_guidelines/js/ebi-global-search-run.js"></script>
        <script src="http://www.ebi.ac.uk/web_guidelines/js/ebi-global-search.js"></script>
    </c:if>
    <div class="grid_24">
        <div class="grid_6 alpha">
            <br/>
        </div>
        <div class="grid_16">
            <div class="grid_24 title alpha">
                <div class="grid_4 aplha">
                    <b>Page:&nbsp;${currentPage}</b>
                </div>
                <div class="grid_20 omega">
                        <%-- <b><spring:message code="ref.msg.Navigate"/></b>--%>
                    <span id="pagination" class="right">
                        <c:if test="${currentPage lt 1}">
                            <b><a href="#"><img ALIGN="texttop" src="img/prev.png" border=0 onClick="navigate(${currentPage})"></a></b>
                        </c:if>
                        <c:if test="${currentPage gt 1}">
                            <a href="#"><img ALIGN="texttop" src="img/prev.png" border=0 onClick="navigate(${currentPage-1})"></a>
                        </c:if>
                        <c:if test="${NumOfPages gt 1}">
                            <c:choose>
                                <c:when test="${currentPage eq 1 }">
                                    ${currentPage}
                                </c:when>
                                <c:otherwise>
                                    <a href="#" style="text-decoration: none"> <span style="font-weight: normal" onClick="navigate(1)"><c:out value="1"/></span></a>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${currentPage lt 5}">
                                    <c:if test="${NumOfPages gt 5 }">
                                        <c:forEach  var="h" begin="2" end="5" step="1" varStatus ="status">
                                            <c:choose>
                                                <c:when test="${currentPage eq h}">
                                                    <b>${currentPage}</b>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="#" style="text-decoration: none"> <span style="font-weight: normal" onClick="navigate(${h})"><c:out value="${h}" /></span></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${NumOfPages lt 5 }">
                                        <c:forEach var="i" begin="2" end="${NumOfPages}" step="1" varStatus="status">
                                            <c:choose>
                                                <c:when test="${currentPage eq i }">
                                                    <b>${currentPage}</b>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="#" style="text-decoration: none"> <span style="font-weight: normal" onClick="navigate(${i})"><c:out value="${i}" /></span></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${NumOfPages eq 5 }">
                                        <c:forEach  var="j" begin="2" end="4" step="1" varStatus ="status">
                                            <c:choose>
                                                <c:when test="${currentPage eq j}">
                                                    <b>${currentPage}</b>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="#" style="text-decoration: none"> <span style="font-weight: normal" onClick="navigate(${j})"><c:out value="${j}" /></span></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    ....<a href="#" style="text-decoration: none"> <span style="font-weight: normal" onClick="navigate(${currentPage-1})"><c:out value="${currentPage-1}" /></span></a>
                                    <c:if test="${currentPage ne NumOfPages }">
                                        <b>${currentPage}</b>
                                        <c:if test="${currentPage ne NumOfPages-1 }">
                                            <a href="#" style="text-decoration: none"> <span style="font-weight: normal" onClick="navigate(${currentPage+1})"><c:out value="${currentPage+1}" /></span></a>
                                        </c:if>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${currentPage eq NumOfPages}">
                                    <b>${currentPage}</b>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${currentPage ne NumOfPages-1 }">
                                        <c:if test="${currentPage ne NumOfPages-2 }">
                                            <c:if test="${NumOfPages gt 6}">
                                                ....
                                            </c:if>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${NumOfPages eq 5 }">
                                        <a href="#" style="text-decoration: none"> <span style="font-weight: normal" onClick="navigate(${NumOfPages})"><c:out value="${NumOfPages}" /></span></a>
                                    </c:if>
                                    <c:if test="${NumOfPages gt 5 }">
                                        <a href="#" style="text-decoration: none"> <span style="font-weight: normal" onClick="navigate(${NumOfPages})"><c:out value="${NumOfPages}" /></span></a>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                        <c:if test="${(((currentPage-1)*pageSize)+pageSize) lt queryResults}">
                            <a href="#"><img ALIGN="texttop" src="img/next.png" border=0 onClick="navigate(${currentPage+1})"></a>
                        </c:if>
                    </span>
                </div>
            </div>
        </div>
        <div class="grid_2">
            <br />
        </div>
    </div>

    <div class="grid_24 clearfix" />
    <div class="grid_24">
        <br />
    </div>

    <div class="grid_6 alpha">
        <form name="Filters" id="filterForm" action="#" method="post">
            <!--Technology filter-->
            <div class="grid_24 refLayerBox">
                <b><spring:message code="ref.msg.technology"></spring:message></b>
                <c:if test="${techClear ne true}">
                    <c:forEach var="technology" items="${technologyList}">
                        <c:if test="${technology.value eq 'true'}">
                            <ul style="max-height: 400px; overflow: auto" id="technology">
                                <input type="checkbox" name="technology" value="${technology.key}" CHECKED onclick="this.form.submit();">
                                    ${technology.key}
                            </ul>
                        </c:if>
                    </c:forEach>
                    <c:forEach var="technology" items="${technologyList}" varStatus="loopStatus">
                        <c:if test="${technology.value eq 'true'}">
                            <c:if test="${loopStatus.index eq (techLen-1)}">
                                <hr>
                            </c:if>
                        </c:if>
                    </c:forEach>
                    <c:forEach var="technology" items="${technologyList}">
                        <c:if test="${technology.value eq 'highlight'}">
                            <ul style="max-height: 400px; overflow: auto" id="technology">
                                <input type="checkbox" name="technology" value="${technology.key}" onclick="this.form.submit();">
                                ${technology.key}
                            </ul>
                        </c:if>
                    </c:forEach>
                    <c:forEach var="technology" items="${technologyList}">
                        <c:if test="${technology.value eq 'false'}">
                            <ul style="max-height: 400px; overflow: auto" id="technology">
                                <input type="checkbox" name="technology" value="${technology.key}" onclick="this.form.submit();">
                                    <span class="dimmed">${technology.key}</span>
                            </ul>
                        </c:if>
                    </c:forEach>
                </c:if>
            </div>
            <br />

            <!--organism filter-->
            <div class="grid_24 refLayerBox" id="orgFilter">
                <b><spring:message code="ref.msg.organism"></spring:message></b>
                <c:forEach var="RefLayerOrg" items="${RefLayer}">
                    <c:if test="${orgClear ne true}">
                        <c:forEach var="orghash" items="${RefLayerOrg.orgHash}">
                            <c:if test="${orghash.value eq 'true'}">
                                <ul style="max-height: 400px; overflow: auto" id="organisms">
                                    <input type="checkbox" name="organisms" value="${orghash.key}" CHECKED onclick="this.form.submit();">
                                    ${orghash.key}
                                </ul>
                            </c:if>
                        </c:forEach>
                        <c:forEach var="orghash" items="${RefLayerOrg.orgHash}" varStatus="loopStatus">
                            <c:if test="${orghash.value eq 'true'}">
                                <c:if test="${loopStatus.index eq (orgLen-1)}">
                                    <hr>
                                </c:if>
                            </c:if>
                        </c:forEach>
                        <c:forEach var="orghash" items="${RefLayerOrg.orgHash}">
                            <c:if test="${orghash.value eq 'highlight'}">
                                <ul style="max-height: 400px; overflow: auto" id="organisms">
                                    <input type="checkbox" name="organisms" value="${orghash.key}" onclick="this.form.submit();">
                                    ${orghash.key}
                                </ul>
                            </c:if>
                        </c:forEach>
                        <c:forEach var="orghash" items="${RefLayerOrg.orgHash}">
                            <c:if test="${orghash.value eq 'false'}">
                                <ul style="max-height: 400px; overflow: auto" id="organisms">
                                    <input type="checkbox" name="organisms" value="${orghash.key}" onclick="this.form.submit();">
                                    <span class="dimmed">${orghash.key}</span>
                            </ul>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </c:forEach>
            </div>

            <input type="hidden" name="query" value="<c:out value="${query}"/>" />
            <input type="hidden" name="PageNumber" value="1" />
        </form>
    </div>

    <div class="grid_16">
        <c:forEach var="entry" items="${entries}">
            <div style='clear: both;'></div>
            <div class="grid_24 refLayerBox">
                <div class="grid_8 alpha">
                    <a href="${entry.accession}"><img
                            src="http://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&imageIndex=0&chebiId=${entry.chebiURL}"
                            onerror="this.src='img/large_noImage.gif';" width="100px"
                            height="100px" /></a>
                </div>
                <div class="grid_16 omega">
                    <div class="grid_24">
                        <b><spring:message code="ref.compound.name"/></b> ${entry.name}
                        (<a href="${entry.accession}">${entry.accession}</a>)
                        <a href='<spring:message code="ref.msg.chebi.url"></spring:message>${entry.chebiURL}'>${entry.chebiId}</a>
                    </div>
                    <br />
                    <br />
                    <div class="grid_24">
                    <c:if test="${not empty entry.description}">
                        <b><spring:message code="ref.compound.description"/></b>${entry.description}
                    </c:if>
                    </div>
                    <br /> <br /> <br />
                    <div class="grid_24">
                        <c:forEach var="MTBLStudiesList" items="${entry.MTBLStudies}"
                                   varStatus="loopStatus">
                            <c:choose>
                                <c:when test="${loopStatus.index eq 0}">
                                    <b><spring:message code="ref.msg.mtbl.studies" /></b>
                                </c:when>
                                <c:otherwise>
                                    ,
                                </c:otherwise>
                            </c:choose>
                            <a href="${MTBLStudiesList}">${MTBLStudiesList}</a>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <div class="grid_24">
        <br />
    </div>
    <div class="grid_24">
        <div class="grid_6 alpha">
            <br />
        </div>
        <div class="grid_16 title">
            <div class="grid_4">
                <b>Page:&nbsp;${currentPage}</b>
            </div>
            <div id="paginationBottom" class="grid_20"></div>
            <script>$('#pagination').clone().appendTo('#paginationBottom');</script>
        </div>
        <div class="grid_2 omega">
            <br />
        </div>
    </div>
</c:if>
<c:if test="${empty entries }">
<div class="grid_12">
    <b><spring:message code="ref.msg.noResult" /><a href="MTBLC1358">Acetic acid</a>, <a href="MTBLC1402">Alanine</a>, <a href="MTBLC1547">Benzene</a> and so on...</b>
</div>
<div class="grid_6 alpha">
    <br />
</div>
</c:if>
<div class="grid_24">
<br /> <br />
</div>

