package jp.chang.myclinic.mockdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NameEntry {

    String kanji;
    String yomi;
    int freq;

    NameEntry(String kanji, String yomi, int freq){
        this.kanji = kanji;
        this.yomi = yomi;
        this.freq = freq;
    }

    @Override
    public String toString() {
        return "NameEntry{" +
                "kanji='" + kanji + '\'' +
                ", yomi='" + yomi + '\'' +
                ", freq=" + freq +
                '}';
    }
}
