package jp.chang.myclinic.server.db.intraclinic;

import javax.persistence.*;

@Entity
@Table(name = "intraclinic_post")
public class Post {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String content;
    @Column(name="created_at")
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
//                ", comments=" + comments +
                '}';
    }
}
