<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%--
  ~ EBI MetaboLights - http://www.ebi.ac.uk/metabolights
  ~ Cheminformatics and Metabolism group
  ~
  ~ Last modified: 27/09/13 14:50
  ~ Modified by:   kenneth
  ~
  ~ Copyright 2013 - European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
  --%>

<%--<script type="text/javascript" src="javascript/protovis-r3.2.js" charset="utf-8"></script>--%>
<%--<script type="text/javascript" src="javascript/Biojs.js" charset="utf-8"></script>--%>
<script type="text/javascript" src="http://www.ebi.ac.uk/Tools/biojs/registry/src/Biojs.js" charset="utf-8"></script>
<script type="text/javascript" src="../javascript/Biojs.ChEBICompound.js" charset="utf-8"></script>

<%--<script type="text/javascript" src="http://www.ebi.ac.uk/Tools/biojs/registry/src/Biojs.ChEBICompound.js" charset="utf-8"></script>--%>
<script type="text/javascript" src="../javascript/jquery.linkify-1.0-min.js" charset="utf-8"></script>
<%--<script type="text/javascript" src="http://www.ebi.ac.uk/Tools/biojs/registry/src/Biojs.js" charset="utf-8"></script>--%>
<%--<script type="text/javascript" src="http://www.ebi.ac.uk/Tools/biojs/registry/src/Biojs.ChEBICompound.js" charset="utf-8"></script>--%>


<link rel="stylesheet"  href="../css/ChEBICompound.css" type="text/css" />

<script type="text/javascript">

    $(document).ajaxStart(function(){showWait();}).ajaxStop(function(){
        hideWait();
    });

    $(document).ready(function() {
        $("#hourglass").dialog({
            create: function(){
                $('.ui-dialog-titlebar-close').removeClass('ui-dialog-titlebar-close');
            },
            width: 200,
            height: 60,
            modal: true,
            autoOpen: false
        });
    });

    function showWait() {
        document.body.style.cursor = "wait";
        $('.ui-dialog-titlebar').hide();
        $( "#hourglass" ).dialog( "open" );
    }

    function hideWait(){
        document.body.style.cursor = "default";
        $( "#hourglass" ).dialog("close");
    }

</script>

<script>
    $(function() {
        $( "#tabs" ).tabs({
            cache:true
        });
    });
</script>

<script language="javascript" type="text/javascript">

    $(document).ready(function() {

        $("[id='protocoldesc']").linkify();

        $("body").append('<div id="chebiInfo"></div>');
//	var chebiInfoDiv = new Biojs.ChEBICompound({target: 'chebiInfo',width:'400px', height:'300px',proxyUrl:'./proxy'});
        var chebiInfoDiv = new Biojs.ChEBICompound({target: 'chebiInfo',width:'400px', height:'300px',proxyUrl:undefined, chebiDetailsUrl: './ebi/webservices/chebi/2.0/test/getCompleteEntity?chebiId='});
        $('#chebiInfo').hide();

        $("a.showLink").click(function(event) {
            var clickedId = event.target.id;
            var idClickedSplit = clickedId.split("_");
            /*id of the link is made up by 3 parts:
             part 1: name of the div (eg.: syn) this is used to distinguish the show more
             link of synonyms from the show more link in other divs
             part 2: "link" to distinguish the link for show more link from other
             ordinary links
             part 3: the order of the result item to distinguish the show more button
             in the result list is click. In case of filters of species or compounds
             the order is always 0
             */
            var idPrefixClicked = idClickedSplit[0];
            /*var itemClicked = idClickedSplit[1];*/
            var orderOfItemClicked = idClickedSplit[2];
            var idOfHiddenText = "#"+idPrefixClicked+"_"+orderOfItemClicked;
            var jqClickedId= "#"+clickedId;
            if ($(idOfHiddenText).is(":hidden")){
                $(jqClickedId).text("Show less");
            }else{
                $(jqClickedId).text("Show more");
            }
            $(idOfHiddenText).slideToggle();

        });

        var metLinkTimer = 0; // 0 is a safe "no timer" value

        $('.metLink').live('mouseenter', function(e) {
            // I'm assuming you don't want to stomp on an existing timer
            if (!metLinkTimer) {
                metLinkTimer = setTimeout(function(){loadMetabolite(e);}, 500); // Or whatever value you want
            }
        }).live('mouseleave', function() {
                    // Cancel the timer if it hasn't already fired
                    if (metLinkTimer) {
                        clearTimeout(metLinkTimer);
                        metLinkTimer = 0;

                    }
                    $('#chebiInfo').fadeOut('slow');
                });

        function loadMetabolite(e) {
            // Clear this as flag there's no timer outstanding
            metLinkTimer = 0;

            var metlink;
            metlink = $(e.target);
            var metaboliteId = metlink.attr('identifier');



            // If its a chebi id
            if (metaboliteId.indexOf("CHEBI:")==0){

                //var mouseX = metlink.left + metlink.offsetParent.offsetLeft + metlink.offsetWidth + 80;
                //var mouseY = metlink.top + metlink.offsetParent.offsetTop + metlink.offsetParent.offsetParent.offsetTop;
                var offset = metlink.offset();
                var mouseX = offset.left + metlink.outerWidth() + 20;
                var mouseY = offset.top;

                chebiId = metaboliteId;

                $('#chebiInfo img:last-child').remove;

                $('#chebiInfo').css({'top':mouseY,'left':mouseX,'float':'left','position':'absolute','z-index':10});
                $('#chebiInfo').fadeIn('slow');

                chebiInfoDiv.setId(chebiId);
            }
        }
    });
