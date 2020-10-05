package com.volodin.interviewtask.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UrlsAvailabilityCheckService: IUrlsCheckService {
    override fun check(urls: Flux<String>): Flux<Pair<String, Boolean>> {
        return urls.flatMap { url -> checkUrlAvailability(url).flatMap { Mono.just(Pair(url, it)) } }
    }

    private fun checkUrlAvailability(url: String): Mono<Boolean> {
        return WebClient.create(url)
                .get()
                .exchange()
                .flatMap {
                    val isAvailable = Mono.just(it.statusCode().is2xxSuccessful || it.statusCode().is3xxRedirection)
                    it.bodyToMono(Void::class.java)
                    return@flatMap isAvailable
                }.onErrorReturn(false)
    }
}
