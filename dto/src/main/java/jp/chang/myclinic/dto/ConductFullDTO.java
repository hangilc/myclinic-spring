package jp.chang.myclinic.dto;

import java.util.List;

public class ConductFullDTO {
	public ConductDTO conduct;
	public GazouLabelDTO gazouLabel;
	public List<ConductShinryouFullDTO> conductShinryouList;
	public List<ConductDrugFullDTO> conductDrugs;
}