</script>

<script>
    $(function() {
        $( "#tabs" ).tabs();
    });

    function toggleColumn(tableId, anchor, duration ) {

        dataIcon = $(anchor).attr('data-icon');

        // if collapsed
        if (dataIcon == 'u'){
            dataIcon = 'w';
            $('#' + tableId + ' tr *:nth-child(1n+5)').show();


            // else expanded
        }else{
            dataIcon = 'u';
            $('#' + tableId + ' tr *:nth-child(1n+5)').hide();
        }

        $(anchor).attr('data-icon', dataIcon);
    }

</script>

<%--<div id="hourglass">--%>
    <%--<img src="img/wait.gif" alt="Please wait"/>&nbsp;<b><spring:message code="msg.fetchingData"/></b>--%>
<%--</div>--%>

<div class="push_1 grid_22 title alpha omega">
    <strong>${study.studyIdentifier}: ${study.title}</strong>
    <c:if test="${study.public}">
        <a class="right noLine" href="ftp://ftp.ebi.ac.uk/pub/databases/metabolights/studies/public/${study.studyIdentifier}" title="View all files">
            <span class="icon icon-functional" data-icon="b"/>
        </a>
    </c:if>
    <span class="right">
        &nbsp;
    </span>
    <a class="right noLine" href="${study.studyIdentifier}/files/${study.studyIdentifier}" title="Download whole study">
        <span class="icon icon-functional" data-icon="="/>
    </a>
    <span class="right">
        &nbsp;
    </span>
    <c:if test="${study.public}">
        <jsp:useBean id="datenow" class="java.util.Date" scope="page" />
        <a class="right noLine" href="updatepublicreleasedateform?study=${study.studyIdentifier}&date=<fmt:formatDate pattern="dd-MMM-yyyy" value="${datenow}" />" title="Make it public">
            <span class="icon icon-generic" data-icon="}" id="ebiicon" />
        </a>
    </c:if>
</div>

<c:set var="stringToFind" value="${study.studyIdentifier}:assay:" />


<div class="push_1 grid_22 box alpha omega">

<%--<div>--%>
<%--<c:if test="${study.status ne 'PUBLIC'}">--%>
<%--<jsp:useBean id="now" class="java.util.Date" scope="page" />--%>
<%--<a href="updatepublicreleasedateform?study=${study.acc}&date=<fmt:formatDate pattern="dd-MMM-yyyy" value="${now}" />">Make it public</a>--%>
<%--</c:if>--%>
<%--</div>--%>

<c:if test="${not empty study.contacts}">
    <br/>
    <c:forEach var="contact" items="${study.contacts}" varStatus="loopStatus">
        <c:if test="${loopStatus.index ne 0}">,</c:if>
	            <span id="aff"
                      <c:if test="${not empty contact.affiliation}">title="${contact.affiliation}"</c:if>
                        >${contact.firstName} ${contact.lastName}</span>
    </c:forEach>
    <br/>
</c:if>

