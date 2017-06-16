package jp.chang.myclinic;

import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties(prefix="myclinic.clinic")
public class ClinicInfo {

    private String name = "";
    private String postalCode = "";
    private String address = "";
    private String tel = "";
    private String fax = "";
    private String homepage = "";
    private String doctorName = "";

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPostalCode(){
        return postalCode;
    }

    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

     public String getTel(){
        return tel;
    }

    public void setTel(String tel){
        this.tel = tel;
    }

    public String getFax(){
        return fax;
    }

    public void setFax(String fax){
        this.fax = fax;
    }

    public String getHomepage(){
        return homepage;
    }

    public void setHomepage(String homepage){
        this.homepage = homepage;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}