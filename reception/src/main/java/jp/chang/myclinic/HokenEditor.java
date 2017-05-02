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
		setLayout(new MigLayout("fill, flowy", "[grow]", "[fill, grow] []"));
		add(makeMainPart(), "grow");
		add(makeEnterCommandPart());
	}

	private JComponent makeMainPart(){
		JPanel panel = new JPanel(new MigLayout("", "[grow, fill]", "[grow]"));
		hokenList.setBorder(BorderFactory.createEtchedBorder());
		panel.add(hokenList, "grow");
		panel.add(makeItemCommandPart());
		return panel;
	}

	private JComponent makeItemCommandPart(){
		JPanel panel = new JPanel(new MigLayout("flowy", "", ""));
		panel.add(editButton);
		panel.add(deleteButton);
		return panel;
	}

	private JComponent makeEnterCommandPart(){
		JPanel panel = new JPanel(new MigLayout("flowx, center", "[] [] []", ""));
		panel.add(enterShahoButton);
		panel.add(enterKoukiButton);
		panel.add(enterKouhiButton);
		return panel;
	}
}
