package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CheckBoxList<T> extends VBox {

    private static Logger logger = LoggerFactory.getLogger(CheckBoxList.class);

    private List<CheckBoxWithData<T>> checkBoxes = new ArrayList<>();
    private Function<T, String> labelMaker;

    public CheckBoxList(Function<T, String> labelMaker) {
        super(4);
        this.labelMaker = labelMaker;
    }

    public CheckBoxList(List<T> list, Function<T,String> labelMaker){
        this(labelMaker);
        addAll(list);
    }

    public void addAll(List<T> dataList){
        List<CheckBoxWithData<T>> checks = dataList.stream()
                .map(data -> {
                    CheckBoxWithData<T> c = new CheckBoxWithData<T>(labelMaker.apply(data), data);
                    checkBoxes.add(c);
                    return c;
                })
                .collect(Collectors.toList());
        getChildren().addAll(checks);
    }

    public void forEachCheckBox(Consumer<CheckBoxWithData<T>> f){
        checkBoxes.forEach(f);
    }

    public List<T> getSelected(){
        return checkBoxes.stream().filter(CheckBox::isSelected).map(CheckBoxWithData::getData)
                .collect(Collectors.toList());
    }

}
