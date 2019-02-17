package jp.chang.myclinic.practice.javafx.drug.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class DrugInputState extends DrugInputBaseState {

    private String comment = "";
    private boolean commentVisible = false;
    private String tekiyou = "";
    private boolean tekiyouVisible = false;

    public DrugInputState(){
        this(new DrugInputBaseState());
    }

    public DrugInputState(DrugInputBaseState baseState){
        baseState.assignTo(this);
    }

    void assignTo(DrugInputState dst){
        super.assignTo(dst);
        dst.comment = comment;
        dst.commentVisible = commentVisible;
        dst.tekiyou = tekiyou;
        dst.tekiyouVisible = tekiyouVisible;
    }

    public DrugInputState copy(){
        DrugInputState dst = new DrugInputState();
        assignTo(dst);
        return dst;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isCommentVisible() {
        return commentVisible;
    }

    public void setCommentVisible(boolean commentVisible) {
        this.commentVisible = commentVisible;
    }

    public String getTekiyou() {
        return tekiyou;
    }

    public void setTekiyou(String tekiyou) {
        this.tekiyou = tekiyou;
    }

    public boolean isTekiyouVisible() {
        return tekiyouVisible;
    }

    public void setTekiyouVisible(boolean tekiyouVisible) {
        this.tekiyouVisible = tekiyouVisible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrugInputState)) return false;
        if (!super.equals(o)) return false;
        DrugInputState that = (DrugInputState) o;
        return commentVisible == that.commentVisible &&
                tekiyouVisible == that.tekiyouVisible &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(tekiyou, that.tekiyou);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), comment, commentVisible, tekiyou, tekiyouVisible);
    }

    @Override
    public String toString() {
        return "DrugInputState{" +
                "comment='" + comment + '\'' +
                ", commentVisible=" + commentVisible +
                ", tekiyou='" + tekiyou + '\'' +
                ", tekiyouVisible=" + tekiyouVisible +
                "} " + super.toString();
    }
}
