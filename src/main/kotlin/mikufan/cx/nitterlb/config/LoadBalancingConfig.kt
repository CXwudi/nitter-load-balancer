package mikufan.cx.nitterlb.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.getBean
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration(proxyBeanMethods = false)
class LoadBalancingConfig {

  @Bean
  fun nitterServiceInstanceListSupplier(
    context: ConfigurableApplicationContext,
    @Value("\${enable-health-check:false}")
    enableHealthCheck: Boolean,
  ): ServiceInstanceListSupplier {
    return ServiceInstanceListSupplier.builder()
      .withDiscoveryClient() // already included a specialized caching
      .run {
        // can't figure out the double-checking problem, hence providing the option to disable health check
        if (enableHealthCheck){
          withHealthChecks(context.getBean<WebClient>("fetchingAndHealthCheckWebClient"))
        } else {
          this
        }
      }
      .withWeighted { it.metadata["score"]?.toInt() ?: 0 }
      .withCaching() // another level of caching
      .withRetryAwareness()
      .build(context)
  }
}