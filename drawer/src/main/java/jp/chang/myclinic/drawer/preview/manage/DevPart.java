package jp.chang.myclinic.drawer.preview.manage;

import jp.chang.myclinic.drawer.printer.DevmodeInfo;
import jp.chang.myclinic.drawer.printer.DevnamesInfo;
import net.miginfocom.swing.MigLayout;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class DevPart extends JPanel {

    private JLabel label = new JLabel();

    DevPart(){
        setLayout(new MigLayout("insets 0", "", ""));
        add(label);
    }

    void clear(){
        label.setText("");
    }

    void setData(byte[] devnames, byte[] devmode){
        List<String> lines = new ArrayList<>();
        DevnamesInfo devnamesInfo = new DevnamesInfo(devnames);
        DevmodeInfo devmodeInfo = new DevmodeInfo(devmode);
        lines.add(String.format("デバイス名：%s", devnamesInfo.getDevice()));
        lines.add(String.format("向き：%s", devmodeInfo.getOrientationLabel()));
        lines.add(String.format("用紙サイズ：%s", devmodeInfo.getPaperSizeLabel()));
        lines.add(String.format("トレイ：%s", devmodeInfo.getDefaultSourceLabel()));
        lines.add(String.format("印刷品質：%s", devmodeInfo.getPrintQualityLabel()));
        lines.add(String.format("コピー数：%d", devmodeInfo.getCopies()));
        label.setText(compileLabel(lines));
    }

    private String compileLabel(List<String> lines){
        return "<html>" +
                lines.stream().map(this::escapeHtml).collect(Collectors.joining("<br />")) +
                "</html>";
    }

    private String escapeHtml(String src){
        return src.replace("<", "&lt;");
    }
}
