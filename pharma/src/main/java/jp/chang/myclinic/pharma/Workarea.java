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

    public Workarea(){
        setupNameLabel();
        setLayout(new MigLayout("gapy 0", "", ""));
        add(nameLabel, "gap top 0, wrap");
        add(yomiLabel, "gap top 5, wrap");
        add(patientInfoLabel, "wrap");
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
        for(DrugFullDTO drugFull: drugs){
            String text = DrugUtil.drugRep(drugFull);
            WrappedText wrap = new WrappedText(200, text);
            JLabel bagButton = new JLabel("<html><font color=\"#0000ff\"><u>薬袋</u></font></html>");
            wrap.append(bagButton);
            add(wrap, "wrap");
        }
        repaint();
        revalidate();
    }

    private void setupNameLabel(){
        Font baseFont = nameLabel.getFont();
        Font font = baseFont.deriveFont(BOLD, (int)(baseFont.getSize() * 1.2));
        nameLabel.setFont(font);
    }

}
