package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

class SearchPaymentDialog extends JDialog {

	private JButton recentPaymentsButton = new JButton("最近の会計");
	private JTextField searchTextField = new JTextField(8);
	private JButton searchButton = new JButton("検索");
	private JButton reprintReceiptButton = new JButton("領収書再発行");

	SearchPaymentDialog(Window owner){
		super(owner, "会計検索", Dialog.ModalityType.DOCUMENT_MODAL);
		setLayout(new MigLayout("fill", "[grow]", "[] [grow]"));
		add(makeSearchInputPane(), "grow, wrap");
		add(makeSearchResultPane(), "grow, wrap");
		add(reprintReceiptButton);
		//setupCenter();
		//setupSouth();
		pack();
	}

	private JComponent makeSearchInputPane(){
		JPanel panel = new JPanel(new MigLayout("insets 0, fill", "[] [grow] []", ""));
		panel.add(recentPaymentsButton, "wrap, span 3");
		panel.add(new JLabel("患者番号"));
		panel.add(searchTextField, "grow");
		panel.add(searchButton);
		return panel;
	}

	private JComponent makeSearchResultPane(){
		JList list = new JList();
		JScrollPane scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(400, 300));
		return scroll;
	}

	private JComponent makeSouth(){
		return null;
	}

	private void setupCenter(){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		{
			JButton recentPaymentsButton = new JButton("最近の会計");
			recentPaymentsButton.setAlignmentX(Component.LEFT_ALIGNMENT);
			panel.add(recentPaymentsButton);
		}
		panel.add(Box.createVerticalStrut(5));
		{
			JPanel box = new JPanel();
			box.setLayout(new BoxLayout(box, BoxLayout.LINE_AXIS));
			box.add(new JLabel("患者番号"));
			box.add(Box.createHorizontalStrut(5));
			JTextField searchTextField = new JTextField(8);
			box.add(searchTextField);
			box.add(Box.createHorizontalStrut(5));
			JButton searchButton = new JButton("検索");
			box.add(searchButton);
			box.setAlignmentX(Component.LEFT_ALIGNMENT);
			panel.add(box);
		}
		panel.add(Box.createVerticalStrut(5));
		{
			JList list = new JList();
			list.setPreferredSize(new Dimension(400, 400));
			panel.add(list);
		}
		panel.add(Box.createVerticalStrut(5));
		{
			JButton reprintReceiptButton = new JButton("領収書再発行");
			panel.add(reprintReceiptButton);
		}
		add(panel, BorderLayout.CENTER);
	}

	private void setupSouth(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		panel.add(Box.createHorizontalGlue());
		JButton closeButton = new JButton("閉じる");
		panel.add(closeButton);
		add(panel, BorderLayout.SOUTH);
	}

}