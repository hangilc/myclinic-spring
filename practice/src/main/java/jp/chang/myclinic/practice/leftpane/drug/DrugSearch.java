package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.PrescExampleUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

class DrugSearch extends JPanel {

    private interface SearchResult {

    }

    private enum SearchMode {
        Master, Example, Prev
    }

    private int patientId;
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

    DrugSearch(int patientId){
        this.patientId = patientId;
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
                Service.api.searchIyakuhinMaster(text, LocalDate.now().toString())
                        .thenAccept(result -> {
                            SearchResult[] listData = result.stream().map(MasterSearchResult::new)
                                    .toArray(SearchResult[]::new);
                            EventQueue.invokeLater(() -> searchResult.setListData(listData));
                        })
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });

                break;
            }
            case Example: {
                Service.api.searchPrescExample(text)
                        .thenAccept(result -> {
                            SearchResult[] listData = result.stream().map(ExampleSearchResult::new)
                                    .toArray(SearchResult[]::new);
                            EventQueue.invokeLater(() -> searchResult.setListData(listData));
                        })
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
                break;
            }
            case Prev: {
                Service.api.searchPrevDrug(text, patientId)
                        .thenAccept(result -> {
                            SearchResult[] listData = result.stream().map(DrugSearchResult::new)
                                    .toArray(SearchResult[]::new);
                            EventQueue.invokeLater(() -> searchResult.setListData(listData));
                        })
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
                break;
            }
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    private static class MasterSearchResult implements SearchResult {
        private IyakuhinMasterDTO master;

        MasterSearchResult(IyakuhinMasterDTO master){
            this.master = master;
        }

        @Override
        public String toString(){
            return master.name;
        }
    }

    private static class ExampleSearchResult implements SearchResult {
        private PrescExampleFullDTO example;

        ExampleSearchResult(PrescExampleFullDTO example){
            this.example = example;
        }

        @Override
        public String toString(){
            return PrescExampleUtil.rep(example);
        }
    }

    private static class DrugSearchResult implements SearchResult {
        private DrugFullDTO drugFull;

        DrugSearchResult(DrugFullDTO drugFull){
            this.drugFull = drugFull;
        }

        @Override
        public String toString(){
            return DrugUtil.drugRep(drugFull);
        }
    }

}
