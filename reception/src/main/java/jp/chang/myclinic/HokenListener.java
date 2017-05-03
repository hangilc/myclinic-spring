package jp.chang.myclinic;

import java.util.concurrent.CompletableFuture;

interface HokenListener<T> {

	CompletableFuture<Boolean> onEntering(T hoken);
	void onEntered(T hoken);
	CompletableFuture<Boolean> onUpdating(T hoken);
	void onUpdated(T hoken);
	CompletableFuture<Boolean> onDeleting(T hoken);
	void onDeleted(T hoken);

}