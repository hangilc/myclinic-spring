package jp.chang.myclinic.web.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.chang.myclinic.model.Shahokokuho;

import java.sql.Date;

/**
 * Created by hangil on 2017/03/01.
 */
public class JsonShahokokuho {

    @JsonProperty("shahokokuho_id")
    private Integer shahokokuhoId;

    public Integer getShahokokuhoId() {
        return shahokokuhoId;
    }

    public void setShahokokuhoId(Integer shahokokuhoId) {
        this.shahokokuhoId = shahokokuhoId;
    }

    @JsonProperty("patient_id")
    private Integer patientId;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    @JsonProperty("hokensha_bangou")
    private Integer hokenshaBangou;

    public Integer getHokenshaBangou() {
        return hokenshaBangou;
    }

    public void setHokenshaBangou(Integer hokenshaBangou) {
        this.hokenshaBangou = hokenshaBangou;
    }

    @JsonProperty("hihokensha_kigou")
    private String hihokenshaKigou;

    public String getHihokenshaKigou() {
        return hihokenshaKigou;
    }

    public void setHihokenshaKigou(String hihokenshaKigou) {
        this.hihokenshaKigou = hihokenshaKigou;
    }

    @JsonProperty("hihokensha_bangou")
    private String hihokenshaBangou;

    public String getHihokenshaBangou() {
        return hihokenshaBangou;
    }

    public void setHihokenshaBangou(String hihokenshaBangou) {
        this.hihokenshaBangou = hihokenshaBangou;
    }

    private Integer honnin;

    public Integer getHonnin() {
        return honnin;
    }

    public void setHonnin(Integer honnin) {
        this.honnin = honnin;
    }

    private Integer kourei;

    public Integer getKourei() {
        return kourei;
    }

    public void setKourei(Integer kourei) {
        this.kourei = kourei;
    }

    @JsonProperty("valid_from")
    private Date validFrom;

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    @JsonProperty("valid_upto")
    private Date validUpto;

    public Date getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(Date validUpto) {
        this.validUpto = validUpto;
    }

    public static JsonShahokokuho fromShahokokuho(Shahokokuho src){
        JsonShahokokuho dst = new JsonShahokokuho();
        dst.setShahokokuhoId(src.getShahokokuhoId());
        dst.setPatientId(src.getPatientId());
        dst.setHokenshaBangou(src.getHokenshaBangou());
        dst.setHihokenshaKigou(src.getHihokenshaKigou());
        dst.setHihokenshaBangou(src.getHihokenshaBangou());
        dst.setHonnin(src.getHonnin());
        dst.setKourei(src.getKourei());
        dst.setValidFrom(src.getValidFrom());
        dst.setValidUpto(src.getValidUpto());
        return dst;
    }
}
