package mikufan.cx.nitterlb.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpHeaders
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestTemplate

/**
 * To be used in URL for load balancing
 */
const val NITTER_INSTANCES = "nitter-instances"

@Configuration
//@LoadBalancerClient(name = NITTER_INSTANCES, configuration = [LoadBalancingConfig::class])
class RestTemplateConfig {

  @Bean
  fun addUserAgentCustomizer(
    @Value("\${user-agent:Nitter Load Balancer}")
    userAgent: String
  ): RestTemplateCustomizer {
    return RestTemplateCustomizer { restTemplate ->
      restTemplate.interceptors.add { request, body, execution ->
        request.headers.add(HttpHeaders.USER_AGENT, userAgent)
        execution.execute(request, body)
      }
    }
  }

  @Bean
  fun useUtf8MessageConverter(): RestTemplateCustomizer {
    return RestTemplateCustomizer { restTemplate ->
      restTemplate.messageConverters.removeIf { it is StringHttpMessageConverter }
      restTemplate.messageConverters.add(0, StringHttpMessageConverter(Charsets.UTF_8))
    }
  }

//  @Bean
//  fun restTemplateForHealthCheckAndFetching(
//    customizers: List<RestTemplateCustomizer>
//  ): RestTemplate {
//    return RestTemplateBuilder()
//      .additionalCustomizers(customizers)
//      .build()
//  }

//  @Bean
//  @LoadBalanced
//  fun restTemplateForLoadBalancing(
//    restTemplateBuilder: RestTemplateBuilder
//  ): RestTemplate {
//    return restTemplateBuilder.build()
//  }
}