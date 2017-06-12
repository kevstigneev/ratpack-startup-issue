import ratpack.service.Service

import static ratpack.groovy.Groovy.ratpack

ratpack {
    serverConfig {
        development(false)
    }
    bindings {
        add Service.startup("startup") {
            throw new IllegalStateException()
        }
    }
}