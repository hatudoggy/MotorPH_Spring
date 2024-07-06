package com.motorph.pms.event;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

public class CompanyDetailsChangedEvent extends ApplicationEvent {

    public CompanyDetailsChangedEvent(Object source) {
        super(source);
    }

    @Component
    public static class CompanyDetailsChangedEventListener {
        private final CacheManager cacheManager;

        @Autowired
        public CompanyDetailsChangedEventListener(CacheManager cacheManager) {
            this.cacheManager = cacheManager;
        }

        @EventListener
        public void handleCompanyDetailsChangedEvent(CompanyDetailsChangedEvent event) {
//            cacheManager.getCache("positions").clear();
//            cacheManager.getCache("departments").clear();
//            cacheManager.getCache("employmentStatuses").clear();
//            cacheManager.getCache("leaveTypes").clear();
//            cacheManager.getCache("leaveStatuses").clear();
//            cacheManager.getCache("benefitTypes").clear();
//            cacheManager.getCache("supervisors").clear();
        }
    }
}
