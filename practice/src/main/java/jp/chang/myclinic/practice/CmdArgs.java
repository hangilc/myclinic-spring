package jp.chang.myclinic.practice;

import java.util.ArrayList;
import java.util.List;

class CmdArgs {

    //private static Logger logger = LoggerFactory.getLogger(CmdArgs.class);

    static void usage(){
        System.out.println("Usage: practice [options] [server-url]");
        System.out.println("If server-url is missing, env var MYCLINIC_SERVICE is used.");
        System.out.println("  Options: ");
        System.out.println("    --test-gui test,... ");
        System.out.println("               server-url is not required (or ignored)");
        System.out.println("    --test-integration");
        System.out.println("    --test-integration-one test (can be applied multiple times)");
    }

    private String serverUrl;
    private List<String> testGui;
    private boolean testIntegration;
    private List<String> testIntegrationOnes = new ArrayList<>();

    private static class Context {
        int i;
        String[] args;
        String currentOption;

        private Context( String[] args){
            this.i = 0;
            this.args = args;
        }

        boolean hasNext(){
            return i < args.length;
        }

        boolean hasNextOption(){
            return hasNext() && args[i].startsWith("-");
        }

        boolean hasNextRegularArg(){
            return hasNext() && !args[i].startsWith("-");
        }

        String getNextOption(){
            this.currentOption = args[i++];
            return currentOption;
        }

        String getOptionArg(){
            if( !hasNextRegularArg() ){
                System.err.printf("Cannot find argument for %s.", currentOption);
                usage();
                System.exit(1);
            }
            this.currentOption = null;
            return args[i++];
        }

        String getNextRegularArg(){
            this.currentOption = null;
            return args[i++];
        }

    }

    CmdArgs(String[] args) {
        Context ctx = new Context(args);
        List<String> regularArgs = new ArrayList<>();
        while( ctx.hasNext() ){
            if( ctx.hasNextOption() ){
                String opt = ctx.getNextOption();
                switch(opt){
                    case "--test-gui": {
                        this.testGui = List.of(ctx.getOptionArg().split(","));
                        break;
                    }
                    case "--test-integration": {
                        this.testIntegration = true;
                        break;
                    }
                    case "--test-integration-one": {
                        testIntegrationOnes.add(ctx.getOptionArg());
                        break;
                    }
                    default: {
                        System.err.printf("Unknown option %s\n", opt);
                        usage();
                        System.exit(1);
                    }
                }
            } else if( ctx.hasNextRegularArg() ){
                regularArgs.add(ctx.getNextRegularArg());
            } else {
                usage();
                throw new RuntimeException("Cannot happen.");
            }
        }
        int remArgs = regularArgs.size();
        if( remArgs == 0 ){
            if( testGui == null ){
                this.serverUrl = System.getenv("MYCLINIC_SERVICE");
            }
        } else if( remArgs == 1 ){
            this.serverUrl = regularArgs.get(0);
        } else {
            System.err.println("Too many arguments");
            usage();
            System.exit(1);
        }
    }

    String getServerUrl() {
        return serverUrl;
    }

    boolean isTestGui(){
        return testGui != null;
    }

    List<String> getTestGui(){
        return testGui;
    }

    public boolean isTestIntegration() {
        return testIntegration;
    }

    public List<String> getTestIntegrationOnes() {
        return testIntegrationOnes;
    }
}
