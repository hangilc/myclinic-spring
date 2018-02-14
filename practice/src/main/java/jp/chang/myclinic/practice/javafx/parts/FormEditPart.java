package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.Node;

import java.util.List;

public interface FormEditPart<M> {
    void setModel(M model);
    List<String> stuffInto(M model);
    Node asNode();
}
