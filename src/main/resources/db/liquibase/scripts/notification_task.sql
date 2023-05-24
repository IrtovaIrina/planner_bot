--liquibase formatted sql

--changeset irina: creating_notification_task

CREATE table notification_task
(
    id        BIGINT PRIMARY KEY,
    chatId    BIGINT,
    event     TEXT,
    time      TIMESTAMP,
    eventTime TIMESTAMP,
    eventDate DATE;
)