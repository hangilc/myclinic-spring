package jp.chang.myclinic.reception.lib;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {
    public T value;
    public List<String> errors = new ArrayList<>();
    public boolean hasError(){
        return errors.size() > 0;
    }
    public void addError(String message){
        errors.add(message);
    }
}
