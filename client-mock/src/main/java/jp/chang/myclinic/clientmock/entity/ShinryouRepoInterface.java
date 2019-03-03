package jp.chang.myclinic.clientmock.entity;

import jp.chang.myclinic.dto.ShinryouAttrDTO;

import java.util.List;

public interface ShinryouRepoInterface {
    List<ShinryouAttrDTO> batchGetShinryouAttr(List<Integer> shinryouIds);
}
