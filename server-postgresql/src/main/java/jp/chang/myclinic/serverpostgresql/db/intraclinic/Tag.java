package jp.chang.myclinic.serverpostgresql.db.intraclinic;

import javax.persistence.*;

@Entity
@Table(name = "intraclinic_tag")
public class Tag {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="tag_id")
    private Integer tagId;
    private String name;

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
