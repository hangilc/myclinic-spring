package jp.chang.myclinic.reception;

import java.awt.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jp.chang.myclinic.dto.KoukikoureiDTO;

class KoukikoureiForm extends JDialog {

	private KoukikoureiDTO koukikoureiDTO;
	private static String defaultGengou = "平成";
	private JTextField hokenshaBangouField = new JTextField(6);
	private JTextField hihokenshaBangouField = new JTextField(6);
	private JRadioButton futan1wariButton = new JRadioButton("１割");
	private JRadioButton futan2wariButton = new JRadioButton("２割");
	private JRadioButton futan3wariButton = new JRadioButton("３割");
	private DateInput validFromInput = new DateInput(new String[]{"平成"}).setGengou(defaultGengou);
	private DateInput validUptoInput = new DateInput(new String[]{"平成"}).setGengou(defaultGengou);

	KoukikoureiForm(JDialog owner, String title, KoukikoureiDTO koukikoureiDTO){
		super(owner, title, true);
		init(koukikoureiDTO);
	}

	KoukikoureiForm(Window owner, String title, KoukikoureiDTO koukikoureiDTO){
		super(owner, title, Dialog.ModalityType.DOCUMENT_MODAL);
		init(koukikoureiDTO);
	}

	private void init(KoukikoureiDTO koukikoureiDTO){
		this.koukikoureiDTO = koukikoureiDTO;
		setupCenter();
		setupSouth();
		setValue(koukikoureiDTO);
		pack();
	}

	private void setupCenter(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 5);
		c.anchor = GridBagConstraints.LINE_START;

		c.gridy = 0;
		c.gridx = 0;
		panel.add(new JLabel("保険者番号"), c);
		c.gridx = 1;
		{
			panel.add(hokenshaBangouField, c);
		}
		
		c.gridy = 1;
		c.gridx = 0;
		panel.add(new JLabel("被保険者番号"), c);
		c.gridx = 1;
		{
			panel.add(hihokenshaBangouField, c);
		}

		c.gridy = 2;
		c.gridx = 0;
		panel.add(new JLabel("資格取得日"), c);
		c.gridx = 1;
		{
			panel.add(validFromInput, c);
		}

		c.gridy = 3;
		c.gridx = 0;
		panel.add(new JLabel("有効期限"), c);
		c.gridx = 1;
		{
			panel.add(validUptoInput, c);
		}

		c.gridy = 4;
		c.gridx = 0;
		panel.add(new JLabel("負担割"), c);
		c.gridx = 1;
		{
			JPanel box = new JPanel();
			futan1wariButton.setSelected(true);
			ButtonGroup futanGroup = new ButtonGroup();
			futanGroup.add(futan1wariButton);
			futanGroup.add(futan2wariButton);
			futanGroup.add(futan3wariButton);
			box.add(futan1wariButton);
			box.add(futan2wariButton);
			box.add(futan3wariButton);
			panel.add(box, c);
		}

		add(panel, BorderLayout.CENTER);
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		JButton enterButton = new JButton("入力");
		enterButton.addActionListener(event -> {
			if( doUpdate() ){
				dispose();
				onEnter();
			}
		});
		JButton cancelButton = new JButton("キャンセル");
		cancelButton.addActionListener(event -> {
			dispose();
			onCancel();
		});
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		panel.add(enterButton);
		panel.add(Box.createHorizontalStrut(5));
		panel.add(cancelButton);
		add(panel, BorderLayout.SOUTH);
	}

	private void setFutanWari(int futanWari){
		switch(futanWari){
			case 1: futan1wariButton.setSelected(true); break;
			case 2: futan2wariButton.setSelected(true); break;
			case 3: futan3wariButton.setSelected(true); break;
			default: futan1wariButton.setSelected(true); break;
		}
	}

	private void setValidFrom(String validFrom){
		if( validFrom != null && !validFrom.isEmpty() ){
			LocalDate validFromDate = LocalDate.parse(validFrom, DateTimeFormatter.ISO_LOCAL_DATE);
			validFromInput.setValue(validFromDate);
		} else {
			validFromInput.clear();
			validFromInput.setGengou(defaultGengou);
		}
	}

	private void setValidUpto(String validUpto){
		if( validUpto != null && !validUpto.isEmpty() && !validUpto.equals("0000-00-00") ){
			LocalDate validUptoDate = LocalDate.parse(validUpto, DateTimeFormatter.ISO_LOCAL_DATE);
			validUptoInput.setValue(validUptoDate);
		} else {
			validUptoInput.clear();
			validUptoInput.setGengou(defaultGengou);
		}
	}

	private void setValue(KoukikoureiDTO koukikoureiDTO){
		hokenshaBangouField.setText(koukikoureiDTO.hokenshaBangou);
		hihokenshaBangouField.setText(koukikoureiDTO.hihokenshaBangou);
		setFutanWari(koukikoureiDTO.futanWari);
		setValidFrom(koukikoureiDTO.validFrom);
		setValidUpto(koukikoureiDTO.validUpto);
	}

	private int getFutanWari(){
		if( futan2wariButton.isSelected() ){
			return 2;
		} else if( futan3wariButton.isSelected() ){
			return 3;
		} else {
			return 1;
		}
	}

	private
	boolean doUpdate(){
		String hokenshaBangou = hokenshaBangouField.getText();
		if( hokenshaBangou.isEmpty() ){
			JOptionPane.showMessageDialog(this, "保険者番号の入力がありません。");
			return false;
		}
		String hihokenshaBangou = hihokenshaBangouField.getText();
		if( hihokenshaBangou.isEmpty() ){
			JOptionPane.showMessageDialog(this, "被保険者番号の入力がありません。");
			return false;
		}
		int futanWari = getFutanWari();
		LocalDate validFrom = null;
		try{
			validFrom = validFromInput.getValue();
		} catch(DateInput.DateInputException ex){
			JOptionPane.showMessageDialog(this, "資格取得日の入力が不適切です。\n" + ex.getMessage());
			return false;
		}
		String validFromValue = DateTimeFormatter.ISO_LOCAL_DATE.format(validFrom);
		LocalDate validUpto = null;
		if( !validUptoInput.isEmpty() ){
			try{
				validUpto = validUptoInput.getValue();
			} catch(DateInput.DateInputException ex){
				JOptionPane.showMessageDialog(this, "有効期限の入力が不適切です。\n" + ex.getMessage());
				return false;
			}
		}
		String validUptoValue = validUpto == null ? "0000-00-00" : DateTimeFormatter.ISO_LOCAL_DATE.format(validUpto);
		koukikoureiDTO.hokenshaBangou = hokenshaBangou;
		koukikoureiDTO.hihokenshaBangou = hihokenshaBangou;
		koukikoureiDTO.futanWari = futanWari;
		koukikoureiDTO.validFrom = validFromValue;
		koukikoureiDTO.validUpto = validUptoValue;
		return true;
	}

	protected
	void onEnter(){ }

	protected
	void onCancel(){ }

}