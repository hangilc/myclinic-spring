package jp.chang.myclinic.server;

import java.util.List;

public class ReferList {

    public static class ReferItem {

        private String hospital;
        private String section;
        private String doctor;

        public String getHospital() {
            return hospital;
        }

        public void setHospital(String hospital) {
            this.hospital = hospital;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getDoctor() {
            return doctor;
        }

        public void setDoctor(String doctor) {
            this.doctor = doctor;
        }

    }
    private List<ReferItem> list;

    public List<ReferItem> getList() {
        return list;
    }

    public void setList(List<ReferItem> list) {
        this.list = list;
    }
}
