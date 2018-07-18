package jp.chang.myclinic.tracker.main;

import jp.chang.myclinic.tracker.TrackerWebsocket;

public class Main {

    public static void main(String[] args){
        TrackerWebsocket tracker = new TrackerWebsocket("http://localhost:18080/practice-log", System.out::println);
        tracker.start(() -> System.out.println("Tracker started."));
    }
}
