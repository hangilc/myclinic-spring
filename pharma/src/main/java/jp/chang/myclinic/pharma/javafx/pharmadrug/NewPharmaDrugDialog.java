package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.scene.Scene;
import javafx.stage.Stage;

class NewPharmaDrugDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(NewPharmaDrugDialog.class);
    private NewPharmaDrugRoot root;

    NewPharmaDrugDialog() {
        setTitle("新規薬剤情報入力");
        root = new NewPharmaDrugRoot(){
            @Override
            protected void onEnter(int iyakuhincode) {
                NewPharmaDrugDialog.this.onEnter(iyakuhincode);
                clear();
            }

            @Override
            protected void onClose() {
                close();
            }
        };
        setScene(new Scene(root));
    }

    void setDescription(String description){
        root.setDescription(description);
    }

    void setSideEffect(String sideEffect){
        root.setSideEffect(sideEffect);
    }

    protected void onEnter(int iyakuhincode){

    }

}
