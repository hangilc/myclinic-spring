package jp.chang.myclinic.pharma;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.swing.DrawerPreviewDialog;
import jp.chang.myclinic.drawer.techou.TechouDrawer;
import jp.chang.myclinic.drawer.techou.TechouDrawerData;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitIdVisitedAtDTO;
import jp.chang.myclinic.pharma.wrappedtext.WrappedText;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DrugUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PrevTechouDialog extends JDialog {

    private PatientDTO patient;

    public PrevTechouDialog(PatientDTO patient, List<VisitIdVisitedAtDTO> visits){
        this.patient = patient;
        setTitle("過去のお薬手帳");
        setLayout(new MigLayout("", "", ""));
        add(new WrappedText("(" + patient.patientId + ") " + patient.lastName + patient.firstName, 260), "wrap");
        JScrollPane sp = new JScrollPane(makeVisitsPane(visits, 260));
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        add(sp, "h 300");
        pack();
    }

    private JComponent makeVisitsPane(List<VisitIdVisitedAtDTO> visits, int width){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 2", "", ""));
        for(VisitIdVisitedAtDTO visit: visits){
            panel.add(makeVisitItem(visit, width), "wrap");
        }
        return panel;
    }

    private CompletableFuture<TechouDrawer> composeTechouDrawer(List<DrugFullDTO> drugs, LocalDate prescDate){
        return Service.api.getClinicInfo()
                .thenApply(clinicInfo -> {
                    TechouDataCreator creator = new TechouDataCreator(patient, prescDate, drugs, clinicInfo);
                    TechouDrawerData data = creator.createData();
                    return new TechouDrawer(data);
                });
    }

    private void doPrintTechou(List<DrugFullDTO> drugs, LocalDate prescDate){
        composeTechouDrawer(drugs, prescDate)
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
                    alert(t.toString());
                    return null;
                });
    }

    private JComponent makeVisitItem(VisitIdVisitedAtDTO visit, int width){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 0", "", ""));
        JPanel workarea = new JPanel(new MigLayout("insets 2 10 2 10, gapy 0", "", ""));
        WrappedText dateText = new WrappedText(width);
        dateText.appendString("・");
        String dateString = DateTimeUtil.sqlDateTimeToKanji(visit.visitedAt, DateTimeUtil.kanjiFormatter1);
        dateText.appendLink(dateString, () -> {
            if( workarea.getComponentCount() == 0 ){
                Service.api.listDrugFull(visit.visitId)
                        .thenAccept(drugs -> {
                            EventQueue.invokeLater(() -> {
                                int index = 1;
                                for(DrugFullDTO drug: drugs){
                                    WrappedText wt = new WrappedText(index + ")" + DrugUtil.drugRep(drug), 220);
                                    index += 1;
                                    workarea.add(wt, "wrap");
                                }
                                JButton printButton = new JButton("お薬手帳印刷");
                                printButton.addActionListener(event -> {
                                    doPrintTechou(drugs, DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate());
//                                    workarea.removeAll();
//                                    workarea.repaint();
//                                    workarea.revalidate();
                                });
                                workarea.add(printButton);
                                workarea.repaint();
                                workarea.revalidate();
                            });
                        })
                        .exceptionally(t -> {
                            t.printStackTrace();;
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
            } else {
                workarea.removeAll();
            }
            workarea.repaint();
            workarea.revalidate();
        });
        panel.add(dateText, "wrap");
        panel.add(workarea);
        return panel;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
