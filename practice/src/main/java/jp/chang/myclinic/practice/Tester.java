package jp.chang.myclinic.practice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Tester {

    public interface TestMethodSig {
        CompletableFuture<Void> invoke(CompletableFuture<Void> pre);
    }

    public static class TestMethod {
        private String name;
        private TestMethodSig sig;

        public TestMethod(String name, TestMethodSig sig) {
            this.name = name;
            this.sig = sig;
        }

        public String getName() {
            return name;
        }

        public TestMethodSig getSig() {
            return sig;
        }
    }

    public interface TestTarget {
        List<TestMethod> listTestMethods();

        List<TestMethod> listSingleTestMethods();
    }

    public interface MethodFilter {
        boolean acceptMethod(String className, String methodName);
    }

    private List<TestTarget> targets = new ArrayList<>();

    void addTargets(TestTarget... targets) {
        this.targets.addAll(Arrays.asList(targets));
    }

    private static class Filter implements MethodFilter {
        private String classPart;
        private String methodPart;

        Filter(String rep) {
            String[] parts = rep.split("\\.");
            if (parts.length != 2) {
                throw new RuntimeException("Invalid test filter: " + rep);
            }
            this.classPart = parts[0];
            this.methodPart = parts[1];
        }

        @Override
        public boolean acceptMethod(String className, String methodName) {
            return partMatches(classPart, className) && partMatches(methodPart, methodName);
        }

        private boolean partMatches(String part, String given) {
            return part.equals("*") || part.equals(given);
        }
    }

    CompletableFuture<Void> runTest(String filterRep, boolean isSingle) {
        Filter filter = new Filter(filterRep);
        return runTest(filter, isSingle);
    }

    CompletableFuture<Void> runTest(MethodFilter filter, boolean isSingle) {
        CompletableFuture<Void> cf = new CompletableFuture<>();
        CompletableFuture<Void> start = cf;
        try {
            outer: for (TestTarget target : targets) {
                String className = target.getClass().getSimpleName();
                if (isSingle) {
                    for (TestMethod m : target.listSingleTestMethods()) {
                        if (filter.acceptMethod(className, m.getName())) {
                            cf = invokeMethod(m, className, cf);
                            break outer;
                        }
                    }
                }
                for (TestMethod m : target.listTestMethods()) {
                    if (filter.acceptMethod(className, m.getName())) {
                        cf = invokeMethod(m, className, cf);
                        if( isSingle ){
                            break outer;
                        }
                    }
                }
            }
            start.complete(null);
            return cf;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<Void> invokeMethod(TestMethod method, String className,
                                                 CompletableFuture<Void> cf) {
        System.out.printf("%s.%s\n", className, method.getName());
        return method.getSig().invoke(cf);
    }

}
