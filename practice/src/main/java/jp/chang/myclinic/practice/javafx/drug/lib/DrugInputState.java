package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;

import java.util.Objects;

public class DrugInputState extends DrugInputBaseState {

    private String comment = "";
    private boolean commentVisible = false;
    private String tekiyou = "";
    private boolean tekiyouVisible = false;

    DrugInputState(){
        this(new DrugInputBaseState());
    }

    DrugInputState(DrugInputBaseState baseState){
        baseState.assignTo(this);
    }

    void assignTo(DrugInputState dst){
        super.assignTo(dst);
        assignProperTo(dst);
    }

    private void assignProperTo(DrugInputState dst){
        dst.comment = comment;
        dst.commentVisible = commentVisible;
        dst.tekiyou = tekiyou;
        dst.tekiyouVisible = tekiyouVisible;
    }

    @Override
    void clear(){
        super.clear();
        DrugInputState src = new DrugInputState();
        src.assignProperTo(this);
    }

    public DrugInputState copy(){
        DrugInputState dst = new DrugInputState();
        assignTo(dst);
        return dst;
    }

    void adapt(){
        setCommentVisible(comment != null && !comment.isEmpty());
        setTekiyouVisible(tekiyou != null && !tekiyou.isEmpty());
    }

    @Override
    void setMaster(IyakuhinMasterDTO master){
        super.setMaster(master);
        setComment("");
        setTekiyou("");
        adapt();
    }

    void setPrescExample(PrescExampleFullDTO example){
        super.setPrescExample(example);
        String comment = example.prescExample.comment;
        setComment(comment == null ? "" : comment);
        setTekiyou("");
        adapt();
    }

    void setDrugAttr(DrugAttrDTO attr){
        String tekiyou = null;
        if( attr != null ){
            tekiyou = attr.tekiyou;
        }
        setTekiyou(tekiyou == null ? "" : tekiyou);
        adapt();
    }

    String getComment() {
        return comment;
    }

    void setComment(String comment) {
        this.comment = comment;
    }

    boolean isCommentVisible() {
        return commentVisible;
    }

    void setCommentVisible(boolean commentVisible) {
        this.commentVisible = commentVisible;
    }

    String getTekiyou() {
        return tekiyou;
    }

    void setTekiyou(String tekiyou) {
        this.tekiyou = tekiyou;
    }

    boolean isTekiyouVisible() {
        return tekiyouVisible;
    }

    void setTekiyouVisible(boolean tekiyouVisible) {
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
