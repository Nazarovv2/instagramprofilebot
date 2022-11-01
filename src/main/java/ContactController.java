
import org.telegram.telegrambots.meta.api.objects.Contact;

import java.util.HashMap;

import java.util.Map;

public class ContactController {

    public static Map<Long, Contact> contactMap=new HashMap<>();
    private Bot bot;

    public ContactController(Bot bot) {
        this.bot = bot;
    }


}


