package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DateTimeUtil;

class HokenEditor extends JPanel {

	private int patientId;
	private HokenList hokenList = new HokenList();
	private JButton editButton = new JButton("編集");
	private JButton deleteButton = new JButton("削除");
	private JButton enterShahoButton = new JButton("新規社保国保");
	private JButton enterKoukiButton = new JButton("新規後期高齢");
	private JButton enterKouhiButton = new JButton("新規公費負担");
	private JCheckBox currentOnlyCheckBox = new JCheckBox("現在有効のみ");
	private List<ShahokokuhoDTO> shahokokuhoList = new ArrayList<>();
	private List<KoukikoureiDTO> koukikoureiList = new ArrayList<>();
	private List<RoujinDTO> roujinList = new ArrayList<>();
	private List<KouhiDTO> kouhiList = new ArrayList<>();

	HokenEditor(int patientId){
		setLayout(new MigLayout("fill, flowy, insets 0", "[grow]", "[grow] []"));
		add(makeMainPart(), "grow");
		add(makeEnterCommandPart(), "center");
		setPatientId(patientId);
		bind();
	}

	public void setHokenList(HokenListDTO hokenListDTO){
		shahokokuhoList = hokenListDTO.shahokokuhoListDTO;
		koukikoureiList = hokenListDTO.koukikoureiListDTO;
		roujinList = hokenListDTO.roujinListDTO;
		kouhiList = hokenListDTO.kouhiListDTO;
		updateHokenList();
	}

	public void setCurrentOnlySelected(boolean selected){
		currentOnlyCheckBox.setSelected(selected);
	}

	public void setPatientId(int patientId){
		if( !(this.patientId == 0 || this.patientId == patientId) ){
			throw new RuntimeException("should not happen");
		}
		this.patientId = patientId;
		if( patientId > 0 ){
			enableUI();
		} else {
			disableUI();
		}
	}

	public int getPatientId(){
		return patientId;
	}

