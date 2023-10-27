package mikufan.cx.nitterlb.util

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.annotation.AliasFor
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest
@ActiveProfiles("test", "dev")
annotation class SpringBootTestWithCustomProfile(
  @get:AliasFor(annotation = ActiveProfiles::class, attribute = "profiles")
  val profiles: Array<String> = ["test", "dev"],
  @get:AliasFor(annotation = SpringBootTest::class, attribute = "properties")
  val properties: Array<String> = [
    "user-agent=Nitter Load Balancer In Test",
  ]
)