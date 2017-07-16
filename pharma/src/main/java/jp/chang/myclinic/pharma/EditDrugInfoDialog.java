package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class EditDrugInfoDialog extends JDialog {

    EditDrugInfoDialog(){
        setTitle("薬剤情報の表示・編集");
        new SearchMode().start();
    }

    EditDrugInfoDialog(String drugName, PharmaDrugDTO pharmaDrug){
        setTitle("薬剤情報の表示・編集");
        new EditMode(drugName, pharmaDrug, false).start();
    }

    private class SearchMode {

        private JList<PharmaDrugNameDTO> searchResultList;

        void start(){
            getContentPane().removeAll();
            setJMenuBar(null);
            setLayout(new MigLayout("", "", ""));
            add(new JLabel("薬剤検索"), "wrap");
            JTextField searchTextField = new JTextField(12);
            add(searchTextField);
            JButton searchButton = new JButton("検索");
            add(searchButton);
            searchButton.addActionListener(event -> {
                String searchText = searchTextField.getText();
                if( searchText.isEmpty() ){
                    return;
                }
                Service.api.searchPharmaDrugNames(searchText)
                        .thenAccept(result -> {
                            EventQueue.invokeLater(() -> {
                                if( searchResultList == null ){
                                    searchResultList = makeSearchResultList(result);
                                    JScrollPane sp = new JScrollPane(searchResultList);
                                    add(sp, "newline, span, growx, w n:n:300, h n:n:360, wrap");
                                    JButton startButton = new JButton("表示");
                                    startButton.addActionListener(ev -> {
                                        PharmaDrugNameDTO selectedData = searchResultList.getSelectedValue();
                                        if( selectedData != null ){
                                            doTransitToEdit(selectedData.iyakuhincode, selectedData.name);
                                        }
                                    });
                                    add(startButton);
                                } else {
                                    searchResultList.setListData(result.toArray(new PharmaDrugNameDTO[]{}));
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

        private JList<PharmaDrugNameDTO> makeSearchResultList(List<PharmaDrugNameDTO> dataList){
            JList<PharmaDrugNameDTO> result = new JList<>();
            result.setCellRenderer((list, data, index, isSelected, cellHasFocus) -> {
                JLabel comp = new JLabel(data.name);
                if( isSelected ){
                    comp.setBackground(list.getSelectionBackground());
                    comp.setForeground(list.getSelectionForeground());
                    comp.setOpaque(true);
                }
                return comp;
            });
            result.setListData(dataList.toArray(new PharmaDrugNameDTO[]{}));
            return result;
        }

        private void doTransitToEdit(int iyakuhincode, String name) {
            Service.api.getPharmaDrug(iyakuhincode)
                    .thenAccept(pharma -> {
                        new EditMode(name, pharma).start();
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        }

    }

    private class EditMode {

        private String currentDrugName;
        private PharmaDrugDTO pharmaDrug;
        private PharmaDrugEditor editor;
        private JPanel commandArea;
        private JMenuBar editMenuBar;
        private boolean showPrevButton = true;

        EditMode(String drugName, PharmaDrugDTO pharmaDrug){
            this(drugName, pharmaDrug, true);
        }

        EditMode(String drugName, PharmaDrugDTO pharmaDrug, boolean showPrevButton) {
            this.currentDrugName = drugName;
            this.pharmaDrug = pharmaDrug;
            this.showPrevButton = showPrevButton;
            start();
        }

        void start() {
            getContentPane().removeAll();
            setLayout(new MigLayout("", "", ""));
            add(new JLabel(currentDrugName), "wrap");
            this.editor = new PharmaDrugEditor(pharmaDrug);
            editor.setEditable(false);
            add(editor, "wrap");
            this.commandArea = new JPanel(new MigLayout("insets 0", "", ""));
            setupDispModeCommands(false);
            add(commandArea);
            setJMenuBar(getEditMenuBar());
            repaint();
            revalidate();
            pack();
        }

        private JButton makeEditButton(){
            JButton editButton = new JButton("編集");
            editButton.addActionListener(event -> {
                editor.setEditable(true);
                setupEditModeCommands();
            });
            return editButton;
        }

        private JButton makeCopyButton(){
            JButton button = new JButton("新規作成");
            button.addActionListener(event -> {
                int reply = JOptionPane.showConfirmDialog(EditDrugInfoDialog.this,
                        "この薬剤情報をもとに別の薬剤情報を作成しますか？", "確認", JOptionPane.OK_CANCEL_OPTION);
                if( reply == JOptionPane.OK_OPTION ){
                    NewDrugInfoDialog dialog = new NewDrugInfoDialog(pharmaDrug);
                    dialog.setLocationByPlatform(true);
                    dialog.setVisible(true);
                }
            });
            return button;
        }

        private JButton makeBackToSearchButton(){
            JButton btn = new JButton("戻る");
            btn.addActionListener(event -> new SearchMode().start());
            return btn;
        }

        private JButton makeEnterButton(){
            JButton enterButton = new JButton("入力");
            enterButton.addActionListener(evt -> {
                pharmaDrug.description = editor.getDescription();
                pharmaDrug.sideeffect = editor.getSideEffect();
                Service.api.updatePharmaDrug(pharmaDrug)
                        .thenAccept(result -> {
                            EventQueue.invokeLater(() -> new SearchMode().start());
                        })
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
            });
            return enterButton;
        }

        private JButton makeCancelButton(){
            JButton button = new JButton("キャンセル");
            button.addActionListener(event -> {
                editor.setEditable(false);
                editor.setData(pharmaDrug);
                setupDispModeCommands(true);
            });
            return button;
        }

        private void setupDispModeCommands(boolean repaint){
            if( repaint ){
                commandArea.removeAll();
            }
            commandArea.add(makeEditButton());
            commandArea.add(makeCopyButton());
            if( showPrevButton ) {
                commandArea.add(makeBackToSearchButton());
            }
            if( repaint ){
                commandArea.repaint();
                commandArea.revalidate();
            }
        }

        private void setupEditModeCommands(){
            commandArea.removeAll();
            commandArea.add(makeEnterButton());
            commandArea.add(makeCancelButton());
            commandArea.repaint();
            commandArea.revalidate();
        }

        private JMenuBar getEditMenuBar(){
            if( editMenuBar == null ){
                editMenuBar = new JMenuBar();
                JMenu manipItem = new JMenu("その他の操作");
                JMenuItem deleteCommand = new JMenuItem("この薬剤情報を削除");
                deleteCommand.addActionListener(event -> {
                    int reply = JOptionPane.showConfirmDialog(EditDrugInfoDialog.this,
                            currentDrugName + "の薬剤情報を削除しますか？", "確認", JOptionPane.OK_CANCEL_OPTION);
                    if (reply == JOptionPane.OK_OPTION) {
                        Service.api.deletePharmaDrug(pharmaDrug.iyakuhincode)
                                .thenAccept(result -> {
                                    new SearchMode().start();
                                })
                                .exceptionally(t -> {
                                    t.printStackTrace();
                                    EventQueue.invokeLater(() -> {
                                        alert(t.toString());
                                    });
                                    return null;
                                });
                    }
                });
                manipItem.add(deleteCommand);
                editMenuBar.add(manipItem);
            }
            return editMenuBar;
        }

    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
