/*
 *
 *   EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 *   Cheminformatics and Metabolism group
 *
 *   European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 *
 *   Last modified: {{ date }}
 *   Modified by:   Venkata Chandrasekhar Nainala
 *
 *   Copyright 2015 EMBL - European Bioinformatics Institute
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 */

var literature = (function() {
    var compound = '';
    var uri = {};
    var publicationsJSON = {};

    var ajax = {
        type: "POST",
        url: "",
        async: true,
        data: {},
        success: {},
        error: {}
    }

    function init(){
        uri = {
            'metabolights' : '../citations?compoundId='+ compound
        };
    }

    function getLiterature(value){
        if (typeof value !== 'undefined') {
            compound = value;
            init();
            queryMetabolights();
        }else{
            console.log('Compound not set');
        }
        return publicationsJSON;
    }

    function queryMetabolights(){
        publicationsJSON = performAjax('metabolights')
    }

    function performAjax(resource) {
        var response_data;
        ajax.type = "GET";
        ajax.async = false;
        ajax.url = uri[resource];
        ajax.success = function(data) { response_data = data };
        ajax.error = function(){ };
        $.ajax(ajax);
        return response_data;
    }

    init();

    return {
        getLiterature: getLiterature
    }
})();