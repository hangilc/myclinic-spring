package jp.chang.myclinic.serverpostgresql.db.myclinic;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="presc_example")
public class PrescExample {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="presc_example_id")
    private Integer prescExampleId;

    @Column(name="iyakuhincode")
    private Integer iyakuhincode;

    @Column(name="master_valid_from")
    private String masterValidFrom;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="usage")
    private String usage;

    @Column(name="days")
    private Integer days;

    @Column(name="category")
    private Integer category;

    @Column(name="comment")
    private String comment;

    public Integer getPrescExampleId() {
        return prescExampleId;
    }

    public void setPrescExampleId(Integer prescExampleId) {
        this.prescExampleId = prescExampleId;
    }

    public Integer getIyakuhincode() {
        return iyakuhincode;
    }

    public void setIyakuhincode(Integer iyakuhincode) {
        this.iyakuhincode = iyakuhincode;
    }

    public String getMasterValidFrom() {
        return masterValidFrom;
    }

    public void setMasterValidFrom(String masterValidFrom) {
        this.masterValidFrom = masterValidFrom;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
