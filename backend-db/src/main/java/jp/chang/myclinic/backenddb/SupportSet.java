package jp.chang.myclinic.backenddb;

import jp.chang.myclinic.support.clinicinfo.ClinicInfoProvider;
import jp.chang.myclinic.support.diseaseexample.DiseaseExampleProvider;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensaService;
import jp.chang.myclinic.support.kizainames.KizaiNamesService;
import jp.chang.myclinic.support.meisai.MeisaiService;
import jp.chang.myclinic.support.shinryounames.ShinryouNamesService;
import jp.chang.myclinic.support.stockdrug.StockDrugService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SupportSet {

    public StockDrugService stockDrugService;
    public HoukatsuKensaService houkatsuKensaService;
    public MeisaiService meisaiService;
    public DiseaseExampleProvider diseaseExampleProvider;
    public ShinryouNamesService shinryouNamesService;
    public KizaiNamesService kizaiNamesService;
    public ClinicInfoProvider clinicInfoProvider;

}
