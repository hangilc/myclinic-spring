package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import jp.chang.myclinic.dto.*;

class NewPatientHoken extends JPanel {

	private HokenList hokenList;
	private JDialog owner;
	private ShahokokuhoDTO shahokokuho;
	private KoukikoureiDTO koukikourei;
	private RoujinDTO roujin;
	private List<KouhiDTO> kouhiList = new ArrayList<>();

	NewPatientHoken(JDialog owner){
		this.owner = owner;
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(makeUpperPanel());
		add(Box.createVerticalStrut(5));
		add(makeLowerPanel());
	}

	public ShahokokuhoDTO getShahokokuhoDTO(){
		return shahokokuho;
	}

	public KoukikoureiDTO getKoukikoureiDTO(){
		return koukikourei;
	}

	public RoujinDTO getRoujinDTO(){
		return roujin;
	}

	public KouhiDTO getKouhi1DTO(){
		if( kouhiList.size() > 0 ){
			return kouhiList.get(0);
		} else {
			return null;
		}
	}

	public KouhiDTO getKouhi2DTO(){
		if( kouhiList.size() > 1 ){
			return kouhiList.get(1);
		} else {
			return null;
		}
	}

	public KouhiDTO getKouhi3DTO(){
		if( kouhiList.size() > 2 ){
			return kouhiList.get(2);
		} else {
			return null;
		}
	}

	private JComponent makeUpperPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		{
			hokenList = new HokenList();
			JScrollPane scroll = new JScrollPane(hokenList);
			scroll.setPreferredSize(new Dimension(300, 160));
			panel.add(scroll);
		}
		panel.add(Box.createHorizontalStrut(5));
		JPanel commandBox = new JPanel();
		{
			commandBox.setLayout(new BoxLayout(commandBox, BoxLayout.PAGE_AXIS));
			JButton editButton = new JButton("訂正");
			editButton.addActionListener(event -> {
				doEditHoken();
			});
			JButton deleteButton = new JButton("削除");
			commandBox.add(editButton);
			commandBox.add(Box.createVerticalStrut(5));
			commandBox.add(deleteButton);
		}
		panel.add(commandBox);
		return panel;
	}

	private JComponent makeLowerPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JPanel upperBox = new JPanel();
		{
			upperBox.setLayout(new FlowLayout());
			JButton enterShahoButton = new JButton("新規社保国保");
			enterShahoButton.addActionListener(event -> {
				ShahokokuhoForm form = new ShahokokuhoForm(owner, "新規社保国保入力", new ShahokokuhoDTO()){
					@Override
					public void onEnter(ShahokokuhoDTO shahokokuhoDTO){
						shahokokuho = shahokokuhoDTO;
						enterShahoButton.setEnabled(false);
						updateHokenList();
					}
				};
				form.setLocationByPlatform(true);
				form.setVisible(true);
			});
			upperBox.add(enterShahoButton);
			JButton enterKoukiButton = new JButton("新規後期高齢");
			enterKoukiButton.addActionListener(event -> {
				KoukikoureiForm form = new KoukikoureiForm(owner, "新規後期高齢入力", new KoukikoureiDTO()){
					@Override
					public void onEnter(KoukikoureiDTO koukikoureiDTO){
						koukikourei = koukikoureiDTO;
						enterKoukiButton.setEnabled(false);
						updateHokenList();
					}
				};
				form.setLocationByPlatform(true);
				form.setVisible(true);
			});
			JButton enterKouhiButton = new JButton("新規公費負担");
			enterKouhiButton.addActionListener(event -> {
				KouhiForm form = new KouhiForm(owner, "新規公費負担入力", new KouhiDTO()){
					@Override
					public void onEnter(KouhiDTO kouhiDTO){
						kouhiList.add(kouhiDTO);
						if( kouhiList.size() >= 3 ){
							enterKouhiButton.setEnabled(false);
						}
						updateHokenList();
					}
				};
				form.setLocationByPlatform(true);
				form.setVisible(true);
			});
			upperBox.add(enterKoukiButton);
			upperBox.add(enterKouhiButton);
		}
		panel.add(upperBox);
		panel.add(Box.createVerticalStrut(5));
		// JPanel lowerBox = new JPanel();
		// {
		// 	lowerBox.setLayout(new BoxLayout(lowerBox, BoxLayout.LINE_AXIS));
		// 	JCheckBox onlyCurrentBox = new JCheckBox("現在有効のみ");
		// 	onlyCurrentBox.setSelected(true);
		// 	lowerBox.add(onlyCurrentBox);
		// }
		// panel.add(lowerBox);
		return panel;
	}

	private Object[] makeHokenDataList(){
		List<Object> list = new ArrayList<Object>();
		if( shahokokuho != null ){
			list.add(shahokokuho);
		}
		if( koukikourei != null ){
			list.add(koukikourei);
		}
		kouhiList.forEach(hoken -> list.add(hoken));
		return list.toArray();
	}

	private void updateHokenList(){
		hokenList.setListData(makeHokenDataList());
	}

	private void doEditHoken(){
		Object obj = hokenList.getSelectedValue();
		if( obj == null ){
			return;
		} else {
			if( obj instanceof ShahokokuhoDTO ){
				ShahokokuhoForm form = new ShahokokuhoForm(owner, "新規社保国保入力", (ShahokokuhoDTO)obj){
					@Override
					public void onEnter(ShahokokuhoDTO shahokokuhoDTO){
						shahokokuho = shahokokuhoDTO;
						updateHokenList();
					}
				};
				form.setLocationByPlatform(true);
				form.setVisible(true);
			} else {
				return;
			}
		}
	}

}