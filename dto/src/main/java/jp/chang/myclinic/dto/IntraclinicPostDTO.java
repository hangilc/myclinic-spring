package jp.chang.myclinic.dto;

public class IntraclinicPostDTO {
    public Integer id;
    public String content;
    public String createdAt;

    @Override
    public String toString() {
        return "IntraclinicPostDTO{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
