package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.util.ArrayList;
import java.util.List;
import jp.chang.myclinic.dto.*;

class HokenEditor extends JPanel {

	private HokenList hokenList = new HokenList();
	private JButton editButton = new JButton("編集");
	private JButton deleteButton = new JButton("削除");
	private JButton enterShahoButton = new JButton("新規社保国保");
	private JButton enterKoukiButton = new JButton("新規後期高齢");
	private JButton enterKouhiButton = new JButton("新規公費負担");
	private List<ShahokokuhoDTO> shahokokuhoList = new ArrayList<>();
	private List<KoukikoureiDTO> koukikoureiList = new ArrayList<>();
	private List<RoujinDTO> roujinList = new ArrayList<>();
	private List<KouhiDTO> kouhiList = new ArrayList<>();
	private HokenListener<ShahokokuhoDTO> shahokokuhoListener;
	private HokenListener<KoukikoureiDTO> koukikoureiListener;
	private HokenListener<KouhiDTO> kouhiListener;

	HokenEditor(){
		setLayout(new MigLayout("fill, flowy, insets 0", "[grow]", "[grow] []"));
		add(makeMainPart(), "grow");
		add(makeEnterCommandPart(), "center");
		bind();
	}

	public void setShahokokuhoListener(HokenListener<ShahokokuhoDTO> shahokokuhoListener){
		this.shahokokuhoListener = shahokokuhoListener;
	}

	public void setKoukikoureiListener(HokenListener<KoukikoureiDTO> koukikoureiListener){
		this.koukikoureiListener = koukikoureiListener;
	}

	public void setKouhiListener(HokenListener<KouhiDTO> kouhiListener){
		this.kouhiListener = kouhiListener;
	}

	private JComponent makeMainPart(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "[grow] []", "[grow]"));
		JScrollPane scroll = new JScrollPane(hokenList);
		panel.add(scroll, "grow");
		panel.add(makeItemCommandPart(), "top");
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
			if( shahokokuhoListener == null ){
				return;
			}
			Window owner = SwingUtilities.getWindowAncestor(this);
			ShahokokuhoDTO shahokokuhoDTO = new ShahokokuhoDTO();
			ShahokokuhoForm form = new ShahokokuhoForm(owner, "新規社保国保入力", shahokokuhoDTO){
				@Override
				public void onEnter(){
					shahokokuhoListener.onEntering(shahokokuhoDTO)
						.whenComplete((ok, t) -> {
							if( t != null ){
								t.printStackTrace();
								JOptionPane.showMessageDialog(this, t.toString());
								return;
							}
							if( ok ){
								shahokokuhoList.add(shahokokuhoDTO);
								updateHokenList();
								shahokokuhoListener.onEntered(shahokokuhoDTO);
							}
						});
				}
			};
			form.setLocationByPlatform(true);
			form.setVisible(true);
		});
		enterKoukiButton.addActionListener(event -> {
			Window owner = SwingUtilities.getWindowAncestor(this);
			KoukikoureiDTO koukikoureiDTO = new KoukikoureiDTO();
			KoukikoureiForm form = new KoukikoureiForm(owner, "新規後期高齢入力", koukikoureiDTO){
				@Override
				public void onEnter(){
					koukikoureiListener.onEntering(koukikoureiDTO)
						.whenComplete((ok, t) -> {
							if( t != null ){
								t.printStackTrace();
								JOptionPane.showMessageDialog(this, t.toString());
								return;
							}
							if( ok ){
								koukikoureiList.add(koukikoureiDTO);
								updateHokenList();
								koukikoureiListener.onEntered(koukikoureiDTO);
							}
						});
				}
			};
			form.setLocationByPlatform(true);
			form.setVisible(true);
		});
		enterKouhiButton.addActionListener(event -> {
			Window owner = SwingUtilities.getWindowAncestor(this);
			KouhiDTO kouhiDTO = new KouhiDTO();
			KouhiForm form = new KouhiForm(owner, "新規公費負担入力", kouhiDTO){
				@Override
				public void onEnter(){
					kouhiListener.onEntering(kouhiDTO)
						.whenComplete((ok, t) -> {
							if( t != null ){
								t.printStackTrace();
								JOptionPane.showMessageDialog(this, t.toString());
								return;
							}
							if( ok ){
								kouhiList.add(kouhiDTO);
								updateHokenList();
								kouhiListener.onEntered(kouhiDTO);
							}
						});
				}
			};
			form.setLocationByPlatform(true);
			form.setVisible(true);
		});
		editButton.addActionListener(event -> doEdit());
	}

	private void doEdit(){
		Object obj = hokenList.getSelectedValue();
		if( obj instanceof ShahokokuhoDTO ){
			if( shahokokuhoListener == null ){
				return;
			}
			ShahokokuhoDTO shahokokuhoDTO = (ShahokokuhoDTO)obj;
			ShahokokuhoDTO copy = shahokokuhoDTO.copy();
			Window owner = SwingUtilities.getWindowAncestor(this);
			ShahokokuhoForm form = new ShahokokuhoForm(owner, "社保国保編集", copy){
				@Override
				public void onEnter(){
					shahokokuhoListener.onUpdating(copy)
						.whenComplete((ok, t) -> {
							if( t != null ){
								t.printStackTrace();
								JOptionPane.showMessageDialog(this, t.toString());
								return;
							}
							if( ok ){
								shahokokuhoDTO.assign(copy);
								updateHokenList();
							}
						});
				}
			};
			form.setLocationByPlatform(true);
			form.setVisible(true);
		} else if( obj instanceof KoukikoureiDTO ){
			if( koukikoureiListener == null ){
				return;
			}
			KoukikoureiDTO koukikoureiDTO = (KoukikoureiDTO)obj;
			KoukikoureiDTO copy = koukikoureiDTO.copy();
			Window owner = SwingUtilities.getWindowAncestor(this);
			KoukikoureiForm form = new KoukikoureiForm(owner, "後期高齢編集", copy){
				@Override
				public void onEnter(){
					koukikoureiListener.onUpdating(copy)
						.whenComplete((ok, t) -> {
							if( t != null ){
								t.printStackTrace();
								JOptionPane.showMessageDialog(this, t.toString());
								return;
							}
							if( ok ){
								koukikoureiDTO.assign(copy);
								updateHokenList();
							}
						});
				}
			};
			form.setLocationByPlatform(true);
			form.setVisible(true);
		} else if( obj instanceof KouhiDTO ){
			if( kouhiListener == null ){
				return;
			}
			KouhiDTO kouhiDTO = (KouhiDTO)obj;
			KouhiDTO copy = kouhiDTO.copy();
			Window owner = SwingUtilities.getWindowAncestor(this);
			KouhiForm form = new KouhiForm(owner, "公費負担編集", copy){
				@Override
				public void onEnter(){
					kouhiListener.onUpdating(copy)
						.whenComplete((ok, t) -> {
							if( t != null ){
								t.printStackTrace();
								JOptionPane.showMessageDialog(this, t.toString());
								return;
							}
							if( ok ){
								kouhiDTO.assign(copy);
								updateHokenList();
							}
						});
				}
			};
			form.setLocationByPlatform(true);
			form.setVisible(true);
		}
	}

	private void updateHokenList(){
		List<Object> list = new ArrayList<Object>();
		list.addAll(shahokokuhoList);
		list.addAll(koukikoureiList);
		list.addAll(roujinList);
		list.addAll(kouhiList);
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
