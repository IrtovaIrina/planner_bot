package com.example.telegram_bot.scheduler;

import com.example.telegram_bot.models.NotificationTask;
import com.example.telegram_bot.service.NotificationTaskService;
import com.pengrad.telegrambot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Service
@Slf4j
public class Scheduler {
    private NotificationTaskService service;
    private TelegramBot telegramBot;

    public Scheduler(NotificationTaskService service, TelegramBot telegramBot) {
        this.service = service;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendEvent() {
        log.info("now: " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        service.getCollectionsByDateEvent(LocalDate.now()).stream().filter(x -> x.getEventTime() != null)
                .filter(x ->
                        x.getEventTime().truncatedTo(ChronoUnit.MINUTES)
                                .equals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)))
                .forEach(x -> {
                    service.sentMessage(x.getChatId(), "Reminder: " + x.getEvent());
                    log.info("the event was sent to " + x.getChatId());
                });
    }

}
