package mikufan.cx.nitterlb.config

import mikufan.cx.nitterlb.model.InstanceHost
import mikufan.cx.nitterlb.model.InstancesStatus
import mikufan.cx.nitterlb.model.RecentCheck
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.context.annotation.Configuration

@RegisterReflectionForBinding(
  InstancesStatus::class,
  InstanceHost::class,
  RecentCheck::class
)
@Configuration(proxyBeanMethods = false)
class NativeBuildConfig