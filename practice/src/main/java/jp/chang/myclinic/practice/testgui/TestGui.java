package jp.chang.myclinic.practice.testgui;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.javafx.drug.lib.TestDrugInput;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class TestGui {

    private Stage stage;
    private Map<String, BiConsumer<Stage, Pane>> tests = new LinkedHashMap<>();

    {
        tests.put("drug", (stage, main) -> new TestDrugInput(stage, main).run());
    }

    public TestGui(Stage stage) {
        this.stage = stage;
    }

    private void run(String test) {
        StackPane main = new StackPane();
        main.setStyle("-fx-padding: 10");
        main.getStylesheets().add("css/Practice.css");
        stage.setScene(new Scene(main));
        stage.show();
        try {
            if (test == null) {
                for (String key : tests.keySet()) {
                    tests.get(key).accept(stage, main);
                }
                System.out.println("Gui tests done.");
            } else {
                BiConsumer<Stage, Pane> f = tests.getOrDefault(test, null);
                if (f == null) {
                    System.err.printf("cannot find gui test: %s\n", test);
                    System.exit(1);
                }
                f.accept(stage, main);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void runAll() {
        run(null);
    }

    public void runTest(String test) {
        run(test);
    }
}
