package jp.chang.myclinic.backendmysql.entity.intraclinic;

import javax.persistence.*;

@Entity
@Table(name = "intraclinic_comment")
public class Comment {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String content;
    @Column(name="post_id")
    private Integer postId;
    @Column(name="created_at")
    private String createdAt;
//    @ManyToOne
//    @JoinColumn(name="post_id")
//    private Post post;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

//    public Post getPost() {
//        return post;
//    }
//
//    public void setPost(Post post) {
//        this.post = post;
//    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", postId=" + getPostId() +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
