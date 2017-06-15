package org.bitbucket.kevstigneev.ratpackservice;

import ratpack.server.RatpackServer;
import ratpack.service.Service;
import ratpack.service.StartEvent;

public class Server {
    public static void main(String[] argv) throws Exception {
        Service failingStartup = new Service() {
            public void onStart(StartEvent event) throws Exception {
                throw new IllegalStateException("Intentional failure");
            }
        };
        RatpackServer.start(s -> s
                .registryOf(r -> r.add(failingStartup)));
    }
}
