package jp.chang.myclinic.serverpostgresql.db.intraclinic;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "intraclinic_comment")
public class Comment {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Integer commentId;
    private String name;
    private String content;
    @Column(name="post_id")
    private Integer postId;
    @Column(name="created_at")
    private LocalDate createdAt;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer id) {
        this.commentId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postid) {
        this.postId = postid;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", postId=" + getPostId() +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
