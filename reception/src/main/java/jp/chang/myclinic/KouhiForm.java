package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import jp.chang.myclinic.dto.KouhiDTO;

class KouhiForm extends JDialog {

	private KouhiDTO kouhiDTO;
	private String defaultGengou = "平成";
	private JTextField futanshaField = new JTextField(6);
	private JTextField jukyuushaField = new JTextField(6);
	private DateInput validFromInput = new DateInput(new String[]{"平成"}).setGengou(defaultGengou);
	private DateInput validUptoInput = new DateInput(new String[]{"平成"}).setGengou(defaultGengou);

	KouhiForm(JDialog owner, String title, KouhiDTO kouhiDTO){
		super(owner, title, true);
		init(kouhiDTO);
	}

	KouhiForm(Window owner, String title, KouhiDTO kouhiDTO){
		super(owner, title, Dialog.ModalityType.DOCUMENT_MODAL);
		init(kouhiDTO);
	}

	private void init(KouhiDTO kouhiDTO){
		this.kouhiDTO = kouhiDTO;
		setupCenter();
		setupSouth();
		setValue();
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
		panel.add(new JLabel("負担者番号"), c);
		c.gridx = 1;
		{
			panel.add(futanshaField, c);
		}
		
		c.gridy = 1;
		c.gridx = 0;
		panel.add(new JLabel("受給者番号"), c);
		c.gridx = 1;
		{
			panel.add(jukyuushaField, c);
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

		add(panel, BorderLayout.CENTER);
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		JButton enterButton = new JButton("入力");
		enterButton.addActionListener(event -> {
			if( updateValue() ){
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
		int futansha, jukyuusha;
		String validFrom, validUpto;
		if( kouhiDTO.futansha > 0 ){
			futanshaField.setText(String.valueOf(kouhiDTO.futansha));
		}
		if( kouhiDTO.jukyuusha > 0 ){
			jukyuushaField.setText(String.valueOf(kouhiDTO.jukyuusha));
		}
		setValidFrom(kouhiDTO.validFrom);
		setValidUpto(kouhiDTO.validUpto);
	}

	private boolean updateValue(){
		int futansha;
		int jukyuusha;
		String validFrom;
		String validUpto;
		try {
			futansha = Integer.parseInt(futanshaField.getText());
		} catch(NumberFormatException ex){
			JOptionPane.showMessageDialog(this, "負担者番号の入力が適切でありません。");
			return false;
		}
		try {
			jukyuusha = Integer.parseInt(jukyuushaField.getText());
		} catch(NumberFormatException ex){
			JOptionPane.showMessageDialog(this, "受給者番号の入力が適切でありません。");
			return false;
		}
		LocalDate validFromDate = null;
		try{
			validFromDate = validFromInput.getValue();
		} catch(DateInput.DateInputException ex){
			JOptionPane.showMessageDialog(this, "資格取得日の入力が不適切です。\n" + ex.getMessage());
			return false;
		}
		validFrom = DateTimeFormatter.ISO_LOCAL_DATE.format(validFromDate);
		LocalDate validUptoDate = null;
		if( !validUptoInput.isEmpty() ){
			try{
				validUptoDate = validUptoInput.getValue();
			} catch(DateInput.DateInputException ex){
				JOptionPane.showMessageDialog(this, "有効期限の入力が不適切です。\n" + ex.getMessage());
				return false;
			}
		}
		validUpto = validUptoDate == null ? "0000-00-00" : DateTimeFormatter.ISO_LOCAL_DATE.format(validUptoDate);
		kouhiDTO.futansha = futansha;
		kouhiDTO.jukyuusha = jukyuusha;
		kouhiDTO.validFrom = validFrom;
		kouhiDTO.validUpto = validUpto;
		return true;
	}

	protected void onEnter(){ }

	protected void onCancel(){ }

}