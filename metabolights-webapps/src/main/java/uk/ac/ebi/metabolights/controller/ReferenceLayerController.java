package uk.ac.ebi.metabolights.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.ebi.ebisearchservice.ArrayOfArrayOfString;
import uk.ac.ebi.ebisearchservice.ArrayOfString;
import uk.ac.ebi.ebisearchservice.EBISearchService;
import uk.ac.ebi.ebisearchservice.EBISearchService_Service;
import uk.ac.ebi.metabolights.authenticate.IsaTabAuthentication;
import uk.ac.ebi.metabolights.properties.PropertyLookup;
import uk.ac.ebi.metabolights.referencelayer.MetabolightsCompound;
import uk.ac.ebi.metabolights.referencelayer.RefLayerFilter;
import uk.ac.ebi.metabolights.service.AppContext;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Controller for login and related actions.
 * @author Tejasvi
 */
@Controller
public class ReferenceLayerController extends AbstractController {

    private @Value ("#{ebiServiceURL}") String url;
    private final String REFLAYERSESSION = "RefLayer";
    RefLayerFilter rffl;
    RefLayerFilter cacheRffl;

    private static EBISearchService ebiSearchService;
    private static final String MTBLDomainName = "metabolights";
    private static ArrayOfString listOfMTBLFields;
    private static ModelAndView mav;
    private static Logger logger = Logger.getLogger(IsaTabAuthentication.class);

    public enum UserAction {
            clickedOnPage,
            freeTextOrExample,
            checkedFacet,
            browseCached,
            firstTimeBrowse;
    }

    private UserAction ua;
    ArrayOfArrayOfString listOfMTBLEntries;

    public enum ColumnMap{
        description,
        id,
        iupac,
        name,
        organism,
        technology_type("technology type"),
        CHEBI,
        METABOLIGHTS,
        study_design,
        submitter,
        study_factor,
        last_modification_date;

        ColumnMap(String altName){this.altName = altName;}
        ColumnMap(){}
        private int index = -1;
        private String altName;
        public int getIndex(){return index;}
        public void setIndex(int index){this.index = index;}

        public String columnName(){
            if (altName != null){
                return altName;
            }

            return this.name();
        }

    }

    private void initEBISearchService() throws Exception {

        if(ebiSearchService == null) try {
            ebiSearchService = new EBISearchService_Service(new URL(url)).getEBISearchServiceHttpPort();
        } catch (Exception e) {
            logger.info("initEBISearchService method error - "+ e);
            throw e;
        }

        listOfMTBLFields = ebiSearchService.listFields(MTBLDomainName);

        for(int g=0; g<listOfMTBLFields.getString().size(); g++){

        }

        listOfMTBLFields.getString().add("CHEBI");
        listOfMTBLFields.getString().add("METABOLIGHTS");
        mapColumns();
        rffl.setMTBLNumOfResults(ebiSearchService.getNumberOfResults(MTBLDomainName, rffl.getEBIQuery()));
    }

    private void mapColumns(){

        // If not already mapped (only needs to be done once)...index will have -1
        if (ColumnMap.METABOLIGHTS.getIndex() == -1){

            for (int i=0;i < listOfMTBLFields.getString().size(); i++){

                String fieldName = listOfMTBLFields.getString().get(i);

                // Loop through the enum (not very optimal but the enum is quite short)
                for (ColumnMap cm : ColumnMap.values()){

                    if (cm.columnName().equals(fieldName)){
                        cm.setIndex(i);
                        continue;
                    }
                }
            }
        }
    }

    @RequestMapping({ "/clearreflayercache" })
    public ModelAndView clearCache(){
        cacheRffl = null;
        return printMessage("Cache cleared.", "The cache has been cleared.");
    }

