package jp.chang.myclinic.practice.leftpane.drug;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DrugSearch extends JPanel {

    private interface SearchResult {

    }

    private JList<SearchResult> searchResult = new JList<>();

    DrugSearch(){
        JTextField searchTextField = new JTextField();
        JButton searchButton = new JButton("検索");
        setupSearchResult();

        setLayout(new MigLayout("insets 0", "[grow] []", ""));
        add(searchTextField, "growx");
        add(searchButton, "wrap");
        add(makeModeBox(), "span, wrap");
//        {
//            JComponent strut = new JComponent(){};
//            strut.addComponentListener(new ComponentAdapter(){
//                @Override
//                public void componentResized(ComponentEvent e) {
//                    int w = (int)strut.getSize().getWidth();
//                    searchResult.setFixedCellWidth(w-4);
//                }
//            });
//            add(strut, "span, growx, wrap, h 0, gapy 0");
//        }
        JScrollPane scrollPane = new JScrollPane(searchResult);
        add(scrollPane, "span, growx, w 20, h 100");
    }

    private void setupSearchResult(){
        searchResult.setCellRenderer((list, result, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel();
            label.setText(result.toString());
            if( isSelected ){
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }
            label.setOpaque(true);
            return label;
        });
    }

    private JComponent makeModeBox(){
        JRadioButton masterRadio = new JRadioButton("マスター");
        JRadioButton exampleRadio = new JRadioButton("約束処方");
        JRadioButton prevRadio = new JRadioButton("過去の処方");
        ButtonGroup bg = new ButtonGroup();
        bg.add(masterRadio);
        bg.add(exampleRadio);
        bg.add(prevRadio);
        exampleRadio.setSelected(true);
        JPanel panel = new JPanel(new MigLayout("insets 0, gapx 0", "", ""));
        panel.add(masterRadio);
        panel.add(exampleRadio);
        panel.add(prevRadio);
        return panel;
    }
}
