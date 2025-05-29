package com.thor.health;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Inject;
public class TemplateHealthCheck extends HealthCheck {
    private final String template;

    @Inject
    public TemplateHealthCheck(String template) {
        this.template = template;
    }

    @Override
    protected Result check() throws Exception {
        final String saying = String.format(template, "TEST"); 
        if (!saying.contains("TEST")) {
            return Result.unhealthy("template doesn't include a name");
        }
        // In a real app, you might check database connectivity, external services, etc.
        return Result.healthy();
    }
}