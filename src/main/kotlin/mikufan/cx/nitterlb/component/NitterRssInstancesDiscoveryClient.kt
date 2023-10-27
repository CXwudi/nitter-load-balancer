package mikufan.cx.nitterlb.component

import mikufan.cx.nitterlb.model.InstanceHost
import mikufan.cx.nitterlb.model.InstancesStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.DefaultServiceInstance
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.stereotype.Component

@Component
class NitterRssInstancesDiscoveryClient(
  private val statusFetcher: CachingInstanceStatusFetcher,
) : DiscoveryClient {

  override fun description() = "Nitter RSS Instances Discovery Client"

  override fun getServices(): MutableList<String> = mutableListOf("default")

  override fun getInstances(serviceId: String?): MutableList<ServiceInstance> {
    val instancesStatus: InstancesStatus = statusFetcher.getInstancesStatus()
    return instancesStatus.instanceHosts
      .filter { isAvailableWithRss(it) }
      .map { toServiceInstance(it) }
      .toMutableList()
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
