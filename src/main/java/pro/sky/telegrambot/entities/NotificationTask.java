package pro.sky.telegrambot.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "message")
    private String message;

    @Column(name = "date_time_to_send")
    private LocalDateTime dateTimeToSend;

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTimeToSend() {
        return dateTimeToSend;
    }

    public void setDateTimeToSend(LocalDateTime dateTimeToSend) {
        this.dateTimeToSend = dateTimeToSend;
    }
}
