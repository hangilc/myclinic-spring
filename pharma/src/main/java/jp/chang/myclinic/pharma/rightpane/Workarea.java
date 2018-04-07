package jp.chang.myclinic.pharma.rightpane;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawer;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawerData;
import jp.chang.myclinic.drawer.presccontent.PrescContentDrawer;
import jp.chang.myclinic.drawer.presccontent.PrescContentDrawerData;
import jp.chang.myclinic.drawer.printer.manage.PrinterSetting;
import jp.chang.myclinic.drawer.swing.DrawerPreviewDialog;
import jp.chang.myclinic.drawer.techou.TechouDrawer;
import jp.chang.myclinic.drawer.techou.TechouDrawerData;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.pharma.*;
import jp.chang.myclinic.pharma.swing.DrugBagDataCreator;
import jp.chang.myclinic.pharma.swing.PharmaConfig;
import jp.chang.myclinic.pharma.swing.PrescContentDataCreator;
import jp.chang.myclinic.pharma.swing.TechouDataCreator;
import jp.chang.myclinic.pharma.wrappedtext.Strut;
import jp.chang.myclinic.pharma.wrappedtext.WrappedText;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DrugUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.awt.Font.BOLD;

class Workarea extends JPanel {

    interface Callbacks {
        void onPrescDone();
        void onCancel();
    }

    private PatientDTO patient;
    private List<DrugFullDTO> drugs = Collections.emptyList();
    private Callbacks callbacks;

    Workarea(PatientDTO patient, List<DrugFullDTO> drugs, Callbacks callbacks){
        this.patient = patient;
        this.drugs = drugs;
        this.callbacks = callbacks;
        setLayout(new MigLayout("gapy 0", "[grow]", ""));
        add(makeNameLabel(patient), "gap top 0, wrap");
        add(makeYomiLabel(patient), "gap top 5, wrap");
        add(makePatientInfoLabel(patient), "wrap");
        {
            JPanel drugsContainer = new JPanel();
            drugsContainer.setLayout(new MigLayout("insets 0, gapy 1", "[grow]", ""));
            drugsContainer.add(new Strut(width -> {
                addDrugs(drugsContainer, width - 5, drugs);
                drugsContainer.repaint();
                drugsContainer.revalidate();
            }), "growx");
            add(drugsContainer, "growx, wrap");
        }
        add(makeCommandRow1(), "wrap");
        add(makeCommandRow2(), "gaptop 5, right");
    }

    private JComponent makeNameLabel(PatientDTO patient){
        JLabel nameLabel = new JLabel(patient.lastName + patient.firstName);
        Font baseFont = nameLabel.getFont();
        Font font = baseFont.deriveFont(BOLD, (int)(baseFont.getSize() * 1.2));
        nameLabel.setFont(font);
        return nameLabel;
    }

    private JComponent makeYomiLabel(PatientDTO patient){
        return new JLabel(patient.lastNameYomi + patient.firstNameYomi);
    }

    private JComponent makePatientInfoLabel(PatientDTO patient){
        LocalDate birthday = DateTimeUtil.parseSqlDate(patient.birthday);
        String text = String.format("患者番号 %d %s生 %d才 %s性", patient.patientId,
                DateTimeUtil.toKanji(birthday),
                DateTimeUtil.calcAge(birthday),
                "M".equals(patient.sex) ? "男" : "女");
        return new JLabel(text);
    }

    private void addDrugs(Container container, int width, List<DrugFullDTO> drugs){
        int index = 1;
        for(DrugFullDTO drugFull: drugs){
            String text = (index++) + ") " + DrugUtil.drugRep(drugFull);
            WrappedText wrap = new WrappedText(text, width);
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
            wrap.appendComponent(bagLink);
            if( drugFull.drug.prescribed != 0 ){
                JLabel prescribedLabel = new JLabel(" 処方済 ");
                Font font = prescribedLabel.getFont().deriveFont(BOLD);
                prescribedLabel.setFont(font);
                wrap.appendComponent(prescribedLabel);
            }
            container.add(wrap, "growx, wrap");
        }

    }