	private void disableUI(){
		enterShahoButton.setEnabled(false);
		enterKoukiButton.setEnabled(false);
		enterKouhiButton.setEnabled(false);
		currentOnlyCheckBox.setEnabled(false);
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	private void enableUI(){
		enterShahoButton.setEnabled(true);
		enterKoukiButton.setEnabled(true);
		enterKouhiButton.setEnabled(true);
		currentOnlyCheckBox.setEnabled(false);
		adaptUI();
	}

	private void adaptUI(){
		if( hokenList.getSelectedIndex() < 0 ){
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
		} else {
			editButton.setEnabled(true);
			deleteButton.setEnabled(true);
		}
	}

	private JComponent makeMainPart(){
		currentOnlyCheckBox.setSelected(true);
		JPanel panel = new JPanel(new MigLayout("insets 0", "[grow] []", "[grow] []"));
		JScrollPane scroll = new JScrollPane(hokenList);
		panel.add(scroll, "grow");
		panel.add(makeItemCommandPart(), "top, wrap");
		panel.add(currentOnlyCheckBox, "left, span 2");
		return panel;
	}

	private JComponent makeItemCommandPart(){
		JPanel panel = new JPanel(new MigLayout("flowy, insets 0", "", ""));
		panel.add(editButton);
		panel.add(deleteButton);
		return panel;
	}

	private JComponent makeEnterCommandPart(){
		JPanel panel = new JPanel(new MigLayout("fill, insets 0", "[] [] []", ""));
		panel.add(enterShahoButton);
		panel.add(enterKoukiButton);
		panel.add(enterKouhiButton);
		return panel;
	}

	private void bind(){
		enterShahoButton.addActionListener(event -> {
			if( patientId == 0 ){
				return;
			}
			Window owner = SwingUtilities.getWindowAncestor(this);
			ShahokokuhoDTO shahokokuhoDTO = new ShahokokuhoDTO();
			ShahokokuhoForm form = new ShahokokuhoForm(owner, "新規社保国保入力", shahokokuhoDTO){
				@Override
				public void onEnter(){
					shahokokuhoDTO.patientId = patientId;
					Service.api.enterShahokokuho(shahokokuhoDTO)
					.whenComplete((result, t) -> {
						shahokokuhoDTO.shahokokuhoId = result;
						EventQueue.invokeLater(() -> {
							shahokokuhoList.add(shahokokuhoDTO);
							updateHokenList();
						});
					});
				}
			};
			form.setLocationByPlatform(true);
			form.setVisible(true);
		});
		enterKoukiButton.addActionListener(event -> {
			if( patientId == 0 ){
				return;
			}
			Window owner = SwingUtilities.getWindowAncestor(this);
			KoukikoureiDTO koukikoureiDTO = new KoukikoureiDTO();
			KoukikoureiForm form = new KoukikoureiForm(owner, "新規後期高齢入力", koukikoureiDTO){
				@Override
				public void onEnter(){
					koukikoureiDTO.patientId = patientId;
					Service.api.enterKoukikourei(koukikoureiDTO)
					.whenComplete((result, t) -> {
						koukikoureiDTO.koukikoureiId = result;
						EventQueue.invokeLater(() -> {
							koukikoureiList.add(koukikoureiDTO);
							updateHokenList();
						});
					});
				}
			};
			form.setLocationByPlatform(true);
			form.setVisible(true);
		});
		enterKouhiButton.addActionListener(event -> {
			if( patientId == 0 ){
				return;
			}
			Window owner = SwingUtilities.getWindowAncestor(this);
			KouhiDTO kouhiDTO = new KouhiDTO();
			KouhiForm form = new KouhiForm(owner, "新規公費負担入力", kouhiDTO){
				@Override
				public void onEnter(){
					kouhiDTO.patientId = patientId;
					Service.api.enterKouhi(kouhiDTO)
					.whenComplete((result, t) -> {
						kouhiDTO.kouhiId = result;
						EventQueue.invokeLater(() -> {
							kouhiList.add(kouhiDTO);
							updateHokenList();
						});
					});
				}
			};
			form.setLocationByPlatform(true);
			form.setVisible(true);
		});
		// enterKoukiButton.addActionListener(event -> {
		// 	Window owner = SwingUtilities.getWindowAncestor(this);
		// 	KoukikoureiDTO koukikoureiDTO = new KoukikoureiDTO();
		// 	KoukikoureiForm form = new KoukikoureiForm(owner, "新規後期高齢入力", koukikoureiDTO){
		// 		@Override
		// 		public void onEnter(){
		// 			koukikoureiListener.onEntering(koukikoureiDTO)
		// 				.whenComplete((ok, t) -> {
		// 					if( t != null ){
		// 						t.printStackTrace();
		// 						JOptionPane.showMessageDialog(this, t.toString());
		// 						return;
		// 					}
		// 					if( ok ){
		// 						koukikoureiList.add(koukikoureiDTO);
		// 						updateHokenList();
		// 						koukikoureiListener.onEntered(koukikoureiDTO);
		// 					}
		// 				});
		// 		}
		// 	};
		// 	form.setLocationByPlatform(true);
		// 	form.setVisible(true);
		// });
		// enterKouhiButton.addActionListener(event -> {
		// 	Window owner = SwingUtilities.getWindowAncestor(this);
		// 	KouhiDTO kouhiDTO = new KouhiDTO();
		// 	KouhiForm form = new KouhiForm(owner, "新規公費負担入力", kouhiDTO){
		// 		@Override
		// 		public void onEnter(){
		// 			kouhiListener.onEntering(kouhiDTO)
		// 				.whenComplete((ok, t) -> {
		// 					if( t != null ){
		// 						t.printStackTrace();
		// 						JOptionPane.showMessageDialog(this, t.toString());
		// 						return;
		// 					}
		// 					if( ok ){
		// 						kouhiList.add(kouhiDTO);
		// 						updateHokenList();
		// 						kouhiListener.onEntered(kouhiDTO);
		// 					}
		// 				});
		// 		}
		// 	};
		// 	form.setLocationByPlatform(true);
		// 	form.setVisible(true);
		// });
		editButton.addActionListener(event -> doEdit());
		deleteButton.addActionListener(event -> doDelete());
		currentOnlyCheckBox.addActionListener(event -> {
			updateHokenList();
		});
		bindHokenList();
	}

	private void bindHokenList(){
		hokenList.addListSelectionListener(event -> {
			if( event.getValueIsAdjusting() == false ){
				adaptUI();
			}
		});
	}

	private void doEdit(){
		// Object obj = hokenList.getSelectedValue();
		// if( obj instanceof ShahokokuhoDTO ){
		// 	ShahokokuhoDTO shahokokuhoDTO = (ShahokokuhoDTO)obj;
		// 	ShahokokuhoDTO copy = shahokokuhoDTO.copy();
		// 	Window owner = SwingUtilities.getWindowAncestor(this);
		// 	ShahokokuhoForm form = new ShahokokuhoForm(owner, "社保国保編集", copy){
		// 		@Override
		// 		public void onEnter(){
		// 			shahokokuhoListener.onUpdating(copy)
		// 				.whenComplete((ok, t) -> {
		// 					if( t != null ){
		// 						t.printStackTrace();
		// 						JOptionPane.showMessageDialog(this, t.toString());
		// 						return;
		// 					}
		// 					if( ok ){
		// 						shahokokuhoDTO.assign(copy);
		// 						updateHokenList();
		// 						shahokokuhoListener.onUpdated(shahokokuhoDTO);
		// 					}
		// 				});
		// 		}
		// 	};
		// 	form.setLocationByPlatform(true);
		// 	form.setVisible(true);
		// } else if( obj instanceof KoukikoureiDTO ){
		// 	KoukikoureiDTO koukikoureiDTO = (KoukikoureiDTO)obj;
		// 	KoukikoureiDTO copy = koukikoureiDTO.copy();
		// 	Window owner = SwingUtilities.getWindowAncestor(this);
		// 	KoukikoureiForm form = new KoukikoureiForm(owner, "後期高齢編集", copy){
		// 		@Override
		// 		public void onEnter(){
		// 			koukikoureiListener.onUpdating(copy)
		// 				.whenComplete((ok, t) -> {
		// 					if( t != null ){
		// 						t.printStackTrace();
		// 						JOptionPane.showMessageDialog(this, t.toString());
		// 						return;
		// 					}
		// 					if( ok ){
		// 						koukikoureiDTO.assign(copy);
		// 						updateHokenList();
		// 						koukikoureiListener.onUpdated(koukikoureiDTO);
		// 					}
		// 				});
		// 		}
		// 	};
		// 	form.setLocationByPlatform(true);
		// 	form.setVisible(true);
		// } else if( obj instanceof KouhiDTO ){
		// 	KouhiDTO kouhiDTO = (KouhiDTO)obj;
		// 	KouhiDTO copy = kouhiDTO.copy();
		// 	Window owner = SwingUtilities.getWindowAncestor(this);
		// 	KouhiForm form = new KouhiForm(owner, "公費負担編集", copy){
		// 		@Override
		// 		public void onEnter(){
		// 			kouhiListener.onUpdating(copy)
		// 				.whenComplete((ok, t) -> {
		// 					if( t != null ){
		// 						t.printStackTrace();
		// 						JOptionPane.showMessageDialog(this, t.toString());
		// 						return;
		// 					}
		// 					if( ok ){
		// 						kouhiDTO.assign(copy);
		// 						updateHokenList();
		// 						kouhiListener.onUpdated(kouhiDTO);
		// 					}
		// 				});
		// 		}
		// 	};
		// 	form.setLocationByPlatform(true);
		// 	form.setVisible(true);
		// }
	}

	private void doDelete(){
		// Object obj = hokenList.getSelectedValue();
		// if( obj instanceof ShahokokuhoDTO ){
		// 	if( shahokokuhoListener == null ){
		// 		return;
		// 	}
		// 	ShahokokuhoDTO shahokokuhoDTO = (ShahokokuhoDTO)obj;
		// 	shahokokuhoListener.onDeleting(shahokokuhoDTO)
		// 		.whenComplete((ok, t) -> {
		// 			if( t != null ){
		// 				t.printStackTrace();
		// 				JOptionPane.showMessageDialog(this, t.toString());
		// 				return;
		// 			}
		// 			if( ok ){
		// 				shahokokuhoList.remove(shahokokuhoDTO);
		// 				updateHokenList();
		// 				shahokokuhoListener.onDeleted(shahokokuhoDTO);
		// 			}
		// 		});
		// } else if( obj instanceof KoukikoureiDTO ){
		// 	if( koukikoureiListener == null ){
		// 		return;
		// 	}
		// 	KoukikoureiDTO koukikoureiDTO = (KoukikoureiDTO)obj;
		// 	koukikoureiListener.onDeleting(koukikoureiDTO)
		// 		.whenComplete((ok, t) -> {
		// 			if( t != null ){
		// 				t.printStackTrace();
		// 				JOptionPane.showMessageDialog(this, t.toString());
		// 				return;
		// 			}
		// 			if( ok ){
		// 				koukikoureiList.remove(koukikoureiDTO);
		// 				updateHokenList();
		// 				koukikoureiListener.onDeleted(koukikoureiDTO);
		// 			}
		// 		});
		// } else if( obj instanceof KouhiDTO ){
		// 	if( kouhiListener == null ){
		// 		return;
		// 	}
		// 	KouhiDTO kouhiDTO = (KouhiDTO)obj;
		// 	kouhiListener.onDeleting(kouhiDTO)
		// 		.whenComplete((ok, t) -> {
		// 			if( t != null ){
		// 				t.printStackTrace();
		// 				JOptionPane.showMessageDialog(this, t.toString());
		// 				return;
		// 			}
		// 			if( ok ){
		// 				kouhiList.remove(kouhiDTO);
		// 				updateHokenList();
		// 				kouhiListener.onDeleted(kouhiDTO);
		// 			}
		// 		});
		// }		
	}

	private List<ShahokokuhoDTO> listShahokokuhoForDisp(boolean currentOnly, LocalDate today){
		if( currentOnly ){
			return shahokokuhoList.stream()
				.filter(h -> DateTimeUtil.isValidAt(today, h.validFrom, h.validUpto))
				.collect(Collectors.toList());
		} else {
			return shahokokuhoList;
		}
	}

	private List<KoukikoureiDTO> listKoukikoureiForDisp(boolean currentOnly, LocalDate today){
		if( currentOnly ){
			return koukikoureiList.stream()
				.filter(h -> DateTimeUtil.isValidAt(today, h.validFrom, h.validUpto))
				.collect(Collectors.toList());
		} else {
			return koukikoureiList;
		}
	}

	private List<RoujinDTO> listRoujinForDisp(boolean currentOnly, LocalDate today){
		if( currentOnly ){
			return roujinList.stream()
				.filter(h -> DateTimeUtil.isValidAt(today, h.validFrom, h.validUpto))
				.collect(Collectors.toList());
		} else {
			return roujinList;
		}
	}

	private List<KouhiDTO> listKouhiForDisp(boolean currentOnly, LocalDate today){
		if( currentOnly ){
			return kouhiList.stream()
				.filter(h -> DateTimeUtil.isValidAt(today, h.validFrom, h.validUpto))
				.collect(Collectors.toList());
		} else {
			return kouhiList;
		}
	}

	private void updateHokenList(){
		boolean currentOnly = currentOnlyCheckBox.isVisible() && currentOnlyCheckBox.isSelected();
		LocalDate today = LocalDate.now();
		List<Object> list = new ArrayList<Object>();
		list.addAll(listShahokokuhoForDisp(currentOnly, today));
		list.addAll(listKoukikoureiForDisp(currentOnly, today));
		list.addAll(listRoujinForDisp(currentOnly, today));
		list.addAll(listKouhiForDisp(currentOnly, today));
		hokenList.setListData(list.toArray());
	}

	public void setEnterShahokokuhoButtonEnabled(boolean enabled){
		enterShahoButton.setEnabled(enabled);
	}

	public void setEnterKoukikoureiButtonEnabled(boolean enabled){
		enterKoukiButton.setEnabled(enabled);
	}

	public void setEnterKouhiButtonEnabled(boolean enabled){
		enterKouhiButton.setEnabled(enabled);
	}

	public int getKouhiListSize(){
		return kouhiList.size();
	}

	public HokenListDTO getHokenListDTO(){
		HokenListDTO list = new HokenListDTO();
		list.shahokokuhoListDTO = shahokokuhoList;
		list.koukikoureiListDTO = koukikoureiList;
		list.roujinListDTO = roujinList;
		list.kouhiListDTO = kouhiList;
		return list;
	}
	
}
