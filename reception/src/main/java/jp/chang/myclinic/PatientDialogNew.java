package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import jp.chang.myclinic.dto.*;

class PatientDialogNew extends PatientDialog {

	PatientDialogNew(){
		super("新規患者入力");
	}

	@Override
	protected void onShahokokuhoEntered(ShahokokuhoDTO shahokokuhoDTO){
		setEnterShahokokuhoButtonEnabled(false);
	}

	@Override
	protected void onKoukikoureiEntered(KoukikoureiDTO koukikoureiDTO){
		setEnterKoukikoureiButtonEnabled(false);
	}

	@Override
	protected void onKouhiEntered(KouhiDTO kouhiDTO){
		if( getKouhiListSize() >= 3 ){
			setEnterKouhiButtonEnabled(false);
		}
	}

	@Override
	protected void onEnter(PatientHokenListDTO patientHokenListDTO){
		Service.api.enterPatientWithHoken(patientHokenListDTO)
			.whenComplete((PatientHokenListDTO result, Throwable t) -> {
				if( t != null ){
					t.printStackTrace();
					JOptionPane.showMessageDialog(PatientDialogNew.this, "エラー\n" + t);
					setEnterButtonEnabled(true);
					return;
				}
				dispose();
			});
	}

}