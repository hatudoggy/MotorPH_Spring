package com.motorph.pms.event;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
public class AttendanceChangedEvent extends ApplicationEvent {
    private final long employeeId;
    private final LocalDate date;

    public AttendanceChangedEvent(Object source, long employeeId, LocalDate date) {
        super(source);
        this.employeeId = employeeId;
        this.date = date;
    }

    @Component
    public static class AttendanceEventListener {

        private final CacheManager cacheManager;

        @Autowired
        public AttendanceEventListener(CacheManager cacheManager) {
            this.cacheManager = cacheManager;
        }

        @EventListener
        public void handleAttendanceChangedEvent(AttendanceChangedEvent event) {
            cacheManager.getCache("attendances").clear();
            cacheManager.getCache("attendances").evict(event.getEmployeeId());
            cacheManager.getCache("attendances").evict(event.getDate());
            cacheManager.getCache("attendancesByDateRange").clear();

            String compositeKey = event.getEmployeeId() + "_" + event.getDate();
            cacheManager.getCache("attendances").evict(compositeKey);
        }
    }
}
