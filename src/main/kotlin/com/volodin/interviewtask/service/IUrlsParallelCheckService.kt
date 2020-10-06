package com.volodin.interviewtask.service

import reactor.core.publisher.Flux
import reactor.core.publisher.ParallelFlux

interface IUrlsParallelCheckService {
    fun check(urls: Flux<String>): ParallelFlux<Pair<String, Boolean>>
}
