package jp.chang.myclinic.dto;


import java.util.List;

public class BatchEnterRequestDTO {

    public List<DrugDTO> drugs;
    public List<DrugAttrDTO> drugAttrs;
    public List<ShinryouDTO> shinryouList;
    public List<ShinryouAttrDTO> shinryouAttrs;
    public List<ConductEnterRequestDTO> conducts;

}
