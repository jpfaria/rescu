package si.mazi.rescu.qume.interceptor;

import lombok.extern.slf4j.Slf4j;
import si.mazi.rescu.Interceptor;
import si.mazi.rescu.RestInvocationHandler;
import si.mazi.rescu.qume.component.EventPublisher;
import si.mazi.rescu.qume.event.RestResponseTimeEvent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URI;

@Slf4j
public class RestResponseTimeInterceptor implements Interceptor {

    private final EventPublisher eventPublisher;

    public RestResponseTimeInterceptor(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Object aroundInvoke(InvocationHandler h, Object proxy, Method m, Object[] args)
            throws Throwable {
        long start = System.nanoTime();

        Object result = h.invoke(proxy, m, args);

        long elapsed = System.nanoTime() - start;

        log.debug(">>>>>>>>>>>>>>>>>>>>>>>> {} took {} ms. {}", m.getName(), elapsed);

        RestInvocationHandler restHandler = (RestInvocationHandler) h;

        URI uri = URI.create(restHandler.getBaseUrl());

        RestResponseTimeEvent event = RestResponseTimeEvent.builder()
                .host(uri.getHost())
                .method(m.getName())
                .responseTime(elapsed)
                .scheme(uri.getScheme())
                .build();

        eventPublisher.publishEvent(event);

        return result;
    }
}