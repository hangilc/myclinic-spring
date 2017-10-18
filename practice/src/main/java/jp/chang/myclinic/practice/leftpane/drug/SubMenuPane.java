package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Service;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class SubMenuPane extends JPopupMenu {

    interface Callback {
        default void onCopyAll(int targetVisitId, List<DrugFullDTO> enteredDrugs){}
        default void onCopySome(int targetVisitId){}
        default void onModifyDays(){}
        default void onDeleteSome(){}
    }

    private Callback callback = new Callback(){};

    SubMenuPane(VisitDTO visit, int currentVisitId, int tempVisitId){
        int targetVisitId = Math.max(currentVisitId, tempVisitId);
        JMenuItem copyAllItem = new JMenuItem("全部コピー");
        copyAllItem.addActionListener(event -> doCopyAll(visit, targetVisitId));
        JMenuItem copySomeItem = new JMenuItem("選択コピー");
        copySomeItem.addActionListener(event -> doCopySome(visit, targetVisitId));
        JMenuItem modifyDaysItem = new JMenuItem("日数変更");
        modifyDaysItem.addActionListener(event -> callback.onModifyDays());
        JMenuItem deleteSomeItem = new JMenuItem("複数削除");
        deleteSomeItem.addActionListener(event -> callback.onDeleteSome());
        JMenuItem cancelItem = new JMenuItem("キャンセル ");
        add(copyAllItem);
        add(copySomeItem);
        add(modifyDaysItem);
        add(deleteSomeItem);
        add(cancelItem);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private void doCopyAll(VisitDTO visit, int targetVisitId){
        if( targetVisitId <= 0 ){
            alert("コピー先が指定されていません。");
            return;
        }
        if( visit.visitId == targetVisitId ){
            alert("同じ診察にコピーすることはできません。");
            return;
        }
        final CopyAllStore store = new CopyAllStore();
        Service.api.getVisit(targetVisitId)
                .thenCompose(targetVisit -> {
                    store.targetVisit = targetVisit;
                    return Service.api.listDrugFull(visit.visitId);
                })
                .thenCompose(drugs -> {
                    store.drugs = drugs;
                    String at = store.targetVisit.visitedAt.substring(0, 10);
                    List<Integer> iyakuhincodes = drugs.stream().map(drug -> drug.drug.iyakuhincode)
                            .collect(Collectors.toList());
                    return Service.api.batchResolveIyakuhinMaster(iyakuhincodes, at);
                })
                .thenCompose(resolvedMap -> {
                    List<DrugDTO> newDrugs = new ArrayList<>();
                    store.drugs.forEach(fullDrug -> {
                        DrugDTO drug = fullDrug.drug;
                        int iyakuhincode = drug.iyakuhincode;
                        IyakuhinMasterDTO resolvedMaster = resolvedMap.getOrDefault(iyakuhincode, null);
                        if( resolvedMaster == null ){
                            throw new RuntimeException(fullDrug.master.name + "は使用できません。");
                        } else {
                            DrugDTO newDrug = DrugDTO.copy(drug);
                            newDrug.drugId = 0;
                            newDrug.visitId = targetVisitId;
                            newDrug.iyakuhincode = resolvedMaster.iyakuhincode;
                            newDrug.prescribed = 0;
                            newDrugs.add(newDrug);
                        }
                    });
                    if( newDrugs.size() != store.drugs.size() ){
                        throw new RuntimeException("cannot noe happen");
                    }
                    return Service.api.batchEnterDrugs(newDrugs);
                })
                .thenCompose(drugIds -> {
                    return Service.api.listDrugFullByDrugIds(drugIds);
                })
                .thenAccept(enteredDrugs -> EventQueue.invokeLater(() -> {
                    callback.onCopyAll(targetVisitId, enteredDrugs);
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });

    }

    private static class CopyAllStore {
        List<DrugFullDTO> drugs;
        VisitDTO targetVisit;
    }

    private void doCopySome(VisitDTO visit, int targetVisitId){
        if( targetVisitId <= 0 ){
            alert("コピー先が指定されていません。");
            return;
        }
        if( visit.visitId == targetVisitId ){
            alert("同じ診察にコピーすることはできません。");
            return;
        }
        callback.onCopySome(targetVisitId);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
