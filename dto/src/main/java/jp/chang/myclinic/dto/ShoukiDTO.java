package jp.chang.myclinic.dto;

public class ShoukiDTO {

    public int visitId;
    public String shouki;

    @Override
    public String toString() {
        return "ShoukiDTO{" +
                "visitId=" + visitId +
                ", shouki='" + shouki + '\'' +
                '}';
    }
}
