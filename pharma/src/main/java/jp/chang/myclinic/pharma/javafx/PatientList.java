package jp.chang.myclinic.pharma.javafx;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PatientList extends ListView<PharmaQueueFullDTO> {

    private static Logger logger = LoggerFactory.getLogger(PatientList.class);
    private Image waitCashierImage = new Image("/wait_cashier.bmp");
    private Image waitPrescImage = new Image("/wait_drug.bmp");
    private static WritableImage blankImage = new WritableImage(12, 12);

    PatientList() {
        getStyleClass().add("patient-list");
        setCellFactory(listView -> new ListCell<>(){
            @Override
            protected void updateItem(PharmaQueueFullDTO item, boolean empty) {
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

    private String itemText(PharmaQueueFullDTO item){
        PatientDTO patient = item.patient;
        return String.format("%s%s(%s%s)", patient.lastName, patient.firstName,
                patient.lastNameYomi, patient.firstNameYomi);
    }

    private Image itemImage(PharmaQueueFullDTO item){
        Image image = blankImage;
        if( item.wqueue != null ) {
            WqueueWaitState state = WqueueWaitState.fromCode(item.wqueue.waitState);
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

    private ImageView createBlankImage(){
        return new ImageView(blankImage);
    }

}
