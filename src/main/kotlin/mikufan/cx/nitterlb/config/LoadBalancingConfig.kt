package mikufan.cx.nitterlb.config

import org.springframework.beans.factory.getBean
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration(proxyBeanMethods = false)
class LoadBalancingConfig {

  @Bean
  fun nitterServiceInstanceListSupplier(
    context: ConfigurableApplicationContext
  ): ServiceInstanceListSupplier {
    return ServiceInstanceListSupplier.builder()
      .withBlockingDiscoveryClient() // already included a specialized caching
      // can figure out the double-checking problem
//      .withBlockingHealthChecks(context.getBean<RestTemplate>("restTemplateForHealthCheckAndFetching"))
      .withWeighted { it.metadata["score"]?.toInt() ?: 0 }
      .withCaching() // another level of caching
      .withRetryAwareness()
      .build(context)
  }
}