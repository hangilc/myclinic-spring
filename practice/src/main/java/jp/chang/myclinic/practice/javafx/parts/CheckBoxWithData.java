package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.control.CheckBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckBoxWithData<T> extends CheckBox {

    private static Logger logger = LoggerFactory.getLogger(CheckBoxWithData.class);

    private T data;

    public CheckBoxWithData(String label, T data) {
        this.data = data;
        setWrapText(true);
    }

    public T getData() {
        return data;
    }

}
