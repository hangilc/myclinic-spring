package jp.chang.myclinic.drawer.printer.manage;

import jp.chang.myclinic.drawer.printer.DevmodeInfo;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

public class CreatedSettingDialog extends JDialog {

    private String settingName;
    private Path settingDir;

    public CreatedSettingDialog(Window owner, String name, Path settingDir) throws IOException{
        super(owner, "新規に作成された印刷設定", ModalityType.APPLICATION_MODAL);
        this.settingName = name;
        this.settingDir = settingDir;
        setLayout(new MigLayout("fill", "[grow]", ""));
        add(makeTitle(), "wrap");
        add(makePrinterInfo(), "wrap");
        pack();
    }

    private JComponent makeTitle(){
        return new JLabel("設定名：" + settingName);
    }

    private JComponent makePrinterInfo() throws IOException {
        JPanel panel = new JPanel(new MigLayout("insets 0", "[grow]", ""));
        panel.setBorder(BorderFactory.createTitledBorder("プリンター設定"));
        PrinterSetting setting = new PrinterSetting(settingDir);
        DevmodeInfo devmodeInfo = new DevmodeInfo(setting.readDevmode(settingName));
        panel.add(new JLabel(String.format("%s: %s", "用紙", devmodeInfo.getPaperSizeLabel())), "wrap");
        panel.add(new JLabel(String.format("%s: %s", "向き", devmodeInfo.getOrientationLabel())), "wrap");
        panel.add(new JLabel(String.format("%s: %s", "印刷品質", devmodeInfo.getPrintQualityLabel())), "wrap");
        panel.add(new JLabel(String.format("%s: %s", "給紙", devmodeInfo.getDefaultSourceLabel())), "wrap");
        panel.add(new JLabel(String.format("%s: %d", "印刷枚数", devmodeInfo.getCopies())), "wrap");
        return panel;
    }

    private String devmodeHtml(DevmodeInfo info){
        List<String> frags = new ArrayList<>();
        frags.add("<html>");
        frags.add(String.format("%s: %s", "用紙", info.getPaperSizeLabel()));
        frags.add(String.format("%s: %s", "向き", info.getOrientationLabel()));
        frags.add(String.format("%s: %s", "印刷品質", info.getPrintQualityLabel()));
        frags.add(String.format("%s: %s", "給紙", info.getDefaultSourceLabel()));
        frags.add(String.format("%s: %d", "印刷枚数", info.getCopies()));
        frags.add("</html>");
        return String.join("<br />", frags);
    }

}
