package jp.chang.myclinic.db.intraclinic;

import javax.persistence.*;

@Entity
@Table(name = "tag_post")
@IdClass(TagPostId.class)
public class TagPost {
    @Id private Integer postId;
    @Id private Integer tagId;

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
