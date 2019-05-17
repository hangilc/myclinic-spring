package jp.chang.myclinic.practice;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Tester {

    public interface MethodFilter {
        boolean acceptMethod(String className, String methodName);
    }

    private List<Object> targets = new ArrayList<>();

    void addTargets(Object... targets){
        this.targets.addAll(Arrays.asList(targets));
    }

    private static class Filter implements MethodFilter {
        private String classPart;
        private String methodPart;

        Filter(String rep){
            String[] parts = rep.split("\\.");
            if( parts.length != 2 ){
                throw new RuntimeException("Invalid test filter: " + rep);
            }
            this.classPart = parts[0];
            this.methodPart = parts[1];
        }

        @Override
        public boolean acceptMethod(String className, String methodName){
            return partMatches(classPart, className) && partMatches(methodPart, methodName);
        }

        private boolean partMatches(String part, String given){
            return part.equals("*") || part.equals(given);
        }
    }

    void runTest(String filterRep){
        Filter filter = new Filter(filterRep);
        runTest(filter);
    }

    void runTest(MethodFilter filter){
        try {
            for (Object target : targets) {
                System.out.println("target: " + target);
                Class<?> targetClass = target.getClass();
                for (Method method : targetClass.getMethods()) {
                    if( method.getDeclaringClass() == targetClass ) {
                        System.out.printf("method: %s\n", method);
                        if (isCandidate(method)) {
                            String className = targetClass.getSimpleName();
                            String methodName = method.getName();
                            if (filter.acceptMethod(className, methodName)) {
                                System.out.printf("%s.%s\n", className, methodName);
                                method.invoke(target);
                            }
                        }
                    }
                }
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private boolean isCandidate(Method method){
        return isPublic(method) && isNoArgMethod(method);
    }

    private boolean isPublic(Method method){
        return (method.getModifiers() & Modifier.PUBLIC) != 0;
    }

    private boolean isNoArgMethod(Method method){
        return method.getParameterCount() == 0;
    }
}
