package com.example.telegram_bot.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue
    private Long Id;

    private Long chatId;
    private String event;
    private LocalDateTime time;
    private LocalDateTime eventTime;

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    private String command;

    public NotificationTask() {
    }

    public NotificationTask(Long chatId, String event, LocalDateTime time, String doing) {
        this.chatId = chatId;
        this.event = event;
        this.time = time;
        this.command = command;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command ) {
        this.command = command;
    }

    public Long getId() {
        return Id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(Id, that.Id) && Objects.equals(chatId, that.chatId) && Objects.equals(event, that.event) && Objects.equals(time, that.time) && Objects.equals(command, that.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, chatId, event, time, command);
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "Id=" + Id +
                ", chatId=" + chatId +
                ", event='" + event + '\'' +
                ", time=" + time +
                ", doing='" + command + '\'' +
                '}';
    }
}