<c:if test="${not empty study.studySubmissionDate || not empty study.studyPublicReleaseDate}">
    <br/>
    <c:if test="${not empty study.studySubmissionDate}"><spring:message code="label.subDate"/>: <fmt:formatDate pattern="dd-MMM-yyyy" value="${study.studySubmissionDate}"/>&nbsp;&nbsp;</c:if>
    <c:if test="${not empty study.studyPublicReleaseDate}"><spring:message code="label.releaseDate"/>: <fmt:formatDate pattern="dd-MMM-yyyy" value="${study.studyPublicReleaseDate}"/></c:if>
</c:if>

<c:if test="${not empty submittedID.submittedId}">
    &nbsp;&nbsp;<spring:message code="label.submittedId"/>: ${submittedID.submittedId}
</c:if>

<c:if test="${not empty study.description}">
    <br/><br/><span style="text-align:justify"><div id="description">${study.description}</div></span>
</c:if>
<br/>

<div id="tabs">
<ul>
    <li><a href="#tabs-1" class="noLine"><spring:message code="label.studyDesign"/></a></li>
    <li>
        <a href="#tabs-2" class="noLine"><spring:message code="label.protocols"/></a>
    </li>
    <li>
        <a href="#tabs-3" class="noLine"><spring:message code="label.sample"/></a>
    </li>
    <li>
        <a href="#tabs-4" class="noLine"><spring:message code="label.assays"/></a>
    </li>
    <li>
        <%--<a href="#tabs-5" class="noLine"><spring:message code="label.metabolites"/>--%>
            <c:if test="${not empty study.assays}">
                <c:forEach var="assay" items="${study.assays}">
                    <a class="noLine" href="metabolitesIdentified?maf=${assay.metaboliteAssignment.metaboliteAssignmentFileNameUriSafe}"><spring:message code="label.metabolites"/></a>
                </c:forEach>
            </c:if>
            <%--<c:if test="${not empty metabolites}">--%>
                <%--(${fn:length(metabolites)})--%>>
            <%--</c:if>--%>
        </a>
    </li>
</ul>
<div id="tabs-1">
    <c:if test="${not empty organismNames}">
        <br/>
        <fieldset class="box">
            <legend><spring:message code="label.organisms"/>:</legend>
            <br/>
            <c:forEach var="organismName" items="${organismNames}" >
                ${organismName}<br/>
            </c:forEach>
        </fieldset>
    </c:if>
    <c:if test="${not empty study.descriptors}">
        <br/>
        <fieldset class="box">
            <legend><spring:message code="label.studyDesign"/>:</legend>
            <br/>
            <ul id="resultList">
                <c:forEach var="design" items="${study.descriptors}" >
                <li>${design.description}
                </c:forEach>
            </ul>
        </fieldset>
    </c:if>
    <%--<c:if test="${not empty study.objective}">--%>
        <%--<br/>--%>
        <%--<fieldset class="box">--%>
            <%--<legend><spring:message code="label.studyDesign"/></legend>--%>
            <%--<br/><br/>${study.objective}--%>
        <%--</fieldset>--%>
    <%--</c:if>--%>

    <c:if test="${not empty study.publications}">
        <br/>
        <fieldset class="box">
            <legend><spring:message code="label.publications"/></legend>
            <c:forEach var="pub" items="${study.publications}">
                <br/>
                <div class="ebiicon book"></div>
                <c:set var="DOIValue" value="${pub.doi}"/>
                <c:choose>
                    <c:when test="${not empty pub.pubmedId}">
                        <a href="http://europepmc.org/abstract/MED/${pub.pubmedId}">${pub.title}</a>
                    </c:when>
                    <c:when test="${not empty pub.doi}">
                        <c:if test="${fn:contains(DOIValue, 'DOI')}">
                            <c:set var="DOIValue" value="${fn:replace(DOIValue, 'DOI:','')}"/>
                        </c:if>
                        <c:if test="${fn:contains(DOIValue, 'dx.doi')}">
                            <a href="http://${DOIValue}">${pub.title}</a>
                        </c:if>
                        <c:if test="${not fn:contains(DOIValue, 'dx.doi')}">
                            <a href="http://dx.doi.org/${DOIValue}">${pub.title}</a>
                        </c:if>
                    </c:when>
                    <c:otherwise>${pub.title}</c:otherwise>
                </c:choose>
            </c:forEach>
            <br/>
        </fieldset>
    </c:if>
    <c:if test="${not empty study.factors}">
        <br/>
        <fieldset class="box">
            <legend><spring:message code="label.experimentalFactors"/></legend>
            <ul>
                <c:forEach var="fv" items="${study.factors}">
                    <li>
                        ${fv.name}
                    </li>
                    <%--DIFFERENT!!!${fv.key}: --%>
                        <%--<c:forEach var="value" items="${fv.value}" varStatus="loopStatus">--%>
                            <%--<c:if test="${loopStatus.index ne 0}">--%>
                                <%--,--%>
                            <%--</c:if>--%>
                            <%--${value}--%>
                        <%--</c:forEach>--%>

                </c:forEach>
            </ul>
        </fieldset>
    </c:if>
