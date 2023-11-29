package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entities.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationTaskRepository notificationTaskRepository;
    private final TelegramBot telegramBot;
    private Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    public NotificationService(NotificationTaskRepository notificationTaskRepository, TelegramBot telegramBot) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void processScheduledNotifications() {
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> tasks = notificationTaskRepository.findByDateTimeToSend(currentTime);

        tasks.forEach(task -> {
            sendMessage(task.getChatId(), task.getMessage());

            notificationTaskRepository.delete(task);
        });
    }

    private void sendMessage(Long chatId, String text) {
        SendResponse sendResponse = telegramBot.execute(new SendMessage(chatId, text));

        if (!sendResponse.isOk()) {
            logger.error("Ошибка при отправке сообщения: {}", sendResponse);
        }
    }
}
