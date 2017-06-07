package jp.chang.myclinic.dto;

import java.util.List;

public class VisitFullDTO {
	public VisitDTO visit;
	public List<TextDTO> texts;
	public List<ShinryouFullDTO> shinryouList;
	public List<DrugFullDTO> drugs;
	public List<ConductFullDTO> conducts;
}