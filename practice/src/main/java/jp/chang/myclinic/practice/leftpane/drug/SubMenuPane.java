package jp.chang.myclinic.practice.leftpane.drug;

import javax.swing.*;

class SubMenuPane extends JPopupMenu {

    SubMenuPane(){
        JMenuItem copyAllItem = new JMenuItem("全部コピー");
        JMenuItem copySomeItem = new JMenuItem("部分コピー");
        JMenuItem modifyDaysItem = new JMenuItem("日数変更");
        JMenuItem deleteSomeItem = new JMenuItem("複数削除");
        JMenuItem cancelItem = new JMenuItem("キャンセル ");
        add(copyAllItem);
        add(copySomeItem);
        add(modifyDaysItem);
        add(deleteSomeItem);
        add(cancelItem);
    }
}
