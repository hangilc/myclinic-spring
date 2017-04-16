package jp.chang.myclinic;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import jp.chang.myclinic.model.PatientRepository;
import jp.chang.myclinic.model.Patient;
import jp.chang.myclinic.model.VisitRepository;
import jp.chang.myclinic.model.Visit;
import jp.chang.myclinic.model.ShahokokuhoRepository;
import jp.chang.myclinic.model.Shahokokuho;
import jp.chang.myclinic.model.KoukikoureiRepository;
import jp.chang.myclinic.model.Koukikourei;
import jp.chang.myclinic.model.RoujinRepository;
import jp.chang.myclinic.model.Roujin;
import jp.chang.myclinic.model.KouhiRepository;
import jp.chang.myclinic.model.Kouhi;
import jp.chang.myclinic.model.TextRepository;
import jp.chang.myclinic.model.Text;
import jp.chang.myclinic.model.IyakuhinMasterRepository;
import jp.chang.myclinic.model.IyakuhinMaster;
import jp.chang.myclinic.model.IyakuhinMasterId;
import jp.chang.myclinic.model.DrugRepository;
import jp.chang.myclinic.model.Drug;
import jp.chang.myclinic.model.ShinryouMasterRepository;
import jp.chang.myclinic.model.ShinryouMaster;
import jp.chang.myclinic.model.ShinryouMasterId;
import jp.chang.myclinic.model.ShinryouRepository;
import jp.chang.myclinic.model.Shinryou;
import jp.chang.myclinic.model.KizaiMaster;
import jp.chang.myclinic.model.KizaiMasterId;
import jp.chang.myclinic.model.KizaiMasterRepository;
import jp.chang.myclinic.model.ConductRepository;
import jp.chang.myclinic.model.Conduct;
import jp.chang.myclinic.model.ConductDrugRepository;
import jp.chang.myclinic.model.ConductDrug;
import jp.chang.myclinic.model.ConductShinryouRepository;
import jp.chang.myclinic.model.ConductShinryou;
import jp.chang.myclinic.model.ConductKizaiRepository;
import jp.chang.myclinic.model.ConductKizai;
import jp.chang.myclinic.model.ShoubyoumeiMaster;
import jp.chang.myclinic.model.ShoubyoumeiMasterId;
import jp.chang.myclinic.model.ShoubyoumeiMasterRepository;
import jp.chang.myclinic.model.ShuushokugoMaster;
import jp.chang.myclinic.model.ShuushokugoMasterRepository;
import jp.chang.myclinic.model.Disease;
import jp.chang.myclinic.model.DiseaseRepository;
import jp.chang.myclinic.model.DiseaseAdj;
import jp.chang.myclinic.model.DiseaseAdjRepository;
import jp.chang.myclinic.model.Hotline;
import jp.chang.myclinic.model.HotlineRepository;
import jp.chang.myclinic.model.PharmaDrug;
import jp.chang.myclinic.model.PharmaDrugRepository;
import jp.chang.myclinic.model.PharmaQueue;
import jp.chang.myclinic.model.PharmaQueueRepository;
import jp.chang.myclinic.model.PrescExample;
import jp.chang.myclinic.model.PrescExampleRepository;
import jp.chang.myclinic.model.Charge;
import jp.chang.myclinic.model.ChargeRepository;
import jp.chang.myclinic.model.Payment;
import jp.chang.myclinic.model.PaymentRepository;
import jp.chang.myclinic.model.Wqueue;
import jp.chang.myclinic.model.WqueueRepository;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private VisitRepository visitRepository;
	@Autowired
	private ShahokokuhoRepository shahokokuhoRepository;
	@Autowired
	private KoukikoureiRepository koukikoureiRepository;
	@Autowired
	private RoujinRepository roujinRepository;
	@Autowired
	private KouhiRepository kouhiRepository;
    @Autowired
    private TextRepository textRepository;
    @Autowired
    private IyakuhinMasterRepository iyakuhinMasterRepository;
	@Autowired
	private DrugRepository drugRepository;
    @Autowired
    private ShinryouMasterRepository shinryouMasterRepository;
    @Autowired
    private ShinryouRepository shinryouRepository;
    @Autowired
    private KizaiMasterRepository kizaiMasterRepository;
    @Autowired
    private ConductRepository conductRepository;
    @Autowired
    private ConductDrugRepository conductDrugRepository;
    @Autowired
    private ConductShinryouRepository conductShinryouRepository;
    @Autowired
    private ConductKizaiRepository conductKizaiRepository;
    @Autowired
    private ShoubyoumeiMasterRepository shoubyoumeiMasterRepository;
    @Autowired
    private ShuushokugoMasterRepository shuushokugoMasterRepository;
    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private DiseaseAdjRepository diseaseAdjRepository;
    @Autowired
    private HotlineRepository hotlineRepository;
    @Autowired
    private PharmaDrugRepository pharmaDrugRepository;
    @Autowired
    private PharmaQueueRepository pharmaQueueRepository;
    @Autowired
    private PrescExampleRepository prescExampleRepository;
    @Autowired
    private ChargeRepository chargeRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private WqueueRepository wqueueRepository;

    public static void main( String[] args )
    {
        SpringApplication.run(Application.class, args);
    }

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
    	System.out.println("Hello, from run");
    	System.out.println(patientRepository);
        {
        	Patient patient = patientRepository.findOne(99);
        	System.out.println(patient.toString());
        	Visit visit = visitRepository.findOne(2003);
        	System.out.println(visit);
        	System.out.println(visit.getTexts());
        	Shahokokuho shahokokuho = shahokokuhoRepository.findOne(389);
        	System.out.println(shahokokuho.toString());
        	Koukikourei koukikourei = koukikoureiRepository.findOne(100);
        	System.out.println(koukikourei);
        	Roujin roujin = roujinRepository.findOne(100);
        	System.out.println(roujin);
        	Kouhi kouhi = kouhiRepository.findOne(100);
        	System.out.println(kouhi);
        }
    	System.out.println(textRepository.findOne(101));
        System.out.println(iyakuhinMasterRepository.findOne(new IyakuhinMasterId(610406001, Date.valueOf(LocalDate.of(2005,11,1)))).getValidFrom());
        System.out.println(iyakuhinMasterRepository.findByIyakuhincodeAndDate(610406047, Date.valueOf(LocalDate.of(2016, 3, 31))));
        System.out.println(drugRepository.findOne(100));
        System.out.println(drugRepository.findOne(100).getMaster());
        System.out.println(drugRepository.findOne(100).getVisit().getDrugs());
        System.out.println(shinryouMasterRepository.findOne(new ShinryouMasterId(111000110, Date.valueOf(LocalDate.of(2006,4,1)))));
        System.out.println(shinryouRepository.findOne(100));
        System.out.println(shinryouRepository.findOne(100).getMaster());
        System.out.println(kizaiMasterRepository.findOne(new KizaiMasterId(700150000, Date.valueOf(LocalDate.of(2008,4,1)))));
        System.out.println(conductRepository.findOne(100));
        //System.out.println(conductRepository.findOne(100).getGazouLabel());
        //System.out.println(conductRepository.findOne(102).getGazouLabel());
        System.out.println(conductDrugRepository.findOne(100));
        System.out.println(conductDrugRepository.findOne(100).getMaster());
        System.out.println(conductShinryouRepository.findOne(100));
        System.out.println(conductShinryouRepository.findOne(100).getMaster());
        System.out.println(conductKizaiRepository.findOne(100));
        System.out.println(conductKizaiRepository.findOne(100).getMaster());
        System.out.println(shoubyoumeiMasterRepository.findOne(new ShoubyoumeiMasterId(219003, Date.valueOf(LocalDate.of(2005,11,1)))));
        System.out.println(shuushokugoMasterRepository.findOne(1561));
        System.out.println(diseaseRepository.findOne(100));
        System.out.println(diseaseRepository.findOne(100).getMaster());
        System.out.println(diseaseAdjRepository.findOne(109));
        System.out.println(diseaseAdjRepository.findOne(109).getMaster());
        System.out.println(diseaseRepository.findOne(1754).getAdjList());
        System.out.println(hotlineRepository.findOne(100));
        System.out.println(iyakuhinMasterRepository.findTopByIyakuhincodeOrderByValidFromDesc(610406002));
        System.out.println(pharmaDrugRepository.findOne(610406389));
        System.out.println(pharmaQueueRepository.findAll());
        System.out.println(prescExampleRepository.findOne(525));
        System.out.println(prescExampleRepository.findOne(525).getMaster());
        System.out.println(chargeRepository.findOne(100));
        System.out.println(paymentRepository.findOne(100));
        System.out.println(wqueueRepository.findAll());

        {
            Visit visit = visitRepository.findOne(2000);
            System.out.println(visit);
            System.out.println(visit.getShinryouList());
        }

        {
            Iterable<PharmaQueue>list = pharmaQueueRepository.findAll();
            int i = 0;
            for(PharmaQueue q: list){
                if( i > 0 ){
                    break;
                }
                System.out.println(q);
                System.out.println(q.getVisit());
                i += 1;
            }
        }
    }
}
