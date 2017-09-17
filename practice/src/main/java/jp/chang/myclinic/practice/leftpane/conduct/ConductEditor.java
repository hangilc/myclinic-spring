package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.practice.leftpane.WorkArea;
import jp.chang.myclinic.practice.leftpane.conduct.adddrug.AddConductDrugForm;
import jp.chang.myclinic.practice.leftpane.conduct.addkizai.AddConductKizaiForm;
import jp.chang.myclinic.practice.leftpane.conduct.addshinryou.AddConductShinryouForm;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.KizaiUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

class ConductEditor extends JPanel {

    interface Callback {
        default void onModified(ConductFullDTO modified){}
        default void onDelete(){}
        default void onClose(ConductFullDTO conductFull){}
    }

    private int width;
    private ConductFullDTO conductFull;
    private VisitDTO visit;
    private JComboBox<ConductKind> kindCombo;
    private Container subWorkAreaContainer;
    private Callback callback = new Callback(){};

    ConductEditor(int width, ConductFullDTO conductFull, VisitDTO visit){
        this.width = width;
        this.conductFull = conductFull;
        this.visit = visit;
        setLayout(new MigLayout("insets 0, hidemode 3", String.format("[%dpx!]", width), ""));
        subWorkAreaContainer = new JPanel(new MigLayout("insets 0", "", ""));
        subWorkAreaContainer.setVisible(false);
        add(makeSubmenu(), "wrap");
        add(subWorkAreaContainer, "growx, wrap");
        add(makeKindArea(), "wrap");
        add(makeGazouLabelArea(), "wrap");
        add(makeShinryouArea(), "wrap");
        add(makeDrugArea(), "wrap");
        add(makeKizaiArea(), "wrap");
        add(makeCommandBox(), "growx");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private Component makeSubmenu(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        Link addShinryouLink = new Link("診療行為の追加");
        addShinryouLink.setCallback(evt -> doAddShinryou(panel));
        Link addDrugLink = new Link("薬剤の追加");
        addDrugLink.setCallback(evt -> doAddDrug(panel));
        Link addKizaiLink = new Link("器材の追加");
        addKizaiLink.setCallback(evt -> doAddKizai(panel));
        panel.add(addShinryouLink);
        panel.add(new JLabel("|"));
        panel.add(addDrugLink);
        panel.add(new JLabel("|"));
        panel.add(addKizaiLink);
        return panel;
    }

    private Component makeKindArea(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        this.kindCombo = makeKindCombo();
        kindCombo.setSelectedItem(ConductKind.fromCode(conductFull.conduct.kind));
        panel.add(new JLabel("種類："));
        panel.add(kindCombo);
        return panel;
    }

    private JComboBox<ConductKind> makeKindCombo(){
        JComboBox<ConductKind> combo = new JComboBox<>(ConductKind.values());
        combo.setRenderer(new ListCellRenderer<ConductKind>(){
            @Override
            public Component getListCellRendererComponent(JList<? extends ConductKind> list, ConductKind value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                return new JLabel(value.getKanjiRep());
            }
        });
        return combo;
    }

    private String[] getConductKindLabels(){
        return Arrays.stream(ConductKind.values()).map(ConductKind::getKanjiRep).toArray(String[]::new);
    }

    private Component makeGazouLabelArea(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JTextField textField = new JTextField(10);
        textField.setText(getGazouLabelString());
        panel.add(new JLabel("画像ラベル："));
        panel.add(textField);
        return panel;
    }

    private String getGazouLabelString(){
        GazouLabelDTO gazouLabelDTO = conductFull.gazouLabel;
        if( gazouLabelDTO != null ){
            return gazouLabelDTO.label;
        } else {
            return "";
        }
    }

    private Component makeShinryouArea(){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapx 0", "", ""));
        List<ConductShinryouFullDTO> shinryouList = conductFull.conductShinryouList;
        if( shinryouList.size() == 0 ){
            panel.setVisible(false);
        } else {
            for(int i=0;i<shinryouList.size();i++){
                ConductShinryouFullDTO shinryou = shinryouList.get(i);
                Link deleteLink = new Link("削除");
                int rightWidth = deleteLink.getPreferredSize().width;
                int leftWidth = width - rightWidth - 6;
                WrappedText text = new WrappedText(leftWidth, shinryou.master.name);
                panel.add(text, "gapright 4");
                panel.add(deleteLink, i == shinryouList.size() - 1 ? "" : "wrap");
            };
        }
        return panel;
    }

    private Component makeDrugArea(){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapx 0", "", ""));
        List<ConductDrugFullDTO> drugs = conductFull.conductDrugs;
        if( drugs.size() == 0 ){
            panel.setVisible(false);
        } else {
            for(int i=0;i<drugs.size();i++) {
                ConductDrugFullDTO drug = drugs.get(i);
                Link deleteLink = new Link("削除");
                int rightWidth = deleteLink.getPreferredSize().width;
                int leftWidth = width - rightWidth - 6;
                String rep = DrugUtil.conductDrugRep(drug);
                WrappedText text = new WrappedText(leftWidth, rep);
                panel.add(text, "gapright 4");
                panel.add(deleteLink, i == drugs.size() - 1 ? "" : "wrap");
            };
        }
        return panel;
    }

    private Component makeKizaiArea(){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapx 0", "", ""));
        List<ConductKizaiFullDTO> kizaiList = conductFull.conductKizaiList;
        if( kizaiList.size() == 0 ){
            panel.setVisible(false);
        } else {
            for(int i = 0;i<kizaiList.size();i++) {
                ConductKizaiFullDTO kizai = kizaiList.get(i);
                Link deleteLink = new Link("削除");
                int rightWidth = deleteLink.getPreferredSize().width;
                int leftWidth = width - rightWidth - 6;
                String rep = KizaiUtil.kizaiRep(kizai);
                WrappedText text = new WrappedText(leftWidth, rep);
                panel.add(text, "gapright 4");
                panel.add(deleteLink, i == kizaiList.size() - 1 ? "" : "wrap");
            };
        }
        return panel;
    }

    private Component makeCommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 2", "", ""));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JButton closeButton = new JButton("閉じる");
        closeButton.addActionListener(event -> callback.onClose(conductFull));
        Link deleteLink = new Link("削除");
        deleteLink.setCallback(event -> doDelete());
        panel.add(closeButton);
        panel.add(deleteLink);
        return panel;
    }