    private JComponent makeCommandRow1(){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 1", "", ""));
        {
            JButton printPrescButton = new JButton("処方内容印刷");
            printPrescButton.addActionListener(event -> doPrintPrescContent());
            panel.add(printPrescButton);
        }
        {
            JButton printDrugBagButton = new JButton("薬袋印刷");
            printDrugBagButton.addActionListener(event -> doPrintDrugBag());
            panel.add(printDrugBagButton);
        }
        {
            JButton printTechouButton = new JButton("薬手帳印刷");
            printTechouButton.addActionListener(event -> doPrintTechou());
            panel.add(printTechouButton, "wrap");
        }
        {
            JButton printAllButton = new JButton("*全部印刷*");
            printAllButton.addActionListener(event -> doPrintAll());
            panel.add(printAllButton, "grow");
        }
        {
            JButton printAllExceptTechouButton = new JButton("*全部印刷(薬手帳なし)*");
            printAllExceptTechouButton.addActionListener(event -> doPrintAllExceptTechou());
            panel.add(printAllExceptTechouButton, "span 2, grow");
        }
        return panel;
    }

    private JComponent makeCommandRow2(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        {
            JButton cancelButton = new JButton("キャンセル");
            cancelButton.addActionListener(event -> doCancel());
            panel.add(cancelButton, "sizegroup btn");
        }
        {
            JButton doneButton = new JButton("薬渡し終了");
            doneButton.addActionListener(event -> doPrescDone());
            panel.add(doneButton, "sizegroup btn");
        }
        return panel;
    }

