package mikufan.cx.nitterlb

import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NitterLoadBalancerApplication

fun main(args: Array<String>) {
	runApplication<NitterLoadBalancerApplication>(*args)
}
