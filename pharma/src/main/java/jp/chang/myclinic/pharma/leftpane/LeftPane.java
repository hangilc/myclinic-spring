package jp.chang.myclinic.pharma.leftpane;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.pharma.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeftPane extends JPanel {
    private PharmaQueueList pharmaQueueList;
    private PatientListControlPane patientListControlPane;
    private PrevTechouPane prevTechouPane;

    public LeftPane(Icon waitCashierIcon, Icon waitDrugIcon){
        pharmaQueueList = new PharmaQueueList(waitCashierIcon, waitDrugIcon);
        patientListControlPane = new PatientListControlPane(waitCashierIcon, waitDrugIcon){
            @Override
            public void onUpdatePharmaQueue(List<PharmaQueueFullDTO> list){
                pharmaQueueList.setListData(list.toArray(new PharmaQueueFullDTO[]{}));
            }

            @Override
            public void onStartPresc(){
                doStartPresc();
            }
        };
        prevTechouPane = new PrevTechouPane();
        setLayout(new MigLayout("fill", "", ""));
        add(new JLabel("患者リスト"), "left, wrap");
        add(new JScrollPane(pharmaQueueList), "grow, wrap");
        add(patientListControlPane, "growx, wrap");
        add(prevTechouPane, "growx");
    }

    public void clear(){
        pharmaQueueList.clearSelection();
    }

    public void reloadPharmaQueueList(){
        pharmaQueueList.reload();
    }

    private void doStartPresc(){
        PharmaQueueFullDTO pharmaQueueFull = pharmaQueueList.getSelectedValue();
        if( pharmaQueueFull == null ){
            return;
        }
        Service.api.listDrugFull(pharmaQueueFull.visitId)
                .thenAccept(drugs -> {
                    EventQueue.invokeLater(() -> {
                        onStartPresc(pharmaQueueFull, drugs);
//                        workarea.update(pharmaQueueFull.patient, drugs);
//                        auxControl.update(pharmaQueueFull.patient);
//                        rightScroll.getVerticalScrollBar().setValue(0);
                    });
                })
                .exceptionally(t -> null);
    }

    public void onStartPresc(PharmaQueueFullDTO pharmaQueueFull, List<DrugFullDTO> drugs){

    }
}
