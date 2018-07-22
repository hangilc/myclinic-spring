package jp.chang.myclinic.recordbrowser.tracking.model;

class HokenModel {

    //private static Logger logger = LoggerFactory.getLogger(HokenModel.class);
    private ShahokokuhoModel shahokokuhoModel;
    private KoukikoureiModel koukikoureiModel;
    private KouhiModel kouhi1Model;
    private KouhiModel kouhi2Model;
    private KouhiModel kouhi3Model;

    public ShahokokuhoModel getShahokokuhoModel() {
        return shahokokuhoModel;
    }

    public void setShahokokuhoModel(ShahokokuhoModel shahokokuhoModel) {
        this.shahokokuhoModel = shahokokuhoModel;
    }

    public KoukikoureiModel getKoukikoureiModel() {
        return koukikoureiModel;
    }

    public void setKoukikoureiModel(KoukikoureiModel koukikoureiModel) {
        this.koukikoureiModel = koukikoureiModel;
    }

    public KouhiModel getKouhi1Model() {
        return kouhi1Model;
    }

    public void setKouhi1Model(KouhiModel kouhi1Model) {
        this.kouhi1Model = kouhi1Model;
    }

    public KouhiModel getKouhi2Model() {
        return kouhi2Model;
    }

    public void setKouhi2Model(KouhiModel kouhi2Model) {
        this.kouhi2Model = kouhi2Model;
    }

    public KouhiModel getKouhi3Model() {
        return kouhi3Model;
    }

    public void setKouhi3Model(KouhiModel kouhi3Model) {
        this.kouhi3Model = kouhi3Model;
    }
}
