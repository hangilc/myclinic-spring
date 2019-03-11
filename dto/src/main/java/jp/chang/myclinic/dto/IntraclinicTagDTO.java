package jp.chang.myclinic.dto;

public class IntraclinicTagDTO {

    public int tagId;
    public String name;

    @Override
    public String toString() {
        return "IntraclinicTagDTO{" +
                "tagId=" + tagId +
                ", name='" + name + '\'' +
                '}';
    }
}
