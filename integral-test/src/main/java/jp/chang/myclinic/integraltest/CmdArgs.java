package jp.chang.myclinic.integraltest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

class CmdArgs {

    private String serverUrl;
    private String receptionHost;
    private String practiceHost;
    private String pharmaHost;
    private List<String> tests = new ArrayList<>();

    void usage(){
        System.out.println("Usage: integral-test options server-url [tests...]");
        System.out.println("  options:");
        System.out.println("    --reception reception-host");
        System.out.println("    --practice practice-host");
        System.out.println("    --pharma pharma-host");
    }

    CmdArgs(String[] args) {
        int i = 0;
        for(;i<args.length;i++){
            String arg = args[i];
            if( arg.startsWith("-") ){
                switch(arg){
                    case "--reception": {
                        i += 1;
                        if( i >= args.length ){
                            System.err.println("Cannot find arg to --reception");
                            System.exit(1);
                        }
                        this.receptionHost = args[i];
                        break;
                    }
                    case "--practice": {
                        i += 1;
                        if( i >= args.length ){
                            System.err.println("Cannot find arg to --practice");
                            System.exit(1);
                        }
                        this.practiceHost = args[i];
                        break;
                    }
                    case "--pharma": {
                        i += 1;
                        if( i >= args.length ){
                            System.err.println("Cannot find arg to --pharma");
                            System.exit(1);
                        }
                        this.pharmaHost = args[i];
                        break;
                    }
                    default: {
                        System.err.printf("Unknown option %s\n", arg);
                        System.exit(1);
                    }
                }
            } else {
                break;
            }
        }
        int rem = args.length - i;
        if( rem == 0 ) {
            System.err.println("Cannot find server-url argument");
            System.exit(1);
        } else {
            this.serverUrl = args[i++];
            for(;i<args.length;i++){
                this.tests.add(args[i]);
            }
        }
    }

    public String getServerUrl() {
        return serverUrl;
    }

    String getReceptionHost() {
        return receptionHost;
    }

    String getPracticeHost() {
        return practiceHost;
    }

    String getPharmaHost() {
        return pharmaHost;
    }

    List<String> getTests(){
        return tests;
    }
}
