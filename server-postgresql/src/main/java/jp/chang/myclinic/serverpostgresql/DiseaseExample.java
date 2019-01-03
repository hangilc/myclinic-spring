package jp.chang.myclinic.serverpostgresql;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("myclinic")
public class DiseaseExample {
    public static class Entry {
        private String byoumei;
        private List<String> pre;
        private List<String> post;
        private String label;

        public String getByoumei() {
            return byoumei;
        }

        public void setByoumei(String byoumei) {
            this.byoumei = byoumei;
        }

        public List<String> getPre() {
            return pre;
        }

        public void setPre(List<String> pre) {
            this.pre = pre;
        }

        public List<String> getPost() {
            return post;
        }

        public void setPost(List<String> post) {
            this.post = post;
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
