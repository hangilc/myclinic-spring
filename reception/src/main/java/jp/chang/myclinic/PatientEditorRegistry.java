package jp.chang.myclinic;

import java.util.Map;
import java.util.HashMap;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class PatientEditorRegistry {

	public static PatientEditorRegistry INSTANCE = new PatientEditorRegistry();

	private Map<Integer, PatientDialog> map = new HashMap<>();

	private PatientEditorRegistry(){

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