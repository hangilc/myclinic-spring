package jp.chang.myclinic.practice.leftpane.drug;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class DrugSearch extends JPanel {

    private interface SearchResult {

    }

    private enum SearchMode {
        Master, Example, Prev
    }

    private JRadioButton masterRadio = new JRadioButton("マスター");
    private JRadioButton exampleRadio = new JRadioButton("約束処方");
    private JRadioButton prevRadio = new JRadioButton("過去の処方");

    private JList<SearchResult> searchResult = new JList<SearchResult>(){
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            Dimension dim = super.getPreferredScrollableViewportSize();
            return new Dimension(20, dim.height);
        }
    };

    DrugSearch(){
        JTextField searchTextField = new JTextField();
        JButton searchButton = new JButton("検索");
        searchButton.addActionListener(event -> doSearch(searchTextField.getText()));
        setupSearchResult();

        setLayout(new MigLayout("insets 0", "[grow] []", ""));
        add(searchTextField, "growx");
        add(searchButton, "wrap");
        add(makeModeBox(), "span, wrap");
        JScrollPane scrollPane = new JScrollPane(searchResult);
        add(scrollPane, "span, growx, h 100");
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

    private SearchMode getSearchMode(){
        if( masterRadio.isSelected() ){
            return SearchMode.Master;
        } else if( exampleRadio.isSelected() ){
            return SearchMode.Example;
        } else if( prevRadio.isSelected() ){
            return SearchMode.Prev;
        }
        throw new RuntimeException("cannot find search mode");
    }

    private void doSearch(String text){
        switch(getSearchMode()){
            case Master: {
                break;
            }
            case Example: {
                break;
            }
            case Prev: {
                break;
            }
        }
    }
}
