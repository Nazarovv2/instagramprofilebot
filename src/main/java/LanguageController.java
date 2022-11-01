import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.*;

public class LanguageController {
    public static Map<Long, String> userLang = new HashMap<>();

    private Bot bot;
    private Buttons buttons;

    public LanguageController(Bot bot) {
        this.bot = bot;
        buttons = new Buttons(bot);

    }

    public void handel(Message message, String text) {


        String[] str = text.split("/");


        EditMessageText editMessageText = new EditMessageText();

        editMessageText.setChatId(message.getChatId());
        editMessageText.setMessageId(message.getMessageId());

        switch (str[2]) {
            case "uz":
                editMessageText.setText("Siz \uD83C\uDDFA\uD83C\uDDFF o'zbek tilini tanladingiz");
                userLang.put(message.getChatId(), "uz");
                break;
            case "ru":
                editMessageText.setText("Вы выбрали \uD83C\uDDF7\uD83C\uDDFA русский язык");
                userLang.put(message.getChatId(), "ru");
                break;
            case "en":
                editMessageText.setText("You have selected  \uD83C\uDDEC\uD83C\uDDE7 english");
                userLang.put(message.getChatId(), "en");
                break;
            default:
                editMessageText.setText("Unkown language");
        }

        bot.edite(editMessageText);

        if (!buttons.check(message)) {
            buttons.subscribeButton(message.getChatId());
        } else if (ContactController.contactMap.get(message.getChatId()) == null) {
            buttons.requestContact(message, str[2]);
        }
    }


}
