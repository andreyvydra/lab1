package itmo.is.lab1.aspects;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.hibernate.stat.CacheRegionStatistics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class CacheStatsAspect {

    @Value("${app.cache.stats.enabled:false}")
    private boolean statsEnabled;

    private final EntityManagerFactory emf;

    @AfterReturning("execution(* itmo.is.lab1.services..*(..))")
    public void logCacheStats() {
        log.debug("Cache stats enabled: {}", statsEnabled);
        if (!statsEnabled) {
            log.info("Cache stats disabled or cacheManager not available");
            return;
        }
        SessionFactory sessionFactory = emf.unwrap(SessionFactory.class);
        Statistics statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);

        String[] regions = statistics.getSecondLevelCacheRegionNames();
        List<String> lines = new ArrayList<>();
        for (String region : regions) {
            CacheRegionStatistics cs = statistics.getCacheRegionStatistics(region);
            if (cs == null) {
                continue;
            }
            lines.add(String.format(
                    "\n%s [hits=%d, misses=%d, puts=%d, entries=%d]",
                    region,
                    cs.getHitCount(),
                    cs.getMissCount(),
                    cs.getPutCount(),
                    cs.getElementCountInMemory()
            ));
        }
        if (!lines.isEmpty()) {
            log.info("L2 cache stats: {}", lines.stream().collect(Collectors.joining("; ")));
        } else {
            log.info("L2 cache stats: no caches to report, names={}", Arrays.toString(regions));
        }
    }
}
