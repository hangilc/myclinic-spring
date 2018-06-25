package jp.chang.myclinic.pharma.javafx;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.pharma.tracker.model.Patient;
import jp.chang.myclinic.pharma.tracker.model.Visit;
import jp.chang.myclinic.pharma.tracker.model.Wqueue;

class PatientList extends ListView<PatientList.Model> {

    //private static Logger logger = LoggerFactory.getLogger(PatientList.class);

    public static class Model {
        Patient patient;
        Wqueue wqueue;
        Visit visit;

        public Model(Patient patient, Wqueue wqueue) {
            this.patient = patient;
            this.wqueue = wqueue;
            this.visit = wqueue.getVisit();
        }
    }

    private Image waitCashierImage = new Image("/wait_cashier.bmp");
    private Image waitPrescImage = new Image("/wait_drug.bmp");
    private static WritableImage blankImage = new WritableImage(12, 12);

    PatientList() {
        getStyleClass().add("patient-list");
        setCellFactory(listView -> new ListCell<>(){
            @Override
            protected void updateItem(Model item, boolean empty) {
                super.updateItem(item, empty);
                if( empty ){
                    setText("");
                    setGraphic(null);
                } else {
                    setText(itemText(item));
                    setGraphic(new ImageView(itemImage(item)));
                }
            }
        });
    }

    private String itemText(Model item){
        Patient patient = item.patient;
        return String.format("%s%s(%s%s)", patient.getLastName(), patient.getFirstName(),
                patient.getLastNameYomi(), patient.getFirstNameYomi());
    }

    private Image itemImage(Model item){
        Image image = blankImage;
        if( item.wqueue != null ) {
            WqueueWaitState state = WqueueWaitState.fromCode(item.wqueue.getWaitState());
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
        }
        return image;
    }

}
