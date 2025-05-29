package com.thor.managed;

import akka.actor.ActorSystem;
import io.dropwizard.lifecycle.Managed;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.google.inject.Inject;

@Slf4j
@Data
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ManagedActorSystem implements Managed {

    private final ActorSystem actorSystem;

    @Override
    public void start() throws Exception {
        log.info("Starting ActorSystem");   
    }

    @Override
    public void stop() throws Exception {
        log.info("Stopping ActorSystem");
        actorSystem.terminate();
    }
}