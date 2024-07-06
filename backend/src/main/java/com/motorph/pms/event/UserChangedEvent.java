package com.motorph.pms.event;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Getter
public class UserChangedEvent extends ApplicationEvent {

    private final long employeeId;

    public UserChangedEvent(Object source, long employeeId) {
        super(source);
        this.employeeId = employeeId;
    }

    public UserChangedEvent(Object source) {
        super(source);
        this.employeeId = 0;
    }

    @Component
    public static class UserEventListener {
        private final CacheManager cacheManager;

        @Autowired
        public UserEventListener(CacheManager cacheManager) {
            this.cacheManager = cacheManager;
        }

//        @EventListener
//        public void handleUserChangedEvent(UserChangedEvent event) {
//            cacheManager.getCache("users").clear();
//            if (event.getEmployeeId() > 0) {
//                cacheManager.getCache("user").evict(event.getEmployeeId());
//            }
//            cacheManager.getCache("userRoles").clear();
//        }
    }
}
