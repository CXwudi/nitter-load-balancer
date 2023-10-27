package mikufan.cx.nitterlb

import mikufan.cx.nitterlb.util.SpringBootTestWithCustomProfile
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

@SpringBootTestWithCustomProfile
class NitterLoadBalancerApplicationTests {

  @Autowired
  private lateinit var ctx: ApplicationContext

  @Test
  fun `should all beans`() {
    ctx.beanDefinitionNames.forEach { println(it) }
  }

}
