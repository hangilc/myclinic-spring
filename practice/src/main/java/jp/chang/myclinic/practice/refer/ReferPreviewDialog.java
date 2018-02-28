package jp.chang.myclinic.practice.refer;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.manager.PrinterEnv;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

public class ReferPreviewDialog extends JDialog {

    private PreviewPart previewPart = new PreviewPart();
    private EditorPart editorPart = new EditorPart();

    public ReferPreviewDialog(PrinterEnv printManager, String settingName){
        setTitle("紹介状のプレビュー");
        setLayout(new MigLayout("", "[] [grow]", "[grow]"));
        add(previewPart, "top");
        add(editorPart, "top, grow");
        pack();
    }

    public void render(List<Op> ops){
        previewPart.render(ops);
    }
}
