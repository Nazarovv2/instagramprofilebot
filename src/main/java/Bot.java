import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;




public class Bot extends TelegramLongPollingBot {
    private MessageController messageController;

    private CallBackQueryController callBackQueryController;

    public Bot() {
        messageController = new MessageController(this);
        callBackQueryController = new CallBackQueryController(this);
    }

    @Override
    public String getBotUsername() {
        return "@instagram_profilebot";
    }

    @Override
    public String getBotToken() {
        return "5429066763:AAGKgx9hA6Dh4C_xvhGR9LUDH8JDnCdcgUY";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            messageController.handel(message);

            KeyboardButton keyboardButton=new KeyboardButton();
            keyboardButton.setRequestContact(true);

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery=update.getCallbackQuery();
            callBackQueryController.handel(callbackQuery);

        }

    }


    public void send(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void edite(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Message sendMessage(SendMessage sendMessage){
        try {
            return   execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
