package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.util.concurrent.CompletableFuture;
import jp.chang.myclinic.dto.*;

class PatientDialogNew extends PatientDialog {

	PatientDialogNew(){
		super("新規患者入力");
		setShahokokuhoListener(new HokenListener<ShahokokuhoDTO>(){
			@Override
			public CompletableFuture<Boolean> onEntering(ShahokokuhoDTO shahokokuhoDTO){
				return CompletableFuture.completedFuture(true);
			}

			@Override
			public void onEntered(ShahokokuhoDTO shahokokuhoDTO){
				setEnterShahokokuhoButtonEnabled(false);
			}

			@Override
			public CompletableFuture<Boolean> onUpdating(ShahokokuhoDTO shahokokuhoDTO){
				return CompletableFuture.completedFuture(true);
			}

			@Override
			public void onUpdated(ShahokokuhoDTO shahokokuhoDTO){
				
			}

			@Override
			public CompletableFuture<Boolean> onDeleting(ShahokokuhoDTO shahokokuhoDTO){
				return CompletableFuture.completedFuture(true);
			}

			@Override
			public void onDeleted(ShahokokuhoDTO shahokokuhoDTO){

			}
		});
		setKoukikoureiListener(new HokenListener<KoukikoureiDTO>(){
			@Override
			public CompletableFuture<Boolean> onEntering(KoukikoureiDTO koukikoureiDTO){
				return CompletableFuture.completedFuture(true);
			}

			@Override
			public void onEntered(KoukikoureiDTO koukikoureiDTO){
				setEnterKoukikoureiButtonEnabled(false);
			}

			@Override
			public CompletableFuture<Boolean> onUpdating(KoukikoureiDTO koukikoureiDTO){
				return CompletableFuture.completedFuture(true);
			}

			@Override
			public void onUpdated(KoukikoureiDTO koukikoureiDTO){
				
			}

			@Override
			public CompletableFuture<Boolean> onDeleting(KoukikoureiDTO koukikoureiDTO){
				return CompletableFuture.completedFuture(true);
			}

			@Override
			public void onDeleted(KoukikoureiDTO koukikoureiDTO){

			}
		});
		setKouhiListener(new HokenListener<KouhiDTO>(){
			@Override
			public CompletableFuture<Boolean> onEntering(KouhiDTO kouhiDTO){
				return CompletableFuture.completedFuture(true);
			}

			@Override
			public void onEntered(KouhiDTO kouhiDTO){
				if( getKouhiListSize() >= 3 ){
					setEnterKouhiButtonEnabled(false);
				}
			}

			@Override
			public CompletableFuture<Boolean> onUpdating(KouhiDTO kouhiDTO){
				return CompletableFuture.completedFuture(true);
			}

			@Override
			public void onUpdated(KouhiDTO kouhiDTO){
				
			}

			@Override
			public CompletableFuture<Boolean> onDeleting(KouhiDTO kouhiDTO){
				return CompletableFuture.completedFuture(true);
			}

			@Override
			public void onDeleted(KouhiDTO kouhiDTO){

			}
		});
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