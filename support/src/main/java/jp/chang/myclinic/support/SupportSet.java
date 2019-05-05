package jp.chang.myclinic.support;

import jp.chang.myclinic.support.clinicinfo.ClinicInfoProvider;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaService;
import jp.chang.myclinic.support.kizaicodes.KizaicodeResolver;
import jp.chang.myclinic.support.meisai.MeisaiService;
import jp.chang.myclinic.support.shinryoucodes.ShinryoucodeResolver;
import jp.chang.myclinic.support.stockdrug.StockDrugService;

public class SupportSet {

    public StockDrugService stockDrugService;
    public HoukatsuKensaService houkatsuKensaService;
    public MeisaiService meisaiService;
    public DiseaseExampleProvider diseaseExampleProvider;
    public ShinryoucodeResolver shinryoucodeResolver;
    public KizaicodeResolver kizaicodeResolver;
    public ClinicInfoProvider clinicInfoProvider;

}
