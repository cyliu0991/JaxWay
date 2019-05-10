package com.gateway.jaxway.core.route;

import com.gateway.common.beans.JaxRouteDefinition;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import reactor.core.publisher.Mono;

/**
 * @Author huaili
 * @Date 2019/5/9 16:21
 * @Description JaxRouteRefreshListener
 **/
public class JaxRouteRefreshListener implements ApplicationListener<JaxRouteRefreshEvent>, ApplicationEventPublisherAware {

    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    public JaxRouteRefreshListener(RouteDefinitionWriter routeDefinitionWriter){
        this.routeDefinitionWriter = routeDefinitionWriter;
    }


    @Override
    public void onApplicationEvent(JaxRouteRefreshEvent jaxRouteRefreshEvent) {
        JaxRouteDefinition jaxRouteDefinition = jaxRouteRefreshEvent.getJaxRouteDefinition();
        switch (jaxRouteDefinition.getOpType()){
            case ADD_ROUTE:
                routeDefinitionWriter.save(Mono.just(jaxRouteDefinition)).subscribe();
                notifyChanged();
                break;
            case DELETE_ROUTE:
                routeDefinitionWriter.delete(Mono.just(jaxRouteDefinition.getId()));
                notifyChanged();
                break;
            case UPDATE_ROUTE:
                routeDefinitionWriter.delete(Mono.just(jaxRouteDefinition.getId()));
                routeDefinitionWriter.save(Mono.just(jaxRouteDefinition)).subscribe();
                notifyChanged();
                break;
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }


    private void notifyChanged() {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }
}