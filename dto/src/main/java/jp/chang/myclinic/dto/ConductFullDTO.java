package jp.chang.myclinic.dto;

import java.util.List;
import java.util.stream.Collectors;

public class ConductFullDTO {
	public ConductDTO conduct;
	public GazouLabelDTO gazouLabel;
	public List<ConductShinryouFullDTO> conductShinryouList;
	public List<ConductDrugFullDTO> conductDrugs;
	public List<ConductKizaiFullDTO> conductKizaiList;

	public static ConductFullDTO deepCopy(ConductFullDTO src){
		ConductFullDTO dst = new ConductFullDTO();
		dst.conduct = ConductDTO.copy(src.conduct);
		if( src.gazouLabel != null ){
			dst.gazouLabel = GazouLabelDTO.copy(src.gazouLabel);
		}
		dst.conductShinryouList = src.conductShinryouList.stream().map(ConductShinryouFullDTO::copy).collect(Collectors.toList());
		dst.conductDrugs = src.conductDrugs.stream().map(ConductDrugFullDTO::copy).collect(Collectors.toList());
		dst.conductKizaiList = src.conductKizaiList.stream().map(ConductKizaiFullDTO::copy).collect(Collectors.toList());
		return dst;
	}
}