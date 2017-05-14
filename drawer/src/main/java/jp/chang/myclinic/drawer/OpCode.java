package jp.chang.myclinic.drawer;

import java.util.Map;
import java.util.HashMap;

public enum OpCode {

    MoveTo("move_to"),
    LineTo("line_to"),
    CreateFont("create_font"),
    SetFont("set_font"),
    DrawChars("draw_chars"),
    SetTextColor("set_text_color"),
    CreatePen("create_pen"),
    SetPen("set_pen");

    private String ident;

    OpCode(String ident){
        this.ident = ident;
    }

    String getIdent(){
        return ident;
    }

    @Override
    public String toString(){
        return "OpCode[" +
                    "ident=" + ident +
                "]";
    }

    public static class IdentMap {

        private static Map<String, OpCode> map = new HashMap<>();

        {
            for(OpCode opCode: OpCode.values()){
                map.put(opCode.getIdent(), opCode);
            }

        }

        public static OpCode get(String ident){
            return map.get(ident);
        }

    }
}