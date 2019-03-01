package jp.chang.myclinic.practice;

public class MainStageServiceMock implements MainStageService {

    private String title = "";

    public String getTitle(){
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

}
