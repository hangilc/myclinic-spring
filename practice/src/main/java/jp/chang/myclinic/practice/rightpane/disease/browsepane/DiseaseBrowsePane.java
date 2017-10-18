package jp.chang.myclinic.practice.rightpane.disease.browsepane;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DiseaseBrowsePane extends JPanel {

    public interface Callback {
        default void onEdit(DiseaseFullDTO disease){}
    }

    private Callback callback = new Callback(){};

    public DiseaseBrowsePane(int width, List<DiseaseFullDTO>initialDiseases, int patientId, int itemsPerPage, int numPages){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        DispPart dispPart = new DispPart(width);
        CommandPart commandPart = new CommandPart();
        commandPart.setCallback(new CommandPart.Callback() {
            @Override
            public void onEdit() {
                DiseaseFullDTO disease = dispPart.getData();
                if( disease != null ){
                    callback.onEdit(disease);
                }
            }
        });
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
        add(dispPart, "growx, wrap");
        add(commandPart, "growx, wrap");
        add(navPart, "wrap");
        add(listPart, "growx");
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}