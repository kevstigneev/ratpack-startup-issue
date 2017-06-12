@Grapes([
        @Grab('io.ratpack:ratpack-groovy:1.4.5'),
        @Grab('org.slf4j:slf4j-simple:1.7.21')
])

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
