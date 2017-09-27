package jp.chang.myclinic.practice.rightpane.disease.addpane;

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
        searchResult.setSelectionHandler(sel -> {
            if (sel instanceof ByoumeiResultData) {
                ByoumeiMasterDTO byoumeiMaster = ((ByoumeiResultData) sel).getMaster();
                callback.onByoumeiSelect(byoumeiMaster);
            } else if (sel instanceof ShuushokugoResultData) {
                ShuushokugoMasterDTO shuushookugoMaster = (((ShuushokugoResultData) sel).getMaster());
                callback.onShuushokugoSelect(shuushookugoMaster);
            }
        });
        JScrollPane searchScroll = new JScrollPane(searchResult);
        add(makeSearchBox(), "growx, wrap");
        add(makeSearchOpt(), "wrap");
        add(searchScroll, "w 10, grow");
    }

//    private Component makeCommandBox(){
//        JPanel panel = new JPanel(new MigLayout("insets 2", "", ""));
//        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
//        JButton enterButton = new JButton("入力");
//        enterButton.addActionListener(evt -> doEnter());
//        Link suspectLink = new Link("の疑い");
//        Link delAdjLink = new Link("修飾語削除");
//        panel.add(enterButton);
//        panel.add(suspectLink);
//        panel.add(new JLabel("|"));
//        panel.add(delAdjLink);
//        return panel;
//    }

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

//    private void doEnter(){
//        if( byoumeiMaster == null ){
//            alert("疾患名が入力されていません。");
//            return;
//        }
//        Optional<LocalDate> optStartDate = startDateInput.getValue();
//        if( optStartDate.isPresent() ){
//            DiseaseDTO disease = new DiseaseDTO();
//            disease.patientId = patientId;
//            disease.shoubyoumeicode = byoumeiMaster.shoubyoumeicode;
//            disease.startDate = optStartDate.get().toString();
//            disease.endDate = "0000-00-00";
//            disease.endReason = 'N';
//            DiseaseNewDTO diseaseNew = new DiseaseNewDTO();
//            diseaseNew.disease = disease;
//            diseaseNew.adjList = adjList.stream()
//                    .map(m -> {
//                        DiseaseAdjDTO adjDTO = new DiseaseAdjDTO();
//                        adjDTO.shuushokugocode = m.shuushokugocode;
//                        return adjDTO;
//                    })
//                    .collect(Collectors.toList());
//            Service.api.enterDisease(diseaseNew)
//                    .thenCompose(diseaseId -> Service.api.getDiseaseFull(diseaseId))
//                    .thenAccept(diseaseFull -> {
//                        // TODO: update disp
//                    })
//                    .exceptionally(t -> {
//                        t.printStackTrace();
//                        EventQueue.invokeLater(() -> {
//                            alert(t.toString());
//                        });
//                        return null;
//                    });
//        } else {
//            alert(String.join("\n", startDateInput.getErrors()));
//        }
//    }

    private void doSearch(String text){
        Mode mode = getMode();
        if( mode == Mode.BYOUMEI ){
            startDateInput.getValue().ifPresent(startDate -> {
                Service.api.searchByoumei(text, startDate.toString())
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
