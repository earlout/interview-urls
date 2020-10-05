package com.volodin.interviewtask.service

import reactor.core.publisher.Flux

interface IUrlsCheckService {
    fun check(urls: Flux<String>): Flux<Pair<String, Boolean>>
}