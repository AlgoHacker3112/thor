package com.ankit.module;

import com.ankit.health.TemplateHealthCheck;
import com.ankit.resources.PublishResource;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class CoreModule extends AbstractModule{

    @Override
    protected void configure() {

        bind(PublishResource.class).in(Scopes.SINGLETON);
        bind(TemplateHealthCheck.class).in(Scopes.SINGLETON);

    }
    
}
