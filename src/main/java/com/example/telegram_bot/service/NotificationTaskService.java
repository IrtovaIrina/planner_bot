package com.example.telegram_bot.service;

import com.example.telegram_bot.config.BotConfig;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.example.telegram_bot.models.NotificationTask;
import com.example.telegram_bot.repositories.NotificationTaskRepository;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
@Slf4j
@Service
public class NotificationTaskService {
    private final NotificationTaskRepository repository;
    @Autowired
    private TelegramBot telegramBot;

    public NotificationTaskService(NotificationTaskRepository repository)
    {
        this.repository = repository;
    }

    public NotificationTask save(NotificationTask notificationTask){
        if (notificationTask == null){
            throw new IllegalArgumentException();
        }
        return repository.save(notificationTask);
    }
    public Collection<NotificationTask> getCollectionNotificationTask(LocalDateTime localDateTime) {
        return repository.findAllByTime(localDateTime);
    }
    public Optional<NotificationTask> returnLastCommand(Long chatId){
        return repository.findAllByChatId(chatId).stream()
                .max(Comparator.comparing(NotificationTask::getId));
    }
    public Collection<NotificationTask> findAllByChatId(Long chatId){
        return repository.findAllByChatId(chatId);
    }
    public Collection<NotificationTask> findAll(){
        return repository.findAll();
    }
    public void sentMessage(Long chatId, String textToSent) {
        SendMessage message = new SendMessage(chatId, textToSent);
        try {
            SendResponse response = telegramBot.execute(message);
        } catch (RuntimeException e) {
            log.error("Error : occurred:" + e.getMessage());
        }
    }
}