    @RequestMapping({ "/refLayerSearch" })
    public ModelAndView searchAndDisplay(
        @RequestParam(required = false, value = "freeTextQuery") String userQuery,
        @RequestParam(required = false, value = "organisms") String[] organismsSelected,
        @RequestParam(required = false, value = "technology") String[] technologiesSelected,
        @RequestParam(required = false, value = "PageNumber") String PageSelected,
        @RequestParam(required = false, value = "userAction") String userAction,
        HttpServletRequest request) {

        if(userQuery == null){
            userQuery = "";
        }

        rffl = (RefLayerFilter)request.getSession().getAttribute(REFLAYERSESSION);
        mav = AppContext.getMAVFactory().getFrontierMav("refLayerSearch");

        mapUserAction(userQuery, organismsSelected, technologiesSelected, PageSelected, userAction);
        try{
            queryEBI();
        } catch (Exception e){
            return new ModelAndView("redirect:index?message="+ PropertyLookup.getMessage("msg.wsdl.error"));
        }

        if(rffl.getMTBLNumOfResults() != 0){
            getEntries();
        }

        updateFacets();
        sortFacets();
        request.getSession().setAttribute("RefLayer", rffl);


        if((rffl.getFreeText().equals("")) && (cacheRffl == null)){
            cacheRffl = rffl.clone();
        }

        mav.addObject("rffl", rffl);
        return mav;
    }

    private void sortFacets() {
        rffl.sortFacets();
    }

    private void getEntries() {

        Collection<MetabolightsCompound> mcs = new ArrayList <MetabolightsCompound>();
        Integer entriesFrom = 0;
        Integer toEntries = 0;

        if(rffl.getCurrentPage().equals(rffl.getTotalNumOfPages())){
            if(rffl.getLastPageEntries() == 0){
                toEntries = 9;
            } else {
                toEntries = rffl.getLastPageEntries();
            }
        } else {
            toEntries = 9;
        }

        for(int z=entriesFrom; z<toEntries; z++){

            // Get the ebiEye entry
            List<String> ebiEyeEntry = listOfMTBLEntries.getArrayOfString().get(z).getString();

            // Instantiate a new entry...
            MetabolightsCompound mc = ebieyeEntry2Metabolite(ebiEyeEntry);

            mcs.add(mc);
        }

        mav.addObject("entries", mcs);
    }

    private MetabolightsCompound ebieyeEntry2Metabolite(List<String> ebieyeEntry) {

        // Instantiate a new Metabolite compound ...
        MetabolightsCompound mc = new MetabolightsCompound();

        String value;

        // Get the chebiId
        value = getValueFromEbieyeEntry(ColumnMap.CHEBI, ebieyeEntry);
        mc.setChebiId(value);
        if (!value.equals("")) mc.setChebiURL(value.split(":")[1]);

        //Get the description
        value = getValueFromEbieyeEntry(ColumnMap.description, ebieyeEntry);
        if(!value.equals("")) mc.setDescription(value);

        // Get the studies
        value = getValueFromEbieyeEntry(ColumnMap.METABOLIGHTS, ebieyeEntry);
        if (!value.equals("")) mc.setMTBLStudies(value.split("\\s"));

        // Get the iupac names
        value = getValueFromEbieyeEntry(ColumnMap.iupac, ebieyeEntry);
        if (!value.equals("")) mc.setIupac(value.split("\\n"));

        // Get the ACCESION
        value = getValueFromEbieyeEntry(ColumnMap.id, ebieyeEntry);
        mc.setAccession(value);

        // Get the name
        value = getValueFromEbieyeEntry(ColumnMap.name, ebieyeEntry);
        mc.setName(value);

        //Get Technology
        value = getValueFromEbieyeEntry(ColumnMap.technology_type, ebieyeEntry);
        if (!value.equals("")) mc.setTechnology_type(value.split("\\n"));

        //Get Organism
        value = getValueFromEbieyeEntry(ColumnMap.organism, ebieyeEntry);
        if (!value.equals("")) mc.setOrganism(value.split("\\n"));

        value = getValueFromEbieyeEntry(ColumnMap.study_design, ebieyeEntry);
        if (!value.equals("")) mc.setStudy_design(value.split("\\n"));

        value = getValueFromEbieyeEntry(ColumnMap.last_modification_date, ebieyeEntry);
        if (!value.equals("")) mc.setLast_modification_date(value);

        value = getValueFromEbieyeEntry(ColumnMap.study_factor, ebieyeEntry);
        if (!value.equals("")) mc.setStudy_factor(value.split("\\n"));

        value = getValueFromEbieyeEntry(ColumnMap.submitter, ebieyeEntry);
        if (!value.equals("")) mc.setSubmitter(value);

        return mc;
    }

