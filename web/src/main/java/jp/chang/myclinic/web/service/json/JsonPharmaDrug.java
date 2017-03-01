package jp.chang.myclinic.web.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.chang.myclinic.model.PharmaDrug;

/**
 * Created by hangil on 2017/03/01.
 */
public class JsonPharmaDrug {

    private Integer iyakuhincode;

    public Integer getIyakuhincode(){
        return iyakuhincode;
    }

    public void setIyakuhincode(Integer iyakuhincode){
        this.iyakuhincode = iyakuhincode;
    }

    private String description;

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @JsonProperty("sideeffect")
    private String sideEffect;

    public String getSideEffect(){
        return sideEffect;
    }

    public void setSideEffect(String sideEffect){
        this.sideEffect = sideEffect;
    }

    public static PharmaDrug toPharmaDrug(JsonPharmaDrug src){
        PharmaDrug dst = new PharmaDrug();
        dst.setIyakuhincode((src.getIyakuhincode()));
        dst.setDescription((src.getDescription()));
        dst.setSideEffect((src.getSideEffect()));
        return dst;
    }

    public static JsonPharmaDrug fromPharmaDrug(PharmaDrug src){
        JsonPharmaDrug dst = new JsonPharmaDrug();
        dst.setIyakuhincode((src.getIyakuhincode()));
        dst.setDescription((src.getDescription()));
        dst.setSideEffect((src.getSideEffect()));
        return dst;
    }
}
