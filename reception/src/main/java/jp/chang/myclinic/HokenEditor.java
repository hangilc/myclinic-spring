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

	HokenEditor(){
		setLayout(new MigLayout("fill, flowy, insets 0", "[grow]", "[grow] []"));
		add(makeMainPart(), "grow");
		add(makeEnterCommandPart(), "center");
		bind();
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
			Window owner = SwingUtilities.getWindowAncestor(this);
			ShahokokuhoForm form = new ShahokokuhoForm(owner, "新規社保国保入力", new ShahokokuhoDTO()){
				@Override
				public void onEnter(ShahokokuhoDTO shahokokuhoDTO){
					shahokokuhoList.add(shahokokuhoDTO);
					updateHokenList();
					onShahokokuhoEntered(shahokokuhoDTO);
				}
			};
			form.setLocationByPlatform(true);
			form.setVisible(true);
		});
		enterKoukiButton.addActionListener(event -> {
			Window owner = SwingUtilities.getWindowAncestor(this);
			KoukikoureiForm form = new KoukikoureiForm(owner, "新規後期高齢入力", new KoukikoureiDTO()){
				@Override
				public void onEnter(KoukikoureiDTO koukikoureiDTO){
					koukikoureiList.add(koukikoureiDTO);
					updateHokenList();
					onKoukikoureiEntered(koukikoureiDTO);
				}
			};
			form.setLocationByPlatform(true);
			form.setVisible(true);
		});
		enterKouhiButton.addActionListener(event -> {
			Window owner = SwingUtilities.getWindowAncestor(this);
			KouhiForm form = new KouhiForm(owner, "新規公費負担入力", new KouhiDTO()){
				@Override
				public void onEnter(KouhiDTO kouhiDTO){
					kouhiList.add(kouhiDTO);
					updateHokenList();
					onKouhiEntered(kouhiDTO);
				}
			};
			form.setLocationByPlatform(true);
			form.setVisible(true);
		});
	}

	private void updateHokenList(){
		List<Object> list = new ArrayList<Object>();
		list.addAll(shahokokuhoList);
		list.addAll(koukikoureiList);
		list.addAll(roujinList);
		list.addAll(kouhiList);
		hokenList.setListData(list.toArray());
	}

	protected void onShahokokuhoEntered(ShahokokuhoDTO shahokokuhoDTO){

	}

	protected void onKoukikoureiEntered(KoukikoureiDTO koukikoureiDTO){

	}

	protected void onKouhiEntered(KouhiDTO kouhiDTO){

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
