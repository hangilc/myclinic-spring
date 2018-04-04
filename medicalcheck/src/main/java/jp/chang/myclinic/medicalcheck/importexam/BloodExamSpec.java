package jp.chang.myclinic.medicalcheck.importexam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class BloodExamSpec {

    private String name;
    private String unit;
    private String label;

    BloodExamSpec() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public String getLabel() {
        return label == null ? name : label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
