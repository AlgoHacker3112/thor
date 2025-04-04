package com.ankit;

import com.ankit.health.TemplateHealthCheck;
import com.ankit.module.CoreModule;
import in.vectorpro.dropwizard.swagger.SwaggerBundle;
import in.vectorpro.dropwizard.swagger.SwaggerBundleConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.val;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class App extends Application<AppConfiguration> {

    public static void main(final String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public String getName() {
        return "Thor";
    }

    @Override
    public void initialize(final Bootstrap<AppConfiguration> bootstrap) {
        val guiceBundle = GuiceBundle.builder()
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
        
        // even if guice is used, we need to register health check and can manually register to environment
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
                    

    }

}
