package com.github.schlegel;


import com.segment.analytics.Analytics;
import com.segment.analytics.Callback;
import com.segment.analytics.Log;
import com.segment.analytics.messages.IdentifyMessage;
import com.segment.analytics.messages.Message;
import com.segment.analytics.messages.TrackMessage;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class AnalyticsService {

    private Analytics analytics;


    static final Log STDOUT = new Log() {
        @Override public void print(Level level, String format, Object... args) {
            System.out.println(new Date().toString() + "\t" + level + ":\t" + String.format(format, args));
        }

        @Override public void print(Level level, Throwable error, String format, Object... args) {
            System.out.println(new Date().toString() + "\t" +  level + ":\t" + String.format(format, args));
            System.out.println(error);
        }
    };

    static final Callback CALLBACK = new Callback() {
        @Override public void success(Message message) {
            System.out.println("Uploaded " + message);
        }

        @Override public void failure(Message message, Throwable throwable) {
            System.out.println("Could not upload " + message);
            System.out.println(throwable);
        }
    };

    public AnalyticsService() {
        analytics = Analytics.builder("xL7gIljlx2oNXU58Y8x5v98fkgZHdjH1")
                .log(STDOUT)
                .callback(CALLBACK)
                .flushInterval(5, TimeUnit.SECONDS)
                .flushQueueSize(10)
                .build();
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        System.out.println("Destroy LoggingEventInterceptor");
        // TODO async delivery

        analytics.flush();
        // wait for flush message to trigger flushing
        Thread.sleep(1000);
        analytics.shutdown();
    }

    public void sendEvent(String event, String clientid, Map<String, String> properties) {
        analytics.enqueue(TrackMessage.builder(event)
                .userId(clientid)
                .properties(properties)
        );
    }

    public void sendIdentification(String clientid, Map<String, String> properties) {
        analytics.enqueue(IdentifyMessage.builder()
                .userId(clientid)
                .traits(properties)
        );
    }
}
