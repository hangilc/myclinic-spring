package jp.chang.myclinic.practice.lib.dateinput;

import java.util.ArrayList;
import java.util.List;

public class DateInputException extends RuntimeException {
    private List<String> errorMessages = new ArrayList<>();

    public List<String> getErrorMessages(){
        return errorMessages;
    }

    void addError(String message){
        errorMessages.add(message);
    }
}
