import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CallBackQueryController {
    private Bot bot;
    private LanguageController languageController;
    private ContactController contactController;
    private Buttons buttons;

    public CallBackQueryController(Bot bot) {
        this.bot = bot;
        languageController = new LanguageController(bot);
        contactController = new ContactController(bot);
        buttons = new Buttons(bot);
    }

    public void handel(CallbackQuery callbackQuery) {

        Message message = callbackQuery.getMessage();

        String data = callbackQuery.getData();

        if (data.startsWith("/lang")) {
            languageController.handel(message, data);
        } else if (data.equals("/subscribeCheck")) {
            if (!buttons.check(message)) {
                buttons.subscribeButton(message.getChatId());
            } else {

              if (ContactController.contactMap.get(message.getChatId())==null){
                  String lang = LanguageController.userLang.get(message.getChatId());
                  buttons.requestContact(message, lang);
              }


                DeleteMessage deleteMessage = new DeleteMessage();
                deleteMessage.setChatId(message.getChatId());
                deleteMessage.setMessageId(message.getMessageId());
                try {
                    bot.execute(deleteMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


}
