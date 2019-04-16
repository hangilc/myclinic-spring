package jp.chang.myclinic.dto;


import java.util.List;

public class BatchEnterRequestDTO {

    public List<DrugWithAttrDTO> drugs;
    public List<ShinryouWithAttrDTO> shinryouList;
    public List<ConductEnterRequestDTO> conducts;

}
