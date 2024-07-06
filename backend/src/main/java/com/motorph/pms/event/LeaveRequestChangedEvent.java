package com.motorph.pms.event;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Getter
public class LeaveRequestChangedEvent extends ApplicationEvent {
    private final long employeeId;

    public LeaveRequestChangedEvent(Object source, long employeeId) {
        super(source);
        this.employeeId = employeeId;
    }

    @Component
    public static class LeaveRequestEventListener {
        private final CacheManager cacheManager;
        @Autowired
        public LeaveRequestEventListener(CacheManager cacheManager) {
            this.cacheManager = cacheManager;
        }

        @EventListener
        public void handleLeaveRequestChangedEvent(LeaveRequestChangedEvent event) {
            cacheManager.getCache("leaveRequests").evict(event.getEmployeeId());
            cacheManager.getCache("leaveRequests").clear();
        }
    }
}
