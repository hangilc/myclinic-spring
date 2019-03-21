package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.Primary;

public class ShoukiDTO {
    @Primary
    public int visitId;
    public String shouki;

    public static ShoukiDTO copy(ShoukiDTO src){
        ShoukiDTO dst = new ShoukiDTO();
        dst.visitId = src.visitId;
        dst.shouki = src.shouki;
        return dst;
    }

    @Override
    public String toString() {
        return "ShoukiDTO{" +
                "visitId=" + visitId +
                ", shouki='" + shouki + '\'' +
                '}';
    }
}
