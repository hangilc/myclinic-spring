package jp.chang.myclinic.serverpostgresql.db.intraclinic;

import javax.persistence.*;

@Entity
@Table(name = "intraclinic_tag_post")
@IdClass(TagPostId.class)
public class TagPost {
    @Id
    @Column(name="post_id")
    private Integer postId;
    @Id
    @Column(name="tag_id")
    private Integer tagId;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}
