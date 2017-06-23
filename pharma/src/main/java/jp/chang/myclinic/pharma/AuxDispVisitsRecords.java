package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitTextDrugDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DrugUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;

public class AuxDispVisitsRecords extends JPanel {

    public AuxDispVisitsRecords(){
        setLayout(new MigLayout("insets 0", "", ""));
    }

    public void showVisits(List<Integer> visitIds) {
        Service.api.listVisitTextDrug(new HashSet<Integer>(visitIds))
                .thenAccept(records -> {
                    EventQueue.invokeLater(() -> {
                        for (VisitTextDrugDTO record : records) {
                            String visitDate = DateTimeUtil.toKanji(DateTimeUtil.parseSqlDateTime(record.visit.visitedAt).toLocalDate());
                            add(makeDateTitle(visitDate), "span 2, grow, wrap");
                            add(makeTextPane(record.texts), "top, gapright 10");
                            add(makeDrugPane(record.drugs), "top, wrap");
                        }
                        repaint();
                        revalidate();
                    });
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    private JComponent makeDateTitle(String date){
        JLabel label = new JLabel(date);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        label.setBackground(new Color(0xdd, 0xdd, 0xdd));
        Font font = label.getFont().deriveFont(Font.BOLD);
        label.setFont(font);
        return label;
    }

    private JComponent makeTextPane(List<TextDTO> texts){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        for(TextDTO text: texts){
            WrappedText t = new WrappedText(180, text.content);
            panel.add(t, "wrap");
        }
        return panel;
    }

    private JComponent makeDrugPane(List<DrugFullDTO> drugs){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        int index = 1;
        for(DrugFullDTO drug: drugs){
            String label = (index++) + ") " + DrugUtil.drugRep(drug);
            WrappedText t = new WrappedText(180, label);
            panel.add(t, "wrap");
        }
        return panel;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
