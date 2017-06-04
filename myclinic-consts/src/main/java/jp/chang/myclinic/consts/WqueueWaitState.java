package jp.chang.myclinic.consts;

public enum WqueueWaitState
{
	WaitExam(MyclinicConsts.WqueueStateWaitExam, "診待"),
	InExam(MyclinicConsts.WqueueStateInExam, "診中"),
	WaitCashier(MyclinicConsts.WqueueStateWaitCashier, "会待"),
	WaitDrug(MyclinicConsts.WqueueStateWaitDrug, "薬待"),
	WaitReExam(MyclinicConsts.WqueueStateWaitReExam, "再待");

	private final int code;
	private final String label;

	WqueueWaitState(int code, String label){
		this.code = code;
		this.label = label;
	}

	public int getCode(){
		return code;
	}

	public String getLabel(){
		return label;
	}

	public static WqueueWaitState fromCode(int code){
		for(WqueueWaitState state: WqueueWaitState.values()){
			if( state.code == code ){
				return state;
			}
		}
		return null;
	}

	@Override
	public String toString(){
		return label;
	}
}
