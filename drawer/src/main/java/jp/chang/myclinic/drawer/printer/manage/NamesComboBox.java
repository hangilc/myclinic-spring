package jp.chang.myclinic.drawer.printer.manage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

public class NamesComboBox extends JComboBox<String> {

    public NamesComboBox() {
        super(new String[]{});
        load();
    }

    public String getSelectedName(){
        int index = getSelectedIndex();
        if( index >= 0 ){
            return getItemAt(index);
        } else {
            return null;
        }
    }

    public void reload() {
        String current = getSelectedName();
        removeAllItems();
        load();
    }

    public void load() {
        PrinterSetting printerSetting = PrinterSetting.INSTANCE;
        try {
            List<String> names = printerSetting.listNames();
            for (String name : names) {
                addItem(name);
            }
            String proto = maxName(names);
            if (proto != null) {
                setPrototypeDisplayValue(proto + "  ");
            }
        } catch(IOException ex){
            ex.printStackTrace();
            throw new UncheckedIOException(ex);
        }
    }

    private String maxName(List<String> names){
        Font font = getFont();
        FontMetrics metrics = getFontMetrics(font);
        String curName = null;
        int curSize = 0;
        for(String name: names){
            int w = metrics.stringWidth(name);
            if( w > curSize ){
                curName = name;
                curSize = w;
            }
        }
        return curName;
    }

}
