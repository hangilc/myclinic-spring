package jp.chang.myclinic.apitool.lib;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnknownType;

import java.util.Arrays;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Helper {

    private static Helper helper = new Helper();

    public static Helper getInstance(){
        return helper;
    }

    public String capitalize(String s){
        return s.substring(0, 1).toUpperCase() + s.substring(1, s.length());
    }

    public String snakeToCapital(String s){
        return Arrays.stream(s.split("_"))
                .map(this::capitalize)
                .collect(joining(""));
    }

    public String snakeToCamel(String s){
        String capital = snakeToCapital(s);
        return capital.substring(0, 1).toLowerCase() + capital.substring(1, capital.length());
    }

    public ClassOrInterfaceType createGenericType(String name, String paramType) {
        return new ClassOrInterfaceType(null, new SimpleName(name),
                NodeList.nodeList(new ClassOrInterfaceType(null, paramType)));
    }

    public ClassOrInterfaceType createGenericType(String name, Type paramType) {
        return new ClassOrInterfaceType(null, new SimpleName(name),
                NodeList.nodeList(paramType));
    }

    public ClassOrInterfaceType createGenericType(String name, String paramType1, String paramType2) {
        return createGenericType(name, createGenericType(paramType1, paramType2));
    }

    public NodeList<Parameter> createParameters(String... paramNames){
        return NodeList.nodeList(Arrays.stream(paramNames)
                .map(name -> new Parameter(new UnknownType(), name))
                .collect(toList()));
    }


}
