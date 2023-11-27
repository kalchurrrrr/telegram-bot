package pro.sky.telegrambot.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
