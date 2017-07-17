package jp.chang.myclinic.pharma.leftpane;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.pharma.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeftPane extends JPanel {

    public interface Callbacks {
        default void onStartPresc(PharmaQueueFullDTO pharmaQueue, List<DrugFullDTO> drugs){}
    }

    private PharmaQueueList pharmaQueueList;
    private PatientListControlPane patientListControlPane;
    private Callbacks callbacks;

    public LeftPane(Icon waitCashierIcon, Icon waitDrugIcon, Callbacks callbacks){
        this.callbacks = callbacks;
        pharmaQueueList = new PharmaQueueList(waitCashierIcon, waitDrugIcon);
        patientListControlPane = new PatientListControlPane(waitCashierIcon, waitDrugIcon,
                new PatientListControlPane.Callbacks() {
                    @Override
                    public void onUpdatePharmaQueue(List<PharmaQueueFullDTO> list) {
                        pharmaQueueList.setListData(list.toArray(new PharmaQueueFullDTO[]{}));
                    }

                    @Override
                    public void onStartPresc() {
                        doStartPresc();
                    }
                });
        PrevTechouPane prevTechouPane = new PrevTechouPane();
        setLayout(new MigLayout("fill", "", ""));
        add(new JLabel("患者リスト"), "left, wrap");
        add(new JScrollPane(pharmaQueueList), "grow, wrap");
        add(patientListControlPane, "growx, wrap");
        add(prevTechouPane, "growx");
    }

    public void clear(){
        pharmaQueueList.clearSelection();
    }

    public void reloadPharmaQueue(){
        patientListControlPane.triggerPharmaQueueUpdate();
    }

    private void doStartPresc(){
        PharmaQueueFullDTO pharmaQueueFull = pharmaQueueList.getSelectedValue();
        if( pharmaQueueFull == null ){
            return;
        }
        Service.api.listDrugFull(pharmaQueueFull.visitId)
                .thenAccept(drugs -> {
                    EventQueue.invokeLater(() -> {
                        callbacks.onStartPresc(pharmaQueueFull, drugs);
                    });
                })
                .exceptionally(t -> null);
    }

}
