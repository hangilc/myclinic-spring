package jp.chang.myclinic.web.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.chang.myclinic.model.Kouhi;

import java.sql.Date;

/**
 * Created by hangil on 2017/03/01.
 */
public class JsonKouhi {
    @JsonProperty("kouhi_id")
    private Integer kouhiId;

    public Integer getKouhiId(){
        return kouhiId;
    }

    public void setKouhiId(Integer kouhiId){
        this.kouhiId = kouhiId;
    }

    @JsonProperty("patient_id")
    private Integer patientId;

    public Integer getPatientId(){
        return patientId;
    }

    public void setPatientId(Integer patientId){
        this.patientId = patientId;
    }

    private Integer futansha;

    public Integer getFutansha(){
        return futansha;
    }

    public void setFutansha(Integer futansha){
        this.futansha = futansha;
    }

    private Integer jukyuusha;

    public Integer getJukyuusha(){
        return jukyuusha;
    }

    public void setJukyuusha(Integer jukyuusha){
        this.jukyuusha = jukyuusha;
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

    public static JsonKouhi fromKouhi(Kouhi src){
        JsonKouhi dst = new JsonKouhi();
        dst.setKouhiId(src.getKouhiId());
        dst.setPatientId(src.getPatientId());
        dst.setFutansha(src.getFutansha());
        dst.setJukyuusha(src.getJukyuusha());
        dst.setValidFrom(src.getValidFrom());
        dst.setValidUpto(src.getValidUpto());
        return dst;
    }
}
