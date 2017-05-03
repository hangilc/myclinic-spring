package jp.chang.myclinic;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jp.chang.myclinic.dto.ShahokokuhoDTO;

class ShahokokuhoForm extends JDialog {

	private ShahokokuhoDTO shahokokuhoDTO;
	private static String defaultGengou = "平成";
	private JTextField hokenshaBangouField = new JTextField(6);
	private JTextField kigouField = new JTextField(10);
	private JTextField bangouField = new JTextField(10);
	private JRadioButton honninButton = new JRadioButton("本人");
	private JRadioButton kazokuButton = new JRadioButton("家族");
	private DateInput validFromInput = new DateInput(new String[]{"平成"}).setGengou(defaultGengou);
	private DateInput validUptoInput = new DateInput(new String[]{"平成"}).setGengou(defaultGengou);
	private JRadioButton noKoureiButton = new JRadioButton("高齢でない");
	private JRadioButton kourei1wariButton = new JRadioButton("１割");
	private JRadioButton kourei2wariButton = new JRadioButton("２割");
	private JRadioButton kourei3wariButton = new JRadioButton("３割");

	ShahokokuhoForm(JDialog owner, String title, ShahokokuhoDTO shahokokuhoDTO){
		super(owner, title, true);
		init(shahokokuhoDTO);
	}

	ShahokokuhoForm(Window owner, String title, ShahokokuhoDTO shahokokuhoDTO){
		super(owner, title, Dialog.ModalityType.DOCUMENT_MODAL);
		init(shahokokuhoDTO);
	}

	private void init(ShahokokuhoDTO shahokokuhoDTO){
		this.shahokokuhoDTO = shahokokuhoDTO;
		setupCenter();
		setupSouth();
		setValue();
		pack();
	}

	public ShahokokuhoDTO getValue(){
		return shahokokuhoDTO;
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

	private void setValue(){
		if( shahokokuhoDTO.hokenshaBangou > 0 ){
			hokenshaBangouField.setText(String.valueOf(shahokokuhoDTO.hokenshaBangou));
		}
		if( !(shahokokuhoDTO.hihokenshaKigou == null || shahokokuhoDTO.hihokenshaKigou.isEmpty()) ){
			kigouField.setText(shahokokuhoDTO.hihokenshaKigou);
		}
		if( !(shahokokuhoDTO.hihokenshaBangou == null || shahokokuhoDTO.hihokenshaBangou.isEmpty()) ){
			bangouField.setText(shahokokuhoDTO.hihokenshaBangou);
		}
		if( shahokokuhoDTO.honnin != 0 ){
			honninButton.setSelected(true);
		} else {
			kazokuButton.setSelected(true);
		}
		setValidFrom(shahokokuhoDTO.validFrom);
		setValidUpto(shahokokuhoDTO.validUpto);
		switch(shahokokuhoDTO.kourei){
			case 1: kourei1wariButton.setSelected(true); break;
			case 2: kourei2wariButton.setSelected(true); break;
			case 3: kourei3wariButton.setSelected(true); break;
			default: noKoureiButton.setSelected(true); break;
		}
	}

	private void setupCenter(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 5);
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("保険者番号"), c);
		c.gridx = 1;
		c.gridy = 0;
		{
			panel.add(hokenshaBangouField, c);
		}
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel("被保険者"), c);
		c.gridx = 1;
		c.gridy = 1;
		{
			JPanel box = new JPanel();
			box.setLayout(new FlowLayout());
			box.add(new JLabel("記号"));
			box.add(kigouField);
			box.add(new JLabel("番号"));
			box.add(bangouField);
			panel.add(box, c);
		}
		c.gridx = 0;
		c.gridy = 2;
		panel.add(new JLabel("本人・家族"), c);
		c.gridx = 1;
		c.gridy = 2;
		{
			JPanel box = new JPanel();
			box.setLayout(new FlowLayout());
			kazokuButton.setSelected(true);
			ButtonGroup honninGroup = new ButtonGroup();
			honninGroup.add(honninButton);
			honninGroup.add(kazokuButton);
			box.add(honninButton);
			box.add(kazokuButton);
			panel.add(box, c);
		}
		c.gridx = 0;
		c.gridy = 3;
		panel.add(new JLabel("資格取得日"), c);
		c.gridx = 1;
		c.gridy = 3;
		{
			panel.add(validFromInput, c);
		}
		c.gridx = 0;
		c.gridy = 4;
		panel.add(new JLabel("有効期限"), c);
		c.gridx = 1;
		c.gridy = 4;
		{
			panel.add(validUptoInput, c);
		}
		c.gridx = 0;
		c.gridy = 5;
		panel.add(new JLabel("高齢"), c);
		c.gridx = 1;
		c.gridy = 5;
		{
			JPanel box = new JPanel();
			noKoureiButton.setSelected(true);
			ButtonGroup koureiGroup = new ButtonGroup();
			koureiGroup.add(noKoureiButton);
			koureiGroup.add(kourei1wariButton);
			koureiGroup.add(kourei2wariButton);
			koureiGroup.add(kourei3wariButton);
			box.add(noKoureiButton);
			box.add(kourei1wariButton);
			box.add(kourei2wariButton);
			box.add(kourei3wariButton);
			panel.add(box, c);
		}

		add(panel, BorderLayout.CENTER);
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		JButton enterButton = new JButton("入力");
		enterButton.addActionListener(event -> {
			if( updateValue() ){
				dispose();
				onEnter(shahokokuhoDTO);
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

	private int getKoureiValue(){
		if( kourei1wariButton.isSelected() ){
			return 1;
		} else if( kourei2wariButton.isSelected() ){
			return 2;
		} else if( kourei3wariButton.isSelected() ){
			return 3;
		} else {
			return 0;
		}
	}

	private boolean updateValue(){
		int hokenshaBangou;
		try {
			hokenshaBangou = Integer.parseInt(hokenshaBangouField.getText());
		} catch(NumberFormatException ex){
			JOptionPane.showMessageDialog(this, "保険者番号の入力が不適切です。");
			return false;
		}
		String kigou = kigouField.getText();
		String bangou = bangouField.getText();
		if( kigou.isEmpty() && bangou.isEmpty() ){
			JOptionPane.showMessageDialog(this, "被保険者記号と被保険者番号が両方空白です。");
			return false;
		}
		int honnin = 0;
		if( honninButton.isSelected() ){
			honnin = 1;
		}
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
		int kourei = getKoureiValue();
		shahokokuhoDTO.hokenshaBangou = hokenshaBangou;
		shahokokuhoDTO.hihokenshaKigou = kigou;
		shahokokuhoDTO.hihokenshaBangou = bangou;
		shahokokuhoDTO.honnin = honnin;
		shahokokuhoDTO.validFrom = validFromValue;
		shahokokuhoDTO.validUpto = validUptoValue;
		shahokokuhoDTO.kourei = kourei;
		return true;
	}

	protected void onEnter(ShahokokuhoDTO shahokokuhoDTO){

	}

	protected void onCancel(){

	}
}