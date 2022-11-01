import org.apache.http.client.ClientProtocolException;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramUploadPhotoRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUploadStoryPhotoRequest;
import org.brunocvcunha.instagram4j.requests.InstagramUploadVideoRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramConfigureMediaResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramConfigureStoryResult;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Instagram {
    public static Map<Long, Instagram4j> accounts = new HashMap<>();
    private Bot bot;
    private Buttons buttons;

    public Instagram(Bot bot) {
        this.bot = bot;
        buttons = new Buttons(bot);
    }

    public void createPhotoPost(Message message) {

        Instagram4j instagram4j = accounts.get(message.getChatId());


        if (instagram4j == null) {
            return;
        }
        String lang = LanguageController.userLang.get(message.getChatId());
        if (lang == null) {
            buttons.langChooseButton(message);
            return;
        }

        SendMessage sendMessage1=new SendMessage();
        sendMessage1.setText("⌛️");
        sendMessage1.setChatId(message.getChatId());
        Message message1 = bot.sendMessage(sendMessage1);

        String caption = "test post created by @instagram_profilebot";
        if (message.getCaption() != null) {
            caption = message.getCaption();
        }

        List<PhotoSize> photo = message.getPhoto();

        PhotoSize photoSize = photo.get(photo.size() - 1);
        photoSize.getFileId();


        GetFile getFile = new GetFile();
        getFile.setFileId(photoSize.getFileId());


        try {

            org.telegram.telegrambots.meta.api.objects.File file = bot.execute(getFile);
            File file1 = bot.downloadFile(file, new File("src/main/resources/123.jpg"));
            InstagramConfigureMediaResult instagramConfigureMediaResult = instagram4j.sendRequest(new InstagramUploadPhotoRequest(file1, caption));

            DeleteMessage deleteMessage=new DeleteMessage();
            deleteMessage.setChatId(message.getChatId());
            deleteMessage.setMessageId(message1.getMessageId());
            bot.delete(deleteMessage);


            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());

            if (instagramConfigureMediaResult.getStatus().equals("ok")) {

                sendMessage.setText(buttons.setText("Post yaratildi", "Пост создан", "Post created", lang)+"✅");
            } else {
                sendMessage.setText(buttons.setText("Post yaratilmadi", "Пост не создан", "Post not created", lang)+"❌");
            }

            bot.send(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void createVideoPost(Message message) {

        Instagram4j instagram4j = accounts.get(message.getChatId());
        if (instagram4j == null) {
            return;
        }

        String lang = LanguageController.userLang.get(message.getChatId());
        if (lang == null) {
            buttons.langChooseButton(message);
            return;
        }

        SendMessage sendMessage1=new SendMessage();
        sendMessage1.setText("⌛️");
        sendMessage1.setChatId(message.getChatId());
        Message message1 = bot.sendMessage(sendMessage1);

        String caption = "test post created by @instagram_profilebot";
        if (message.getCaption() != null) {
            caption = message.getCaption();
        }

        Document document = message.getDocument();

        GetFile getFile = new GetFile();
        getFile.setFileId(document.getFileId());


        try {
            org.telegram.telegrambots.meta.api.objects.File file = bot.execute(getFile);
            File file1 = bot.downloadFile(file, new File("src/main/resources/123.mp3"));
            InstagramConfigureMediaResult instagramConfigureMediaResult = instagram4j.sendRequest(new InstagramUploadVideoRequest(file1, caption));


            DeleteMessage deleteMessage=new DeleteMessage();
            deleteMessage.setChatId(message.getChatId());
            deleteMessage.setMessageId(message1.getMessageId());
            bot.delete(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());

            if (instagramConfigureMediaResult.getStatus().equals("ok")) {

                sendMessage.setText(buttons.setText("Post yaratildi", "Пост создан", "Post created", lang)+"✅");
            } else {
                sendMessage.setText(buttons.setText("Post yaratilmadi", "Пост не создан", "Post not created", lang)+"❌");
            }

            bot.send(sendMessage);


        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public void createStory(Message message) {

        Instagram4j instagram4j = accounts.get(message.getChatId());


        if (instagram4j == null) {
            return;
        }



        String lang = LanguageController.userLang.get(message.getChatId());
        if (lang == null) {
            buttons.langChooseButton(message);
            return;
        }
        SendMessage sendMessage1=new SendMessage();
        sendMessage1.setText("⌛️");
        sendMessage1.setChatId(message.getChatId());
        Message message1 = bot.sendMessage(sendMessage1);


        List<PhotoSize> photo = message.getPhoto();

        PhotoSize photoSize = photo.get(photo.size() - 1);
        photoSize.getFileId();


        GetFile getFile = new GetFile();
        getFile.setFileId(photoSize.getFileId());


        try {

            org.telegram.telegrambots.meta.api.objects.File file = bot.execute(getFile);
            File file1 = bot.downloadFile(file, new File("src/main/resources/123.jpg"));
            InstagramConfigureStoryResult instagramConfigureStoryResult = instagram4j.sendRequest(new InstagramUploadStoryPhotoRequest(file1));

            DeleteMessage deleteMessage=new DeleteMessage();
            deleteMessage.setChatId(message.getChatId());
            deleteMessage.setMessageId(message1.getMessageId());
            bot.delete(deleteMessage);

            SendMessage sendMessage = new SendMessage();
            if (instagramConfigureStoryResult.getStatus().equals("ok")) {
                sendMessage.setChatId(message.getChatId());
                sendMessage.setText(buttons.setText("Istorya yaratildi", "История создана", "Story created", lang)+"✅");
            } else {
                sendMessage.setText(buttons.setText("Istorya yaratilmadi", "История не создана", "Story noy created", lang)+"❌");
            }

            bot.send(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
