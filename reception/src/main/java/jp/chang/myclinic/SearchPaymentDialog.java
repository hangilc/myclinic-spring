package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.receipt.ReceiptDrawer;
import jp.chang.myclinic.drawer.receipt.ReceiptDrawerData;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.PaymentVisitPatientDTO;
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
		add(makeSearchInputPane(), "grow, wrap");
		add(makeSearchResultPane(), "grow, wrap");
		add(reprintReceiptButton, "wrap");
		add(closeButton, "right");
		bind();
		pack();
	}

	private JComponent makeSearchInputPane() {
		JPanel panel = new JPanel(new MigLayout("insets 0, fill", "[] [grow] []", ""));
		panel.add(recentPaymentsButton, "wrap, span 3");
		panel.add(new JLabel("患者番号"));
		panel.add(searchTextField, "grow");
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
			final DataStore dataStore = new DataStore();

			Service.api.getVisitMeisai(selection.payment.visitId)
					.thenAccept((MeisaiDTO meisai) -> {
						ReceiptDrawerData data = ReceiptDrawerDataCreator.create(selection.patient, selection.visit, meisai);
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
		closeButton.addActionListener(event -> {
			dispose();
		});
	}

	private void alert(String message){
		JOptionPane.showMessageDialog(this, message);
	}

	private static class DataStore {
		HokenDTO hoken;
	}
}