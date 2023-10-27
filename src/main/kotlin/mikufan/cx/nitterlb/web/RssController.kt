package mikufan.cx.nitterlb.web

import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.nitterlb.config.NITTER_INSTANCES
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class RssController(
  @Qualifier("restTemplateForLoadBalancing")
  @LoadBalanced
  private val restTemplate: RestTemplate
) {

  @GetMapping("/{id}/rss", produces = ["${MediaType.APPLICATION_RSS_XML_VALUE};charset=UTF-8"])
  fun getRss(@PathVariable id: String): String {
    log.info { "Get for $id by load balancer" }
    return restTemplate.getForObject("https://$NITTER_INSTANCES/$id/rss", String::class.java)!!
  }
}

private val log = KInlineLogging.logger()