</div>

<div id="tabs-2">
    <c:if test="${not empty study.protocols}">
        <table width="100%">
            <thead class='text_header'>
            <tr>
                <th>Protocol</th>
                <th>Description</th>
            </tr>
            </thead>

            <tbody>
            <c:set var="blanks" value="0"/>
            <c:forEach var="protocol" items="${study.protocols}" varStatus="loopStatus">
                <c:choose>
                    <c:when test="${not empty protocol.description}">
                        <tr class="${(loopStatus.index+blanks) % 2 == 0 ? '' : 'coloured'}">
                            <td class="tableitem">${protocol.name}</td>
                            <td id="protocoldesc" class="tableitem">${protocol.description}</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:set var="blanks" value="${blanks+1}"/>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>
<!-- ends tabs-2 -->

<div id="tabs-3">
    <c:if test="${not empty study.samples}">
        <table width="100%">
            <thead class='text_header'>
            <tr>
                <th><spring:message code="label.sampleName"/></th>
                <th><spring:message code="label.organism"/></th>
                <th><spring:message code="label.organismPart"/></th>
                <c:forEach var="factor" items="${factors}">
                    <th>${factor.factorKey}</th>
                </c:forEach>
            </tr>
            </thead>

            <tbody>
                <c:set var="blanks" value="0"/>
                <c:forEach var="sample" items="${study.samples}" varStatus="loopStatus">
                    <c:choose>
                        <c:when test="${not empty sample}">
                            <tr class="${(loopStatus.index+blanks) % 2 == 0 ? '' : 'coloured'}">
                                <td>${sample.sampleName}</td>
                                <td>${sample.charactersticsOrg}</td>
                                <td>${sample.charactersticsOrgPart}</td>
                                <c:forEach var="factor" items="${sample.factors}">
                                    <td>${factor.factorValue}</td>
                                </c:forEach>
                                <%--<td>${sample.protocolRef}</td>--%>
                                <%--<td class="tableitem">${sample.sourceName}</td>--%>
                            </tr>
                        </c:when>
                    </c:choose>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>


<div id="tabs-4">
    <c:if test="${not empty study.assays}">
        <table width="100%">
            <thead class='text_header'>
                <tr>
                    <th>Source</th>
                </tr>
            </thead>
            <tbody>
                <c:set var="blanks" value="0"/>
                <c:forEach var="assay" items="${study.assays}">
                    <c:forEach var="assayLine" items="${assay.assayLines}" varStatus="loopStatus">
                        <c:choose>
                            <c:when test="${not empty assayLine.sampleName}">
                                <tr class="${(loopStatus.index+blanks) % 2 == 0 ? '' : 'coloured'}">
                                    <td class="tableitem">${assayLine.sampleName}</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:set var="blanks" value="${blanks+1}"/>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>

