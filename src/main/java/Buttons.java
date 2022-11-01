
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Buttons {
    private Bot bot;


    public Buttons(Bot bot) {
        this.bot = bot;
    }

    public void langChooseButton(Message message) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setParseMode("HTML");
        sendMessage.disableWebPagePreview();

        sendMessage.setText("\n Tilni tanlang !\n Choose Language !\n Выберите язык !");

        InlineKeyboardButton uzbButton = new InlineKeyboardButton();
        uzbButton.setText("\uD83C\uDDFA\uD83C\uDDFF Uz");
        uzbButton.setCallbackData("/lang/uz");

        InlineKeyboardButton engButton = new InlineKeyboardButton();
        engButton.setText("\uD83C\uDDEC\uD83C\uDDE7 En");
        engButton.setCallbackData("/lang/en");

        InlineKeyboardButton ruButton = new InlineKeyboardButton();
        ruButton.setText("\uD83C\uDDF7\uD83C\uDDFA Ru");
        ruButton.setCallbackData("/lang/ru");


        List<InlineKeyboardButton> row1 = Arrays.asList(uzbButton, ruButton, engButton);

        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        rowList.add(row1);


        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        keyboard.setKeyboard(rowList);

        sendMessage.setReplyMarkup(keyboard);
        bot.send(sendMessage);
    }

    public void menuAfterLog(Long chatId) {
        String lang = LanguageController.userLang.get(chatId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(setText(
                "Instagram profilingizni \n quyidagi tugamalar orqali boshqaring",
                "Управляйте своим профилем \nв Instagram с помощью кнопок ниже",
                "Manage your Instagram profile \n using the buttons below", lang));

        KeyboardButton logout = new KeyboardButton();
        logout.setText(setText("chiqish", "выйти", "log out", lang));

        KeyboardButton createPost = new KeyboardButton();
        createPost.setText(setText("Post yaratish", "Создание публикации", "Create a post", lang));

        KeyboardButton createStory = new KeyboardButton();
        createStory.setText(setText("Istorya yaratish", "Создание истории", "Create a story", lang));

        KeyboardRow row = new KeyboardRow();
        row.add(createPost);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(logout);

        KeyboardRow row3 = new KeyboardRow();
        row3.add(createStory);

        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(row);
        rowList.add(row3);
        rowList.add(row2);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setKeyboard(rowList);
        keyboard.setResizeKeyboard(true);
        sendMessage.setReplyMarkup(keyboard);
        bot.send(sendMessage);
    }

    public void menu(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        String lang = LanguageController.userLang.get(chatId);
        sendMessage.setText("*Login\uD83D\uDD10 -*  _" + setText("Instagram profilga kirish", "Войти в профиль Инстаграм", "Login to Instagram profile", lang) + "_\n" +
                "*Info\uD83D\uDCCB -*  _" + setText("Bot haqida ma'lumot", "Информация о боте", "Bot info", lang) + "_");

        KeyboardButton login = new KeyboardButton();
        login.setText("Login\uD83D\uDD10");

        KeyboardButton info = new KeyboardButton();
        info.setText("Info\uD83D\uDCCB");

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(login);
        keyboardRow.add(info);


        List<KeyboardRow> rowList = new ArrayList<>();
        rowList.add(keyboardRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(rowList);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        bot.send(sendMessage);
    }

    public void subscribeButton(Long chatId) {

        SendMessage sendMessage = new SendMessage();

        sendMessage.setParseMode(ParseMode.MARKDOWN);

        String lang = LanguageController.userLang.get(chatId);
        String text;
        if (lang == null) {
            text = "Kanalga obuna bolmagansiz";
            text += "Вы не подписаны на канал";
            text += "You are not subscribed to the channel";
        } else {
            text = setText("Kanalga obuna bolmagansiz",
                    "Вы не подписаны на канал", "You are not subscribed to the channel", lang);
        }

        sendMessage.setText("*" + text + "*");
        sendMessage.setChatId(chatId);


        InlineKeyboardButton subsButton = new InlineKeyboardButton();
        subsButton.setCallbackData("/channel");
        subsButton.setUrl("https://t.me/space_coders_uz");

        InlineKeyboardButton checkButton = new InlineKeyboardButton();
        checkButton.setCallbackData("/subscribeCheck");


        lang = LanguageController.userLang.get(chatId);

        text = setText("obuna bo'lish", "Подписаться", "to subscribe", lang);
        subsButton.setText(text);

        checkButton.setText(setText("Tekshirish", "Проверять", "Check", lang) + "✅");


        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(subsButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(checkButton);

        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        lists.add(row);
        lists.add(row2);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(lists);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        bot.send(sendMessage);
    }

    public void requestContact(Message message, String lang) {
        KeyboardButton getContact = new KeyboardButton();
        getContact.setRequestContact(true);
        getContact.setText(setText("Kontakt jonatish", "Отправить контакт", "Send contact", lang));

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(getContact);

        List<KeyboardRow> keyboardRowList = new LinkedList<>();
        keyboardRowList.add(keyboardRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setResizeKeyboard(true);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(setText("Kontakt jonatish tugmasiga bosin", "Нажмите кнопку Отправить контакт", "Click the Send Contact button", lang) + "☎️");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        bot.send(sendMessage);
    }

    public String setText(String uz, String ru, String en, String lang) {
        String result = null;
        switch (lang) {
            case "uz":
                result = uz;
                break;
            case "ru":
                result = ru;
                break;
            case "en":
                result = en;
                break;
        }
        return result;

    }

    public boolean check(Message message) {

        GetChatMember getChatMember = new GetChatMember("-1001550716634", message.getChatId());
        ChatMember chatMember;
        try {
            chatMember = bot.execute(getChatMember);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        return !chatMember.getStatus().equalsIgnoreCase("left");

    }


}
