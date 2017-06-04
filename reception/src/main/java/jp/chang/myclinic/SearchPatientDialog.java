package jp.chang.myclinic;

import jp.chang.myclinic.dto.PatientDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

class SearchPatientDialog extends JDialog {

	private JTextField lastNameTextField = new JTextField(6);
	private JTextField firstNameTextField = new JTextField(6);
	private JTextField lastNameYomiTextField = new JTextField(6);
	private JTextField firstNameYomiTextField = new JTextField(6);
	private JButton searchByNameButton = new JButton("検索");
	private JButton searchByYomiButton = new JButton("検索");
	private JButton recentButton = new JButton("最近の登録");
	private DefaultListModel<PatientDTO> listModel = new DefaultListModel<>();
	private JList<PatientDTO> resultList;
	private JButton editButton = new JButton("編集");
	private JButton registerButton = new JButton("診療受付");
	private PatientInfo infoArea = new PatientInfo();
	private JButton closeButton = new JButton("閉じる	");
	private PatientDTO currentPatientDTO;

	SearchPatientDialog(){
		setTitle("患者検索");
		resultList = new JList<PatientDTO>(listModel);
		resultList.setCellRenderer(new Renderer());
		resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setupUI();
		bind();
		pack();
		Broadcast.patientModified.addListener(this, this::onPatientModified);
	}

	private void onPatientModified(PatientDTO newPatientDTO){
		if( currentPatientDTO != null && Objects.equals(currentPatientDTO.patientId, newPatientDTO.patientId) ){
			setInfo(newPatientDTO);
			int n = listModel.getSize();
			for(int i=0;i<n;i++){
				PatientDTO element = listModel.getElementAt(i);
				if( Objects.equals(element.patientId, newPatientDTO.patientId) ){
					listModel.setElementAt(newPatientDTO, i);
					break;
				}
			}
		}
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
		editButton.addActionListener(this::onEdit);
		closeButton.addActionListener(event -> dispose());
	}

	private void bindList(){
		resultList.addListSelectionListener(event -> {
			if( event.getValueIsAdjusting() == false ){
				PatientDTO select = resultList.getSelectedValue();
				if( select != null ) {
					setInfo(select);
				}
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
		JPanel panel = new JPanel(new MigLayout("insets 0", "[grow] []", "[grow]"));
		JScrollPane scroll = new JScrollPane(resultList);
		panel.add(scroll, "grow, width 260, height 300");
		return panel;
	}

	private JComponent makeInfo(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", ""));
		JScrollPane sp = new JScrollPane(infoArea);
		sp.setBorder(BorderFactory.createEmptyBorder());
		panel.add(sp, "w 260, h 160, grow");
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
		currentPatientDTO = data;
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

	private void onEdit(ActionEvent event){
		if( currentPatientDTO == null ){
			return;
		}
		PatientEditorRegistry.INSTANCE.openPatientEditor(currentPatientDTO);
//		PatientDialog editor = PatientEditorRegistry.INSTANCE.find(currentPatientDTO.patientId);
//		if( editor != null ){
//			editor.toFront();
//			return;
//		}
//		Service.api.listHoken(currentPatientDTO.patientId)
//			.whenComplete((result, t) -> {
//				if( t != null ){
//					t.printStackTrace();
//					alert("保険情報を取得できませんでした。" + t);
//					return;
//				}
//				EventQueue.invokeLater(() -> {
//					PatientDialog dialog = new PatientDialog("患者情報の編集", currentPatientDTO, result);
//					PatientEditorRegistry.INSTANCE.register(currentPatientDTO.patientId, dialog);
//					dialog.setVisible(true);
//				});
//			});
	}

	private void handleResult(CompletableFuture<List<PatientDTO>> future){
		future.whenComplete((List<PatientDTO> result, Throwable t) -> {
			if( t != null ){
				t.printStackTrace();
				JOptionPane.showMessageDialog(this, "サーバーからデータを取得できませんでした。" + t);
				return;
			}
			EventQueue.invokeLater(() -> setResult(result));
		});
	}

	private void setResult(List<PatientDTO> result){
		listModel.clear();
		result.forEach(patientDTO -> listModel.addElement(patientDTO));
	}

	private void alert(String message){
		JOptionPane.showMessageDialog(this, message);
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