package jp.chang.myclinic.web.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.chang.myclinic.model.Roujin;

import java.sql.Date;

/**
 * Created by hangil on 2017/03/01.
 */
public class JsonRoujin {
    @JsonProperty("roujin_id")
    private Integer roujinId;

    public Integer getRoujinId(){
        return roujinId;
    }

    public void setRoujinId(Integer roujinId){
        this.roujinId = roujinId;
    }

    @JsonProperty("patient_id")
    private Integer patientId;

    public Integer getPatientId(){
        return patientId;
    }

    public void setPatientId(Integer patientId){
        this.patientId = patientId;
    }

    private Integer shichouson;

    public Integer getShichouson(){
        return shichouson;
    }

    public void setShichouson(Integer shichouson){
        this.shichouson = shichouson;
    }

    private Integer jukyuusha;

    public Integer getJukyuusha(){
        return jukyuusha;
    }

    public void setJukyuusha(Integer jukyuusha){
        this.jukyuusha = jukyuusha;
    }

    @JsonProperty("futan_wari")
    private Integer futanWari;

    public Integer getFutanWari(){
        return futanWari;
    }

    public void setFutanWari(Integer futanWari){
        this.futanWari = futanWari;
    }

    @JsonProperty("valid_from")
    private Date validFrom;

    public Date getValidFrom(){
        return validFrom;
    }

    public void setValidFrom(Date validFrom){
        this.validFrom = validFrom;
    }

    @JsonProperty("valid_upto")
    private Date validUpto;

    public Date getValidUpto(){
        return validUpto;
    }

    public void setValidUpto(Date validUpto){
        this.validUpto = validUpto;
    }

    public static JsonRoujin fromRoujin(Roujin src){
        JsonRoujin dst = new JsonRoujin();
        dst.setRoujinId(src.getRoujinId());
        dst.setPatientId(src.getPatientId());
        dst.setShichouson(src.getShichouson());
        dst.setJukyuusha(src.getJukyuusha());
        dst.setFutanWari(src.getFutanWari());
        dst.setValidFrom(src.getValidFrom());
        dst.setValidUpto(src.getValidUpto());
        return dst;
    }
}
