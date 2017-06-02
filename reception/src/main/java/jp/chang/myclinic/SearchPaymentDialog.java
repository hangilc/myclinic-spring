package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.receipt.ReceiptDrawer;
import jp.chang.myclinic.drawer.receipt.ReceiptDrawerData;
import jp.chang.myclinic.dto.*;
import net.miginfocom.swing.MigLayout;

class SearchPaymentDialog extends JDialog {

	private JButton recentPaymentsButton = new JButton("最近の会計");
	private JTextField searchTextField = new JTextField(8);
	private JButton searchButton = new JButton("検索");
	private SearchPaymentResultList resultList = new SearchPaymentResultList();
	private JButton reprintReceiptButton = new JButton("領収書再発行");
	private JButton closeButton = new JButton("閉じる");

	SearchPaymentDialog(Window owner) {
		super(owner, "会計検索", Dialog.ModalityType.DOCUMENT_MODAL);
		setLayout(new MigLayout("fill", "[grow]", "[] [grow]"));
		add(makeSearchInputPane(), "wrap");
		add(makeSearchResultPane(), "grow, wrap");
		add(reprintReceiptButton, "wrap");
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
		searchButton.addActionListener(event -> doSearch());
		closeButton.addActionListener(event -> {
			dispose();
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
			System.out.println(patientId);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			alert("患者番号の入力が不適切です。");
		}
	}

	private void alert(String message){
		JOptionPane.showMessageDialog(this, message);
	}

	private static class DataStore {
		ChargeDTO charge;
		ClinicInfoDTO clinicInfo;

		int getChargeValue(){
			return charge == null ? 0 : charge.charge;
		}
	}
}