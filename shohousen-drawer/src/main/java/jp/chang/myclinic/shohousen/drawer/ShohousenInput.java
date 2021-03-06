package jp.chang.myclinic.shohousen.drawer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

//@JsonIgnoreProperties(ignoreUnknown=true)
public class ShohousenInput {

    public String clinicAddress = "";
    public String clinicName = "";
    public String clinicPhone = "";
    public String kikancode = "";
    public String doctorName = "";
    public String hokenshaBangou = "";
    public String hihokensha = "";
    public String futansha = "";
    public String jukyuusha = "";
    public String futansha2 = "";
    public String jukyuusha2 = "";
    public String shimei = "";
    public String birthday = "";
    public String sex;
    public Boolean honnin = null;
    public Integer futanWari = null;
    public String koufuDate = "";
    public String validUptoDate = "";
    public String content;
    public String pharmacyName;

}
