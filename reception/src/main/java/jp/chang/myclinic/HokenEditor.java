package jp.chang.myclinic;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

class HokenEditor extends JPanel {

	private HokenList hokenList = new HokenList();
	private JButton editButton = new JButton("編集");
	private JButton deleteButton = new JButton("削除");
	private JButton enterShahoButton = new JButton("新規社保国保");
	private JButton enterKoukiButton = new JButton("新規後期高齢");
	private JButton enterKouhiButton = new JButton("新規公費負担");

	HokenEditor(){
		setLayout(new MigLayout("fill, flowy, insets 0", "[grow]", "[grow] []"));
		add(makeMainPart(), "grow");
		add(makeEnterCommandPart());
	}

	private JComponent makeMainPart(){
		JPanel panel = new JPanel(new MigLayout("insets 0", "[grow] []", "[grow]"));
		JScrollPane scroll = new JScrollPane(hokenList);
		panel.add(scroll, "grow");
		panel.add(makeItemCommandPart(), "top");
		return panel;
	}

	private JComponent makeItemCommandPart(){
		JPanel panel = new JPanel(new MigLayout("flowy, insets 0", "", ""));
		panel.add(editButton);
		panel.add(deleteButton);
		return panel;
	}

	private JComponent makeEnterCommandPart(){
		JPanel panel = new JPanel(new MigLayout("flowx, center, insets 0", "[] [] []", ""));
		panel.add(enterShahoButton);
		panel.add(enterKoukiButton);
		panel.add(enterKouhiButton);
		return panel;
	}
}
