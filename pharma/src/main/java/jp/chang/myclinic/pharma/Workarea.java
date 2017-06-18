package jp.chang.myclinic.pharma;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawer;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawerData;
import jp.chang.myclinic.drawer.presccontent.PrescContentDrawer;
import jp.chang.myclinic.drawer.presccontent.PrescContentDrawerData;
import jp.chang.myclinic.drawer.swing.DrawerPreviewDialog;
import jp.chang.myclinic.drawer.techou.TechouDrawer;
import jp.chang.myclinic.drawer.techou.TechouDrawerData;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DrugUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static java.awt.Font.BOLD;

public class Workarea extends JPanel {

    private JLabel nameLabel = new JLabel("");
    private JLabel yomiLabel = new JLabel("");
    private JLabel patientInfoLabel = new JLabel("");
    private JPanel drugsContainer = new JPanel();
    private JButton printPrescButton = new JButton("処方内容印刷");
    private JButton printDrugBagButton = new JButton("薬袋印刷");
    private JButton printTechouButton = new JButton("薬手帳印刷");
    private JButton printAllButton = new JButton("*全部印刷*");
    private JButton printAllExceptTechouButton = new JButton("*全部印刷(薬手帳なし)*");
    private JButton cancelButton = new JButton("キャンセル");
    private JButton doneButton = new JButton("薬渡し終了");
    private PatientDTO patient;
    private List<DrugFullDTO> drugs = Collections.emptyList();

    public Workarea(){
        setupNameLabel();
        setLayout(new MigLayout("gapy 0", "", ""));
        add(nameLabel, "gap top 0, wrap");
        add(yomiLabel, "gap top 5, wrap");
        add(patientInfoLabel, "wrap");
        drugsContainer.setLayout(new MigLayout("insets 0, gapy 1", "", ""));
        add(drugsContainer, "wrap");
        add(makeCommandRow1(), "wrap");
        add(makeCommandRow2(), "wrap");
        add(makeCommandRow3(), "right");
        bind();
    }

    public void update(PatientDTO patient, List<DrugFullDTO> drugs){
        this.patient = patient;
        this.drugs = drugs;
        nameLabel.setText(patient.lastName + patient.firstName);
        yomiLabel.setText(patient.lastNameYomi + patient.firstNameYomi);
        LocalDate birthday = DateTimeUtil.parseSqlDate(patient.birthday);
        String infoLabel = String.format("患者番号 %d %s生 %d才 %s性", patient.patientId,
                DateTimeUtil.toKanji(birthday),
                DateTimeUtil.calcAge(birthday),
                "M".equals(patient.sex) ? "男" : "女");
        patientInfoLabel.setText(infoLabel);
        drugsContainer.removeAll();
        int index = 1;
        for(DrugFullDTO drugFull: drugs){
            String text = (index++) + ") " + DrugUtil.drugRep(drugFull);
            WrappedText wrap = new WrappedText(200, text);
            JLabel bagLink = new JLabel("薬袋");
            bagLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            bagLink.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    doPreviewDrugBag(drugFull, patient);
                }
            });
            bagLink.setForeground(Color.BLUE);
            bagLink.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            wrap.append(bagLink);
            // TODO: add 'prescribed' label
            drugsContainer.add(wrap, "wrap");
        }
        repaint();
        revalidate();
    }

    private void setupNameLabel(){
        Font baseFont = nameLabel.getFont();
        Font font = baseFont.deriveFont(BOLD, (int)(baseFont.getSize() * 1.2));
        nameLabel.setFont(font);
    }

    private JComponent makeCommandRow1(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(printPrescButton);
        panel.add(printDrugBagButton);
        panel.add(printTechouButton);
        return panel;
    }

    private JComponent makeCommandRow2(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(printAllButton);
        panel.add(printAllExceptTechouButton);
        return panel;
    }

    private JComponent makeCommandRow3(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(cancelButton);
        panel.add(doneButton);
        return panel;
    }

    private void bind(){
        printPrescButton.addActionListener(event -> doPrintPrescContent());
        printTechouButton.addActionListener(event -> doPrintTechou());
    }

    private void doPreviewDrugBag(DrugFullDTO drugFull, PatientDTO patient){
        final DataStore dataStore = new DataStore();
        Service.api.getPharmaDrug(drugFull.drug.iyakuhincode)
                .thenCompose(pharmaDrug -> {
                    dataStore.pharmaDrug = pharmaDrug;
                    return Service.api.getClinicInfo();
                })
                .thenAccept(clinicInfo -> {
                    EventQueue.invokeLater(() -> {
                        DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(null, "薬袋印刷プレビュー", false);
                        previewDialog.setImageSize(128, 182);
                        previewDialog.setPrinterSetting(PharmaConfig.INSTANCE.getDrugbagPrinterSetting());
                        previewDialog.setLocationByPlatform(true);
                        DrugBagDataCreator dataCreator = new DrugBagDataCreator(drugFull, patient, dataStore.pharmaDrug, clinicInfo);
                        DrugBagDrawerData data = dataCreator.createData();
                        List<Op> ops = new DrugBagDrawer(data).getOps();
                        previewDialog.render(ops);
                        previewDialog.setVisible(true);
                    });
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    private static class DataStore {
        PharmaDrugDTO pharmaDrug;
    }

    private void doPrintPrescContent(){
        LocalDate prescDate = LocalDate.now();
        PrescContentDataCreator creator = new PrescContentDataCreator(patient, prescDate, drugs);
        PrescContentDrawerData data = creator.createData();
        PrescContentDrawer drawer = new PrescContentDrawer(data);
        DrawerPreviewDialog dialog = new DrawerPreviewDialog(SwingUtilities.getWindowAncestor(this), "処方内容の印刷", true);
        dialog.setImageSize(drawer.getPageWidth(), drawer.getPageHeight());
        dialog.render(drawer.getOps());
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
    }

    private void doPrintTechou(){
        Service.api.getClinicInfo()
                .thenAccept(clinicInfo -> {
                    LocalDate prescDate = LocalDate.now();
                    TechouDataCreator creator = new TechouDataCreator(patient, prescDate, drugs, clinicInfo);
                    TechouDrawerData data = creator.createData();
                    TechouDrawer drawer = new TechouDrawer(data);
                    List<Op> ops = drawer.getOps();
                    EventQueue.invokeLater(() -> {
                        DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(null, "お薬手帳印刷プレビュー", false);
                        previewDialog.setImageSize(drawer.getPageWidth(), drawer.getPageHeight());
                        previewDialog.setPrinterSetting(PharmaConfig.INSTANCE.getDrugbagPrinterSetting());
                        previewDialog.setLocationByPlatform(true);
                        previewDialog.render(ops);
                        previewDialog.setVisible(true);
                    });
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });

    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
