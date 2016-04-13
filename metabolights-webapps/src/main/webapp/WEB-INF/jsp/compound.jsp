<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/MetCompound.css" type="text/css"/>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/css/bootstrap-select.min.css">
<link rel="stylesheet" type="text/css" href="https://cdn.rawgit.com/jmvillaveces/biojs-vis-keggviewer/master/dependencies/css/bootstrap-slider.css">
<div id="content" class="grid_24">
    <div class="container-fluid">
        <div class="row">
            <div class="wrapper col">
                <div id="app">
                    <div class="col-md-3 image">
                        <div class="row met-img">
                            <div class="card">
                                <!-- Nav tabs -->
                                <ul class="nav nav-tabs" role="tablist">
                                    <li role="presentation" class="active"><a href="#2d" aria-controls="home" role="tab" data-toggle="tab">2D</a></li>
                                    <li role="presentation"><a href="#3d" aria-controls="profile" role="tab" data-toggle="tab">3D</a></li>
                                </ul>

                                <!-- Tab panes -->
                                <div class="tab-content" id="displayMol">
                                    <div role="tabpanel" class="tab-pane active" id="2d">
                                        <%--<h5>Structure</h5><br>--%>
                                        <img :src="mtblc.imageUrl" class="metabolite-image"/>

                                    </div>
                                    <div role="tabpanel" class="tab-pane" id="3d">
                                        <div id="3dDisplay" style="position: relative;"></div>
                                            <textarea style="display: none;" id="moldata_sdf">
                                                {{ mtblc.structure }}
                                            </textarea>
                                    </div>
                                    <span id='zoom'>
                                        <a data-toggle="modal" data-target="#zoomModal"><i class="fa fa-search-plus"></i></a>
                                    </span>
                                    <div class="modal fade" id="zoomModal" tabindex="-1" role="dialog" aria-labelledby="zoomModalLabel">
                                        <div class="modal-dialog zoom-dialog" role="document">
                                            <div class="modal-content">
                                                <div class="modal-body">
                                                    <div>
                                                        <!-- Nav tabs -->
                                                        <ul class="nav nav-tabs" role="tablist">
                                                            <li role="presentation" class="active"><a href="#zoom2d" aria-controls="home" role="tab" data-toggle="tab">2D</a></li>
                                                            <li role="presentation"><a href="#zoom3d" aria-controls="profile" role="tab" data-toggle="tab">3D</a></li>
                                                        </ul>
                                                        <div class="tab-content">
                                                            <div role="tabpanel" class="tab-pane active" id="zoom2d">
                                                                <%--<h5>Structure</h5><br>--%>
                                                                <img style="height: 400px; width: 450px; position: relative;" :src="mtblc.imageUrlLarge" class="metabolite-image"/>

                                                            </div>
                                                            <div role="tabpanel" class="tab-pane" id="zoom3d">
                                                                <div style="height: 400px; width: 400px; position: relative;" data-backgroundcolor="white" data-style="{&quot;stick&quot;:{}}" data-datatype="sdf" data-element="zoom_moldata_sdf" class="viewer_3Dmoljs"></div>
                                                                    <textarea style="display: none;" id="zoom_moldata_sdf">
                                                                        {{ mtblc.structure }}
                                                                    </textarea>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div class="text-center">
                                &nbsp;
                            </div>
                        </div>
                    </div>
                    <div class="col-md-9">
                        <div class="banner">
                            <h2>{{ mtblc['name'] }}
                            </h2>
                                <span class="met-id pull-right">
                                    <div class="btn-group" role="group" aria-label="">
                                        <button type="button" class="btn btn-default btn-xs" onclick="downloadJSONFile()"><i class="fa fa-save"></i> JSON</button>
                                        <!-- <div class="btn-group" role="group">
                                            <button type="button" class="btn btn-default btn-xs" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="fa fa-share"></i> Share</button>
                                            <ul class="dropdown-menu">
                                                <li></li>
                                                <li><a href="#"></a></li>
                                            </ul>
                                        </div> -->
                                        <a href="${pageContext.request.contextPath}/referencespectraupload?cid=${compoundId}" class="btn btn-default btn-xs"><i class="fa fa-upload"></i> Upload Spectra </a>
                                        <button type="button" class="btn btn-default btn-xs" data-toggle="modal" data-target="#discussionModal"><i class="fa fa-comment"></i> Discussion</button>
                                        <a target="_blank" href="${pageContext.request.contextPath}/contact" class="btn btn-default btn-xs"><i class="fa fa-question"></i> Help</a>
                                    </div>
                                </span>
                        </div>
                        <div class="modal fade" id="discussionModal" tabindex="-1" role="dialog" aria-labelledby="discussionModal">
                            <div class="modal-dialog disqus-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-body">
                                        <div id="disqus_thread"></div>
                                        <script>
                                            /**
                                             *  RECOMMENDED CONFIGURATION VARIABLES: EDIT AND UNCOMMENT THE SECTION BELOW TO INSERT DYNAMIC VALUES FROM YOUR PLATFORM OR CMS.
                                             *  LEARN WHY DEFINING THESE VARIABLES IS IMPORTANT: https://disqus.com/admin/universalcode/#configuration-variables
                                             */

                                            var disqus_config = function () {
                                                this.page.url = "http://www.ebi.ac.uk/metabolights/${compoundId}";  // Replace PAGE_URL with your page's canonical URL variable
                                                this.page.identifier = "${compoundId}"; // Replace PAGE_IDENTIFIER with your page's unique identifier variable
                                            };

                                            (function() {  // DON'T EDIT BELOW THIS LINE
                                                var d = document, s = d.createElement('script');

                                                s.src = '//metabolights.disqus.com/embed.js';

                                                s.setAttribute('data-timestamp', +new Date());
                                                (d.head || d.body).appendChild(s);
                                            })();
                                        </script>
                                        <noscript>Please enable JavaScript to view the <a href="https://disqus.com/?ref_noscript" rel="nofollow">comments powered by Disqus.</a></noscript>


                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="met-content">
                            <div class="card">
                                <ul class="nav nav-tabs" role="tablist">
                                    <li role="presentation" class="active"><a href="#chemistry" aria-controls="chemistry" role="tab" data-toggle="tab">Chemistry</a></li>
                                    <li v-if="mtblc.species" role="presentation"><a href="#biology" aria-controls="biology" role="tab" data-toggle="tab">Biology</a></li>
                                    <li v-if="mtblc.pathways" role="presentation"><a href="#pathways" aria-controls="pathways" role="tab" data-toggle="tab">Pathways</a></li>
                                    <li v-if="mtblc.spectra" role="presentation"><a href="#spectra" aria-controls="spectra" role="tab" data-toggle="tab">Spectra</a></li>
                                    <li v-if="mtblc.reactions.length > 0" role="presentation"><a href="#reaction" aria-controls="spectra" role="tab" data-toggle="tab">Reaction</a></li>
                                    <li v-if="mtblc.citations" role="presentation"><a href="#citations" aria-controls="citations" role="tab" data-toggle="tab">Literature</a></li>
                                </ul>

                                <!-- Tab panes -->
                                <div class="tab-content cols">
                                    <div role="tabpanel" class="tab-pane active" id="chemistry">
                                        <div class="alert alert-default" id="description">
                                            <label>Compound Description</label>
                                            <h4>
                                                {{ mtblc.definition }}
                                            </h4>
                                        </div>
                                        <div class="met-panel col-md-12">


                                            <div class="col-md-12  ml_tr_th">
                                                <div class="col-md-12 ml_trc"><b><h4>Identification</h4></b></div>
                                            </div>
                                            <div class="col-md-12 ml_tr">
                                                <div class="col-md-3 ml_trc">MetaboLights Identifiewe</div>
                                                <div class="col-md-9 ml_trc">
                                                    <p v-for="iupac in mtblc.iupacNames" class="label-spaced">{{ mtblc.id }}</p>
                                                </div>
                                            </div>
                                            <div class="col-md-12 ml_tr">
                                                <div class="col-md-3 ml_trc">IUPAC Names</div>
                                                <div class="col-md-9 ml_trc">
                                                    <p v-for="iupac in mtblc.iupacNames" class="label-spaced">{{ iupac }},</p>
                                                </div>
                                            </div>
                                            <div class="col-md-12 ml_tr">
                                                <div class="col-md-3 ml_trc">Inchikey</div>
                                                <div class="col-md-9 ml_trc">{{ mtblc.inchiKey }}</div>
                                            </div>
                                            <div class="col-md-12 ml_tr">
                                                <div class="col-md-3 ml_trc">Inchi</div>
                                                <div class="col-md-9 ml_trc">{{ mtblc.inchi }}</div>
                                            </div>
                                            <div class="col-md-12 ml_tr">
                                                <div class="col-md-3 ml_trc">Smiles</div>
                                                <div class="col-md-9 ml_trc">{{ mtblc.smiles }}</div>
                                            </div>
                                            <div class="col-md-12 ml_tr">
                                                <div class="col-md-3 ml_trc">Molecular Formula</div>
                                                <div class="col-md-9 ml_trc">{{ mtblc.formula }}</div>
                                            </div>
                                            <div class="col-md-12 ml_tr">
                                                <div class="col-md-3 ml_trc">Average Mass</div>
                                                <div class="col-md-9 ml_trc">{{ mtblc.averagemass }}</div>
                                            </div>
                                            <div class="col-md-12 ml_tr">
                                                <div class="col-md-3 ml_trc">Exact Mass</div>
                                                <div class="col-md-9 ml_trc">{{ mtblc.exactmass }}</div>
                                            </div>
                                            <div class="col-md-12 ml_tr">
                                                <div class="col-md-3 ml_trc">Molecular Weight</div>
                                                <div class="col-md-9 ml_trc">{{ mtblc.molweight }}</div>
                                            </div>
                                            <div class="col-md-12 ml_tr">
                                                <div class="col-md-3 ml_trc">Charge</div>
                                                <div class="col-md-9 ml_trc">{{ mtblc.charge }}</div>
                                            </div>

                                            <div class="col-md-12 ml_tr">
                                                <div class="col-md-3 ml_trc">Synonymns</div>
                                                <div class="col-md-9 ml_trc">
                                                    <p v-for="synonym in mtblc.synonyms" class="label-spaced">{{ synonym }},</p>
                                                </div>
                                            </div>
                                            <div class="clearfix"></div><div class="clearfix"></div>

                                            <div class="col-md-12  ml_tr_th">
                                                <div class="col-md-12 ml_trc"><b><h4>External Links</h4></b></div>
                                            </div>
                                            <div class="col-md-12 ml_tr" v-for="id in mtblc.externalIds">
                                                <div class="col-md-3 ml_trc">{{ $key }}</div>
                                                <div class="col-md-9 ml_trc">{{{ id }}}</div>
                                            </div>
                                        </div>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div role="tabpanel" class="tab-pane" id="biology">
                                        <div class="met-panel col-md-12">
                                            <div class="col-md-12  ml_tr_th">
                                                <div class="col-md-12 ml_trc"><b><h4>Species</h4></b></div>
                                            </div>
                                            <div class="col-md-12 ml_tr" v-for="id in mtblc.species">
                                                <div class="col-md-3 ml_trc"><b>{{ $key }}</b></div>
                                                <div class="col-md-9 ml_trc">
                                                    <p v-for="source in id">
                                                        <span>{{{ source.SpeciesAccession }}}</span>&emsp;<span>{{{ source.SourceAccession }}}</span>
                                                    </p>
                                                </div>
                                            </div>
                                            <div class="clearfix"></div>
                                        </div>
                                        <div class="clearfix"></div>
                                    </div>


                                    <div role="tabpanel" class="tab-pane" id="pathways">
                                        <div>
                                            <!-- Nav tabs -->
                                            <ul class="nav nav-tabs" role="tablist">
                                                <li v-if="mtblc.pathways.WikiPathways" role="presentation" class="active"><a href="#wiki" aria-controls="wiki" role="tab" data-toggle="tab">WikiPathways</a></li>
                                                <li v-if="mtblc.pathways.KEGGPathways.length > 0" role="presentation"><a href="#kegg" aria-controls="settings" role="tab" data-toggle="tab">KEGG Pathways</a></li>
                                            </ul>

                                            <!-- Tab panes -->
                                            <div class="tab-content">
                                                <div role="tabpanel" class="tab-pane active" id="wiki">

                                                    <div class="form-group">
                                                        <label>Select Species</label>
                                                        <select class="selectpicker form-control" v-model="selectedSpecies"  data-live-search="true">
                                                            <option value="" >Select Species</option>
                                                            <option v-for="(key,value) in mtblc.pathways.WikiPathways" value="{{key}}" >{{ key }}</option>
                                                        </select>
                                                    </div>
                                                    <div class="form-group">
                                                        <label>Select Pathways</label>
                                                        <select class="form-control" v-model="selectedPathway" data-live-search="true">
                                                            <option value="" selected>Select Pathway</option>
                                                            <option v-for="pathway in selectedPathways"  value="{{pathway.id}}">{{ pathway.name }}</option>
                                                        </select>
                                                    </div>
                                                    <div v-if="selectedPathway" class="well no-padding">
                                                        <iframe src ="http://www.wikipathways.org/wpi/PathwayWidget.php?id={{selectedPathway}}" frameborder="0" width="98%" height="500px" seamless="seamless" scrolling="no"></iframe>
                                                    </div>

                                                </div>
                                                <div role="tabpanel" class="tab-pane" id="kegg">
                                                    <select class="form-control" v-model="selectedKEGGPathway" data-live-search="true">
                                                        <option value="" selected>Select Pathway</option>
                                                        <option v-for="pathway in mtblc.pathways.KEGGPathways" value="{{ pathway.KO_PATHWAY }}" >{{ pathway.name }}</option>
                                                    </select>
                                                    <div v-if="selectedKEGGPathway">
                                                        <hr>
                                                        <div class="col-md-12 well">
                                                            <div id='snippetDiv'></div>
                                                            <div class="clearfix"></div>
                                                        </div>
                                                    </div>
                                                    <div class="clearfix"></div>
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>

                                        </div>

                                    </div>

                                    <div role="tabpanel" class="tab-pane" id="reaction">
                                        <label>Select Reaction</label>
                                        <select class="form-control" v-model="selectedReaction" data-live-search="true">
                                            <option value="" selected>Select Reaction</option>
                                            <option v-for="reaction in mtblc.reactions" value="{{ pathway.KO_PATHWAY }}" >{{ pathway.name }}</option>
                                        </select>
                                    </div>

                                    <div role="tabpanel" class="tab-pane" id="spectra">

                                        <ul class="nav nav-tabs" role="tablist">
                                            <li role="presentation" class="active"><a href="#nmr" aria-controls="nmr" role="tab" data-toggle="tab">NMR Spectra</a></li>
                                            <li role="presentation"><a href="#ms" aria-controls="ms" role="tab" data-toggle="tab">MS Spectra</a></li>
                                        </ul>

                                        <!-- Tab panes -->
                                        <div class="tab-content">
                                            <div role="tabpanel" class="tab-pane active" id="nmr">
                                                <div class="form-group">
                                                    <label>Select NMR spectra</label>
                                                    <select v-model="selectedNMR" class="form-control">
                                                        <option v-for="spectra in nmrSpectra">
                                                            {{ spectra.name }}
                                                        </option>
                                                    </select>
                                                    <hr>
                                                    <div class="col-md-12  ml_tr_th">
                                                        <div class="col-md-12 ml_trc"><b><h4>{{selectedNMRSpectra.name}}</h4></b></div>
                                                    </div>
                                                    <div class="col-md-12 ml_tr" v-for="attribute in selectedNMRSpectra.attributes">
                                                        <div class="col-md-3 ml_trc">{{ attribute.attributeName }}</div>
                                                        <div class="col-md-9 ml_trc">
                                                            <p v-for="iupac in mtblc.iupacNames" class="label-spaced">{{ attribute.attributeValue }}</p>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>
                                            <div role="tabpanel" class="tab-pane" id="ms">
                                                <div class="form-group">
                                                    <label>Select MS spectra</label>
                                                    <select v-model="selectedMS" class="form-control">
                                                        <option v-for="spectra in msSpectra">
                                                            {{ spectra.name }}
                                                        </option>
                                                    </select>
                                                    <hr>
                                                    <div class="col-md-12  ml_tr_th">
                                                        <div class="col-md-12 ml_trc"><b><h4>{{selectedMSSpectra.name}}</h4></b></div>
                                                    </div>
                                                    <div class="col-md-12 ml_tr" v-for="attribute in selectedMSSpectra.attributes">
                                                        <div class="col-md-3 ml_trc">{{ attribute.attributeName }}</div>
                                                        <div class="col-md-9 ml_trc">
                                                            <p v-for="iupac in mtblc.iupacNames" class="label-spaced">{{ attribute.attributeValue }}</p>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="clearfix"></div>
                                            </div>
                                            <div class="clearfix"></div>
                                        </div>
                                    </div>

                                    <div role="tabpanel" class="tab-pane" id="citations">
                                        <h1>Citations</h1>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/vue/1.0.10/vue.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/vue-resource/0.1.17/vue-resource.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.10.0/js/bootstrap-select.min.js"></script>

