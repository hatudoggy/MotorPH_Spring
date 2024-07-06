package com.motorph.pms.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Getter
public class EmployeeChangedEvent extends ApplicationEvent {
    private final Long employeeId;
    private final boolean isActive;

    public EmployeeChangedEvent(Object source, Long employeeId, boolean isActive) {
        super(source);
        this.employeeId = employeeId;
        this.isActive = isActive;
    }

    @Component
    public static class EmployeeEventListener {

//        private final CacheManager cacheManager;
//
//        @Autowired
//        public EmployeeEventListener(CacheManager cacheManager) {
//            this.cacheManager = cacheManager;
//        }
//
//        @EventListener
//        public void handleEmployeeChangedEvent(EmployeeChangedEvent event) {
//            cacheManager.getCache("employees").clear();
//            cacheManager.getCache("employees").evict("#" + event.getEmployeeId());
//            cacheManager.getCache("employees").evict("#" + event.isActive());
//            cacheManager.getCache("supervisors").clear();
//        }
    }
}