package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.preview.PreviewDialog;
import jp.chang.myclinic.drawer.printer.manager.PrintManager;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.shohousen.ShohousenData;
import jp.chang.myclinic.practice.shohousen.ShohousenDrawer;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TextEditor extends JPanel {

    public interface Callback {
        default void onEnter(TextDTO newText){}
        default void onDelete(){}
        default void onCancel(){}
    }

    private TextDTO textDTO;
    private JEditorPane ep;
    private Callback callback = new Callback(){};

    TextEditor(TextDTO textDTO, int width){
        this.textDTO = textDTO;
        setLayout(new MigLayout("insets 0, fill", String.format("[%dpx!]", width), ""));
        ep = new JEditorPane("text/plain", textDTO.content);
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> doEnter());
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> TextAreaContext.get(this).onEditorCancel(this));
        Link deleteLink = new Link("削除", event -> doDelete());
        Link prescLink = new Link("処方箋発行", event -> doPresc());
        Link copyLink = new Link("コピー", event -> {});
        add(new JScrollPane(ep), "growx, h 200:n:n, wrap");
        add(enterButton, "split 2");
        add(cancelButton, "wrap");
        add(deleteLink, "split 3");
        add(prescLink);
        add(copyLink);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private void doEnter(){
        TextDTO newText = textDTO.copy();
        newText.content = ep.getText();
        Service.api.updateText(newText)
                .thenCompose(res -> Service.api.getText(newText.textId))
                .thenAccept(enteredText -> {
                    TextAreaContext.get(this).onTextUpdated(enteredText, this);
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void doPresc(){
        PrintManager pringManager = new PrintManager(PracticeEnv.INSTANCE.getPrinterSettingsDir());
        PreviewDialog previewDialog = new PreviewDialog(SwingUtilities.getWindowAncestor(this), "処方箋印刷", pringManager, null);
        ShohousenData shohousenData = new ShohousenData();
        shohousenData.setClinicInfo(PracticeEnv.INSTANCE.getClinicInfo());
        ShohousenDrawer shohousenDrawer = new ShohousenDrawer();
        PrescData.fetch(textDTO)
                .thenAccept(prescData -> {
                    LocalDate visitedAt = LocalDate.parse(prescData.getVisit().visitedAt.substring(0, 10));
                    shohousenData.setHoken(prescData.getHoken());
                    shohousenData.setPatient(prescData.getPatient());
                    shohousenData.setFutanWari(prescData.getHoken(), prescData.getPatient(), visitedAt);
                    shohousenData.setKoufuDate(visitedAt);
                    shohousenData.setValidUptoDate(visitedAt.plusDays(3));
                    shohousenData.setDrugs(textDTO.content);
                    shohousenData.applyTo(shohousenDrawer);
                    List<Op> ops = shohousenDrawer.getOps();
                    EventQueue.invokeLater(() -> {
                        previewDialog.setPageSize(PaperSize.A5);
                        previewDialog.setPage(ops);
                        previewDialog.pack();
                        previewDialog.setLocationByPlatform(true);
                        previewDialog.setVisible(true);
                    });
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void doDelete(){
        int select = JOptionPane.showConfirmDialog(this, "この文章を削除していいですか？", "確認",
                JOptionPane.YES_NO_OPTION);
        if( select == JOptionPane.YES_OPTION ){
            Service.api.deleteText(textDTO.textId)
                    .thenAccept(res -> EventQueue.invokeLater(() ->{
                        TextAreaContext.get(this).onTextDeleted(this, textDTO.textId);
                    }))
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
