package com.ankit.module;

import com.ankit.health.TemplateHealthCheck;
import com.ankit.managed.RmqManager;
import com.ankit.resources.PublishResource;
import com.ankit.rmq.ActionType;
import com.ankit.rmq.MessageHandler;
import com.ankit.rmq.actors.ActionMessageDispatcherActor;
import com.ankit.rmq.handlers.TnxMessageHandler;
import com.ankit.rmq.message.BaseRmqMessage;
import com.ankit.core.PublishService;
import com.ankit.core.impl.PublishServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;

import akka.actor.ActorSystem;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.Map;

import com.ankit.AppConfiguration;
import com.ankit.config.ConfigManager;
import com.ankit.config.impl.ConfigManagerImpl;
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
