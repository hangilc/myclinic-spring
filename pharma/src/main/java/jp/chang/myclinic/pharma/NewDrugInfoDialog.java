package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.PharmaDrugDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class NewDrugInfoDialog extends JDialog {

    private JList<IyakuhinMasterDTO> searchResultList;
    private PharmaDrugDTO basePharmaDrug;

    NewDrugInfoDialog() {
        init();
    }

    public NewDrugInfoDialog(PharmaDrugDTO basePharmaDrug) {
        this.basePharmaDrug = basePharmaDrug;
        init();
    }

    private void init() {
        setTitle("新規薬剤情報入力");
        setLayout(new MigLayout("", "", ""));
        add(new JLabel("薬剤検索"), "wrap");
        JTextField searchTextField = new JTextField(12);
        add(searchTextField);
        JButton searchButton = new JButton("検索");
        add(searchButton);
        searchButton.addActionListener(event -> {
            String searchText = searchTextField.getText();
            if (searchText.isEmpty()) {
                return;
            }
            Service.api.searchIyakuhinMasterByName(searchText, LocalDate.now().toString())
                    .thenAccept(masters -> {
                        EventQueue.invokeLater(() -> {
                            if (searchResultList == null) {
                                searchResultList = makeSearchResultList(masters);
                                JScrollPane sp = new JScrollPane(searchResultList);
                                add(sp, "newline, span, growx, w n:n:300, h n:n:360, wrap");
                                JButton startButton = new JButton("作成");
                                startButton.addActionListener(ev -> {
                                    IyakuhinMasterDTO selectedMaster = searchResultList.getSelectedValue();
                                    if (selectedMaster != null) {
                                        doTransitToEdit(selectedMaster);
                                    }
                                });
                                add(startButton);
                            } else {
                                searchResultList.setListData(masters.toArray(new IyakuhinMasterDTO[]{}));
                            }
                            searchResultList.repaint();
                            searchResultList.revalidate();
                            pack();
                        });
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        });
        pack();
    }

    private JList<IyakuhinMasterDTO> makeSearchResultList(List<IyakuhinMasterDTO> masters) {
        JList<IyakuhinMasterDTO> result = new JList<>();
        result.setCellRenderer((list, master, index, isSelected, cellHasFocus) -> {
            JLabel comp = new JLabel(master.name);
            if (isSelected) {
                comp.setBackground(list.getSelectionBackground());
                comp.setForeground(list.getSelectionForeground());
                comp.setOpaque(true);
            }
            return comp;
        });
        result.setListData(masters.toArray(new IyakuhinMasterDTO[]{}));
        return result;
    }

    private void doTransitToEdit(IyakuhinMasterDTO master) {
        Service.api.findPharmaDrug(master.iyakuhincode)
                .thenAccept(pharma -> EventQueue.invokeLater(() -> {
                    if (pharma == null) {
                        String description = "";
                        String sideeffect = "";
                        if (basePharmaDrug != null) {
                            description = basePharmaDrug.description;
                            sideeffect = basePharmaDrug.sideeffect;
                        }
                        doOpenEditor(master.name, master.iyakuhincode, description, sideeffect);
                    } else {
                        EventQueue.invokeLater(() -> {
                            String msg = master.name + "はすでに登録されています。\n内容を表示しますか？";
                            int choice = JOptionPane.showConfirmDialog(this, msg, "オプションの選択",
                                    JOptionPane.OK_CANCEL_OPTION);
                            if (choice == JOptionPane.OK_OPTION) {
                                EditDrugInfoDialog dialog = new EditDrugInfoDialog(master.name, pharma);
                                dialog.setLocationByPlatform(true);
                                dialog.setVisible(true);
                                dispose();
                            }
                        });
                    }
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void doOpenEditor(String name, int iyakuhincode, String description, String sideeffect) {
        getContentPane().removeAll();
        setLayout(new MigLayout("", "", ""));
        add(new JLabel(name), "wrap");
        PharmaDrugEditor editor = new PharmaDrugEditor(description, sideeffect);
        add(editor, "wrap");
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> {
            PharmaDrugDTO pharmaDrug = new PharmaDrugDTO();
            pharmaDrug.iyakuhincode = iyakuhincode;
            pharmaDrug.description = editor.getDescription();
            pharmaDrug.sideeffect = editor.getSideEffect();
            Service.api.enterPharmaDrug(pharmaDrug)
                    .thenAccept(result -> {
                        EventQueue.invokeLater(() -> {
                            dispose();
                            onPharmaDrugEntered(pharmaDrug);
                        });
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        });
        add(enterButton);
        repaint();
        revalidate();
        pack();
    }

    public void onPharmaDrugEntered(PharmaDrugDTO enteredPharmaDrug) {

    }

    private void alert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

}
