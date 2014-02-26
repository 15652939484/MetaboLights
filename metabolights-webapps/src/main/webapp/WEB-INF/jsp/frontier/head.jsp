<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<head>
  	<meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
    <meta name="description" content="MetaboLights metabolomics experiments" />
    <meta name="author" content="The MetaboLights team" />
    <meta http-equiv="Content-Language" content="en-GB" />
    <meta http-equiv="Window-target" content="_top" />
    <meta name="no-email-collection" content="http://www.unspam.com/noemailcollection/" />
    <meta name="keywords" content="metabolite, metabolites, metabolism, metabolic, metabonomics, metabolomics, metabolomics study, metabolomics experiment, metabolic pathway, metabolite database" />
    <link rel="stylesheet" href="http://www.ebi.ac.uk/inc/css/userstyles.css" type="text/css">
	<link rel="stylesheet" href="http://www.ebi.ac.uk/inc/css/contents.css" type="text/css">

	<link rel="stylesheet" href="//www.ebi.ac.uk/web_guidelines/css/compliance/mini/ebi-fluid-embl.css">
   	<!-- you can replace this with [projectname]-colours.css. See http://frontier.ebi.ac.uk/web/style/colour for details of how to do this -->
  	<!-- also inform ES so we can host your colour palette file -->
 	<%--<link rel="stylesheet" href="http://www.ebi.ac.uk/web_guidelines/css/compliance/develop/embl-petrol-colours.css" type="text/css" media="screen">--%>

	<!-- you can replace this with [projectname]-colours.css. See http://frontier.ebi.ac.uk/web/style/colour for details of how to do this -->
	<%--<link rel="stylesheet" type="text/css" href="cssrl/icons.css" media="screen" />--%>

    <%--Test--%>
    <link rel="stylesheet" href='<spring:url value="/cssrl/test-scheme.css"/>' type="text/css" media="screen">
    <link rel="stylesheet" type="text/css" href='<spring:url value="/cssrl/metabolights_test.css"/>' media="screen" />
    <link rel="stylesheet" type="text/css" href='<spring:url value="/cssrl/jquery-ui-1.9.2.custom.min.css"/>' media="all" />
    <link rel="stylesheet" type="text/css" href='<spring:url value="/cssrl/icons.css"/>' media="all" />
 	<link rel="stylesheet" type="text/css" href='<spring:url value="/cssrl/movingboxes.css"/>' media="screen" />


    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js" ></script>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js" charset="utf-8"></script>
	<script type="text/javascript" src='<spring:url value="/javascript/jquery.movingboxes.js"/>' charset="utf-8"></script>
	<script type="text/javascript" src='<spring:url value="/javascript/menu.js"/>'></script>
    <script type="text/javascript" src="//www.ebi.ac.uk/web_guidelines/js/libs/modernizr.custom.49274.js"></script>
	

    <c:if test="${pageContext.request.serverName!='www.ebi.ac.uk'}" >
        <script type="text/javascript">var redline = {};redline.project_id = 196734042;</script>
        <script id="redline_js" src="http://www.redline.cc/assets/button.js" type="text/javascript"> </script>
        <script>
            $(document).ready(function() {
                setTimeout(function(){
                    // Handler for .ready() called.
                    $("#redline_side_car").css("background-image","url(img/redline_left_button.png)");
                    $("#redline_side_car").css("display", "block");
                    $("#redline_side_car").css("z-index", "-1");
                },1000);
            });
        </script>
    </c:if>
</head>