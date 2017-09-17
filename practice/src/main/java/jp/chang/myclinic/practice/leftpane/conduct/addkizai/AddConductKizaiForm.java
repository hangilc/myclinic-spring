package jp.chang.myclinic.practice.leftpane.conduct.addkizai;

import jp.chang.myclinic.dto.ConductKizaiDTO;
import jp.chang.myclinic.dto.KizaiMasterDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class AddConductKizaiForm extends JPanel {
    public interface Callback {
        default void onEntered(){}
        default void onCancel(){}
    }

    private Callback callback = new Callback(){};

    public AddConductKizaiForm(int width, String at, int conductId){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        Disp disp = new Disp(width);
        CommandBox commandBox = new CommandBox();
        commandBox.setCallback(new CommandBox.Callback() {
            @Override
            public void onEnter() {
                Disp.ValidatedData data = disp.getData();
                if( data.hasError() ){
                    alert(String.join("\n", data.getErrors()));
                } else {
                    ConductKizaiDTO kizai = data.toConductKizai(conductId);
                    Service.api.enterConductKizai(kizai)
                            .thenAccept(res -> callback.onEntered())
                            .exceptionally(t -> {
                                t.printStackTrace();
                                EventQueue.invokeLater(() -> {
                                    alert(t.toString());
                                });
                                return null;
                            });
                }
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });
        SearchResult searchResult = new SearchResult();
        searchResult.setCallback(new SearchResult.Callback() {
            @Override
            public void onSelected(KizaiMasterDTO master) {
                disp.setMaster(master);
            }
        });
        SearchBox searchBox = new SearchBox();
        searchBox.setCallback(new SearchBox.Callback() {
            @Override
            public void onSearch(String searchText) {
                Service.api.searchKizaiMaster(searchText, at)
                        .thenAccept(result -> EventQueue.invokeLater(() -> {
                            searchResult.setListData(result.toArray(new KizaiMasterDTO[]{}));
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
