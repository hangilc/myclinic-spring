package jp.chang.myclinic.practice.rightpane.disease.browsepane;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DiseaseBrowsePane extends JPanel {

    public DiseaseBrowsePane(int width, List<DiseaseFullDTO>initialDiseases, int patientId, int itemsPerPage, int numPages){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        DispPart dispPart = new DispPart(width);
        ListPart listPart = new ListPart(itemsPerPage, initialDiseases);
        listPart.setSelectionHandler(dispPart::setData);
        NavPart navPart = new NavPart(0, numPages);
        navPart.setCallback(new NavPart.Callback() {
            @Override
            public void gotoPage(int page, Runnable uiCallback) {
                Service.api.pageDiseaseFull(patientId, page, itemsPerPage)
                        .thenAccept(dlist -> EventQueue.invokeLater(() ->{
                            listPart.setData(dlist);
                            uiCallback.run();
                        }))
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
            }
        });
        add(dispPart, "wrap");
        add(navPart, "wrap");
        add(listPart, "growx");
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
