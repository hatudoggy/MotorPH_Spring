package com.motorph.pms.event;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Getter
public class PayrollChangedEvent extends ApplicationEvent {

    private final long employeeId;
    private final LocalDate periodStart;
    private final LocalDate periodEnd;

    public PayrollChangedEvent(Object source) {
        super(source);
        this.employeeId = -1;
        this.periodStart = null;
        this.periodEnd = null;
    }

    public PayrollChangedEvent(Object source, long employeeId, LocalDate periodStart, LocalDate periodEnd) {
        super(source);
        this.employeeId = employeeId;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    @Component
    public static class PayrollChangedEventListener {

//        private final RestTemplate restTemplate;
//
//        @Autowired
//        public PayrollChangedEventListener(RestTemplate restTemplate) {
//            this.restTemplate = restTemplate;
//        }
//
//        // Send notification
//        sendNotification(event);
//
//        // Log the event
//        logPayrollChangeEvent(event);
//
//        // Synchronize data with external systems
//        synchronizeWithExternalSystems(event);
    }
}
