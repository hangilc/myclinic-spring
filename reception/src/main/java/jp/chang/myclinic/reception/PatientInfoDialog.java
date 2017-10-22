package jp.chang.myclinic.reception;

import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class PatientInfoDialog extends JDialog {

	private PatientDTO patient;
	private PatientInfo infoPane = new PatientInfo();
	private JButton editButton = new JButton("編集");
	private JButton closeButton = new JButton("閉じる");

	PatientInfoDialog(Window owner, PatientDTO patient, boolean modal){
		super(owner, "患者情報",
				modal ? Dialog.ModalityType.DOCUMENT_MODAL : Dialog.ModalityType.MODELESS);
		this.patient = patient;
		setLayout(new MigLayout("fill, flowy, wrap", "[grow]", "[grow] []"));
		JScrollPane sp = new JScrollPane(infoPane);
		sp.setBorder(BorderFactory.createEmptyBorder());
		add(sp, "w 300, grow");
		add(makeSouth(), "right");
		infoPane.setPatient(patient);
		bind();
		pack();
	}

	private JComponent makeSouth(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
		panel.add(editButton, "sizegroup btn");
		panel.add(closeButton, "sizegroup btn");
		return panel;
	}

	private void bind(){
		closeButton.addActionListener(event -> {
			dispose();
		});
		editButton.addActionListener(event -> doEdit());
	}

	private void doEdit() {
		PatientEditorRegistry.INSTANCE.openPatientEditor(patient);
		dispose();
	}

}