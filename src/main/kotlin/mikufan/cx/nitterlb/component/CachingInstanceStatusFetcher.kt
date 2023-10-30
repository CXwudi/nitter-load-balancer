package mikufan.cx.nitterlb.component

import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.nitterlb.model.InstancesStatus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.net.URI
import java.time.ZonedDateTime

@Component
class CachingInstanceStatusFetcher(
  @Value("\${nitter-status-api}")
  private val statusApi: URI,
  @Qualifier("fetchingAndHealthCheckWebClient")
  private val webClient: WebClient,
//  @Qualifier("restTemplateForHealthCheckAndFetching")
//  private val restTemplate: RestTemplate,
) {

  private val lock = Any()
  private var cachedResult: InstancesStatus? = null

  fun getInstancesStatus(): Mono<InstancesStatus> {
    return Mono.justOrEmpty(cachedResult).filter { it.lastUpdate.isAfter(ZonedDateTime.now().minusMinutes(15)) }
      .switchIfEmpty(realGet())
      .doOnNext { synchronized(lock) { cachedResult = it } }
  }

//  fun getInstancesStatus(): InstancesStatus {
//    log.debug { "Fetching new result from $statusApi" }
//    return realGet()
//  }

  internal fun realGet(): Mono<InstancesStatus> {
//    return restTemplate.getForObject<InstancesStatus>(statusApi)
    return webClient.get().uri(statusApi)
      .retrieve().bodyToMono(InstancesStatus::class.java)
  }

//  fun deepCheckAvailability(instance: InstanceHost): Boolean {
//    return try {
//      val uri = UriComponentsBuilder.fromUri(instance.url.toURI()).path("/elonmusk/rss").build().toUri()
//      val entity = restTemplate.getForEntity(uri, String::class.java)
//      if (entity.statusCode.is2xxSuccessful) {
//        log.debug { "Instance ${instance.url} is available" }
//        true
//      } else {
//        log.debug { "Instance ${instance.url} is not available" }
//        false
//      }
//    } catch (e: Exception) {
//      log.debug(e) { "Instance ${instance.url} is not available" }
//      false
//    }
//  }
}

private val log = KInlineLogging.logger()