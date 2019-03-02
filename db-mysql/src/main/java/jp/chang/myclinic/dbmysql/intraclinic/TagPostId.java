package jp.chang.myclinic.dbmysql.intraclinic;

import java.io.Serializable;
import java.util.Objects;

public class TagPostId implements Serializable {
    public Integer postId;
    public Integer tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagPostId tagPostId = (TagPostId) o;
        return Objects.equals(postId, tagPostId.postId) &&
                Objects.equals(tagId, tagPostId.tagId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(postId, tagId);
    }
}
