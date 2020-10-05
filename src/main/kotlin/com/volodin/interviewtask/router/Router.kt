package com.volodin.interviewtask.router

import com.volodin.interviewtask.handler.MainHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*

@Configuration
class Router {
    @Bean
    fun mainRouter(handler: MainHandler) = router {
        "/api".nest {
            accept(MediaType.APPLICATION_JSON).nest {
                POST("/checkUrls", handler::checkUrlsAvailability)
            }
        }
    }
}
