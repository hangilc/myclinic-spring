package jp.chang.myclinic.practice.javafx.parts;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;

public interface FormSearchPart<S> {
    ObjectProperty<S> selectedItemProperty();
    Node asNode();
}