    private void doAddShinryou(Container submenu){
        WorkArea wa = new WorkArea(width, "診療行為の追加");
        String at = visit.visitedAt.substring(0, 10);
        AddConductShinryouForm form = new AddConductShinryouForm(wa.getInnerColumnWidth(), at,
                conductFull.conduct.conductId);
        form.setCallback(new AddConductShinryouForm.Callback() {
            @Override
            public void onEntered(ConductShinryouFullDTO entered) {
                doModified();
            }

            @Override
            public void onCancel() {
                removeSubWorkArea(wa);
            }
        });
        wa.setComponent(form);
        addSubWorkArea(wa, submenu);
    }

    private void doAddDrug(Container submenu){
        WorkArea wa = new WorkArea(width, "薬剤の追加");
        String at = visit.visitedAt.substring(0, 10);
        AddConductDrugForm form = new AddConductDrugForm(wa.getInnerColumnWidth(), at,
                conductFull.conduct.conductId);
        form.setCallback(new AddConductDrugForm.Callback(){
            @Override
            public void onEntered() {
                doModified();
            }

            @Override
            public void onCancel() {
                removeSubWorkArea(wa);
            }
        });
        wa.setComponent(form);
        addSubWorkArea(wa, submenu);
    }

    private void doAddKizai(Container submenu){
        WorkArea wa = new WorkArea(width, "器材の追加");
        String at = visit.visitedAt.substring(0, 10);
        AddConductKizaiForm form = new AddConductKizaiForm(wa.getInnerColumnWidth(), at,
                conductFull.conduct.conductId);
        form.setCallback(new AddConductKizaiForm.Callback(){
            @Override
            public void onEntered() {
                doModified();
            }

            @Override
            public void onCancel() {
                removeSubWorkArea(wa);
            }
        });
        wa.setComponent(form);
        addSubWorkArea(wa, submenu);
    }

    private void addSubWorkArea(WorkArea wa, Container submenu){
        Container parent = subWorkAreaContainer;
        parent.add(wa, "newline");
        parent.setVisible(true);
        parent.revalidate();
        parent.repaint();
    }

    private void removeSubWorkArea(WorkArea wa){
        subWorkAreaContainer.remove(wa);
        if( subWorkAreaContainer.getComponents().length == 0 ){
            subWorkAreaContainer.setVisible(false);
        }
        revalidate();
        repaint();
    }

    private void doModified(){
        Service.api.getConductFull(conductFull.conduct.conductId)
                .thenAccept(modified -> EventQueue.invokeLater(() -> callback.onModified(modified)))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void doDelete(){
        if( !confirm("この処置を削除していいですか？") ){
            return;
        }
        Service.api.deleteConduct(conductFull.conduct.conductId)
                .thenAccept(res -> EventQueue.invokeLater(()-> callback.onDelete()))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private boolean confirm(String message){
        int choice = JOptionPane.showConfirmDialog(this, message, "確認", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
