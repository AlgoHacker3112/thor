package com.thor.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.thor.AppConfiguration;
import com.thor.config.ConfigManager;
import com.thor.config.impl.ConfigManagerImpl;
import com.thor.core.PublishService;
import com.thor.core.impl.PublishServiceImpl;
import com.thor.health.TemplateHealthCheck;
import com.thor.managed.RmqManager;
import com.thor.resources.PublishResource;
import com.thor.rmq.ActionType;
import com.thor.rmq.MessageHandler;
import com.thor.rmq.actors.ActionMessageDispatcherActor;
import com.thor.rmq.handlers.TnxMessageHandler;
import com.thor.rmq.message.BaseRmqMessage;

import akka.actor.ActorSystem;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.Map;

import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CoreModule extends AbstractModule{

    @Inject
    @Override
    protected void configure() {

        bind(PublishResource.class).in(Scopes.SINGLETON);
        bind(TemplateHealthCheck.class).in(Scopes.SINGLETON);
        bind(PublishService.class).to(PublishServiceImpl.class).in(Scopes.SINGLETON);
        bind(ConfigManager.class).to(ConfigManagerImpl.class).in(Scopes.SINGLETON);

        MapBinder<ActionType, MessageHandler<? extends BaseRmqMessage>> binder =
            MapBinder.newMapBinder(
                binder(),
                new TypeLiteral<ActionType>() {},
                new TypeLiteral<MessageHandler<? extends BaseRmqMessage>>() {}
            );

        binder.addBinding(ActionType.TXN).to(TnxMessageHandler.class);
    
    }

    @Singleton
    @Provides
    public RmqManager provideRmqManager(AppConfiguration appConfiguration) {
        return new RmqManager(appConfiguration.getRmqConfiguration());
    }
    
    @Provides
    @Singleton
    public ActorSystem provideActorSystem() {
        return ActorSystem.create("rmq-actor-system");
    }

    @Provides
    @Singleton
    public ActorRef provideActionMessageDispatcherActor(
        ActorSystem actorSystem,
        Map<ActionType, MessageHandler<? extends BaseRmqMessage>> handlers
    ) {
        return actorSystem.actorOf(Props.create(ActionMessageDispatcherActor.class, handlers), "actionMessageDispatcherActor");
    }

}
