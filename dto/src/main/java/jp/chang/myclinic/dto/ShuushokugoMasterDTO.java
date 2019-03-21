package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.Primary;

public class ShuushokugoMasterDTO {
    @Primary
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
