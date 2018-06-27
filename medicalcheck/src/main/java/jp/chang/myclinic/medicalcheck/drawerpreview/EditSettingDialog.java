package jp.chang.myclinic.medicalcheck.drawerpreview;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DevnamesInfo;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

class EditSettingDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(EditSettingDialog.class);
    private static List<Op> defaultTestPrintOps = new ArrayList<>();
    {
        DrawerCompiler comp = new DrawerCompiler();
        comp.createFont("default-font", "sans-serif", 10);
        comp.setFont("default-font");
        comp.textAt("こんにちは、世界", 0, 0, DrawerCompiler.HAlign.Left, DrawerCompiler.VAlign.Top);
        defaultTestPrintOps = comp.getOps();
    }
    private PrinterEnv printerEnv;
    private String name;
    private byte[] devmode;
    private byte[] devnames;
    private AuxSetting auxSetting;
    private List<Op> testPrintOps = defaultTestPrintOps;

    private class PrinterPart extends VBox {
        private Text text;

        PrinterPart(){
            super(4);
            text = new Text("");
            Button modifyButton = new Button("変更");
            modifyButton.setOnAction(evt -> doModify());
            getChildren().addAll(
                    new TextFlow(text),
                    modifyButton
            );
            update();
        }

        private void update(){
            text.setText(createLabel());
        }

        private String createLabel(){
            DevnamesInfo devnamesInfo = new DevnamesInfo(devnames);
            return String.format("%s", devnamesInfo.getDevice());
        }

        private void doModify(){
            DrawerPrinter drawerPrinter = new DrawerPrinter();
            DrawerPrinter.DialogResult result = drawerPrinter.printDialog(devmode, devnames);
            if( result.ok ){
                byte[] newDevmode = result.devmodeData;
                byte[] newDevnames = result.devnamesData;
                try {
                    printerEnv.saveSetting(name, newDevnames, newDevmode, auxSetting);
                    EditSettingDialog.this.devmode = newDevmode;
                    EditSettingDialog.this.devnames = newDevnames;
                    update();
                } catch (Exception e) {
                    logger.error("Failed to save printer setting.", e);
                    GuiUtil.alertException("印刷設定の保存に失敗しました。", e);
                }
            }
        }
    }

    private class AuxPart extends HBox {

        String label;
        private Supplier<Double> getter;
        private Consumer<Double> setter;
        String description;
        private Text text = new Text("");

        AuxPart(String label, Supplier<Double> getter, Consumer<Double> setter, String description){
            super(4);
            setAlignment(Pos.CENTER_LEFT);
            this.label = label;
            this.getter = getter;
            this.setter = setter;
            this.description = description;
            text.setText("" + getter.get());
            Button modifyButton = new Button("変更");
            modifyButton.setOnAction(evt -> doModify());
            getChildren().addAll(
                    text, modifyButton
            );
        }

        private void doModify(){
            String prompt = String.format("%s （%s）の値を入力", label, description);
            GuiUtil.askForString(prompt, text.getText())
                    .ifPresent(input -> {
                        try {
                            double newValue = Double.parseDouble(input);
                            setter.accept(newValue);
                            printerEnv.saveSetting(name, auxSetting);
                            text.setText("" + getter.get());
                        } catch(NumberFormatException ex){
                            GuiUtil.alertError(label + " の入力が不適切です。");
                        } catch (Exception e) {
                            logger.error("Failed to save printer aux setting.", e);
                            GuiUtil.alertException("印刷設定の保存に失敗しました。", e);
                        }
                    });

        }

    }

    EditSettingDialog(PrinterEnv printerEnv, String name, byte[] devmode, byte[] devnames, AuxSetting auxSetting) {
        this.printerEnv = printerEnv;
        this.name = name;
        this.devmode = devmode;
        this.devnames = devnames;
        this.auxSetting = auxSetting;
        setTitle("印刷設定の編集");
        BorderPane root = new BorderPane();
        root.setStyle("-fx-padding:10px");
        root.setCenter(createCenter());
        setScene(new Scene(root));
    }

    public void setTestPrintOps(List<Op> testPrintOps) {
        this.testPrintOps = testPrintOps;
    }

    private Node createCenter(){
        VBox vbox = new VBox(4);
        vbox.getChildren().addAll(
                createMain(),
                createCommands()
        );
        return vbox;
    }

    private Node createMain(){
        DispGrid root = new DispGrid();
        root.addRow("名前：", new Label(name));
        root.addRow("プリンター：", new PrinterPart());
        root.addRow("dx：", new AuxPart("dx", () -> auxSetting.getDx(), auxSetting::setDx, "右方向への変位"));
        root.addRow("dy：", new AuxPart("dy", () -> auxSetting.getDy(), auxSetting::setDy, "下方向への変位"));
        root.addRow("scale：", new AuxPart("scale", () -> auxSetting.getScale(), auxSetting::setScale, "拡大倍数"));
        return root;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button doneButton = new Button("終了");
        Button testPrintButton = new Button("テスト印刷");
        doneButton.setOnAction(evt -> close());
        testPrintButton.setOnAction(evt -> doTestPrint());
        hbox.getChildren().addAll(
                doneButton,
                testPrintButton
        );
        return hbox;
    }

    private void doTestPrint(){
        DrawerPrinter drawerPrinter = new DrawerPrinter();
        drawerPrinter.print(testPrintOps, devmode, devnames, auxSetting);
    }

}
