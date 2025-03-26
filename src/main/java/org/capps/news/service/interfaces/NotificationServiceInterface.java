package org.capps.news.service.interfaces;

import org.capps.news.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationServiceInterface {

    List<Notification> getAllNotifications();

    Optional<Notification> getNotificationById(Long id);

    Notification createNotification(Notification notification);

    Notification updateNotification(Long id, Notification notificationDetails);

    void deleteNotification(Long id);
}
