package jp.chang.myclinic.practice.leftpane.conduct.addshinryou;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class AddConductShinryouForm extends JPanel {

    public interface Callback {
        default void onCancel(){}
    }

    private Callback callback = new Callback(){};

    public AddConductShinryouForm(int width, String at){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        CommandBox commandBox = new CommandBox();
        commandBox.setCallback(new CommandBox.Callback() {
            @Override
            public void onEnter() {

            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });
        SearchResult searchResult = new SearchResult();
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
