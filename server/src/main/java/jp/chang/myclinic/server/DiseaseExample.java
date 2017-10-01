package jp.chang.myclinic.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("myclinic")
public class DiseaseExample {
    public static class Entry {
        private String byoumei;
        private List<String> adjList;
        private String label;

        public String getByoumei() {
            return byoumei;
        }

        public void setByoumei(String byoumei) {
            this.byoumei = byoumei;
        }

        public List<String> getAdjList() {
            return adjList;
        }

        public void setAdjList(List<String> adjList) {
            this.adjList = adjList;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    private List<Entry> diseaseExample = new ArrayList<>();

    public List<Entry> getDiseaseExample(){
        return diseaseExample;
    }
}
