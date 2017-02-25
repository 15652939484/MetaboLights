<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

    <%--
  ~ EBI MetaboLights - http://www.ebi.ac.uk/metabolights
  ~ Cheminformatics and Metabolism group
  ~
  ~ European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
  ~
  ~ Last modified: 5/2/13 2:34 PM
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


<link rel="stylesheet" href="${pageContext.request.contextPath}/cssrl/iconfont/font_style.css" type="text/css"/>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" type="text/css"/>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/metabolights.css" type="text/css"/>

<div class="container-fluid">
    <div class="col-md-12">
        <div class="row">
            <h3><spring:message code="msg.metabolightsAbout14"/></h3>
            <p>
                Please note that these links will take you to our online Google Documents (PDF versions available below). In case of any firewall restrictions, please use our FTP links, if you are unable to access the above online Google documents.
                <br/>
                <br/>

                    <div class="col-md-6">
                            <div class="ml-row row">
                                <div class="col-md-8"><strong>Overview</strong></div>
                                <div class="col-md-3">
                                    <a class="btn btn-default" target="_blank" href="https://docs.google.com/document/d/1gNITtVK_Ikwa14HDfPG04OfqbMo7NbnuulR4yjaZePo/edit?usp=sharing"><img style="height: 16px" src="${pageContext.request.contextPath}/img/gd-icon.png"></a>
                                    <a class="btn btn-default" target="_blank" href="ftp://ftp.ebi.ac.uk/pub/databases/metabolights/documentation/MetaboLights%20Tutorial%20-%200%20-%20Thank%20You%20and%20Overview.pdf"><strong></strong></a>
                                </div>
                            </div>

                            <div class="ml-row row">
                                <div class="col-md-8"><strong>Download&nbsp;and&nbsp;setup&nbsp;ISAcreator</strong></div>
                                <div class="col-md-3">
                                    <a class="btn btn-default" target="_blank" href="https://docs.google.com/document/d/1iXfJ3VmXXvubxpSnSIj5vzJpsXE1ZNxOwcwovFYwPxs/edit?usp=sharing"><img style="height: 16px" src="${pageContext.request.contextPath}/img/gd-icon.png"></a>
                                    <a class="btn btn-default" target="_blank" href="ftp://ftp.ebi.ac.uk/pub/databases/metabolights/documentation/MetaboLights%20Tutorial%20-%201%20-%20Download%20and%20setup%20ISAcreator.pdf"><strong></strong></a>
                                </div>
                            </div>
                            <div class="ml-row row">
                                <div class="col-md-8"><strong>Setup&nbsp;and&nbsp;create&nbsp;NMR&nbsp;study</strong></div>
                                <div class="col-md-3">
                                    <a class="btn btn-default" target="_blank" href="https://docs.google.com/document/d/17DjTZRv-MAuRFDqkLPu3_3xC7dExA7VIi0QEtBNpbGY/edit?usp=sharing"><img style="height: 16px" src="${pageContext.request.contextPath}/img/gd-icon.png"></a>
                                    <a class="btn btn-default" target="_blank" href="ftp://ftp.ebi.ac.uk/pub/databases/metabolights/documentation/MetaboLights%20Tutorial%20-%202A%20-%20NMR%20Assay.pdf"><strong></strong></a>
                                </div>
                            </div>
                            <div class="ml-row row">
                                <div class="col-md-8"><strong>Setup&nbsp;and&nbsp;create&nbsp;GC/MS&nbsp;study</strong></div>
                                <div class="col-md-3">
                                    <a class="btn btn-default" target="_blank" href="https://docs.google.com/document/d/1s7X5tLF0q0tXm76LMbBovW1NReOdmWoOGLO3bm7xm6E/edit?usp=sharing"><img style="height: 16px" src="${pageContext.request.contextPath}/img/gd-icon.png"></a>
                                    <a class="btn btn-default" target="_blank" href="ftp://ftp.ebi.ac.uk/pub/databases/metabolights/documentation/MetaboLights%20Tutorial%20-%202B%20-%20GC%20MS%20Assay.pdf"><strong></strong></a>
                                </div>
                            </div>
                            <div class="ml-row row">
                                <div class="col-md-8"><strong>Setup&nbsp;and&nbsp;create&nbsp;LC/MS&nbsp;study</strong></div>
                                <div class="col-md-3">
                                    <a class="btn btn-default" target="_blank" href="https://docs.google.com/document/d/1QIAwwt9yMZuZMi3sYaRnaHkW9BVgimgIBbF0lec7tu0/edit?usp=sharing"><img style="height: 16px" src="${pageContext.request.contextPath}/img/gd-icon.png"></a>
                                    <a class="btn btn-default" target="_blank" href="ftp://ftp.ebi.ac.uk/pub/databases/metabolights/documentation/MetaboLights%20Tutorial%20-%202C%20-%20LCMS%20Assay.pdf"><strong></strong></a>
                                </div>
                            </div>
                            <div class="ml-row row">
                                <div class="col-md-8"><strong>Submit&nbsp;your&nbsp;study&nbsp;to&nbsp;MetaboLights</strong></div>
                                <div class="col-md-3">
                                    <a class="btn btn-default" target="_blank" href="https://docs.google.com/document/d/1Gnv0pvYpbYOY1pv-XZKXanCYMNIlMhPgVUEHCJ4gm98/edit?usp=sharing"><img style="height: 16px" src="${pageContext.request.contextPath}/img/gd-icon.png"></a>
                                    <a class="btn btn-default" target="_blank" href="ftp://ftp.ebi.ac.uk/pub/databases/metabolights/documentation/MetaboLights%20Tutorial%20-%203%20-%20Login%20and%20submit%20to%20MetaboLights.pdf"><strong></strong></a>
                                </div>
                            </div>
                            <div class="ml-row row">
                                <div class="col-md-8"><strong>Curation&nbsp;status&nbsp;and&nbsp;automatic&nbsp;validations</strong></div>
                                <div class="col-md-3">
                                    <a class="btn btn-default" target="_blank" href="https://docs.google.com/document/d/1eNF4Gho0BMxst5ZAj1awrWP4QRFBLW8ycwhiIqHoYk8/edit?usp=sharing"><img style="height: 16px" src="${pageContext.request.contextPath}/img/gd-icon.png"></a>
                                    <a class="btn btn-default" target="_blank" href="ftp://ftp.ebi.ac.uk/pub/databases/metabolights/documentation/MetaboLights%20Tutorial%20-%204%20-%20MetaboLights%20automatic%20validation.pdf"><strong></strong></a>
                                </div>
                            </div>
                            <div class="ml-row row">
                                <div class="col-md-8"><strong>Frequently&nbsp;asked&nbsp;questions&nbsp;(FAQ)</strong></div>
                                <div class="col-md-3">
                                    <a class="btn btn-default" target="_blank" href="https://docs.google.com/document/d/15rSS6Mrj2Aj_uMyh4-3n8RJzAdWi1uoHsaKkxqPyC4I/edit?usp=sharing"><img style="height: 16px" src="${pageContext.request.contextPath}/img/gd-icon.png"></a>
                                    <a class="btn btn-default" target="_blank" href="ftp://ftp.ebi.ac.uk/pub/databases/metabolights/documentation/MetaboLights%20Tutorial%20-%20FAQ.pdf"><strong></strong></a>
                                </div>
                            </div>
                            <div class="ml-row row">
                                <div class="col-md-8"><strong>常见问题与回答</strong></div>
                                <div class="col-md-3">
                                    <a class="btn btn-default" target="_blank" href="ftp://ftp.ebi.ac.uk/pub/databases/metabolights/documentation/MetaboLights\ 常见问题与回答20170224.pdf"><strong></strong></a>
                                </div>
                            </div>


                    </div>
                <br/>
                <br/>
            </p>
            <br>
            <div class="well">
                <p><spring:message code="msg.trainOnline" /></p>
                <p><spring:message code="msg.metabolightsAbout8" /></p>
                <p><a href="<spring:url value="contact"/>"><strong><spring:message code="label.contact"/></strong></a></p>
            </div>
        </div>
    </div>
</div>
<hr/>
<div class="container-fluid">
    <div class="col-md-12">
        <div class="row">
            <a class="noLine" href="<spring:message code="url.isatools"/>"><img src="img/softwaresuitelogo2.png" alt="ISAtools" /></a>
            <p><spring:message code="msg.metabolightsAbout6" /></p>
        </div>
    </div>
</div>



