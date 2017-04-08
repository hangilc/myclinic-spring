package jp.chang.myclinic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;

class PatientDocInfoPanel extends JPanel {

	private JLabel totalPagesLabel;

	PatientDocInfoPanel(int patientId){
		super();
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(boxLayout);
        JLabel patientIdLabel = new JLabel("患者番号：" + patientId);
        patientIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(patientIdLabel);
		totalPagesLabel = new JLabel();
        totalPagesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		updateTotalPages(0);
        add(totalPagesLabel);
	}

	public void updateTotalPages(int totalPages){
		totalPagesLabel.setText("スキャンしたページ数：" + totalPages);
	}

}