import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;


public class TextController {

    private ArrayList<Long> userWouldSendPhoto;
    private Map<Long, String> lastItem = new HashMap<>();
    private Bot bot;
    private Buttons buttons;

    public TextController(Bot bot) {
        this.bot = bot;
        buttons = new Buttons(bot);
        userWouldSendPhoto = new ArrayList<>();

    }

    public void handle(Message message) {
        String text = message.getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());

        if (text.equals("/start") || text.equals("/lang")) {
            buttons.langChooseButton(message);
            return;
        }

        String lang = LanguageController.userLang.get(message.getChatId());
        if (lang == null) {
            buttons.langChooseButton(message);
            return;
        }

        if (text.equals("Login\uD83D\uDD10")) {
            login(sendMessage, lang, message.getFrom());
            userWouldSendPhoto.remove(message.getChatId());
        } else if (text.equals("Info\uD83D\uDCCB")) {
            info(sendMessage, lang);
            userWouldSendPhoto.remove(message.getChatId());
        } else if (text.equals("chiqish") || text.equals("выйти") || text.equals("log out")) {
            Instagram.accounts.remove(message.getChatId());
            buttons.menu(message.getChatId());
            userWouldSendPhoto.remove(message.getChatId());
            lastItem.remove(message.getChatId());
        } else if (text.equals("Istorya yaratish") || text.equals("Создание истории") || text.equals("Create a story")) {
            userWouldSendPhoto.add(message.getChatId());
            sendMessage.setText(buttons.setText("Fileni yuboring (foto\uD83D\uDDBC)", "Введите файл (фото\uD83D\uDDBC)", "Send file (photo\uD83D\uDDBC)", lang));
        } else if (text.equals("Post yaratish") || text.equals("Создание публикации") || text.equals("Create a post")) {
            userWouldSendPhoto.remove(message.getChatId());
            sendMessage.setText(buttons.setText("Fileni yuboring (foto\uD83D\uDDBC / video\uD83C\uDF9E)", "Введите файл (фото\uD83D\uDDBC / видео\uD83C\uDF9E)", "Faylni kiriting (photo\uD83D\uDDBC / video\uD83C\uDF9E)", lang));

        } else {
            userWouldSendPhoto.remove(message.getChatId());
            String last = lastItem.get(message.getChatId());
            String[] logPass;
            if (last == null) {
                sendMessage.setText(buttons.setText("topilmadi", "не найден", "not found", lang));
                return;
            }
            logPass = message.getText().split("-");
            System.out.println(Arrays.toString(logPass));
            if (logPass.length == 2) {
                SendMessage sendMessage1 = new SendMessage();
                sendMessage1.setText("⌛️");
                sendMessage1.setChatId(message.getChatId());

                Message message1 = bot.sendMessage(sendMessage1);

                Instagram4j instagram = Instagram4j.builder().username(logPass[0]).password(logPass[1].trim()).build();

                instagram.setup();
                try {
                    instagram.login();
                    InstagramSearchUsernameResult instagramSearchUsernameResult = instagram.sendRequest(new InstagramSearchUsernameRequest("profilebot_uz"));
                    System.out.println(instagramSearchUsernameResult.getStatus());
                    DeleteMessage deleteMessage = new DeleteMessage();
                    deleteMessage.setChatId(message.getChatId());
                    deleteMessage.setMessageId(message1.getMessageId());
                    bot.delete(deleteMessage);
                    if (instagramSearchUsernameResult.getStatus().equals("ok")) {
                        Instagram.accounts.put(message.getChatId(), instagram);
                        buttons.menuAfterLog(message.getChatId());
                        
                    } else {
                        sendMessage.setParseMode(ParseMode.MARKDOWN);
                        sendMessage.setText("*" + buttons.setText("Login yoki parol xato", "неверный логин или пароль", "Incorrect login or password", lang) + "!*");
                        bot.send(sendMessage);
                    }


                } catch (IOException e) {
                    SendMessage sendMessage2 = new SendMessage();
                    sendMessage2.setChatId(message.getChatId());
                    sendMessage2.setText("Login xato");
                    bot.send(sendMessage2);
                    throw new RuntimeException(e);
                }
                return;
            } else {
                SendMessage sendMessage1 = new SendMessage();
                sendMessage1.setText("Unknown");
                sendMessage1.setChatId(message.getChatId());
                bot.send(sendMessage1);
                return;
            }

        }
        bot.send(sendMessage);

    }

    private void instagramLog() {

    }

    private void info(SendMessage sendMessage, String lang) {
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setText(buttons.setText(
                " Bu botni *Azimjon Nazarov* yaratgan \n_Telegram:_ *@Nazarov22222*",
                " Этот бот создан *Азимжоном Назаровым* \n_Telegram:_ *@Nazarov22222*",
                " This bot was created by *Azimjon Nazarov* \n_Telegram:_ *@Nazarov22222*", lang));

    }

    private void login(SendMessage sendMessage, String lang, User user) {
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        lastItem.put(user.getId(), "login");
        sendMessage.setText("⚠️_" + buttons.setText(
                "Instagram login va parolingizni namunadagidek kiriting",
                "Введите логин и пароль от Instagram, как показано в примере",
                "Enter your Instagram login and password as shown in the example", lang) + "_ \uD83D\uDC47 \n" +
                "*@alish123 - 12456Azef*");

    }

    public ArrayList<Long> getUserWouldSendPhoto() {
        return userWouldSendPhoto;
    }

    public void setUserWouldSendPhoto(ArrayList<Long> userWouldSendPhoto) {
        this.userWouldSendPhoto = userWouldSendPhoto;
    }
}

