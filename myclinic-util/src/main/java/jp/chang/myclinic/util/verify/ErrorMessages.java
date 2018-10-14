package jp.chang.myclinic.util.verify;

import java.util.ArrayList;
import java.util.List;

public class ErrorMessages {

    //private static Logger logger = LoggerFactory.getLogger(ErrorMessages.class);
    private List<String> errorMessages = new ArrayList<>();

    public void addIfError(String... errs){
        for(String err: errs) {
            if (err != null) {
                errorMessages.add(err);
            }
        }
    }

    public void add(String msg){
        errorMessages.add(msg);
    }

    public boolean hasError(){
        return errorMessages.size() > 0;
    }

    public boolean hasNoError(){
        return !hasError();
    }

    public List<String> getErrorMessages(){
        return errorMessages;
    }

    public String getErrorMessage(){
        return String.join("\n", errorMessages);
    }
}
