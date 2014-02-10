<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%--
  ~ EBI MetaboLights - http://www.ebi.ac.uk/metabolights
  ~ Cheminformatics and Metabolism group
  ~
  ~ Last modified: 03/02/14 15:22
  ~ Modified by:   kenneth
  ~
  ~ Copyright 2014 - European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
  --%>

<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script type="text/javascript" src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script type="text/javascript" src="http://d3js.org/d3.v3.min.js"></script>
<script type="text/javascript" src="javascript/dndTree.js"></script>

<script type="text/javascript">

    var speciesAutocomplete = [];

    function configAutocomplete(){
        if(typeof speciesData !== "undefined"){

            //variable exists, do what you want
            extractSpecies(speciesData);

            $("#searchspecies").autocomplete({
                    source:speciesAutocomplete,
                    minLength: 3,
                    select: function(event, ui)
                        {
                            $(location).attr('href', "reference?organisms=" + ui.item.value);

                        }
            }).attr('autocomplete','on').attr("z-index", 1000);

        }
    }

    function extractSpecies(node){

        // For some weird reason children array's name change into _children???
        var children = node.children == undefined? node._children: node.children;

        if (children){

            for (var index in children){
                extractSpecies(children[index]);
            }
        } else {

            speciesAutocomplete.push (node.name);
        }
    }

</script>
	<h2>
    	<spring:message code="menu.speciespageheader" />
    </h2>
    <p>
        <spring:message code="menu.speciespagedescription" />
    </p>
    <div class="grid_10 alpha">
        <h3><spring:message code="menu.speciesmodeltitle"/></h3>
        <ul class="species">
            <li class="icon icon-species" data-icon="H"><A href="reference?organisms=Homo sapiens (Human)">Homo sapiens (Human)</a></li>
            <li class="icon icon-species" data-icon="M"><a href="reference?organisms=Mus musculus (Mouse)">Mus musculus (Mouse)</a></li>
            <%--<li class="icon icon-species" data-icon="B"><a href="reference?Arabidopsis thaliana (thale cress)">Arabidopsis thaliana (thale cress)</a></li>--%>
            <%--<li class="icon icon-species" data-icon="L"><a href="reference?organisms=">E. coli</li>--%>
            <li class="icon icon-species" data-icon="Y"><a href="reference?organisms=Saccharomyces cerevisiae">Saccharomyces cerevisiae (Baker's yeast)</a></li>
            <li class="icon icon-species" data-icon="W"><a href="reference?organisms=Caenorhabditis elegans">Caenorhabditis elegans</a></li>
            <%--<li class="icon icon-species" data-icon="F"><a href="reference?organisms=Drosophila">Drosophila</li>--%>
        </ul>


    </div>

    <div class="grid_14 omega">
        <h3><spring:message code="menu.speciesTypeSearch"/></h3>
        <input class="width95" id="searchspecies" placeholder="type the species name to search">
    </div>

    <div class="grid_24 alpha omega">
        <h3><spring:message code="menu.speciesbrowsetitle"/></h3>
        <div id="tree-container"></div>
    </div>



