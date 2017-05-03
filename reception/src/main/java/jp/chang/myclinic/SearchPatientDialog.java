package jp.chang.myclinic;

import jp.chang.myclinic.dto.PatientDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import net.miginfocom.swing.MigLayout;
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
	private JButton searchByNameButton = new JButton("検索");
	private JButton searchByYomiButton = new JButton("検索");
	private JButton recentButton = new JButton("最近の登録");
	private JList<PatientDTO> resultList = new JList<>();
	private JButton editButton = new JButton("編集");
	private JButton registerButton = new JButton("診療受付");
	private PatientInfo infoArea = new PatientInfo();
	private JButton closeButton = new JButton("閉じる	");

	SearchPatientDialog(){
		setTitle("患者検索");
		setupUI();
		bind();
		pack();
	}

	private void setupUI(){
		setLayout(new MigLayout("fill, wrap 1", "[grow]", "[] [grow] [grow] []"));
		add(makeSearchTextInput());
		add(makeSearchResult(), "grow");
		add(makeInfo(), "grow");
		add(makeSouth(), "right");
	}

	private void bind(){
		lastNameTextField.addActionListener(this::onSearchByName);
		firstNameTextField.addActionListener(this::onSearchByName);
		searchByNameButton.addActionListener(this::onSearchByName);
		lastNameYomiTextField.addActionListener(this::onSearchByYomi);
		firstNameYomiTextField.addActionListener(this::onSearchByYomi);
		searchByYomiButton.addActionListener(this::onSearchByYomi);
		recentButton.addActionListener(this::onRecentPatients);
		bindList();
		closeButton.addActionListener(event -> dispose());
	}

	private void bindList(){
		resultList.addListSelectionListener(event -> {
			if( event.getValueIsAdjusting() ){
				PatientDTO select = resultList.getSelectedValue();
				setInfo(select);
			}
		});
	}

	private JComponent makeSearchTextInput(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "[right] [grow]", ""));
		panel.add(new JLabel("名前"));
		panel.add(lastNameTextField, "split 3");
		panel.add(firstNameTextField);
		panel.add(searchByNameButton, "wrap");
		panel.add(new JLabel("よみ"));
		panel.add(lastNameYomiTextField, "split 3");
		panel.add(firstNameYomiTextField);
		panel.add(searchByYomiButton, "wrap");
		panel.add(recentButton, "left, span 2");
		return panel;
	}

	private JComponent makeSearchResult(){
		resultList.setCellRenderer(new Renderer());
		resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JPanel panel = new JPanel(new MigLayout("insets 0", "[grow] []", "[grow]"));
		JScrollPane scroll = new JScrollPane(resultList);
		panel.add(scroll, "grow, width 260, height 300");
		return panel;
	}

	private JComponent makeInfo(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", ""));
		panel.add(infoArea, "grow");
		panel.add(editButton, "top, flowy, split 2, sizegroup btn");
		panel.add(registerButton, "sizegroup btn");
		return panel;
	}

	private JComponent makeSouth(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
		panel.add(closeButton);
		return panel;
	}

	private void setInfo(PatientDTO data){
		infoArea.setPatient(data);
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