package jp.chang.myclinic.dto;

import java.util.List;

public class EnterConductByNamesRequestDTO {
    public int kind;
    public String gazouLabel;
    public List<String> shinryouNames;
    public List<EnterConductKizaiByNamesRequestDTO> kizaiList;
}