    private void doPrescDone(){
        if( drugs.size() > 0 ){
            int visitId = drugs.get(0).drug.visitId;
            Service.api.prescDone(visitId)
                    .thenAccept(result -> {
                            EventQueue.invokeLater(() -> callbacks.onPrescDone());
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> alert(t.toString()));
                        return null;
                    });
        } else {
            EventQueue.invokeLater(() -> callbacks.onPrescDone());
        }
    }

    private void doCancel(){
        callbacks.onCancel();
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
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private static class DataStore {
        PharmaDrugDTO pharmaDrug;
        ClinicInfoDTO clinicInfo;
    }

    private static class DrugBagOps {
        boolean prescribed;
        List<Op> ops;
    }

    private CompletableFuture<List<DrugBagOps>> composeDrugBagPages(List<DrugFullDTO> drugs) {
        final DataStore dataStore = new DataStore();
        return Service.api.getClinicInfo()
                .thenCompose(clinicInfo -> {
                    dataStore.clinicInfo = clinicInfo;
                    List<Integer> iyakuhincodes = drugs.stream().map(d -> d.drug.iyakuhincode).collect(Collectors.toList());
                    System.out.println("DEBUG: " + iyakuhincodes);
                    return Service.api.collectPharmaDrugByIyakuhincodes(iyakuhincodes);
                })
                .thenApply(pharmaDrugs -> {
                    Map<Integer, PharmaDrugDTO> pharmaMap = new HashMap<>();
                    for (PharmaDrugDTO pharmaDrug : pharmaDrugs) {
                        pharmaMap.put(pharmaDrug.iyakuhincode, pharmaDrug);
                    }
                    final ClinicInfoDTO clinicInfo = dataStore.clinicInfo;
                    return drugs.stream().map(drug -> {
                        PharmaDrugDTO pharmaDrug = pharmaMap.get(drug.drug.iyakuhincode);
                        DrugBagDataCreator dataCreator = new DrugBagDataCreator(drug, patient, pharmaDrug, clinicInfo);
                        DrugBagDrawerData data = dataCreator.createData();
                        DrugBagDrawer drawer = new DrugBagDrawer(data);
                        DrugBagOps drugBagOps = new DrugBagOps();
                        drugBagOps.prescribed = drug.drug.prescribed != 0;
                        drugBagOps.ops = drawer.getOps();
                        return drugBagOps;
                    }).collect(Collectors.toList());
                });
    }

    private void doPrintDrugBag(){
        composeDrugBagPages(drugs)
                .thenAccept(drugBagOps -> {
                    EventQueue.invokeLater(() -> {
                        DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(null, "薬袋印刷のプレビュー", true);
                        previewDialog.setImageSize(128, 182);
                        previewDialog.setPrinterSetting(PharmaConfig.INSTANCE.getDrugbagPrinterSetting());
                        JCheckBox includePrescribedCheckBox = new JCheckBox("処方済も含める");
                        List<List<Op>> allDrugBags = drugBagOps.stream()
                                .map(arg -> arg.ops)
                                .collect(Collectors.toList());
                        List<List<Op>> unprescribedDrugBags = drugBagOps.stream()
                                .filter(arg -> !arg.prescribed)
                                .map(arg -> arg.ops)
                                .collect(Collectors.toList());
                        includePrescribedCheckBox.addItemListener(event -> {
                            if( includePrescribedCheckBox.isSelected() ){
                                previewDialog.changePages(allDrugBags);
                                previewDialog.pack();
                            } else {
                                previewDialog.changePages(unprescribedDrugBags);
                                previewDialog.pack();
                            }
                        });
                        previewDialog.renderPages(unprescribedDrugBags);
                        previewDialog.addComponent(includePrescribedCheckBox);
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

    private PrescContentDrawer createPrescContentDrawer(){
        LocalDate prescDate = LocalDate.now();
        PrescContentDataCreator creator = new PrescContentDataCreator(patient, prescDate, drugs);
        PrescContentDrawerData data = creator.createData();
        return new PrescContentDrawer(data);
    }

    private void doPrintPrescContent(){
        PrescContentDrawer drawer = createPrescContentDrawer();
        DrawerPreviewDialog dialog = new DrawerPreviewDialog(SwingUtilities.getWindowAncestor(this), "処方内容の印刷", true);
        dialog.setPrinterSetting(PharmaConfig.INSTANCE.getPrescPrinterSetting());
        dialog.setImageSize(drawer.getPageWidth(), drawer.getPageHeight());
        dialog.render(drawer.getOps());
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
    }

    private CompletableFuture<TechouDrawer> composeTechouDrawer(){
        return Service.api.getClinicInfo()
                .thenApply(clinicInfo -> {
                    LocalDate prescDate = LocalDate.now();
                    TechouDataCreator creator = new TechouDataCreator(patient, prescDate, drugs, clinicInfo);
                    TechouDrawerData data = creator.createData();
                    return new TechouDrawer(data);
                });
    }

    private void doPrintTechou(){
        composeTechouDrawer()
                .thenAccept(drawer -> {
                    List<Op> ops = drawer.getOps();
                    EventQueue.invokeLater(() -> {
                        DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(null, "お薬手帳印刷プレビュー", false);
                        previewDialog.setImageSize(drawer.getPageWidth(), drawer.getPageHeight());
                        previewDialog.setPrinterSetting(PharmaConfig.INSTANCE.getTechouPrinterSetting());
                        previewDialog.setPrinterSetting(PharmaConfig.INSTANCE.getDrugbagPrinterSetting());
                        previewDialog.setLocationByPlatform(true);
                        previewDialog.render(ops);
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

    private void printAll(boolean includeTechou){
        {
            PrescContentDrawer drawer = createPrescContentDrawer();
            String settingName = PharmaConfig.INSTANCE.getPrescPrinterSetting();
            PrinterSetting.INSTANCE.print(drawer.getOps(), settingName);
        }
        {
            composeDrugBagPages(drugs)
                    .thenAccept(drugBagOps -> {
                        List<List<Op>> pages = drugBagOps.stream()
                                .map(drugBagOp -> drugBagOp.ops)
                                .collect(Collectors.toList());
                        String settingName = PharmaConfig.INSTANCE.getDrugbagPrinterSetting();
                        PrinterSetting.INSTANCE.printPages(pages, settingName);
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        }
        if( includeTechou ){
            composeTechouDrawer()
                    .thenAccept(drawer -> {
                        String settingName = PharmaConfig.INSTANCE.getTechouPrinterSetting();
                        PrinterSetting.INSTANCE.print(drawer.getOps(), settingName);
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });

        }
    }

    private void doPrintAll(){
        printAll(true);
    }

    private void doPrintAllExceptTechou(){
        printAll(false);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
