package jp.chang.myclinic.practice.rightpane.disease.editpane;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.lib.dateinput.DateInput;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

class SearchArea extends JPanel {

    interface Callback {
        default void onByoumeiSelect(ByoumeiMasterDTO byoumeiMaster){}
        default void onShuushokugoSelect(ShuushokugoMasterDTO shuushokugoMaster){}
    }

    private enum Mode { BYOUMEI, SHUUSHOKUGO };

    private DateInput startDateInput;
    private JRadioButton byoumeiButton = new JRadioButton("病名");
    private JRadioButton shuushokuButton = new JRadioButton("修飾語");
    private SearchResult searchResult = new SearchResult();
    private Callback callback = new Callback(){};

    SearchArea(int width, DateInput startDateInput) {
        this.startDateInput = startDateInput;
        setLayout(new MigLayout("insets 0, gapy 0", String.format("[%dpx!]", width), ""));
        searchResult.setSelectionHandler(this::doSelected);
        JScrollPane searchScroll = new JScrollPane(searchResult);
        add(makeSearchBox(), "growx, wrap");
        add(makeSearchOpt(), "wrap");
        add(searchScroll, "w 10, grow");
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private void doSelected(SearchResultData data){
        data.getData()
                .thenAccept(d -> EventQueue.invokeLater(() ->{
                    ByoumeiMasterDTO byoumeiMaster = d.byoumeiMaster;
                    if( byoumeiMaster != null ){
                        callback.onByoumeiSelect(byoumeiMaster);
                    }
                    d.adjList.forEach(callback::onShuushokugoSelect);
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private Component makeSearchBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow] [] []", ""));
        JTextField searchTextField = new JTextField();
        JButton searchButton = new JButton("検索");
        searchButton.addActionListener(evt -> {
            String text = searchTextField.getText();
            if( !text.isEmpty() ){
                doSearch(searchTextField.getText());
            }
        });
        Link exampleLink = new Link("例");
        exampleLink.setCallback(evt -> doExample());
        panel.add(searchTextField, "growx");
        panel.add(searchButton);
        panel.add(exampleLink);
        return panel;
    }

    private Component makeSearchOpt(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        ButtonGroup group = new ButtonGroup();
        group.add(byoumeiButton);
        group.add(shuushokuButton);
        byoumeiButton.setSelected(true);
        panel.add(byoumeiButton);
        panel.add(shuushokuButton);
        return panel;
    }

    private void doSearch(String text){
        Mode mode = getMode();
        if( mode == Mode.BYOUMEI ){
            startDateInput.getValue().ifPresent(startDate -> {
                Service.api.searchByoumei(text, startDate.toString())
                        .thenAccept(masters -> EventQueue.invokeLater(() ->{
                            List<SearchResultData> dataList = masters.stream()
                                    .map(SearchResultMaster::of)
                                    .collect(Collectors.toList());
                            searchResult.setListData(dataList.toArray(new SearchResultData[]{}));
                        }))
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
            });
        } else if( mode == Mode.SHUUSHOKUGO ){
            Service.api.searchShuushokugo(text)
                    .thenAccept(masters -> EventQueue.invokeLater(() ->{
                        List<SearchResultData> dataList = masters.stream()
                                .map(SearchResultMaster::of)
                                .collect(Collectors.toList());
                        searchResult.setListData(dataList.toArray(new SearchResultData[]{}));
                    }))
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        }
    }

    private void doExample(){
        Service.api.listDiseaseExample()
                .thenAccept(examples -> EventQueue.invokeLater(() ->{
                    List<SearchResultData> dataList = examples.stream()
                            .map(ex -> new SearchResultExample(ex, startDateInput)).collect(Collectors.toList());
                    searchResult.setListData(dataList.toArray(new SearchResultData[]{}));
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private Mode getMode(){
        if( byoumeiButton.isSelected() ){
            return Mode.BYOUMEI;
        } else if( shuushokuButton.isSelected() ){
            return Mode.SHUUSHOKUGO;
        } else {
            throw new RuntimeException("cannot happen");
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
