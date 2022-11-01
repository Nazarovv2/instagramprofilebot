import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.*;
import java.time.LocalDateTime;


public class MessageController {
    private TextController textController;
    private Bot bot;
    private Buttons buttons;
    private Instagram instagram;

    public MessageController(Bot bot) {
        this.bot = bot;
        textController = new TextController(bot);
        buttons = new Buttons(bot);
        instagram = new Instagram(bot);
    }

    public void handel(Message message) {


        if ((message.hasText() && !message.getText().equals("/start") && !message.hasContact()) && !buttons.check(message)) {
            buttons.subscribeButton(message.getChatId());
            return;
        }

        if (message.hasText()) {
            textController.handle(message);

        } else if (message.hasPhoto() && textController.getUserWouldSendPhoto().contains(message.getChatId())) {
            instagram.createStory(message);
        } else if (message.hasPhoto() && !textController.getUserWouldSendPhoto().contains(message.getChatId())) {
            instagram.createPhotoPost(message);
        } else if (message.hasDocument() && !textController.getUserWouldSendPhoto().contains(message.getChatId())) {
            instagram.createVideoPost(message);
        } else if (message.hasContact()) {
            try {
                User user = message.getFrom();

                BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/Users.txt"));
                String s = bufferedReader.readLine();
                boolean b = true;

                while (s != null) {
                    if (s.split("#")[0].equals(String.valueOf(user.getId()))) {
                        b = false;
                        break;
                    }
                    s = bufferedReader.readLine();

                }

                if (b) {
                    PrintWriter printWriter = new PrintWriter(new FileWriter("src/main/resources/Users.txt", true));
                    printWriter.println(user.getId() + "#" + message.getContact().getPhoneNumber() + "#" + user.getFirstName() + "#" + user.getUserName() + "#" + LocalDateTime.now() + "#" + user.getLanguageCode());
                    printWriter.flush();
                    printWriter.close();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ContactController.contactMap.put(message.getChatId(), message.getContact());
            buttons.menu(message.getChatId());
        } else if (textController.getUserWouldSendPhoto().contains(message.getChatId())) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText(buttons.setText("Hozirda Istorya yaratish video formatli file bilan ishlamaydi", "В настоящее время создание историй не работает с файлами видеоформата.", "Currently, Story Creation does not work with video format files", LanguageController.userLang.get(message.getChatId())));
            bot.send(sendMessage);
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("Unknown");
            bot.send(sendMessage);
        }

    }


}
