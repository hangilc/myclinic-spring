package jp.chang.myclinic.web.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.chang.myclinic.model.Disease;
import jp.chang.myclinic.model.ShoubyoumeiMaster;

import java.sql.Date;
import java.util.List;

/**
 * Created by hangil on 2017/03/01.
 */
public class JsonFullDisease {

    @JsonProperty("disease_id")
    private Integer diseaseId;

    public Integer getDiseaseId(){
        return diseaseId;
    }

    public void setDiseaseId(Integer diseaseId){
        this.diseaseId = diseaseId;
    }

    @JsonProperty("patient_id")
    private Integer patientId;

    public Integer getPatientId(){
        return patientId;
    }

    public void setPatientId(Integer patientId){
        this.patientId = patientId;
    }

    @JsonProperty("shoubyoumeicode")
    private Integer shoubyoumeicode;

    public Integer getShoubyoumeicode(){
        return shoubyoumeicode;
    }

    public void setShoubyoumeicode(Integer shoubyoumeicode){
        this.shoubyoumeicode = shoubyoumeicode;
    }

    @JsonProperty("start_date")
    private Date startDate;

    public Date getStartDate(){
        return startDate;
    }

    public void setStartDate(Date startDate){
        this.startDate = startDate;
    }

    @JsonProperty("end_date")
    private Date endDate;

    public Date getEndDate(){
        return endDate;
    }

    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }

    @JsonProperty("end_reason")
    private Character endReason;

    public Character getEndReason(){
        return endReason;
    }

    public void setEndReason(Character endReason){
        this.endReason = endReason;
    }

    @JsonProperty("master_valid_from")
    private Date masterValidFrom;

    public Date getMasterValidFrom(){
        return masterValidFrom;
    }

    public void setMasterValidFrom(Date masterValidFrom){
        this.masterValidFrom = masterValidFrom;
    }

    @JsonProperty("valid_from")
    private Date validFrom;

    public Date getValidFrom(){
        return validFrom;
    }

    public void setValidFrom(Date validFrom){
        this.validFrom = validFrom;
    }

    private String name;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    private Date validUpto;

    public Date getValidUpto(){
        return validUpto;
    }

    public void setValidUpto(Date validUpto){
        this.validUpto = validUpto;
    }

    private List<JsonFullDiseaseAdj> adjList;

    public List<JsonFullDiseaseAdj> getAdjList() {
        return adjList;
    }

    public void setAdjList(List<JsonFullDiseaseAdj> adjList) {
        this.adjList = adjList;
    }

    public static JsonFullDisease fromDisease(Disease src){
        JsonFullDisease dst = new JsonFullDisease();
        dst.setDiseaseId(src.getDiseaseId());
        dst.setPatientId(src.getPatientId());
        dst.setShoubyoumeicode(src.getShoubyoumeicode());
        dst.setStartDate(src.getStartDate());
        dst.setEndDate(src.getEndDate());
        dst.setEndReason(src.getEndReason());
        dst.setMasterValidFrom(src.getMasterValidFrom());
        ShoubyoumeiMaster m = src.getMaster();
        dst.setValidFrom(m.getValidFrom());
        dst.setName(m.getName());
        dst.setValidUpto(m.getValidUpto());
        return dst;
    }
}