<script src="https://wzrd.in/bundle/biojs-vis-keggviewer@1.1.2"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/3Dmol-min.js"></script>
<script id="dsq-count-scr" src="//metabolights.disqus.com/count.js" async></script>


<script>

    var data = {

        compound: '${compoundId}',

        mtblc: {},

        selectedSpecies : "",

        selectedPathway: "",

        selectedKEGGPathway: "",

        selectedMS: "",

        selectedNMR: "",

        selectedMSSpectra: {},

        selectedNMRSpectra: {}

    }

    var vm = new Vue({

        el: "#app",

        data: data,

        computed: {

            selectedPathways: function () {
                var tempPathways = [];

                if(this.selectedSpecies != ""){

                    tempPathways = this.mtblc.pathways.WikiPathways[this.selectedSpecies];

                }

                return tempPathways;

            },

            nmrSpectra: function () {

                return this.mtblc.spectra.filter(function(spec){
                    return spec.type == "NMR" ? true : false;
                });

            },

            msSpectra: function () {

                return this.mtblc.spectra.filter(function(spec){
                    return spec.type == "MS" ? true : false;
                });

            }

        },

        methods: {

            load3DMolecule: function(){
                var width = document.getElementById('displayMol').offsetWidth - 10;
                $("#3dDisplay").width(width).height(width);

                var viewer = $3Dmol.createViewer($("#3dDisplay"));
                viewer.setBackgroundColor('white');
                viewer.clear();
                viewer.addAsOneMolecule(this.mtblc.structure, "sdf");
                viewer.setStyle({},{stick: {radius:0.2}});
                viewer.zoomTo();
                viewer.render();
            }

        },

        ready: function(){

            this.$http.get('${pageContext.request.contextPath}/webservice/beta/compound/'+ this.compound, function (data, status, request) {

                this.$set('mtblc', data);

                this.mtblc['chebiId'] = this.mtblc['id'].replace("MTBLC", "");
                this.mtblc['imageUrl'] = "http://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&imageIndex=0&chebiId=" + this.mtblc['chebiId'] + "&dimensions=600&transbg=true";
                this.mtblc['imageUrlLarge'] = "http://www.ebi.ac.uk/chebi/displayImage.do?defaultImage=true&imageIndex=0&chebiId=" + this.mtblc['chebiId'] + "&dimensions=1000&transbg=true";

                this.load3DMolecule();

            }).error(function (data, status, request) {

                console.log(data);
                console.log(status);
                console.log(status);

            });

        }

    });

    vm.$watch('selectedKEGGPathway', function () {
        var rootDiv = document.getElementById('snippetDiv');
        rootDiv.innerHTML = "";
        var biojsviskegg = require("biojs-vis-keggviewer");
        var proxy = function(url){
            return 'https://cors-anywhere.herokuapp.com/'+url;
        };

        biojsviskegg.pathway(this.selectedKEGGPathway).proxy(proxy).target(rootDiv).init();
    })

    vm.$watch('selectedKEGGPathway', function () {
        var rootDiv = document.getElementById('snippetDiv');
        rootDiv.innerHTML = "";
        var biojsviskegg = require("biojs-vis-keggviewer");
        var proxy = function(url){
            return 'https://cors-anywhere.herokuapp.com/'+url;
        };

        biojsviskegg.pathway(this.selectedKEGGPathway).proxy(proxy).target(rootDiv).init();
    });

    vm.$watch('selectedMS', function () {
        this.selectedMSSpectra = vm.mtblc.spectra.filter(function(spec){
            return spec.name == vm.selectedMS ? true : false;
        })[0];
    })

    vm.$watch('selectedNMR', function () {
        this.selectedNMRSpectra = vm.mtblc.spectra.filter(function(spec){
            return spec.name == vm.selectedNMR ? true : false;
        })[0];
    })



</script>