    private void mapUserAction(String userQuery, String[] organismsSelected, String[] technologiesSelected, String pageSelected, String userAction) {

        if(userAction != null){
            if(userAction.equals("facetClicked")){
                ua = UserAction.checkedFacet;
                rffl.setCurrentPage(Integer.parseInt(pageSelected));
                rffl.resetFacets();
                rffl.checkFacets(organismsSelected, technologiesSelected);
                if (rffl.getFacetsQuery().equals("")){
                    rffl.uncheckFacets();
                }
            } else if(userAction.equals("pageClicked")){
                ua = UserAction.clickedOnPage;
                rffl.setCurrentPage(Integer.parseInt(pageSelected));
            }
        } else if(userQuery.equals("")){
            if ((cacheRffl != null)){
                ua = UserAction.browseCached;
                rffl = cacheRffl.clone();
            } else{
                ua = UserAction.firstTimeBrowse;
                rffl = new RefLayerFilter(userQuery, organismsSelected, technologiesSelected, pageSelected);
            }
        } else {
            ua = UserAction.freeTextOrExample;
            rffl = new RefLayerFilter(userQuery, organismsSelected, technologiesSelected, pageSelected);
        }
    }

    private void queryEBI() throws Exception {

        try {
            initEBISearchService();
        } catch (Exception e){
            logger.info("queryEBI method error - "+ e);
            throw e;
        }

        if ((ua == UserAction.clickedOnPage) || ((ua == UserAction.browseCached) && (rffl.getFacetsQuery().equals(""))) || (ua == UserAction.checkedFacet && (rffl.getFacetsQuery().equals("")))){
            ArrayOfString listOfMTBLIds = ebiSearchService.getResultsIds(MTBLDomainName, rffl.getEBIQuery(), ((rffl.getCurrentPage()*10)-10), 10);
            listOfMTBLEntries = ebiSearchService.getEntries(MTBLDomainName, listOfMTBLIds, listOfMTBLFields);
        } else {
            ArrayOfString listOfMTBLIds = ebiSearchService.getAllResultsIds(MTBLDomainName, rffl.getEBIQuery());
            listOfMTBLEntries = ebiSearchService.getEntries(MTBLDomainName, listOfMTBLIds, listOfMTBLFields);
        }
    }

    private void updateFacets() {

        if(ua != UserAction.clickedOnPage){
            for(ArrayOfString entry: this.listOfMTBLEntries.getArrayOfString()){
                String organisms = getValueFromEbieyeEntry(ColumnMap.organism, entry.getString());
                String technologies = getValueFromEbieyeEntry(ColumnMap.technology_type, entry.getString());
                String[] organismsList = organisms.split("\\n");
                String[] technologiesList = technologies.split("\\n");
                rffl.updateOrganismFacet(organismsList);
                rffl.updateTechnologyFacet(technologiesList);
            }
        }
    }

    private String getValueFromEbieyeEntry(ColumnMap columnName, List<String> ebieyeEntry){

        // If index is -1 the field is not there
        if (columnName.getIndex() == -1 ) return "";

        // If the ebeeye entry do not have that value....
        if (ebieyeEntry.size() < columnName.getIndex()){
            return "";
        }

        // Get the value
        String value = ebieyeEntry.get(columnName.getIndex());

        // If it's null
        return (value == null? "": value);
    }
}