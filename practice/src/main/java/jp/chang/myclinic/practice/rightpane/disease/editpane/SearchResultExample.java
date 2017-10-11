package jp.chang.myclinic.practice.rightpane.disease.editpane;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.DiseaseExampleDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.lib.dateinput.DateInput;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

class SearchResultExample extends SearchResultData {

    private DiseaseExampleDTO example;
    private DateInput dateInput;

    SearchResultExample(DiseaseExampleDTO example, DateInput dateInput){
        this.example = example;
        this.dateInput = dateInput;
    }

    @Override
    CompletableFuture<Data> getData() {
        Data data = new Data();
        List<CompletableFuture<ShuushokugoMasterDTO>> adjList = new ArrayList<>();
        return fetchByoumei(example.byoumei)
                .thenCompose(m -> {
                    data.byoumeiMaster = m;
                    adjList.addAll(fetchShuushokugo(example.adjList));
                    return CompletableFuture.allOf(adjList.toArray(new CompletableFuture[]{}));
                })
                .thenApply(v -> {
                    data.adjList = adjList.stream().map(CompletableFuture::join).collect(Collectors.toList());
                    return data;
                });
    }

    @Override
    String getRep() {
        return example.label;
    }

    private CompletableFuture<ByoumeiMasterDTO> fetchByoumei(String name){
        if( name == null ){
            return CompletableFuture.completedFuture(null);
        } else {
            return dateInput.map(
                    at -> Service.api.findByoumeiMasterByName(name, at.toString()),
                    errors -> {
                        String message = "開始日：\n" + String.join("\n", errors);
                        alert(message);
                        return CompletableFuture.completedFuture(null);
                    });
        }
    }

    private List<CompletableFuture<ShuushokugoMasterDTO>> fetchShuushokugo(List<String> names){
        if( names == null || names.size() == 0 ){
            return Collections.emptyList();
        } else {
            return names.stream().map(Service.api::findShuushokugoMasterByName).collect(Collectors.toList());
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(null, message);
    }

}
