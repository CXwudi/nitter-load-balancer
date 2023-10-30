package mikufan.cx.nitterlb.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient

/**
 * To be used in URL for load balancing
 */
const val NITTER_INSTANCES = "nitter-instances"

@Configuration
@LoadBalancerClient(name = NITTER_INSTANCES, configuration = [LoadBalancingConfig::class])
class WebClientConfig {

  @Bean
  fun fetchingAndHealthCheckWebClient(
    @Value("\${user-agent:Nitter Load Balancer}")
    userAgent: String
  ) = WebClient.builder()
    .defaultHeader(HttpHeaders.USER_AGENT, userAgent)
    .build()


  @Bean
  @LoadBalanced
  fun loadBalancingWebClient(
    @Value("\${user-agent:Nitter Load Balancer}")
    userAgent: String
  ) = WebClient.builder()
    .defaultHeader(HttpHeaders.USER_AGENT, userAgent)
}