package jp.chang.myclinic;

import jp.chang.myclinic.dto.PatientDTO;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class PatientEditorRegistry {

	public static PatientEditorRegistry INSTANCE = new PatientEditorRegistry();

	private Map<Integer, PatientDialog> map = new HashMap<>();

	private PatientEditorRegistry(){

	}

	public void openPatientEditor(PatientDTO patient){
		PatientDialog editor = find(patient.patientId);
		if( editor != null ){
			editor.toFront();
			return;
		}
		Service.api.listHoken(patient.patientId)
				.whenComplete((result, t) -> {
					if( t != null ){
						t.printStackTrace();
						JOptionPane.showMessageDialog(null, "保険情報を取得できませんでした。" + t);
						return;
					}
					EventQueue.invokeLater(() -> {
						PatientDialog dialog = new PatientDialog("患者情報の編集", patient, result);
						register(patient.patientId, dialog);
						dialog.setLocationByPlatform(true);
						dialog.setVisible(true);
					});
				});
	}

	public PatientDialog find(Integer patientId){
		return map.get(patientId);
	}

	public void register(Integer patientId, PatientDialog dialog){
		map.put(patientId, dialog);
		System.out.println("registered: " + dialog);
		dialog.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent event){
				map.remove(patientId);
				System.out.println("released: " + dialog);
			}
		});
	}

}