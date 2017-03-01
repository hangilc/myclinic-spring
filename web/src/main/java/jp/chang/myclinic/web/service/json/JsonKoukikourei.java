package jp.chang.myclinic.web.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.chang.myclinic.model.Koukikourei;

import java.sql.Date;

/**
 * Created by hangil on 2017/03/01.
 */
public class JsonKoukikourei {
    @JsonProperty("koukikourei_id")
    private Integer koukikoureiId;

    public Integer getKoukikoureiId(){
        return koukikoureiId;
    }

    public void setKoukikoureiId(Integer koukikoureiId){
        this.koukikoureiId = koukikoureiId;
    }

    @JsonProperty("patient_id")
    private Integer patientId;

    public Integer getPatientId(){
        return patientId;
    }

    public void setPatientId(Integer patientId){
        this.patientId = patientId;
    }

    @JsonProperty("hokensha_bangou")
    private String hokenshaBangou;

    public String getHokenshaBangou(){
        return hokenshaBangou;
    }

    public void setHokenshaBangou(String hokenshaBangou){
        this.hokenshaBangou = hokenshaBangou;
    }

    @JsonProperty("hihokensha_bangou")
    private String hihokenshaBangou;

    public String getHihokenshaBangou(){
        return hihokenshaBangou;
    }

    public void setHihokenshaBangou(String hihokenshaBangou){
        this.hihokenshaBangou = hihokenshaBangou;
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

    public static JsonKoukikourei fromKoukikourei(Koukikourei src){
        JsonKoukikourei dst = new JsonKoukikourei();
        dst.setKoukikoureiId(src.getKoukikoureiId());
        dst.setPatientId(src.getPatientId());
        dst.setHokenshaBangou(src.getHokenshaBangou());
        dst.setHihokenshaBangou(src.getHihokenshaBangou());
        dst.setFutanWari(src.getFutanWari());
        dst.setValidFrom(src.getValidFrom());
        dst.setValidUpto(src.getValidUpto());
        return dst;
    }
}
