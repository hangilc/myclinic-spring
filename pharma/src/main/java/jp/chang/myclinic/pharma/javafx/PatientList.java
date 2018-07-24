package jp.chang.myclinic.pharma.javafx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import jp.chang.myclinic.consts.WqueueWaitState;

class PatientList extends ListView<PatientList.Model> {

    //private static Logger logger = LoggerFactory.getLogger(PatientList.class);
    public interface Model {
        int getVisitId();

        ObjectProperty<WqueueWaitState> waitStateProperty();

        StringProperty nameProperty();
    }

    private Image waitCashierImage = new Image("/wait_cashier.bmp");
    private Image waitPrescImage = new Image("/wait_drug.bmp");
    private static WritableImage blankImage = new WritableImage(12, 12);

    PatientList() {
        getStyleClass().add("patient-list");
        setCellFactory(listView -> new ListCell<Model>() {
            @Override
            protected void updateItem(Model item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                    setGraphic(null);
                } else {
                    textProperty().bind(item.nameProperty());
                    setGraphic(new ImageView(itemImage(item)));
                }
            }
        });
    }

    private String itemText(Model item) {
        return item.nameProperty().getValue();
    }

    private Image itemImage(Model item) {
        Image image = blankImage;
        WqueueWaitState state = item.waitStateProperty().getValue();
        if (state != null) {
            switch (state) {
                case WaitCashier:
                    image = waitCashierImage;
                    break;
                case WaitDrug:
                    image = waitPrescImage;
                    break;
            }
        }
        return image;
    }

}
