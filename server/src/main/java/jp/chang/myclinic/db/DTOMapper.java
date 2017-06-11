package jp.chang.myclinic.db;

import jp.chang.myclinic.dto.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DTOMapper {

	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");

	public PatientDTO toPatientDTO(Patient patient){
		PatientDTO patientDTO = new PatientDTO();
		patientDTO.patientId = patient.getPatientId();
		patientDTO.lastName = patient.getLastName();
		patientDTO.firstName = patient.getFirstName();
		patientDTO.lastNameYomi = patient.getLastNameYomi();
		patientDTO.firstNameYomi = patient.getFirstNameYomi();
		patientDTO.birthday = nullableDateToString(patient.getBirthday());
		patientDTO.sex = patient.getSex();
		patientDTO.address = patient.getAddress();
		patientDTO.phone = patient.getPhone();
		return patientDTO;
	}

	public Patient fromPatientDTO(PatientDTO patientDTO){
		Patient patient = new Patient();
		patient.setPatientId(patientDTO.patientId);
		patient.setLastName(patientDTO.lastName);
		patient.setFirstName(patientDTO.firstName);
		patient.setLastNameYomi(patientDTO.lastNameYomi);
		patient.setFirstNameYomi(patientDTO.firstNameYomi);
		patient.setBirthday(stringToDate(patientDTO.birthday));
		patient.setSex(patientDTO.sex);
		patient.setAddress(patientDTO.address);
		patient.setPhone(patientDTO.phone);
		return patient;
	}

	public WqueueDTO toWqueueDTO(Wqueue wqueue){
		WqueueDTO wqueueDTO = new WqueueDTO();
		wqueueDTO.visitId = wqueue.getVisitId();
		wqueueDTO.waitState = wqueue.getWaitState();
		return wqueueDTO;
	}

	public Wqueue fromWqueueDTO(WqueueDTO wqueueDTO){
		Wqueue wqueue = new Wqueue();
		wqueue.setVisitId(wqueueDTO.visitId);
		wqueue.setWaitState(wqueueDTO.waitState);
		return wqueue;
	}

	public VisitDTO toVisitDTO(Visit visit){
		VisitDTO visitDTO = new VisitDTO();
		visitDTO.visitId = visit.getVisitId();
		visitDTO.patientId = visit.getPatientId();
		visitDTO.visitedAt = timestampToString(visit.getVisitedAt());
		visitDTO.shahokokuhoId = visit.getShahokokuhoId();
		visitDTO.koukikoureiId = visit.getKoukikoureiId();
		visitDTO.roujinId = visit.getRoujinId();
		visitDTO.kouhi1Id = visit.getKouhi1Id();
		visitDTO.kouhi2Id = visit.getKouhi2Id();
		visitDTO.kouhi3Id = visit.getKouhi3Id();
		return visitDTO;
	}

	public Visit fromVisitDTO(VisitDTO visitDTO){
		Visit visit = new Visit();
		visit.setVisitId(visitDTO.visitId);
		visit.setPatientId(visitDTO.patientId);
		visit.setVisitedAt(stringToTimestamp(visitDTO.visitedAt));
		visit.setShahokokuhoId(visitDTO.shahokokuhoId);
		visit.setKoukikoureiId(visitDTO.koukikoureiId);
		visit.setRoujinId(visitDTO.roujinId);
		visit.setKouhi1Id(visitDTO.kouhi1Id);
		visit.setKouhi2Id(visitDTO.kouhi2Id);
		visit.setKouhi3Id(visitDTO.kouhi3Id);
		return visit;
	}

	public ShahokokuhoDTO toShahokokuhoDTO(Shahokokuho shahokokuho){
		ShahokokuhoDTO shahokokuhoDTO = new ShahokokuhoDTO();
		shahokokuhoDTO.shahokokuhoId = shahokokuho.getShahokokuhoId();
		shahokokuhoDTO.patientId = shahokokuho.getPatientId();
		shahokokuhoDTO.hokenshaBangou = shahokokuho.getHokenshaBangou();
		shahokokuhoDTO.hihokenshaBangou = shahokokuho.getHihokenshaBangou();
		shahokokuhoDTO.hihokenshaKigou= shahokokuho.getHihokenshaKigou();
		shahokokuhoDTO.honnin = shahokokuho.getHonnin();
		shahokokuhoDTO.kourei = shahokokuho.getKourei();
		shahokokuhoDTO.validFrom = shahokokuho.getValidFrom().toString();
		shahokokuhoDTO.validUpto = shahokokuho.getValidUpto();
		return shahokokuhoDTO;
	}

	public Shahokokuho fromShahokokuhoDTO(ShahokokuhoDTO shahokokuhoDTO){
		Shahokokuho shahokokuho = new Shahokokuho();
		shahokokuho.setShahokokuhoId(shahokokuhoDTO.shahokokuhoId);
		shahokokuho.setPatientId(shahokokuhoDTO.patientId);
		shahokokuho.setHokenshaBangou(shahokokuhoDTO.hokenshaBangou);
		shahokokuho.setHihokenshaBangou(shahokokuhoDTO.hihokenshaBangou);
		shahokokuho.setHihokenshaKigou(shahokokuhoDTO.hihokenshaKigou);
		shahokokuho.setHonnin(shahokokuhoDTO.honnin);
		shahokokuho.setKourei(shahokokuhoDTO.kourei);
		shahokokuho.setValidFrom(stringToDate(shahokokuhoDTO.validFrom));
		shahokokuho.setValidUpto(shahokokuhoDTO.validUpto);
		return shahokokuho;
	}

	public KoukikoureiDTO toKoukikoureiDTO(Koukikourei koukikourei){
		KoukikoureiDTO koukikoureiDTO = new KoukikoureiDTO();
		koukikoureiDTO.koukikoureiId = koukikourei.getKoukikoureiId();
		koukikoureiDTO.patientId = koukikourei.getPatientId();
		koukikoureiDTO.hokenshaBangou = koukikourei.getHokenshaBangou();
		koukikoureiDTO.hihokenshaBangou = koukikourei.getHihokenshaBangou();
		koukikoureiDTO.futanWari = koukikourei.getFutanWari();
		koukikoureiDTO.validFrom = koukikourei.getValidFrom().toString();
		koukikoureiDTO.validUpto = koukikourei.getValidUpto();
		return koukikoureiDTO;
	}

	public Koukikourei fromKoukikoureiDTO(KoukikoureiDTO koukikoureiDTO){
		Koukikourei koukikourei = new Koukikourei();
		koukikourei.setKoukikoureiId(koukikoureiDTO.koukikoureiId);
		koukikourei.setPatientId(koukikoureiDTO.patientId);
		koukikourei.setHokenshaBangou(koukikoureiDTO.hokenshaBangou);
		koukikourei.setHihokenshaBangou(koukikoureiDTO.hihokenshaBangou);
		koukikourei.setFutanWari(koukikoureiDTO.futanWari);
		koukikourei.setValidFrom(stringToDate(koukikoureiDTO.validFrom));
		koukikourei.setValidUpto(koukikoureiDTO.validUpto);
		return koukikourei;
	}

	public RoujinDTO toRoujinDTO(Roujin roujin){
		RoujinDTO roujinDTO = new RoujinDTO();
		roujinDTO.roujinId = roujin.getRoujinId();
		roujinDTO.patientId = roujin.getPatientId();
		roujinDTO.shichouson = roujin.getShichouson();
		roujinDTO.jukyuusha = roujin.getJukyuusha();
		roujinDTO.futanWari = roujin.getFutanWari();
		roujinDTO.validFrom = roujin.getValidFrom().toString();
		roujinDTO.validUpto = roujin.getValidUpto();
		return roujinDTO;
	}

	public Roujin fromRoujinDTO(RoujinDTO roujinDTO){
		Roujin roujin = new Roujin();
		roujin.setRoujinId(roujinDTO.roujinId);
		roujin.setPatientId(roujinDTO.patientId);
		roujin.setShichouson(roujinDTO.shichouson);
		roujin.setJukyuusha(roujinDTO.jukyuusha);
		roujin.setFutanWari(roujinDTO.futanWari);
		roujin.setValidFrom(stringToDate(roujinDTO.validFrom));
		roujin.setValidUpto(roujinDTO.validUpto);
		return roujin;
	}

	public KouhiDTO toKouhiDTO(Kouhi kouhi){
		KouhiDTO kouhiDTO = new KouhiDTO();
		kouhiDTO.kouhiId = kouhi.getKouhiId();
		kouhiDTO.patientId = kouhi.getPatientId();
		kouhiDTO.futansha = kouhi.getFutansha();
		kouhiDTO.jukyuusha = kouhi.getJukyuusha();
		kouhiDTO.validFrom = kouhi.getValidFrom().toString();
		kouhiDTO.validUpto = kouhi.getValidUpto();
		return kouhiDTO;
	}

	public Kouhi fromKouhiDTO(KouhiDTO kouhiDTO){
		Kouhi kouhi = new Kouhi();
		kouhi.setKouhiId(kouhiDTO.kouhiId);
		kouhi.setPatientId(kouhiDTO.patientId);
		kouhi.setFutansha(kouhiDTO.futansha);
		kouhi.setJukyuusha(kouhiDTO.jukyuusha);
		kouhi.setValidFrom(stringToDate(kouhiDTO.validFrom));
		kouhi.setValidUpto(kouhiDTO.validUpto);
		return kouhi;
	}

	public ChargeDTO toChargeDTO(Charge charge){
		ChargeDTO chargeDTO = new ChargeDTO();
		chargeDTO.visitId = charge.getVisitId();
		chargeDTO.charge = charge.getCharge();
		return chargeDTO;
	}

	public Charge fromChargeDTO(ChargeDTO chargeDTO){
		Charge charge = new Charge();
		charge.setVisitId(chargeDTO.visitId);
		charge.setCharge(chargeDTO.charge);
		return charge;
	}

	public PaymentDTO toPaymentDTO(Payment payment){
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.visitId = payment.getVisitId();
		paymentDTO.amount = payment.getAmount();
		paymentDTO.paytime = timestampToString(payment.getPaytime());
		return paymentDTO;
	}

	public Payment fromPaymentDTO(PaymentDTO paymentDTO){
		Payment payment = new Payment();
		payment.setVisitId(paymentDTO.visitId);
		payment.setAmount(paymentDTO.amount);
		payment.setPaytime(stringToTimestamp(paymentDTO.paytime));
		return payment;
	}

	public ShinryouDTO toShinryouDTO(Shinryou shinryou){
		ShinryouDTO shinryouDTO = new ShinryouDTO();
		shinryouDTO.shinryouId = shinryou.getShinryouId();
		shinryouDTO.visitId = shinryou.getVisitId();
		shinryouDTO.shinryoucode = shinryou.getShinryoucode();
		return shinryouDTO;
	}

	public Shinryou fromShinryouDTO(ShinryouDTO shinryouDTO){
		Shinryou shinryou = new Shinryou();
		shinryou.setShinryouId(shinryouDTO.shinryouId);
		shinryou.setVisitId(shinryouDTO.visitId);
		shinryou.setShinryoucode(shinryouDTO.shinryoucode);
		return shinryou;
	}

	public ShinryouMasterDTO toShinryouMasterDTO(ShinryouMaster master){
		ShinryouMasterDTO masterDTO = new ShinryouMasterDTO();
		masterDTO.shinryoucode = master.getShinryoucode();
		masterDTO.validFrom = master.getValidFrom().toString();
		masterDTO.name = master.getName();
		masterDTO.tensuu = master.getTensuu();
		masterDTO.tensuuShikibetsu = master.getTensuuShikibetsu();
		masterDTO.shuukeisaki = master.getShuukeisaki();
		masterDTO.houkatsukensa = master.getHoukatsukensa();
		masterDTO.oushinkubun = master.getOushinkubun();
		masterDTO.kensaGroup = master.getKensagroup();
		masterDTO.roujinTekiyou = master.getRoujintekiyou();
		masterDTO.codeShou = master.getCodeShou();
		masterDTO.codeBu = master.getCodeBu();
		masterDTO.codeAlpha = master.getCodeAlpha();
		masterDTO.codeKubun = master.getCodeKubun();
		masterDTO.validUpto = master.getValidUpto();
		return masterDTO;
	}

	public ShinryouMaster fromShinryouMasterDTO(ShinryouMasterDTO masterDTO){
		ShinryouMaster master = new ShinryouMaster();
		master.setShinryoucode(masterDTO.shinryoucode);
		master.setValidFrom(stringToDate(masterDTO.validFrom));
		master.setName(masterDTO.name);
		master.setTensuu(masterDTO.tensuu);
		master.setTensuuShikibetsu(masterDTO.tensuuShikibetsu);
		master.setShuukeisaki(masterDTO.shuukeisaki);
		master.setHoukatsukensa(masterDTO.houkatsukensa);
		master.setOushinkubun(masterDTO.oushinkubun);
		master.setKensagroup(masterDTO.kensaGroup);
		master.setRoujintekiyou(masterDTO.roujinTekiyou);
		master.setCodeShou(masterDTO.codeShou);
		master.setCodeBu(masterDTO.codeBu);
		master.setCodeAlpha(masterDTO.codeAlpha);
		master.setCodeKubun(masterDTO.codeKubun);
		master.setValidUpto(masterDTO.validUpto);
		return master;
	}

	public DrugDTO toDrugDTO(Drug drug){
		DrugDTO drugDTO = new DrugDTO();
		drugDTO.drugId = drug.getDrugId();
		drugDTO.visitId = drug.getVisitId();
		drugDTO.iyakuhincode = drug.getIyakuhincode();
		drugDTO.amount = drug.getAmount().doubleValue();
		drugDTO.usage = drug.getUsage();
		drugDTO.days = drug.getDays();
		drugDTO.category = drug.getCategory();
		drugDTO.shuukeisaki = drug.getShuukeisaki();
		drugDTO.prescribed = drug.getPrescribed();
		return drugDTO;
	}

	public Drug fromDrugDTO(DrugDTO drugDTO){
		Drug drug = new Drug();
		drug.setDrugId(drugDTO.drugId);
		drug.setVisitId(drugDTO.visitId);
		drug.setIyakuhincode(drugDTO.iyakuhincode);
		drug.setAmount(BigDecimal.valueOf(drugDTO.amount));
		drug.setUsage(drugDTO.usage);
		drug.setDays(drugDTO.days);
		drug.setCategory(drugDTO.category);
		drug.setShuukeisaki(drugDTO.shuukeisaki);
		drug.setPrescribed(drugDTO.prescribed);
		return drug;
	}

	public IyakuhinMasterDTO toIyakuhinMasterDTO(IyakuhinMaster master){
		IyakuhinMasterDTO masterDTO = new IyakuhinMasterDTO();
		masterDTO.iyakuhincode = master.getIyakuhincode();
		masterDTO.validFrom = master.getValidFrom().toString();
		masterDTO.name = master.getName();
		masterDTO.yomi = master.getYomi();
		masterDTO.unit = master.getUnit();
		masterDTO.yakka = master.getYakka().doubleValue();
		masterDTO.madoku = master.getMadoku();
		masterDTO.kouhatsu = master.getKouhatsu();
		masterDTO.zaikei = master.getZaikei();
		masterDTO.validUpto = master.getValidUpto();
		return masterDTO;
	}

	public IyakuhinMaster fromIyakuhinMasterDTO(IyakuhinMasterDTO masterDTO){
		IyakuhinMaster master = new IyakuhinMaster();
		master.setIyakuhincode(masterDTO.iyakuhincode);
		master.setValidFrom(stringToDate(masterDTO.validFrom));
		master.setName(masterDTO.name);
		master.setYomi(masterDTO.yomi);
		master.setUnit(masterDTO.unit);
		master.setYakka(BigDecimal.valueOf(masterDTO.yakka));
		master.setMadoku(masterDTO.madoku);
		master.setKouhatsu(masterDTO.kouhatsu);
		master.setZaikei(masterDTO.zaikei);
		master.setValidUpto(masterDTO.validUpto);
		return master;
	}

	public KizaiMasterDTO toKizaiMasterDTO(KizaiMaster master){
		KizaiMasterDTO masterDTO = new KizaiMasterDTO();
		masterDTO.kizaicode = master.getKizaicode();
		masterDTO.validFrom = master.getValidFrom().toString();
		masterDTO.name = master.getName();
		masterDTO.yomi = master.getYomi();
		masterDTO.unit = master.getUnit();
		masterDTO.kingaku = master.getKingaku().doubleValue();
		masterDTO.validUpto = master.getValidUpto();
		return masterDTO;
	}

	public KizaiMaster fromKizaiMasterDTO(KizaiMasterDTO masterDTO){
		KizaiMaster master = new KizaiMaster();
		master.setKizaicode(masterDTO.kizaicode);
		master.setValidFrom(stringToDate(masterDTO.validFrom));
		master.setName(masterDTO.name);
		master.setYomi(masterDTO.yomi);
		master.setUnit(masterDTO.unit);
		master.setKingaku(BigDecimal.valueOf(masterDTO.kingaku));
		master.setValidUpto(masterDTO.validUpto);
		return master;
	}

	public ConductDTO toConductDTO(Conduct conduct){
		ConductDTO conductDTO = new ConductDTO();
		conductDTO.conductId = conduct.getConductId();
		conductDTO.visitId = conduct.getVisitId();
		conductDTO.kind = conduct.getKind();
		return conductDTO;
	}

	public Conduct fromConductDTO(ConductDTO conductDTO){
		Conduct conduct = new Conduct();
		conduct.setConductId(conductDTO.conductId);
		conduct.setVisitId(conductDTO.visitId);
		conduct.setKind(conductDTO.kind);
		return conduct;	
	}

	public ConductDrugDTO toConductDrugDTO(ConductDrug drug){
		ConductDrugDTO drugDTO = new ConductDrugDTO();
		drugDTO.conductDrugId = drug.getConductDrugId();
		drugDTO.conductId = drug.getConductId();
		drugDTO.iyakuhincode = drug.getIyakuhincode();
		drugDTO.amount = drug.getAmount().doubleValue();
		return drugDTO;
	}

	public ConductDrug fromConductDrugDTO(ConductDrugDTO drugDTO){
		ConductDrug drug = new ConductDrug();
		drug.setConductDrugId(drugDTO.conductDrugId);
		drug.setConductId(drugDTO.conductId);
		drug.setIyakuhincode(drugDTO.iyakuhincode);
		drug.setAmount(BigDecimal.valueOf(drugDTO.amount));
		return drug;
	}

	public ConductKizaiDTO toConductKizaiDTO(ConductKizai kizai){
		ConductKizaiDTO kizaiDTO = new ConductKizaiDTO();
		kizaiDTO.conductKizaiId = kizai.getConductKizaiId();
		kizaiDTO.conductId = kizai.getConductId();
		kizaiDTO.kizaicode = kizai.getKizaicode();
		kizaiDTO.amount = kizai.getAmount().doubleValue();
		return kizaiDTO;
	}

	public ConductKizai fromConductKizaiDTO(ConductKizaiDTO kizaiDTO){
		ConductKizai kizai = new ConductKizai();
		kizai.setConductKizaiId(kizaiDTO.conductKizaiId);
		kizai.setConductId(kizaiDTO.conductId);
		kizai.setKizaicode(kizaiDTO.kizaicode);
		kizai.setAmount(BigDecimal.valueOf(kizaiDTO.amount));
		return kizai;
	}

	public ConductShinryouDTO toConductShinryouDTO(ConductShinryou shinryou){
		ConductShinryouDTO shinryouDTO = new ConductShinryouDTO();
		shinryouDTO.conductShinryouId = shinryou.getConductShinryouId();
		shinryouDTO.conductId = shinryou.getConductId();
		shinryouDTO.shinryoucode = shinryou.getShinryoucode();
		return shinryouDTO;
	}

	public ConductShinryou fromConductShinryouDTO(ConductShinryouDTO shinryouDTO){
		ConductShinryou shinryou = new ConductShinryou();
		shinryou.setConductShinryouId(shinryouDTO.conductShinryouId);
		shinryou.setConductId(shinryouDTO.conductId);
		shinryou.setShinryoucode(shinryouDTO.shinryoucode);
		return shinryou;
	}

	public GazouLabelDTO toGazouLabelDTO(GazouLabel gazouLabel){
		GazouLabelDTO gazouLabelDTO = new GazouLabelDTO();
		gazouLabelDTO.conductId = gazouLabel.getConductId();
		gazouLabelDTO.label = gazouLabel.getLabel();
		return gazouLabelDTO;
	}

	public GazouLabel fromGazouLabelDTO(GazouLabelDTO gazouLabelDTO){
		GazouLabel gazouLabel = new GazouLabel();
		gazouLabel.setConductId(gazouLabelDTO.conductId);
		gazouLabel.setLabel(gazouLabelDTO.label);
		return gazouLabel;
	}

	public Text fromTextDTO(TextDTO textDTO){
		Text text = new Text();
		text.setTextId(textDTO.textId);
		text.setVisitId(textDTO.visitId);
		text.setContent(textDTO.content);
		return text;
	}

	public TextDTO toTextDTO(Text text){
		TextDTO textDTO = new TextDTO();
		textDTO.textId = text.getTextId();
		textDTO.visitId = text.getVisitId();
		textDTO.content = text.getContent();
		return textDTO;
	}

	public PharmaQueueDTO toPharmaQueueDTO(PharmaQueue pharmaQueue){
		PharmaQueueDTO pharmaQueueDTO = new PharmaQueueDTO();
		pharmaQueueDTO.visitId = pharmaQueue.getVisitId();
		pharmaQueueDTO.pharmaState = pharmaQueue.getPharmaState();
		return pharmaQueueDTO;
	}

	public PharmaQueue fromPharmaQueueDTO(PharmaQueueDTO pharmaQueueDTO){
		PharmaQueue pharmaQueue = new PharmaQueue();
		pharmaQueue.setVisitId(pharmaQueueDTO.visitId);
		pharmaQueue.setPharmaState(pharmaQueueDTO.pharmaState);
		return pharmaQueue;
	}

	private String nullableDateToString(Date date){
		if( date == null ){
			return null;
		} else {
			return date.toString();
		}
	}

	private Date stringToDate(String str){
		if( str == null ){
			return null;
		} else if( "0000-00-00".equals(str) ){
			return null;
		} else {
			LocalDate d = LocalDate.parse(str, dateFormatter);
			return Date.valueOf(d);
		}
	}

	private String timestampToString(Timestamp ts){
		LocalDateTime dt = ts.toLocalDateTime();
		return dt.format(dateTimeFormatter);
	}

	private Timestamp stringToTimestamp(String str){
		LocalDateTime dt = LocalDateTime.parse(str, dateTimeFormatter);
		return Timestamp.valueOf(dt);
	}
}