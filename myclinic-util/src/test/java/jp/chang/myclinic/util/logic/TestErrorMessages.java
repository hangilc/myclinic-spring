package jp.chang.myclinic.util.logic;


import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestErrorMessages {

    //private static Logger logger = LoggerFactory.getLogger(TestErrorMessages.class);

    @Test
    public void testEmpty(){
        ErrorMessages em = new ErrorMessages();
        assertEquals(0, em.getMessages().size());
    }

    @Test
    public void testSingle(){
        ErrorMessages em = new ErrorMessages();
        String m = "テスト";
        em.add(m);
        assertEquals(1, em.getMessages().size());
        assertEquals(m, em.getMessages().get(0));
    }

    @Test
    public void testIndent(){
        ErrorMessages em = new ErrorMessages();
        String a = "a";
        String b = "b";
        String c = "c";
        em.add(a);
        em.indent();
        em.add(b);
        em.unindent();
        em.add(c);
        assertEquals(List.of(a, em.getIndentUnit() + b, c), em.getMessages());
    }

    @Test
    public void testDoubleIndent(){
        ErrorMessages em = new ErrorMessages();
        String a = "a";
        String b = "b";
        String c = "c";
        em.add(a);
        em.indent();
        em.add(b);
        em.indent();
        em.add(c);
        em.unindent();
        em.add(b);
        em.unindent();
        em.add(a);
        String indent = em.getIndentUnit();
        assertEquals(List.of(
                a, indent + b, indent + indent + c, indent + b, a
        ), em.getMessages());
    }

}
