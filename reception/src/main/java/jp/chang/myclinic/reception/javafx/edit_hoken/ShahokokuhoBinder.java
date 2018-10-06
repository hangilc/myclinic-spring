package jp.chang.myclinic.reception.javafx.edit_hoken;

public class ShahokokuhoBinder {

    //private static Logger logger = LoggerFactory.getLogger(ShahokokuhoBinder.class);

    public static ShahokokuhoForm create(){
        ShahokokuhoForm form = new ShahokokuhoForm();
        ShahokokuhoLogic logic = new ShahokokuhoLogic();
        logic.hokenshaBangouProperty().bindBidirectional(form.hokenshaBangouProperty());
        logic.hihokenshaKigouProperty().bindBidirectional(form.hihokenshaKigouProperty());
        logic.hihokenshaBangouProperty().bindBidirectional(form.hihokenshaBangouProperty());
        logic.honninKazokuProperty().bindBidirectional(form.honninKazokuProperty());
        return form;
    }

    private ShahokokuhoBinder() {

    }

}
