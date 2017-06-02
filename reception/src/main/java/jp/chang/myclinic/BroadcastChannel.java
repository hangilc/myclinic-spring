package jp.chang.myclinic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Window;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

class BroadcastChannel<T> {

	private List<BroadcastListener<T>> listeners = new ArrayList<>();

	public void broadcast(T value){
		EventQueue.invokeLater(() -> {
			listeners.forEach(listener -> listener.onBroadcast(value));
		});
	}

	public void addListener(BroadcastListener<T> listener){
		listeners.add(listener);
	}

	public void addListener(Window window, BroadcastListener<T> listener){
		listeners.add(listener);
		//System.out.println("listener added: " + listener);
		window.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent event){
				//System.out.println("listener removed: " + listener);
				removeListener(listener);
			}
		});
	}

	public void removeListener(BroadcastListener<T> listener){
		listeners.remove(listener);
	}

}