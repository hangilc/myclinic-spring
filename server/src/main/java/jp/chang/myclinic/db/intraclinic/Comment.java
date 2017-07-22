package jp.chang.myclinic.db.intraclinic;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String content;
//    @Column(name="post_id")
//    private Integer postid;
    @Column(name="created_at")
    private String createdAt;
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

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

//    public Integer getPostid() {
//        return postid;
//    }
//
//    public void setPostid(Integer postid) {
//        this.postid = postid;
//    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", postid=" + post.getId() +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
