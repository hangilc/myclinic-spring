package jp.chang.myclinic;

import jp.chang.myclinic.consts.WqueueWaitState;

class WqueueData {

	private final WqueueWaitState state;
	private final String label;

	public WqueueData(WqueueWaitState state, String label){
		this.state = state;
		this.label = label;
	}

	public WqueueWaitState getState(){
		return state;
	}

	public String getLabel(){
		return label;
	}
}