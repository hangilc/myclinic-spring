package jp.chang.myclinic.support.meisai;

import jp.chang.myclinic.consts.MeisaiSection;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.support.houkatsukensa.HoukatsuKensa;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.HokenUtil;
import jp.chang.myclinic.util.RcptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MeisaiServiceImpl implements MeisaiService {

    @Override
    public MeisaiDTO getMeisai(PatientDTO patient, HokenDTO hoken, LocalDate at,
                               List<ShinryouFullDTO> shinryouList, HoukatsuKensa.Revision revision,
                               List<DrugFullDTO> drugs, List<ConductFullDTO> conducts) {
        RcptVisit rcptVisit = new RcptVisit();
        rcptVisit.addShinryouList(shinryouList, revision);
        rcptVisit.addDrugs(drugs);
        rcptVisit.addConducts(conducts);
        Meisai meisai = rcptVisit.getMeisai();
        MeisaiDTO meisaiDTO = new MeisaiDTO();
        meisaiDTO.sections = new ArrayList<>();
        for (MeisaiSection section : MeisaiSection.values()) {
            List<SectionItem> items = meisai.getItems(section);
            if (items != null) {
                MeisaiSectionDTO meisaiSectionDTO = new MeisaiSectionDTO();
                meisaiSectionDTO.name = section.toString();
                meisaiSectionDTO.label = section.getLabel();
                meisaiSectionDTO.items = items.stream()
                        .map(this::toSectionItemDTO)
                        .collect(Collectors.toList());
                meisaiSectionDTO.sectionTotalTen = SectionItem.sum(items);
                meisaiDTO.sections.add(meisaiSectionDTO);
            }
        }
        meisaiDTO.totalTen = meisai.totalTen();
        if (patient.birthday != null) {
            meisaiDTO.hoken = hoken;
            LocalDate birthdayDate = DateTimeUtil.parseSqlDate(patient.birthday);
            int rcptAge = HokenUtil.calcRcptAge(birthdayDate.getYear(), birthdayDate.getMonth().getValue(),
                    birthdayDate.getDayOfMonth(), at.getYear(), at.getMonth().getValue());
            meisaiDTO.futanWari = HokenUtil.calcFutanWari(hoken, rcptAge);
        } else {
            meisaiDTO.futanWari = 10;
        }
        meisaiDTO.charge = RcptUtil.calcCharge(meisaiDTO.totalTen, meisaiDTO.futanWari);
        return meisaiDTO;
    }

    private SectionItemDTO toSectionItemDTO(SectionItem sectionItem) {
        SectionItemDTO sectionItemDTO = new SectionItemDTO();
        sectionItemDTO.label = sectionItem.getLabel();
        sectionItemDTO.tanka = sectionItem.getTanka();
        sectionItemDTO.count = sectionItem.getCount();
        return sectionItemDTO;
    }

}
