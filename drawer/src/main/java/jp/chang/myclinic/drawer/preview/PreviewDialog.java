package jp.chang.myclinic.drawer.preview;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
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
    private static final Logger logger = LoggerFactory.getLogger(PreviewDialog.class);

    private PreviewPane previewPane = new PreviewPane(14.8, 21.0);
    private List<List<Op>> pages = new ArrayList<>();
    private PrintManager printManager;
    private String settingName;

    public PreviewDialog(Window owner, String title){
        this(owner, title, new PrintManager(null), null);
    }

    public PreviewDialog(Window owner, String title, PrintManager printManager, String settingName){
        super(owner, title, ModalityType.DOCUMENT_MODAL);
        this.printManager = printManager;
        this.settingName = settingName;
        if( printManager != null ){
            setupMenu();
        }
        setLayout(new MigLayout("", "[grow]", "[grow]"));
        CommandBox commandBox = new CommandBox();
        commandBox.setCallback(new CommandBox.Callback() {
            @Override
            public void onPrint() {
                doPrint();
            }
        });
        JScrollPane previewScroll = new JScrollPane(previewPane);
        add(commandBox, "wrap");
        add(previewScroll, "grow");
    }

    public PreviewDialog setPageSize(double width, double height){
        previewPane.setPageSize(width, height);
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

    private void setupMenu(){
        JMenuBar bar = new JMenuBar();
        bar.add(makeManageMenu("印刷設定"));
        setJMenuBar(bar);
    }

    private JMenu makeManageMenu(String label){
        JMenu menu = new JMenu(label);
        {
            JMenuItem item = new JMenuItem("印刷設定の新規作成");
            item.addActionListener(evt -> doNewSetting());
            menu.add(item);
        }
        return menu;
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
