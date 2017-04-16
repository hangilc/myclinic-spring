package jp.chang.myclinic;

import jp.chang.myclinic.MyclinicConsts.WqueueState;

class WqueueData {

	private final WqueueState state;
	private final String label;

	public WqueueData(WqueueState state, String label){
		this.state = state;
		this.label = label;
	}

	public WqueueState getState(){
		return state;
	}

	public String getLabel(){
		return label;
	}
}