package jp.chang.myclinic.medicalcheck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
class Config {

    private String name = "";
    private String postalCode = "";
    private String address = "";
    private String tel = "";
    private String fax = "";
    private String doctorName = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("postal-code")
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @JsonProperty("doctor-name")
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @Override
    public String toString() {
        return "Config{" +
                "name='" + name + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                ", fax='" + fax + '\'' +
                ", doctorName='" + doctorName + '\'' +
                '}';
    }
}
