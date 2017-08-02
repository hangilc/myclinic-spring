package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.practice.Link;

import javax.swing.*;

class SubMenuPane extends JPopupMenu {

    SubMenuPane(){
        JMenuItem copyAllItem = new JMenuItem("全部コピー")
        Link copyAllLink = new Link("全部コピー");
        Link copySomeLink = new Link("部分コピー");
        Link modifyDaysLink = new Link("日数変更");
        Link deleteSomeLink = new Link("複数削除");
        Link cancelLink = new Link("キャンセル");
        add(copyAllItem);
        //add(copySomeLink);
        //add(modifyDaysLink);
        //add(deleteSomeLink);
        //add(cancelLink);
    }
}
