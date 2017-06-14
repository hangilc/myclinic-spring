package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DrugUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import static java.awt.Font.BOLD;

/**
 * Created by hangil on 2017/06/12.
 */
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
    }

    public void update(PatientDTO patient, List<DrugFullDTO> drugs){
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
            bagLink.setForeground(Color.BLUE);
            bagLink.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            wrap.append(bagLink);
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



}
