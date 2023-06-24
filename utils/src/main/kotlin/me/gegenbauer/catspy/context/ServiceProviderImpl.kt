package me.gegenbauer.catspy.context

import java.util.concurrent.ConcurrentHashMap

/**
 * ServiceProvider 本身也是一种 ContextService，不同 Context 下的 ServiceProvider 是不同的
 * 可以通过 Context id 获取对应的 ServiceProvider，所以这里的 scope 没有实际意义
 */
class ServiceProviderImpl : ServiceProvider, ContextService {
    private val services = ConcurrentHashMap<Class<out ContextService>, InstanceFetcher<ContextService>>()

    override fun <T : ContextService> get(serviceClazz: Class<out T>): T {
        return services.getOrPut(serviceClazz) { serviceClazz.defaultFetcher }.get() as T
    }

    override fun <T : ContextService> register(
        serviceClazz: Class<out T>,
        serviceSupplier: InstanceFetcher<out ContextService>
    ) {
        services[serviceClazz] = serviceSupplier as InstanceFetcher<ContextService>
    }

    override fun onContextDestroyed(context: Context) {
        services.values.forEach { it.get().onContextDestroyed(context) }
        services.clear()
    }

}