package jp.chang.myclinic.practice.rightpane.selectvisit;

import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SelectVisit extends JPanel {

    public interface Callback {
        default void onSelect(WqueueFullDTO wqueue){ throw new RuntimeException("not implemented"); }
    }

    private JScrollPane scrollPane;
    private SearchResult searchResult = new SearchResult();
    private Callback callback = new Callback(){};

    public SelectVisit(){
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        JButton button = new JButton("患者選択");
        scrollPane = new JScrollPane(searchResult);
        scrollPane.setVisible(false);
        button.addActionListener(event -> {
            if( scrollPane.isVisible() ){
                searchResult.setListData(new WqueueFullDTO[]{});
                scrollPane.setVisible(false);
                repaint();
                revalidate();
            } else {
                Service.api.listWqueueFull()
                        .thenAccept(list -> EventQueue.invokeLater(() -> {
                            searchResult.setListData(list.toArray(new WqueueFullDTO[]{}));
                            scrollPane.setVisible(true);
                            repaint();
                            revalidate();
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
        searchResult.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if( e.getClickCount() >= 2 ){
                    WqueueFullDTO data = searchResult.getSelectedValue();
                    if( data != null ){
                        callback.onSelect(data);
                        searchResult.setListData(new WqueueFullDTO[]{});
                        scrollPane.setVisible(false);
                        repaint();
                        revalidate();
                    }
                }
            }
        });
        add(button);
        add(scrollPane, "newline, w 10, h 160, grow, hidemode 3");
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
