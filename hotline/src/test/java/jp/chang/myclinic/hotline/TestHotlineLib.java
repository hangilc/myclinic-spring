package jp.chang.myclinic.hotline;

import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.hotline.lib.HotlineUtil;
import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestHotlineLib {

    @Test
    public void testCreateHotlineDTO(){
        String sender = "practice";
        String recipient = "reception";
        String message = "おはようございます。";
        HotlineDTO dto = HotlineUtil.createHotlineDTO(sender, recipient, message);
        assertEquals(sender, dto.sender);
        assertEquals(recipient, dto.recipient);
        assertEquals(message, dto.message);
        assertNotNull(dto.postedAt);
        assertTrue(dto.postedAt.matches("\\d{4}-\\d{2}-\\d{2}"));
    }
}
