package jp.chang.myclinic.apitool.lib.tables;

import com.github.javaparser.ast.expr.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

class DtoFieldSetterCreator {

    String setterMethod;
    List<Expression> setterArgs = Collections.emptyList();
    Function<Expression, Expression> argMaker;

    public DtoFieldSetterCreator(String setterMethod, Function<Expression, Expression> argMaker) {
        this.setterMethod = setterMethod;
        this.argMaker = argMaker;
    }

    public DtoFieldSetterCreator(String setterMethod, List<Expression> setterAargs,
                                 Function<Expression, Expression> argMaker) {
        this.setterMethod = setterMethod;
        this.setterArgs = setterAargs;
        this.argMaker = argMaker;
    }

}
