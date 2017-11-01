package jp.chang.myclinic.drawer.preview;

import jp.chang.myclinic.drawer.lib.Link;
import jp.chang.myclinic.drawer.printer.manager.PrintManager;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

class BottomBox extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(BottomBox.class);

    private String settingName;
    private PrintManager printManager;
    private JLabel nameLabel;

    BottomBox(String settingName, PrintManager printManager){
        this.settingName = settingName;
        this.printManager = printManager;
        setLayout(new MigLayout("insets 0", "", ""));
        nameLabel = new JLabel(makeSettingLabel());
        Link selectLink = new Link("選択");
        selectLink.setCallback(this::doSelect);
        Link createLink = new Link("新規");
        createLink.setCallback(evt -> doNewSetting());
        add(new JLabel("印刷設定："));
        add(nameLabel);
        add(selectLink, "gap right 12");
        add(createLink);
    }

    private String makeSettingLabel(){
        if( settingName == null ){
            return "(未選択)";
        } else {
            return settingName;
        }
    }

    private void doSelect(MouseEvent evt){
        if( printManager == null ){
            return;
        }
        JPopupMenu popup = new JPopupMenu();
        try {
            printManager.listNames().forEach(name -> {
                JMenuItem item = new JMenuItem(name);
                popup.add(name);
            });
            popup.show(this, evt.getX(), evt.getY());
        } catch(IOException ex){
            logger.error("Failed to list setting names.", ex);
            alert("設定名リストの取得に失敗しました。");
        }
    }

    private void doNewSetting(){
        if( printManager == null ){
            alert("No printManager supplied");
            return;
        }
        String name = JOptionPane.showInputDialog(this, "新しい設定の名前");
        if( name == null ){
            return;
        }
        try {
            printManager.createNewSetting(name);
        } catch(PrintManager.SettingDirNotSuppliedException ex){
            logger.error("Setting dir not specified", ex);
            alert("Setting dir is not specified.");
        } catch(IOException ex){
            logger.error("", ex);
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
