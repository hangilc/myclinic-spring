package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import jp.chang.myclinic.dto.PatientDTO;

class PatientInfoDialog extends JDialog {

	private PatientInfo infoPane = new PatientInfo();

	PatientInfoDialog(Window owner, PatientDTO patient){
		super(owner, "患者情報", Dialog.ModalityType.DOCUMENT_MODAL);
		setLayout(new MigLayout("fill, flowy, wrap", "[grow]", "[grow] []"));
		add(infoPane, "grow");
		add(makeSouth(), "right");
		infoPane.setPatient(patient);
		pack();
	}

	private JComponent makeSouth(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
		JButton closeButton = new JButton("閉じる");
		panel.add(closeButton);
		return panel;
	}

}