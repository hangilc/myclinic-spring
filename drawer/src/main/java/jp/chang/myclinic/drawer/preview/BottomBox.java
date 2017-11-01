package jp.chang.myclinic.drawer.preview;

import jp.chang.myclinic.drawer.lib.Link;
import jp.chang.myclinic.drawer.preview.manage.ManageDialog;
import jp.chang.myclinic.drawer.printer.manager.PrintManager;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

class BottomBox extends JPanel {

    interface Callback {
        default void onSelectionChange(String newSettingName){}
    }

    private static final Logger logger = LoggerFactory.getLogger(BottomBox.class);

    private String settingName;
    private PrintManager printManager;
    private JLabel nameLabel;
    private Callback callback = new Callback(){};

    BottomBox(String settingName, PrintManager printManager){
        this.settingName = settingName;
        this.printManager = printManager;
        setLayout(new MigLayout("insets 0", "", ""));
        nameLabel = new JLabel(makeSettingLabel());
        Link selectLink = new Link("選択");
        selectLink.setCallback(this::doSelect);
        Link createLink = new Link("新規");
        createLink.setCallback(evt -> doNewSetting());
        Link manageLink = new Link("管理");
        manageLink.setCallback(evt -> doManage());
        add(new JLabel("印刷設定："));
        add(nameLabel);
        add(selectLink, "gap right 12");
        add(createLink);
        add(manageLink);
    }

    void setCallback(Callback callback){
        this.callback = callback;
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
                item.addActionListener(e -> {
                    settingName = name;
                    nameLabel.setText(name);
                    callback.onSelectionChange(name);
                });
                popup.add(item);
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

    private void doManage(){
        if( printManager == null ){
            return;
        }
        try {
            List<String> names = printManager.listNames();
            if( names.size() == 0 ){
                alert("登録されている印刷設定がありません。");
                return;
            }
            ManageDialog manageDialog = new ManageDialog(printManager, names, settingName);
            manageDialog.setLocationByPlatform(true);
            manageDialog.setVisible(true);
        } catch(IOException ex){
            logger.error("Failed to list print setting names.", ex);
            alert("設定名リストの取得に失敗しました。");
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
