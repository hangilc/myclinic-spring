package jp.chang.myclinic.database;

import jp.chang.myclinic.MyclinicConsts.WqueueState;

public interface Wqueue {
	WqueueState getWaitState();
	int getVisitId();
}