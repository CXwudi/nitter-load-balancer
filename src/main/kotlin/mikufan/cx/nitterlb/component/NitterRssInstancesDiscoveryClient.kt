package mikufan.cx.nitterlb.component

import mikufan.cx.nitterlb.model.InstanceHost
import mikufan.cx.nitterlb.model.InstancesStatus
import org.springframework.cloud.client.DefaultServiceInstance
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class NitterRssInstancesDiscoveryClient(
  private val statusFetcher: CachingInstanceStatusFetcher,
) : ReactiveDiscoveryClient {

  override fun description() = "Nitter RSS Instances Discovery Client"

  override fun getServices(): Flux<String>? = Flux.just("default")

  override fun getInstances(serviceId: String?): Flux<ServiceInstance> {
    val instancesStatusMono: Mono<InstancesStatus> = statusFetcher.getInstancesStatus()
//    return instancesStatus.instanceHosts
//      .filter { isAvailableWithRss(it) }
//      .map { toServiceInstance(it) }
//      .toMutableList()
//      .let { Flux.fromIterable(it) }
    return instancesStatusMono.flatMapMany { instancesStatus ->
      instancesStatus.instanceHosts
        .filter { isAvailableWithRss(it) }
        .map { toServiceInstance(it) }
        .let { Flux.fromIterable(it) }
    }
  }

  internal fun isAvailableWithRss(host: InstanceHost): Boolean {
    return host.rss && host.score > 0
  }

  internal fun toServiceInstance(host: InstanceHost): ServiceInstance =
    DefaultServiceInstance(
      host.url.toString(),
      "default",
      host.domain,
      443,
      true,
      mapOf(
        "rss" to host.rss.toString(),
        "score" to host.score.toString(),
        "last_healthy" to host.lastHealthy.toString(),
      )
    )
}
