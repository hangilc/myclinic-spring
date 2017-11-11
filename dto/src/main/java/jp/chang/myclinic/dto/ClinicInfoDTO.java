package jp.chang.myclinic.dto;

public class ClinicInfoDTO {

    public String name;
    public String postalCode;
    public String address;
    public String tel;
    public String fax;
    public String kikancode;
    public String homepage;
    public String doctorName;

    @Override
    public String toString() {
        return "ClinicInfoDTO{" +
                "name='" + name + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                ", fax='" + fax + '\'' +
                ", kikancode='" + kikancode + '\'' +
                ", homepage='" + homepage + '\'' +
                ", doctorName='" + doctorName + '\'' +
                '}';
    }
}