<%--<div id="tabs-3">--%>
    <%--<c:if test="${not empty assays}">--%>
        <%--<c:forEach var="assay" items="${assays}" varStatus="loopStatusAssay">--%>
            <%--<br/>--%>
            <%--<strong><spring:message code="label.data.table.name"/>: </strong>${assay.technology} - ${assay.measurement} -  ${assay.platform}--%>
            <%--<br/>--%>
            <%--&lt;%&ndash;<a href="${study.acc}/files/${assay.fileName}" class="icon icon-functional" data-icon="="><spring:message code="submittedFile"/></a><br/>&ndash;%&gt;--%>
            <%--<table width="100%">--%>
                <%--<thead class='text_header'>--%>
                <%--<tr>--%>
                    <%--<th><spring:message code="label.data.table.groupName"/></th>--%>
                    <%--<!-- Add one column per factor -->--%>
                    <%--<c:forEach var="factor" items="${assay.factors}">--%>
                        <%--<th>${factor.value}</th>--%>
                    <%--</c:forEach>--%>
                <%--</tr>--%>
                <%--</thead>--%>
                <%--<tbody>--%>
                <%--<c:forEach var="MLAssayResult" items="${assay.MLAssayResult}" varStatus="loopStatus">--%>
                <%--<tr class="${loopStatus.index % 2 == 0 ? '' : 'coloured'}">--%>
                    <%--<c:if test="${loopStatus.index == 10}">--%>
                        <%--&lt;%&ndash; <tr><td colspan=2><a href="#" class="showLink" id="data_link_${loopStatusAssay.index}">Show more</a></td></tr> &ndash;%&gt;--%>
                <%--</tbody><tbody id="data_${loopStatusAssay.index}" style='display:none'>--%>
            <%--</c:if>--%>
                <%--&lt;%&ndash; <td class="tableitem">${assay.fileName} ${fn:length(assayResult.assays)}</td> &ndash;%&gt;--%>
            <%--<td class="tableitem">--%>
                    <%--&lt;%&ndash; we expect only one assay per assayResult, so we can loop the assay collection and get the first &ndash;%&gt;--%>
                <%--<c:forEach var="assayline" items="${MLAssayResult.assayResult.assays}" varStatus="loopStatus">--%>
                    <%--&lt;%&ndash; Replace the string <ACCESION>:assay: before displaying. NB!! only display up to first "." &ndash;%&gt;--%>
                    <%--<c:set var="stringFormated" value="${fn:substringBefore(fn:replace(assayline.acc,stringToFind,''),'.')}" />--%>
                    <%--<c:set var="materialName" value="${assayline.material.name}" />--%>
                    <%--<c:choose>--%>
                        <%--<c:when test="${not empty stringFormated}">${stringFormated}</c:when>--%>
                        <%--<c:when test="${not empty materialName}">${materialName}</c:when>--%>
                        <%--<c:otherwise> </c:otherwise>--%>
                    <%--</c:choose>--%>

                <%--</c:forEach>--%>
            <%--</td>--%>
            <%--<c:forEach var="fv" items="${MLAssayResult.assayResult.data.factorValues}">--%>
                <%--<td class="tableitem">${fv.value} ${fv.unit.value}</td>--%>
            <%--</c:forEach>--%>
            <%--</tr>--%>
            <%--</c:forEach>--%>
            <%--</tbody>--%>
            <%--</table>--%>
            <%--<c:if test="${fn:length(assay.MLAssayResult) > 10}"><a href="#" class="showLink" id="data_link_${loopStatusAssay.index}">Show more</a></c:if>--%>
            <%--<br/>--%>
        <%--</c:forEach>--%>
    <%--</c:if>--%>
