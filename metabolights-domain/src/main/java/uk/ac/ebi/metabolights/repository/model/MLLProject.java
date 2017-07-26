package uk.ac.ebi.metabolights.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.json.internal.json_simple.JSONObject;
import uk.ac.ebi.metabolights.utils.json.FileUtils;
import uk.ac.ebi.metabolights.utils.json.LabsFormatter;
import uk.ac.ebi.metabolights.utils.json.LabsUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by venkata on 11/10/2016.
 */
public class MLLProject {

    private Logger logger = null;
    private FileHandler fh;
    private String Id;
    private String Title;
    private String Description;
    private MLLUser Owner;
    private String Settings;
    private String ProjectLocation;
    private String AsperaSettings;
    private Timestamp CreatedAt;
    private Timestamp UpdatedAt;
    private List<MLLJob> Jobs;
    private Boolean IsBusy = false;

    public String getAsperaSettings() {
        return AsperaSettings;
    }

    public void setAsperaSettings(String asperaSettings) {
        this.AsperaSettings = asperaSettings;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public MLLUser getOwner() {
        return Owner;
    }

    public void setOwner(MLLUser owner) {
        Owner = owner;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSettings() {
        return Settings;
    }

    public void setSettings(String settings) {
        this.Settings = settings;
    }

    public String getProjectLocation() {
        return ProjectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.ProjectLocation = projectLocation;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Timestamp getCreatedAt() { return CreatedAt; }

    public void setCreatedAt() { this.CreatedAt = LabsUtils.getCurrentTimeStamp(); }

    public void setCreatedAt(Timestamp timestamp) { this.CreatedAt = timestamp; }

    public Timestamp getUpdatedAt() { return UpdatedAt; }

    public void setUpdatedAt() { this.UpdatedAt = LabsUtils.getCurrentTimeStamp(); }

    public void setUpdatedAt(Timestamp timestamp) { this.UpdatedAt = timestamp; }

    public Boolean getBusy() { return IsBusy; }

    public void setBusy(Boolean busy) { IsBusy = busy; }

    public List<MLLJob> getJobs() {
        return Jobs;
    }

    public void setJobs(List<MLLJob> jobs) {
        Jobs = jobs;
    }

    public MLLProject(){}

    public MLLProject(MLLWorkSpace mllWorkSpace){

        Id =  UUID.randomUUID().toString();
        Title = "Untitled Project";
        Description = "This is a untitled project";
        Owner = mllWorkSpace.getOwner();
        Settings = "{}";
        ProjectLocation = mllWorkSpace.getWorkspaceLocation() + File.separator + Id;
        AsperaSettings = "{ \"asperaURL\" : \""+ mllWorkSpace.getOwner().getApiToken() + File.separator + Id + "\", \"asperaUser\" : \"\",  \"asperaServer\" : \"ah01.ebi.ac.uk\", \"asperaSecret\" :  \"\" }";
        CreatedAt = LabsUtils.getCurrentTimeStamp();
        UpdatedAt = LabsUtils.getCurrentTimeStamp();
        IsBusy = false;
        this.save();

        mllWorkSpace.getProjects().add(this);
        mllWorkSpace.save();
    }

    public MLLProject(MLLWorkSpace mllWorkSpace, String title, String description){

        Id =  UUID.randomUUID().toString();
        Title = title;
        Description = description;
        Owner = mllWorkSpace.getOwner();
        Settings = "{}";
        ProjectLocation = mllWorkSpace.getWorkspaceLocation() + File.separator + Id;
        AsperaSettings = "{ \"asperaURL\" : \""+ mllWorkSpace.getOwner().getApiToken() + File.separator + Id + "\", \"asperaUser\" : \"\",  \"asperaServer\" : \"ah01.ebi.ac.uk\", \"asperaSecret\" :  \"\" }";
        CreatedAt = LabsUtils.getCurrentTimeStamp();
        UpdatedAt = LabsUtils.getCurrentTimeStamp();
        IsBusy = false;
        this.save();

        mllWorkSpace.getProjects().add(this);
        mllWorkSpace.save();
    }

    private void initLogger(){

        try {

            this.logger = Logger.getLogger(this.getId());

            this.fh = new FileHandler(this.ProjectLocation + File.separator + Id + ".log");

            this.logger.addHandler(this.fh);

            LabsFormatter formatter = new LabsFormatter();

            this.fh.setFormatter(formatter);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    @JsonIgnore
    public Boolean saveJob(MLLJob mllJob){

        this.Jobs.add(mllJob);

        this.save();

        return true;
    }


    @JsonIgnore
    public MLLJob getJob(String jobid){
        for(MLLJob job : this.Jobs) {
            if(job.getJobId().equalsIgnoreCase(jobid)){
                return job;
            }
        }
        return null;
    }

    @JsonIgnore
    public String getLogs(){

        String LogFile = ProjectLocation + File.separator + Id + ".log";

        String logs = null;

        try {

            logs =  FileUtils.file2String(LogFile);

        } catch (IOException e) {

            e.printStackTrace();

        }

        return logs;
    }

    @JsonIgnore
    public void log(String message){

        getLogger().info(message);

    }

    @JsonIgnore
    public Boolean save(){

        String projectInfoFile = ProjectLocation + File.separator + Id + ".info";

        String LogFile = ProjectLocation + File.separator + Id + ".log";

        File dir = new File( ProjectLocation );

        if (!dir.exists()) {
            try {

                setCreatedAt();

                setUpdatedAt();

                FileUtils.createFolder(ProjectLocation);

                FileUtils.String2File("Created at:" + this.getCreatedAt(), LogFile);

                this.initLogger();

                this.logger.info( "Created " + this.getTitle());


            } catch (IOException e) {

                e.printStackTrace();

            }
        }

        try {


            if(this.logger == null){

                this.initLogger();

            }

            setUpdatedAt();

            this.logger.info( this.getTitle() + " Updated");

            FileUtils.project2File(this, projectInfoFile);

        } catch (IOException e) {

            e.printStackTrace();

        }

        return false;

    }

    @JsonIgnore
    public void saveJobDetails(String key, JSONObject value){

        JSONObject settings = LabsUtils.parseRequest(this.getSettings());

        settings.put("jobs", value.toString());

        this.setSettings(settings.toString());

        this.save();

    }

    @JsonIgnore
    public String delete(List<String> files){

        getLogger().info( "Deleting file(s) :" + LabsUtils.listToString(files));

        return FileUtils.deleteFilesFromProject(files, ProjectLocation);

    }

    @JsonIgnore
    private Logger getLogger(){

        if(this.logger == null){
            this.initLogger();
        }

        return this.logger;
    }

    @JsonIgnore
    public String getAsJSON() {

        ObjectMapper mapper = new ObjectMapper();

        try {

            return mapper.writeValueAsString(this) ;

        } catch (JsonProcessingException e) {

            e.printStackTrace();

        }

        return null;
    }
}
