package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entities.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    private NotificationTaskRepository notificationTaskRepository;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Обработка обновлений: {}", update);

            if (update.message() != null && update.message().text() != null) {
                String messageText = update.message().text();
                Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
                Matcher matcher = pattern.matcher(messageText);

                if (matcher.find()) {
                    String dateTimeString = matcher.group(1);
                    String reminderText = matcher.group(3);

                    LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

                    NotificationTask notificationTask = new NotificationTask();
                    notificationTask.setDateTimeToSend(dateTime);
                    notificationTask.setMessage(reminderText);

                    notificationTaskRepository.save(notificationTask);
                }

                if (update.message().text().equalsIgnoreCase("/start")) {
                    sendMessage(update.message().chat().id(), "Бот успешно активирован!");
                } else if (update.message().text().equalsIgnoreCase("/help")) {
                    String helpText = "Информация о том, как использовать ботом, отображается здесь";
                    sendMessage(update.message().chat().id(), helpText);
                }
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    private void sendMessage(Long chatId, String text) {
        SendResponse sendResponse = telegramBot.execute(new SendMessage(chatId, text));

        if (!sendResponse.isOk()) {
            logger.error("Ошибка при отправке сообщения: {}", sendResponse);
        }
    }

}
