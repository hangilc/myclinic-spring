package jp.chang.myclinic;

import jp.chang.myclinic.dto.PatientDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.chrono.JapaneseDate;
import java.time.temporal.ChronoUnit;

class SearchPatientDialog extends JDialog {

	private JTextField lastNameTextField = new JTextField(6);
	private JTextField firstNameTextField = new JTextField(6);
	private JTextField lastNameYomiTextField = new JTextField(6);
	private JTextField firstNameYomiTextField = new JTextField(6);
	private JList<PatientDTO> resultList = new JList<>();
	private JTextArea infoArea = new JTextArea(7, 44);
	private static DateTimeFormatter birthdayFormatter = DateTimeFormatter.ofPattern("Gyy年MM月dd日");

	SearchPatientDialog(){
		setTitle("患者検索");
		setupCenter();
		setupSouth();
		pack();
	}

	private void setupCenter(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(centerBox1());
		add(panel, BorderLayout.CENTER);
	}

	private JComponent centerBox1(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 5, 5);
		c.anchor = GridBagConstraints.LINE_START;
		c.gridy = 0;
		panel.add(new JLabel("名前"), c);
		c.gridx = 1;
		{
			JPanel box = new JPanel();
			box.setLayout(new BoxLayout(box, BoxLayout.LINE_AXIS));
			box.add(lastNameTextField);
			box.add(Box.createHorizontalStrut(5));
			box.add(firstNameTextField);
			box.add(Box.createHorizontalStrut(5));
			JButton searchByNameButton = new JButton("検索");
			searchByNameButton.addActionListener(this::onSearchByName);
			box.add(searchByNameButton);
			box.add(Box.createHorizontalGlue());
			panel.add(box, c);
		}
		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel("よみ"), c);
		c.gridx = 1;
		{
			JPanel box = new JPanel();
			box.setLayout(new BoxLayout(box, BoxLayout.LINE_AXIS));
			box.add(lastNameYomiTextField);
			box.add(Box.createHorizontalStrut(5));
			box.add(firstNameYomiTextField);
			box.add(Box.createHorizontalStrut(5));
			JButton searchByYomiButton = new JButton("検索");
			searchByYomiButton.addActionListener(this::onSearchByYomi);
			box.add(searchByYomiButton);
			box.add(Box.createHorizontalGlue());
			panel.add(box, c);
		}
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		{
			JButton recentButton = new JButton("最近の登録");
			recentButton.addActionListener(this::onRecentPatients);
			panel.add(recentButton, c);
		}
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		{
			resultList.setCellRenderer(new Renderer());
			resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			resultList.addListSelectionListener(event -> {
				if( event.getValueIsAdjusting() ){
					PatientDTO select = resultList.getSelectedValue();
					setInfo(select);
				}
			});
			JScrollPane scroll = new JScrollPane(resultList);
			scroll.setPreferredSize(new Dimension(400, 260));
			panel.add(scroll, c);
		}
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		{
			JPanel box = new JPanel();
			box.setLayout(new BoxLayout(box, BoxLayout.LINE_AXIS));
			infoArea.setEditable(false);
			JScrollPane scroll = new JScrollPane(infoArea);
			JPanel commandBox = new JPanel();
			commandBox.setLayout(new BoxLayout(commandBox, BoxLayout.PAGE_AXIS));
			JButton patientInfoButton = new JButton("患者情報");
			JButton registerButton = new JButton("診療受付");
			commandBox.add(patientInfoButton);
			commandBox.add(Box.createVerticalStrut(5));
			commandBox.add(registerButton);
			commandBox.add(Box.createVerticalGlue());
			box.add(scroll);
			box.add(Box.createHorizontalStrut(5));
			box.add(commandBox);
			panel.add(box, c);
		}
		return panel;
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		JButton closeButton = new JButton("閉じる");
		closeButton.addActionListener(event -> dispose());
		panel.add(closeButton);
		add(panel, BorderLayout.SOUTH);
	}

	private String makeBirthdayPart(String dateString){
		if( dateString == null || dateString.isEmpty() || "0000-00-00".equals(dateString) ){
			return null;
		} else {
			LocalDate birthday = LocalDate.parse(dateString);
			JapaneseDate jd = JapaneseDate.from(birthday);
			long age = birthday.until(LocalDate.now(), ChronoUnit.YEARS);
			return String.format("%s（%d 才）", birthdayFormatter.format(jd), age);
		}
	}

	private void setInfo(PatientDTO data){
		String birthdayPart = makeBirthdayPart(data.birthday);

		infoArea.setText("");
		infoArea.append(String.format("患者番号： %d\n", data.patientId));
		infoArea.append(String.format("名前： %s %s\n", data.lastName, data.firstName));
		infoArea.append(String.format("よみ： %s %s\n", data.lastNameYomi, data.firstNameYomi));
		infoArea.append(String.format("生年月日： %s\n", birthdayPart == null ? "（不明）" : birthdayPart));
		infoArea.append(String.format("性別： %s性\n", "M".equals(data.sex) ? "男" : "女"));
		infoArea.append(String.format("住所： %s\n", data.address));
		infoArea.append(String.format("電話： %s", data.phone));
	}

	private void onSearchByName(ActionEvent event){
		String lastName = lastNameTextField.getText();
		String firstName = firstNameTextField.getText();
		handleResult(Service.api.searchPatientByName(lastName, firstName));
	}

	private void onSearchByYomi(ActionEvent event){
		String lastNameYomi = lastNameYomiTextField.getText();
		String firstNameYomi = firstNameYomiTextField.getText();
		handleResult(Service.api.searchPatientByYomi(lastNameYomi, firstNameYomi));
	}

	private void onRecentPatients(ActionEvent event){
		handleResult(Service.api.listRecentlyRegisteredPatients());
	}

	private void handleResult(CompletableFuture<List<PatientDTO>> future){
		future.whenComplete((List<PatientDTO> result, Throwable t) -> {
			if( t != null ){
				t.printStackTrace();
				JOptionPane.showMessageDialog(this, "サーバーからデータを取得できませんでした。" + t);
				return;
			}
			setResult(result);
		});
	}

	private void setResult(List<PatientDTO> result){
		PatientDTO[] arr = new PatientDTO[result.size()];
		for(int i=0;i<result.size();i++){
			arr[i] = result.get(i);
		}
		resultList.setListData(arr);
	}

	static class Renderer extends JLabel implements ListCellRenderer<PatientDTO> {

		Renderer(){
			setOpaque(true);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends PatientDTO> list, PatientDTO value, int index,
			boolean isSelected, boolean cellHasFocus){
			setText(makeLabel(value));
			if( isSelected ){
				setBackground(list.getSelectionBackground());
			} else {
				setBackground(list.getBackground());
			}
			return this;
		}

		private String makeLabel(PatientDTO patientDTO){
			return String.format("[%04d] %s %s （%s %s）", patientDTO.patientId, patientDTO.lastName, patientDTO.firstName,
				patientDTO.lastNameYomi, patientDTO.firstNameYomi);
		}
	}

}