package mikufan.cx.nitterlb.web

import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.nitterlb.config.NITTER_INSTANCES
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@RestController
class RssController(
  @LoadBalanced
  @Qualifier("loadBalancingWebClient")
  webClientBuilder: WebClient.Builder,
) {

  private val webClient = webClientBuilder.build()

  @GetMapping("/{id}/rss", produces = ["${MediaType.APPLICATION_RSS_XML_VALUE};charset=UTF-8"])
  fun getRss(@PathVariable id: String): Mono<String> {
    log.info { "Get for $id by load balancer" }
    return webClient.get().uri("https://{instance}/{id}/rss", NITTER_INSTANCES, id)
      .retrieve().bodyToMono(String::class.java)
  }
}
private val log = KInlineLogging.logger()