<%--</div> <!--  ends tabs-3 -->--%>
<%--<div id="tabs-4"> <!-- Metabolites Identified -->--%>
    <%--<c:if test="${not empty assays}">--%>

        <%--<c:forEach var="mlAssay" items="${assays}" varStatus="loopStatusAssay">--%>

            <%--<c:if test="${fn:length(mlAssay.metabolitesGUI) gt 0}">--%>
                <%--<br/>--%>
                <%--<a href="${study.acc}/files/${mlAssay.fileName}/maf" class="icon icon-functional" data-icon="="><spring:message code="submittedFile"/></a><br/>--%>
                <%--<div style="overflow: auto">--%>

                    <%--<table id="metabolites${loopStatusAssay.index}">--%>

                        <%--<c:forEach var="met" items="${mlAssay.metabolitesGUI}" varStatus="loopStatusMet">--%>

                            <%--&lt;%&ndash; Write the header, only the first time &ndash;%&gt;--%>
                        <%--<c:if test="${loopStatusMet.index == 1}">--%>
                        <%--<thead class='text_header'>--%>
                        <%--<tr>--%>
                            <%--<th><spring:message code="label.metabolites.description"/></th>--%>
                            <%--<th><spring:message code="label.metabolites.formula"/></th>--%>

                            <%--<c:if test="${mlAssay.technology eq  'mass spectrometry'}">--%>
                                <%--<th><spring:message code="label.metabolites.mz"/></th>--%>
                                <%--<th><spring:message code="label.metabolites.retentiontime"/><a class="right icon icon-functional" data-icon="u" onclick="toggleColumn('metabolites${loopStatusAssay.index}', this, 2500)"></a></th>--%>
                            <%--</c:if>--%>
                            <%--<c:if test="${mlAssay.technology eq 'NMR spectroscopy'}">--%>
                                <%--<th><spring:message code="label.metabolites.chemicalshift"/></th>--%>
                                <%--<th><spring:message code="label.metabolites.multiplicity"/><a class="right icon icon-functional" data-icon="u" onclick="toggleColumn('metabolites${loopStatusAssay.index}', this, 2500)"></a></th>--%>
                            <%--</c:if>--%>

                            <%--<th><spring:message code="label.metabolites.smiles"/></th>--%>
                            <%--<th><spring:message code="label.metabolites.inchi"/></th>--%>
                            <%--<c:forEach var="sampleHeader" items="${met.metabolite.metaboliteSamples}" varStatus="loopStatusSamplesName" >--%>
                                <%--<th>--%>
                                        <%--${sampleHeader.sampleName}--%>
                                <%--</th>--%>
                            <%--</c:forEach>--%>
                        <%--</tr>--%>
                        <%--</thead>--%>
                        <%--<tbody>--%>
                        <%--</c:if>--%>

                            <%--&lt;%&ndash; Show more stuff...show only ten lines by default &ndash;%&gt;--%>
                        <%--<c:if test="${loopStatusMet.index == 10}">--%>
                        <%--</tbody><tbody id="met_${loopStatusAssay.index}" style='display:none'>--%>
                    <%--</c:if>--%>

                        <%--&lt;%&ndash;Line itself &ndash;%&gt;--%>
                    <%--<tr class="${loopStatusMet.index % 2 == 0 ? '' : 'coloured'}">--%>
                        <%--<td>--%>
                                <%--${met.metabolite.description}--%>
                            <%--<c:choose>--%>
                                <%--<c:when test="${empty met.identifier}"></c:when>--%>
                                <%--<c:when test="${empty met.link }"> (${met.identifier})</c:when>--%>
                                <%--<c:otherwise><a class="metLink" identifier="${met.identifier}" href="${met.link}" target="_blank">(${met.identifier})</a></c:otherwise>--%>
                            <%--</c:choose>--%>
                        <%--</td>--%>
                        <%--<td>--%>
                                <%--${met.metabolite.chemical_formula}--%>
                        <%--</td>--%>
                        <%--<c:if test="${mlAssay.technology eq 'mass spectrometry'}">--%>
                            <%--<td>${met.metabolite.mass_to_charge}</td>--%>
                            <%--<td>${met.metabolite.retention_time}</td>--%>
                        <%--</c:if>--%>
                        <%--<c:if test="${mlAssay.technology eq 'NMR spectroscopy'}">--%>
                            <%--<td>${met.metabolite.chemical_shift}</td>--%>
                            <%--<td>${met.metabolite.multiplicity}</td>--%>
                        <%--</c:if>--%>

                        <%--<td>--%>
                                <%--${met.metabolite.smiles}--%>
                        <%--</td>--%>
                        <%--<td>--%>
                                <%--${met.metabolite.inchi}--%>
                        <%--</td>--%>

                            <%--&lt;%&ndash; sampleValues &ndash;%&gt;--%>
                        <%--<c:forEach var="sample" items="${met.metabolite.metaboliteSamples}" varStatus="loopStatusSamples" >--%>
                            <%--<td class="tableitem">--%>
                                    <%--${sample.value}--%>
                            <%--</td>--%>
                        <%--</c:forEach>--%>
                            <%--&lt;%&ndash; For each sample &ndash;%&gt;--%>

                    <%--</tr>--%>

                    <%--</c:forEach> &lt;%&ndash; For each metabolite (line)&ndash;%&gt;--%>
                    <%--</tbody>--%>
                    <%--</table>--%>
                    <%--<script>toggleColumn('metabolites${loopStatusAssay.index}',null,0) </script>--%>
                <%--</div>--%>

                <%--<c:if test="${fn:length(mlAssay.metabolitesGUI) > 10}"><a href="#" class="showLink" id="met_link_${loopStatusAssay.index}">Show more</a></c:if>--%>
                <%--<br/>--%>
            <%--</c:if>--%>
        <%--</c:forEach> <!-- For each assayGroup -->--%>
    <%--</c:if>--%>
<%--</div> <!--  ends tabs-4 -->--%>
</div> <!-- end tabs -->
</div>
