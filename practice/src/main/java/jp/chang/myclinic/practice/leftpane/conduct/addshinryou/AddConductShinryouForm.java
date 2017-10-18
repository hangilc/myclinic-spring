package jp.chang.myclinic.practice.leftpane.conduct.addshinryou;

import jp.chang.myclinic.dto.ConductShinryouDTO;
import jp.chang.myclinic.dto.ConductShinryouFullDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class AddConductShinryouForm extends JPanel {

    public interface Callback {
        default void onEntered(ConductShinryouFullDTO entered){}
        default void onCancel(){}
    }

    private Callback callback = new Callback(){};

    public AddConductShinryouForm(int width, String at, int conductId){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        Disp disp = new Disp(width);
        CommandBox commandBox = new CommandBox();
        commandBox.setCallback(new CommandBox.Callback() {
            @Override
            public void onEnter() {
                ShinryouMasterDTO master = disp.getMaster();
                if( master == null ){
                    alert("診療行為が指定されていません。");
                    return;
                }
                ConductShinryouDTO newShinryou = new ConductShinryouDTO();
                newShinryou.conductId = conductId;
                newShinryou.shinryoucode = master.shinryoucode;
                Service.api.enterConductShinryou(newShinryou)
                        .thenCompose(Service.api::getConductShinryouFull)
                        .thenAccept(newConductShinryou -> EventQueue.invokeLater(() ->{
                            callback.onEntered(newConductShinryou);
                        }))
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });
        SearchResult searchResult = new SearchResult();
        searchResult.setCallback(new SearchResult.Callback() {
            @Override
            public void onSelected(ShinryouMasterDTO master) {
                disp.setMaster(master);
            }
        });
        SearchBox searchBox = new SearchBox();
        searchBox.setCallback(new SearchBox.Callback() {
            @Override
            public void onSearch(String searchText) {
                if( searchText.isEmpty() ){
                    return;
                }
                Service.api.searchShinryouMaster(searchText, at)
                        .thenAccept(masters -> EventQueue.invokeLater(() ->{
                            searchResult.setListData(masters.toArray(new ShinryouMasterDTO[]{}));
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
        JScrollPane resultScroll = new JScrollPane(searchResult);
        add(disp, "growx, wrap");
        add(commandBox, "growx, wrap");
        add(searchBox, "growx, wrap");
        add(resultScroll, "growx");
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
