<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%--
  Created by IntelliJ IDEA.
  User: kalai
  Date: 20/04/2017
  Time: 11:04
  ~ EBI MetaboLights - http://www.ebi.ac.uk/metabolights
  ~ Cheminformatics and Metabolism group
  ~
  ~ European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
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
<base href="${pageContext.request.contextPath}/ngeditor/">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" type="image/x-icon" href="favicon.ico">
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" type="text/css" rel="stylesheet">
<link href="styles.8da2364522e0749f90c2.bundle.css" rel="stylesheet"/></head>

<%--Loading study editor for ${studyId}...--%>
<div class="container">
    <ml-study-editor studyID="${studyId}" apiToken="${apiToken}"></ml-study-editor>
</div>
<script type="text/javascript" src="inline.60dea4f2a964ccdaf95b.bundle.js"></script><script type="text/javascript" src="polyfills.63929c2b04758c996018.bundle.js"></script><script type="text/javascript" src="vendor.298e93e19050483cd5f3.bundle.js"></script><script type="text/javascript" src="main.0f22b71bc2ed1ea31ec6.bundle.js"></script></body>

