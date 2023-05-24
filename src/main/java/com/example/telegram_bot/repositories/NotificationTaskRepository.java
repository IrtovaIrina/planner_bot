package com.example.telegram_bot.repositories;

import com.example.telegram_bot.models.NotificationTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    Collection<NotificationTask> findAllByEventDate(LocalDate localDate);

    Collection<NotificationTask> findAllByChatId(Long chatId);
}