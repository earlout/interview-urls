package com.volodin.interviewtask.service

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.ParallelFlux
import reactor.core.scheduler.Schedulers

@Service
class UrlsAvailabilityParallelCheckService: IUrlsParallelCheckService {
    override fun check(urls: Flux<String>): ParallelFlux<Pair<String, Boolean>> {
        return urls.parallel(3)
                .runOn(Schedulers.parallel())
                .flatMap { url -> checkUrlAvailability(url) }
    }

    private fun checkUrlAvailability(url: String): Mono<Pair<String, Boolean>> {
        return WebClient.create(url)
                .get()
                .exchange()
                .flatMap {
                    val isAvailable = Mono.just(it.statusCode().is2xxSuccessful || it.statusCode().is3xxRedirection)
                    it.bodyToMono(Void::class.java)
                    return@flatMap isAvailable
                }.onErrorReturn(false)
                .flatMap { Mono.just(Pair(url, it)) }
    }
}
