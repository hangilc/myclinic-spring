package jp.chang.myclinic.serverpostgresql.db.intraclinic;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "intraclinic_post")
public class Post {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="post_id")
    private Integer postId;
    private String content;
    @Column(name="created_at")
    private LocalDate createdAt;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + postId +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
//                ", comments=" + comments +
                '}';
    }
}
