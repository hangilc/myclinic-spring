package jp.chang.myclinic.reception;

public interface BroadcastListener<T> {
	void onBroadcast(T value);
}

