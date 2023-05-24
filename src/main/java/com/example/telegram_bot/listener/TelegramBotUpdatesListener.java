package com.example.telegram_bot.listener;
import com.example.telegram_bot.config.BotConfig;
import com.example.telegram_bot.models.NotificationTask;
import com.example.telegram_bot.service.NotificationTaskService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetMyCommandsResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.pengrad.telegrambot.model.BotCommand;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.pengrad.telegrambot.model.Message;

import javax.annotation.PostConstruct;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Pattern PATTERN = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2})\\s(.+)");
    private NotificationTaskService service;
    @Autowired
    private TelegramBot telegramBot;
    @PostConstruct
    public void init(){
        telegramBot.setUpdatesListener(this);
    }

    public TelegramBotUpdatesListener(NotificationTaskService service) {
        this.service = service;

        BotCommand[] commandsArray = new BotCommand[]{
        new BotCommand("/add","add an event in the format : 01.01.2022 20:00 Сделать домашнюю работу")};
        SetMyCommands commands = new SetMyCommands(commandsArray);
        //try{
            //telegramBot.execute(commands,null);
        //}catch (RuntimeException e) {
            //log.error("Error : occurred:" + e.getMessage());
        //}
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            log.info("Processing updates " + update);
            if(update.message() != null) {
                processMessage(updates);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    public void processMessage(List<Update> updates) {
        for (Update update : updates) {
            String text = update.message().text();
            Long chatId = update.message().chat().id();
            LocalDateTime time = LocalDateTime.now();
            Matcher matcher = PATTERN.matcher(text);
            if (service.findAllByChatId(chatId).isEmpty()) {
                if (text.equals("/start")) {
                    TelegramBot bot = new TelegramBot(telegramBot.getToken());
                    NotificationTask nt = new NotificationTask();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    nt.setCommand(text);
                    nt.setTime(time);
                    nt.setChatId(chatId);
                    service.save(nt);
                    service.sentMessage(chatId, "Hi, now: " + time.format(formatter) + "."
                            + "\n" + "We are start!");
                    log.info("New bot was saved");
                }
            }
            else{
                if(matcher.matches()){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                    String date = matcher.group(0).substring(0,16);
                    String task = matcher.group(2);
                    NotificationTask nt = new NotificationTask();
                    nt.setCommand("/add");
                    nt.setEvent(task);
                    nt.setTime(time);
                    nt.setEventTime(LocalDateTime.parse(date,formatter));
                    nt.setChatId(chatId);
                    service.save(nt);
                    service.sentMessage(chatId,"Event was saved");
                    log.info("event from " + chatId + " was saved");
                }else{
                    service.sentMessage(chatId,"wrong message");
                    log.info("it was wrong message");
                }
            }
        }

    }
        //updates.forEach(update -> {
            //if(update.message() != null){
                //TelegramBot bot = new TelegramBot(telegramBot.getToken());
                //String messageText = update.message().text();
                //Long chatId = update.message().chat().id();
               // LocalDateTime time = LocalDateTime.now();
               // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                //Optional<NotificationTask> last = service.returnLastCommand(chatId);
                //Matcher matcher = PATTERN.matcher(messageText);
                //if (messageText.equals("/start")) {
                    //registerUser(last, update.message());
                    //startCommandReceived(chatId, time, formatter);
                //}else if(messageText.equals("/add")){

                //} else if (matcher.matches()) {
                    //putEvent(last.get(), chatId, messageText);
                //}
            //}
        //});
    //}
    //private NotificationTask registerUser(Optional<NotificationTask> last, Message message){
        ////
        //Long chatId = message.chat().id();
        //if (!last.isPresent() | last.get().getEvent().isBlank()){
            //NotificationTask nt = new NotificationTask();
            //nt.setChatId(chatId);
            //nt.setTime(LocalDateTime.now());
            //nt.setCommand(message.text());
            //service.save(nt);
            //log.info("new command was saved from : " + chatId);
            //return nt;
        //}else{
            //last.get().setCommand(message.text());
            //service.save(last.get());
            //log.info("new NoticedTask was saved from" + chatId);
            //return last.get();
        //}

    //}
    //private void startCommandReceived(Long chatId, LocalDateTime time, DateTimeFormatter formatter){
        //String answer = "Сегодня: " + time.format(formatter) + "\n" +  "Добрый день!";
        //log.info("Replied to user" + chatId);
        //sentMessage(chatId,answer);
    //}

    //}
    //public void putEvent(NotificationTask last,Long chatId, String messageText){
}
