package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.util.DiseaseUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class SearchArea extends JPanel {

    private enum Mode { BYOUMEI, SHUUSHOKUGO };

    private int width;
    private ByoumeiMasterDTO byoumeiMaster;
    private List<ShuushokugoMasterDTO> adjList = new ArrayList<>();
    private WrappedText nameText;
    private JRadioButton byoumeiButton = new JRadioButton("病名");
    private JRadioButton shuushokuButton = new JRadioButton("修飾語");
    private SearchResult searchResult = new SearchResult();

    SearchArea(int width){
        this.width = width;
        setLayout(new MigLayout("insets 0, gapy 0", String.format("[%dpx!]", width), ""));
        searchResult.setSelectionHandler(sel -> {
            if( sel instanceof ByoumeiResultData ){
                this.byoumeiMaster = ((ByoumeiResultData)sel).getMaster();
            } else if( sel instanceof ShuushokugoResultData ){
                this.adjList.add(((ShuushokugoResultData)sel).getMaster());
            }
            updateName();
        });
        add(makeNameArea(), "wrap");
        JScrollPane searchScroll = new JScrollPane(searchResult);
        add(makeSearchBox(), "growx, wrap");
        add(makeSearchOpt(), "wrap");
        add(searchScroll, "w 10, grow");
    }

    private Component makeNameArea(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "[]4[]", ""));
        JLabel nameLabel = new JLabel("名称：");
        nameText = new WrappedText(width - nameLabel.getPreferredSize().width - 4, "");
        panel.add(nameLabel);
        panel.add(nameText);
        return panel;
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

    private LocalDate getStartDate(){
        // TODO: fix start date
        return LocalDate.now();
    }

    private void updateName(){
        nameText.setText(DiseaseUtil.getFullName(byoumeiMaster, adjList));
    }

    private void setByoumei(ByoumeiMasterDTO byoumeiMaster){
        this.byoumeiMaster = byoumeiMaster;
    }

    private void doSearch(String text){
        Mode mode = getMode();
        if( mode == Mode.BYOUMEI ){
            Service.api.searchByoumei(text, getStartDate().toString())
                    .thenAccept(masters -> EventQueue.invokeLater(() ->{
                        List<SearchResultData> dataList = masters.stream().map(ByoumeiResultData::new).collect(Collectors.toList());
                        searchResult.setListData(dataList.toArray(new SearchResultData[]{}));
                    }))
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        } else if( mode == Mode.SHUUSHOKUGO ){
            Service.api.searchShuushokugo(text)
                    .thenAccept(masters -> EventQueue.invokeLater(() ->{
                        List<SearchResultData> dataList = masters.stream().map(ShuushokugoResultData::new).collect(Collectors.toList());
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
