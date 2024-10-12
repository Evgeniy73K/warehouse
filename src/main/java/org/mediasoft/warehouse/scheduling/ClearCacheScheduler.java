package org.mediasoft.warehouse.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClearCacheScheduler {
    private final CacheManager cacheManager;

    @Scheduled(fixedRateString = "${scheduler.clearCacheTiming}")
    public void evictCurrencyCache() {
        log.info("clear  currency cache!!!!!!!!!");
        Objects.requireNonNull(cacheManager.getCache("currency")).clear();
    }
}
