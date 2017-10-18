package jp.chang.myclinic;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.receipt.ReceiptDrawer;
import jp.chang.myclinic.drawer.receipt.ReceiptDrawerData;
import jp.chang.myclinic.dto.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class SearchPaymentDialog extends JDialog {

	private JButton recentPaymentsButton = new JButton("最近の会計");
	private JTextField searchTextField = new JTextField(8);
	private JButton searchButton = new JButton("検索");
	private SearchPaymentResultList resultList = new SearchPaymentResultList();
	private JButton reprintReceiptButton = new JButton("領収書再発行");
	private JButton dispMeisaiButton = new JButton("明細情報表示");
	private JButton closeButton = new JButton("閉じる");

	SearchPaymentDialog(Window owner) {
		super(owner, "会計検索", Dialog.ModalityType.DOCUMENT_MODAL);
		setLayout(new MigLayout("fill", "[grow]", "[] [grow]"));
		add(makeSearchInputPane(), "wrap");
		add(makeSearchResultPane(), "grow, wrap");
		add(makeCommandPane(), "wrap");
		add(closeButton, "right");
		bind();
		pack();
	}

	private JComponent makeSearchInputPane() {
		JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
		panel.add(recentPaymentsButton, "wrap, span 3");
		panel.add(new JLabel("患者番号"));
		panel.add(searchTextField, "");
		panel.add(searchButton);
		return panel;
	}

	private JComponent makeCommandPane(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
		panel.add(reprintReceiptButton);
		panel.add(dispMeisaiButton);
		return panel;
	}

	private JComponent makeSearchResultPane() {
		JScrollPane scroll = new JScrollPane(resultList);
		scroll.setPreferredSize(new Dimension(400, 300));
		return scroll;
	}

	private void bind() {
		recentPaymentsButton.addActionListener(event -> {
			Service.api.listRecentPayment(50)
					.whenComplete((result, t) -> {
						if( t != null ){
							t.printStackTrace();
							EventQueue.invokeLater(() -> {
								alert(t.toString());
							});
							return;
						}
						EventQueue.invokeLater(() -> {
							resultList.setListData(result.toArray(new PaymentVisitPatientDTO[0]));
						});
					});
		});
		reprintReceiptButton.addActionListener(event -> {
			PaymentVisitPatientDTO selection = resultList.getSelectedValue();
			if( selection == null ){
				return;
			}
			int visitId = selection.payment.visitId;
			final DataStore dataStore = new DataStore();
			Service.api.getClinicInfo()
					.thenCompose((ClinicInfoDTO clinicInfo) -> {
						dataStore.clinicInfo = clinicInfo;
						return Service.api.findCharge(visitId);
					})
					.thenCompose((ChargeOptionalDTO optCharge) -> {
						dataStore.charge = optCharge.charge;
						return Service.api.getVisitMeisai(visitId);
					})
					.thenAccept((MeisaiDTO meisai) -> {
						ReceiptDrawerData data = ReceiptDrawerDataCreator.create(dataStore.getChargeValue(),
								selection.patient, selection.visit, meisai, dataStore.clinicInfo);
						ReceiptDrawer receiptDrawer = new ReceiptDrawer(data);
						final List<Op> ops = receiptDrawer.getOps();
						EventQueue.invokeLater(() -> {
							ReceiptPreviewDialog dialog = new ReceiptPreviewDialog(this, ops);
							dialog.setLocationByPlatform(true);
							dialog.setVisible(true);
						});
					})
					.exceptionally(t -> {
						t.printStackTrace();
						EventQueue.invokeLater(() -> {
							alert(t.toString());
						});
						return null;
					});
		});
		dispMeisaiButton.addActionListener(event -> doDispMeisai());
		searchButton.addActionListener(event -> doSearch());
		closeButton.addActionListener(event -> {
			dispose();
		});
	}

	private static class DataStore {
		ChargeDTO charge;
		ClinicInfoDTO clinicInfo;

		int getChargeValue(){
			return charge == null ? 0 : charge.charge;
		}
	}

	private void doDispMeisai() {
		PaymentVisitPatientDTO selection = resultList.getSelectedValue();
		if( selection == null ){
			return;
		}
		PatientDTO patient = selection.patient;
		int visitId = selection.payment.visitId;
		final DataStore dataStore = new DataStore();
		Service.api.findCharge(visitId)
				.thenCompose(charge -> {
					dataStore.charge = charge.charge;
					if( dataStore.charge == null ){
						throw new RuntimeException("cannot find charge");
					}
					return Service.api.getVisitMeisai(visitId);
				})
				.thenAccept(meisai -> EventQueue.invokeLater(() ->{
					MeisaiDetailDialog meisaiDetailDialog = new MeisaiDetailDialog(this, meisai, patient, selection.visit);
					meisaiDetailDialog.setLocationByPlatform(true);
					meisaiDetailDialog.setVisible(true);
				}))
				.exceptionally(t -> {
					t.printStackTrace();
					EventQueue.invokeLater(() -> {
						alert(t.toString());
					});
					return null;
				});
	}

	private void doSearch(){
		String searchText = searchTextField.getText();
		System.out.println("searchText: " + searchText);
		if( searchText.isEmpty() ){
			return;
		}
		try {
			int patientId = Integer.parseInt(searchText);
			Service.api.listPaymentByPatient(patientId, 30)
					.whenComplete((result, t) -> {
						if( t != null ){
							t.printStackTrace();
							EventQueue.invokeLater(() -> {
								alert(t.toString());
							});
							return;
						}
						EventQueue.invokeLater(() -> {
							resultList.setListData(result.toArray(new PaymentVisitPatientDTO[0]));
						});
					});
		} catch (NumberFormatException e) {
			e.printStackTrace();
			alert("患者番号の入力が不適切です。");
		}
	}

	private void alert(String message){
		JOptionPane.showMessageDialog(this, message);
	}

}