package com.volodin.interviewtask.handler

import com.volodin.interviewtask.service.IUrlsCheckService
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class MainHandler(private val urlsAvailabilityCheckService: IUrlsCheckService) {
    fun checkUrlsAvailability(req: ServerRequest): Mono<ServerResponse> {
        val urlsMono = req.bodyToMono<List<String>>() // some problem with deserialization of JSON Array<String>
        val result = urlsMono.flatMapMany { urlsAvailabilityCheckService.check(Flux.fromIterable(it)) }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(result, Pair::class.java)
    }
}
