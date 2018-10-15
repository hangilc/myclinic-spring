package jp.chang.myclinic.util.logic;

import java.util.ArrayList;
import java.util.List;

public class ErrorMessages {

    //private static Logger logger = LoggerFactory.getLogger(ErrorMessages.class);
    private List<String> errorMessages = new ArrayList<>();
    private String indent = "";
    private String indentUnit = "    ";

    public void add(String message){
        errorMessages.add(indent + message);
    }

    public void add(ErrorMessages em){
        em.getMessages().forEach(this::add);
    }

    public void addIndenting(ErrorMessages em){
        indent();
        add(em);
        unindent();
    }

    public void indent(){
        this.indent = indent + indentUnit;
    }

    public void unindent(){
        this.indent = indent.substring(indentUnit.length());
    }

    String getIndentUnit(){
        return indentUnit;
    }

    public List<String> getMessages(){
        return errorMessages;
    }

    public String getMessage(){
        return String.join("\n", errorMessages);
    }

    public boolean hasError(){
        return errorMessages.size() > 0;
    }

    public boolean hasNoError(){
        return !hasError();
    }

    public int getNumberOfErrors(){
        return errorMessages.size();
    }

    public boolean hasErrorSince(int previousNumberOfErrors){
        return errorMessages.size() > previousNumberOfErrors;
    }

    public boolean hasNoErrorSince(int previousNumberOfErrors){
        return !hasErrorSince(previousNumberOfErrors);
    }

    public void addComposite(String title, ErrorMessages compositeErrorMessages){
        add(title);
        indent();
        add(compositeErrorMessages);
        unindent();
    }
}
