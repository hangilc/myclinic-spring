package jp.chang.myclinic.drawer.preview;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.lib.Link;
import jp.chang.myclinic.drawer.printer.manager.PrintManager;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PreviewDialog extends JDialog {

    public interface Callback {
        default void onRememberSetting(String settingName){}
    }

    private static final Logger logger = LoggerFactory.getLogger(PreviewDialog.class);

    private PreviewPane previewPane = new PreviewPane(14.8, 21.0);
    private List<List<Op>> pages = new ArrayList<>();
    private PrintManager printManager;
    private String settingName;
    private Callback callback = new Callback(){};

    public PreviewDialog(JDialog owner, String title, PrintManager printManager, String settingName){
        super(owner, title, ModalityType.DOCUMENT_MODAL);
        setupDialog(title, printManager, settingName);
    }

    public PreviewDialog(Window owner, String title, PrintManager printManager, String settingName){
        super(owner, title, ModalityType.DOCUMENT_MODAL);
        setupDialog(title, printManager, settingName);
    }

    private void setupDialog(String title, PrintManager printManager, String settingName){
        this.printManager = printManager;
        this.settingName = settingName;
        setLayout(new MigLayout("insets 4", "[grow]", "[] [grow] []"));
        CommandBox commandBox = new CommandBox();
        commandBox.setCallback(new CommandBox.Callback() {
            @Override
            public void onPrint() {
                doPrint();
            }
        });
        JScrollPane previewScroll = new JScrollPane(previewPane);
        BottomBox bottomBox = new BottomBox(settingName, printManager);
        bottomBox.setCallback(new BottomBox.Callback() {
            @Override
            public void onRememberSetting(String settingName) {
                callback.onRememberSetting(settingName);
            }

            @Override
            public void onSelectionChange(String newSettingName) {
                setSettingName(newSettingName);
            }
        });
        add(commandBox, "wrap");
        add(previewScroll, "grow, wrap");
        add(bottomBox, "growx");
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void setSettingName(String settingName){
        this.settingName = settingName;
    }

    public PreviewDialog setPageSize(double width, double height){
        previewPane.setPageSize(width, height);
        return this;
    }

    public PreviewDialog setPageSize(PaperSize pageSize){
        setPageSize(pageSize.getWidth(), pageSize.getHeight());
        return this;
    }

    public PreviewDialog setScale(double scale){
        previewPane.setScale(scale);
        return this;
    }

    public PreviewDialog setPages(List<List<Op>> pages){
        this.pages = pages;
        if( pages.size() > 0 ){
            List<Op> ops = pages.get(0);
            previewPane.setOps(ops);
        }
        return this;
    }

    public PreviewDialog setPage(List<Op> ops){
        List<List<Op>> pages = new ArrayList<>();
        pages.add(ops);
        return setPages(pages);
    }

    private void doPrint(){
        printManager.print(pages, settingName);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
