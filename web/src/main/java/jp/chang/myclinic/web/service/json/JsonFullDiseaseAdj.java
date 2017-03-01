package jp.chang.myclinic.web.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.chang.myclinic.model.DiseaseAdj;
import jp.chang.myclinic.model.ShuushokugoMaster;

/**
 * Created by hangil on 2017/03/01.
 */
public class JsonFullDiseaseAdj {

    @JsonProperty("disease_adj_id")
    private Integer diseaseAdjId;

    public Integer getDiseaseAdjId(){
        return diseaseAdjId;
    }

    public void setDiseaseAdjId(Integer diseaseAdjId){
        this.diseaseAdjId = diseaseAdjId;
    }

    @JsonProperty("disease_id")
    private Integer diseaseId;

    public Integer getDiseaseId(){
        return diseaseId;
    }

    public void setDiseaseId(Integer diseaseId){
        this.diseaseId = diseaseId;
    }

    @JsonProperty("shuushokugocode")
    private Integer shuushokugocode;

    public Integer getShuushokugocode(){
        return shuushokugocode;
    }

    public void setShuushokugocode(Integer shuushokugocode){
        this.shuushokugocode = shuushokugocode;
    }

    private String name;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public static JsonFullDiseaseAdj fromDiseaseAdj(DiseaseAdj src){
        JsonFullDiseaseAdj dst = new JsonFullDiseaseAdj();
        dst.setDiseaseAdjId(src.getDiseaseAdjId());
        dst.setDiseaseId(src.getDiseaseId());
        dst.setShuushokugocode(src.getShuushokugocode());
        ShuushokugoMaster m = src.getMaster();
        dst.setName(m.getName());
        return dst;
    }
}
