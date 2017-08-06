package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.consts.MyclinicConsts;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.PrescExampleUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

class DrugSearch extends JPanel {

    interface Callback {
        void onDrugSelected(SearchResult selectedItem);
    }

    interface SearchResult {
        CompletableFuture<IyakuhinMasterDTO> resolveMaster();
        default DrugCategory getCategory(){ return DrugCategory.Naifuku; };
        default Optional<Double> getAmount(){ return Optional.empty(); };
        default String getUsage() { return ""; };
        default Optional<Integer> getDays(){ return Optional.empty(); };
        default Optional<String> getComment(){ return Optional.empty(); }
    }

    private enum SearchMode {
        Master, Example, Prev
    }

    private int patientId;
    private String at;
    private Callback callback;
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

    DrugSearch(int patientId, String at, Callback callback){
        this.patientId = patientId;
        this.at = at;
        this.callback = callback;
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
        searchResult.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                SearchResult select = searchResult.getSelectedValue();
                if( select == null ){
                    return;
                }
                callback.onDrugSelected(select);
            }
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
                            SearchResult[] listData = result.stream()
                                    .map(ex -> new ExampleSearchResult(ex, at))
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
                            SearchResult[] listData = result.stream()
                                    .map(m -> new DrugSearchResult(m, at))
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

        @Override
        public CompletableFuture<IyakuhinMasterDTO> resolveMaster() {
            return CompletableFuture.completedFuture(master);
        }

        @Override
        public DrugCategory getCategory() {
            if( master.zaikei == MyclinicConsts.ZaikeiGaiyou ){
                return DrugCategory.Gaiyou;
            } else {
                return DrugCategory.Naifuku;
            }
        }
    }

    private static class ExampleSearchResult implements SearchResult {

        private PrescExampleFullDTO example;
        private String at;

        ExampleSearchResult(PrescExampleFullDTO example, String at){
            this.example = example;
            this.at = at;
        }

        @Override
        public String toString(){
            return PrescExampleUtil.rep(example);
        }

        @Override
        public CompletableFuture<IyakuhinMasterDTO> resolveMaster() {
            return Service.api.resolveIyakuhinMaster(example.prescExample.iyakuhincode, at)
                    .thenApply(result -> {
                        if( result != null ){
                            return result;
                        } else {
                            throw new RuntimeException(example.master.name + "は現在使用できません。");
                        }
                    });
        }

        @Override
        public DrugCategory getCategory() {
            return DrugCategory.fromCode(example.prescExample.category);
        }

        @Override
        public Optional<Double> getAmount() {
            return Optional.of(Double.valueOf(example.prescExample.amount));
        }

        @Override
        public String getUsage() {
            return example.prescExample.usage;
        }

        @Override
        public Optional<Integer> getDays() {
            return Optional.of(example.prescExample.days);
        }

        @Override
        public Optional<String> getComment() {
            return Optional.of(example.prescExample.comment);
        }
    }

    private static class DrugSearchResult implements SearchResult {
        private DrugFullDTO drugFull;
        private String at;

        DrugSearchResult(DrugFullDTO drugFull, String at){
            this.drugFull = drugFull;
            this.at = at;
        }

        @Override
        public String toString(){
            return DrugUtil.drugRep(drugFull);
        }

        @Override
        public CompletableFuture<IyakuhinMasterDTO> resolveMaster() {
            return Service.api.resolveIyakuhinMaster(drugFull.drug.iyakuhincode, at)
                    .thenApply(result -> {
                        if( result != null ){
                            return result;
                        } else {
                            throw new RuntimeException(drugFull.master.name + "は現在使用できません。");
                        }
                    });
        }

        @Override
        public DrugCategory getCategory() {
            return DrugCategory.fromCode(drugFull.drug.category);
        }

        @Override
        public Optional<Double> getAmount() {
            return Optional.of(drugFull.drug.amount);
        }

        @Override
        public String getUsage() {
            return drugFull.drug.usage;
        }

        @Override
        public Optional<Integer> getDays() {
            return Optional.of(drugFull.drug.days);
        }
    }

}
