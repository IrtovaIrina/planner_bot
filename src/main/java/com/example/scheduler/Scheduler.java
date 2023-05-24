package com.example.scheduler;

import com.example.telegram_bot.service.NotificationTaskService;
import com.pengrad.telegrambot.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class Scheduler {
    private NotificationTaskService service;
    private TelegramBot telegramBot;

    public Scheduler(NotificationTaskService service, TelegramBot telegramBot) {
        this.service = service;
        this.telegramBot = telegramBot;
    }
    @Scheduled(cron = "0 0/1 * * * *")
    public void sendEvent(){
        log.info("now: " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        service.findAllByTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)).forEach(x ->{
            service.sentMessage(x.getChatId(),"Reminder: " + x.getEvent());
            log.info("the event was sent to " + x.getChatId());
        });

    }
}

