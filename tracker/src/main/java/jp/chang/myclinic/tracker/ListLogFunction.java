package jp.chang.myclinic.tracker;

import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import retrofit2.Call;

import java.util.List;

@FunctionalInterface
public interface ListLogFunction {
    Call<List<PracticeLogDTO>> listPracticeLogInRangeCall(String date, int afterId, int beforeId);
}
