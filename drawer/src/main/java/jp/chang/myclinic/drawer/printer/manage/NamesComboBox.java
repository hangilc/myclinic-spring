package jp.chang.myclinic.drawer.printer.manage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by hangil on 2017/05/31.
 */
public class NamesComboBox extends JComboBox<String> {

    public NamesComboBox(Path settingDir, String currentSetting) throws IOException {
        super(new String[]{});
        PrinterSetting printerSetting = new PrinterSetting(settingDir);
        List<String> names = printerSetting.listNames();
        for (String name : names) {
            addItem(name);
        }
        for (String name : names) {
            if (name.equals(currentSetting)) {
                setSelectedItem(name);
                break;
            }
        }
        String proto = maxName(names);
        if( proto != null ){
            setPrototypeDisplayValue(proto + "  ");
        }
    }

    public String getSelectedName(){
        int index = getSelectedIndex();
        if( index >= 0 ){
            return getItemAt(index);
        } else {
            return null;
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
