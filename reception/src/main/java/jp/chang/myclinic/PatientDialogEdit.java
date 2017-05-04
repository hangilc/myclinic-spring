package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.util.concurrent.CompletableFuture;
import jp.chang.myclinic.dto.*;

class PatientDialogEdit extends PatientDialog {

	PatientDialogEdit(PatientDTO patientDTO, HokenListDTO hokenListDTO){
		super("患者編集", true);
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
				return CompletableFuture.completedFuture(confirm("この社保国保を削除していいですか？"));
			}

			@Override
			public void onDeleted(ShahokokuhoDTO shahokokuhoDTO){
				setEnterShahokokuhoButtonEnabled(true);
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
				return CompletableFuture.completedFuture(confirm("この後期高齢を削除していいですか？"));
			}

			@Override
			public void onDeleted(KoukikoureiDTO koukikoureiDTO){
				setEnterKoukikoureiButtonEnabled(true);
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
				return CompletableFuture.completedFuture(confirm("この公費負担を削除していいですか？"));
			}
			
			@Override
			public void onDeleted(KouhiDTO kouhiDTO){
				setEnterKouhiButtonEnabled(true);
			}
		});
		setCurrentOnlySelected(true);
		setPatient(patientDTO);
		setHokenList(hokenListDTO);
	}

	private boolean confirm(String msg){
		return JOptionPane.showConfirmDialog(this, msg, "確認", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}

	private void alert(String msg){
		JOptionPane.showMessageDialog(this, msg);
	}

	@Override
	protected void onEnter(PatientHokenListDTO patientHokenListDTO){
		Service.api.updatePatient(patientHokenListDTO.patientDTO)
			.whenComplete((result, t) -> {
				if( t != null ){
					t.printStackTrace();
					alert("エラー\n" + t);
					setEnterButtonEnabled(true);
					return;
				}
				dispose();
			});
	}
}