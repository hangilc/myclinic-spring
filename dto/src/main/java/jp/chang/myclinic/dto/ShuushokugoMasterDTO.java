package jp.chang.myclinic.dto;

public class ShuushokugoMasterDTO {
    public int shuushokugocode;
    public String name;

    @Override
    public String toString() {
        return "ShuushokugoMasterDTO{" +
                "shuushokugocode=" + shuushokugocode +
                ", name='" + name + '\'' +
                '}';
    }
}
