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

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Обработка обновления: {}", update);

            if (update.message() != null && update.message().text() != null) {
                if (update.message().text().equalsIgnoreCase("/start")) {
                    sendMessage(update.message().chat().id(), "Бот успешно активирован!");
                }
                else {
                    if (update.message().text().equalsIgnoreCase("/help")) {
                        String helpText = "Тут выводится информация о том, как пользоваться ботом";
                        sendMessage(update.message().chat().id(), helpText);
                    }
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
