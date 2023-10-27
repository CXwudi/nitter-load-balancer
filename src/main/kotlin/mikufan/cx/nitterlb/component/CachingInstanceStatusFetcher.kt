package mikufan.cx.nitterlb.component

import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.nitterlb.model.InstanceHost
import mikufan.cx.nitterlb.model.InstancesStatus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.time.ZonedDateTime

@Component
class CachingInstanceStatusFetcher(
  @Value("\${nitter-status-api}")
  private val statusApi: URI,
  @Qualifier("restTemplateForHealthCheckAndFetching")
  private val restTemplate: RestTemplate,
) {

  private val lock = Any()
  private var cachedResult: InstancesStatus? = null

  fun getInstancesStatus(): InstancesStatus {
    synchronized(lock) {
      if (cachedResult != null && cachedResult!!.lastUpdate.isAfter(ZonedDateTime.now().minusMinutes(15))) {
        log.debug { "Returning cached result" }
      } else {
        log.debug { "Fetching new result from $statusApi" }
        cachedResult = realGet()
      }
    }
    return cachedResult!!
  }

//  fun getInstancesStatus(): InstancesStatus {
//    log.debug { "Fetching new result from $statusApi" }
//    return realGet()
//  }

  internal fun realGet(): InstancesStatus {
    return restTemplate.getForObject<InstancesStatus>(statusApi)
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