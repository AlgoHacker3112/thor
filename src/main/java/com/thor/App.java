package com.thor;

import com.google.inject.Injector;
import com.thor.health.TemplateHealthCheck;
import com.thor.managed.ManagedActorSystem;
import com.thor.managed.RmqManager;
import com.thor.module.CoreModule;
import com.thor.rmq.RmqConsumerManager;

import akka.actor.ActorSystem;
import in.vectorpro.dropwizard.swagger.SwaggerBundle;
import in.vectorpro.dropwizard.swagger.SwaggerBundleConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.val;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class App extends Application<AppConfiguration> {

    private Injector injector;

    private GuiceBundle guiceBundle;

    public static void main(final String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public String getName() {
        return "Thor";
    }

    @Override
    public void initialize(final Bootstrap<AppConfiguration> bootstrap) {
        guiceBundle = GuiceBundle.builder()
            .modules(new CoreModule())
            .enableAutoConfig(getClass().getPackage().getName())
            .build();

        bootstrap.addBundle(guiceBundle);
        bootstrap.addBundle(getSwaggerBundleConfiguration());

    }

    private SwaggerBundle<AppConfiguration> getSwaggerBundleConfiguration() {
        return new SwaggerBundle<AppConfiguration>() {
            @Override   
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(AppConfiguration configuration) {
                return configuration.getSwagger();
            }
        };
    }

    @Override
    public void run(final AppConfiguration configuration,
                    final Environment environment) {
        
        injector = guiceBundle.getInjector();
        
        final RmqManager rmqManager = injector.getInstance(RmqManager.class);
        final RmqConsumerManager rmqConsumerManager = injector.getInstance(RmqConsumerManager.class);
        final ActorSystem actorSystem = injector.getInstance(ActorSystem.class);
        // even if guice is used, we need to register health check and can manually register to environment
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);

        environment.lifecycle().manage(new ManagedActorSystem(actorSystem));
        environment.lifecycle().manage(rmqManager);
        environment.lifecycle().manage(rmqConsumerManager);

    }

}
