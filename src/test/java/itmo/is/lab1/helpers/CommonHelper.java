package itmo.is.lab1.helpers;

import org.apache.http.entity.ContentType;
import us.abstracta.jmeter.javadsl.core.DslTestPlan;
import us.abstracta.jmeter.javadsl.http.DslCacheManager;
import us.abstracta.jmeter.javadsl.http.DslHttpDefaults;
import us.abstracta.jmeter.javadsl.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import static us.abstracta.jmeter.javadsl.dashboard.DashboardVisualizer.dashboardVisualizer;

public class CommonHelper {
    public static DslHttpDefaults getHttpDefaults() {
        return httpDefaults()
                .protocol("http")
                .host("localhost")
                .port(8081)
                .encoding(StandardCharsets.UTF_8)
                .connectionTimeout(Duration.ofSeconds(5))
                .responseTimeout(Duration.ofSeconds(30));
    }

    public static DslCacheManager getCacheDisable() {
        return httpCache()
                .disable();
    }

    public static HttpHeaders getHeadersDefaults() {
        return httpHeaders()
                .contentType(ContentType.APPLICATION_JSON);

    }

    public static DslTestPlan.TestPlanChild resultTree(boolean enable) {
        if (enable) {
            return resultsTreeVisualizer();
        } else {
            return constantTimer(Duration.ZERO);
        }
    }

    public static DslTestPlan.TestPlanChild resultTree() {
        return resultsTreeVisualizer();
    }

    public static DslTestPlan.TestPlanChild resultDashboard(boolean enable) {
        if (enable) {
            return dashboardVisualizer();
        } else {
            return constantTimer(Duration.ZERO);
        }
    }

    public static DslTestPlan.TestPlanChild resultDashboard() {
        return dashboardVisualizer();
    }
}