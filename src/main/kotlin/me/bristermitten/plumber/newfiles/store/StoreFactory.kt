package me.bristermitten.plumber.newfiles.store

import com.google.inject.Inject
import com.google.inject.Singleton
import me.bristermitten.plumber.PlumberPlugin
import me.bristermitten.plumber.newfiles.store.id.IDResolver
import me.bristermitten.plumber.newfiles.store.id.IDResolvers
import me.bristermitten.plumber.newfiles.store.id.IDStrategy
import me.bristermitten.plumber.util.getAnnotation
import me.bristermitten.plumber.util.isAssignableFrom
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy
import java.lang.reflect.Type

@Singleton
class StoreFactory @Inject constructor(
    private val plumberPlugin: PlumberPlugin,
    private val idResolvers: IDResolvers
) {

    fun <K: Any, V: Any> createStoreImplementation(clazz: Class<out Store<K, V>>): Store<K, V> {
        val storeStrategy = clazz.getAnnotation<StoreStrategy>()?.strategy ?: IDStrategy.INCREMENT

        //turning Class<out Store<K, V>> into [K, V]
        val storeTypeParameters = getStoreTypeParameters(clazz)
        val idType = storeTypeParameters[0]
        val valueType = storeTypeParameters[1]

        require(idType is Class<*>) { "$idType is not a Class" }
        require(valueType is Class<*>) { "$valueType is not a Class" }


        @Suppress("UNCHECKED_CAST")
        val idClass = idType as Class<K>

        val resolver = idResolvers.getResolver(storeStrategy, valueType, idClass)

        return createProxy(resolver)
    }

    private fun <K, V> createProxy(idResolver: IDResolver<K>): Store<K, V> {
        @Suppress("UNCHECKED_CAST")
        return Proxy.newProxyInstance(
            plumberPlugin.javaClass.classLoader,
            arrayOf(Store::class.java),
            StoreProxyHandler<K, V>(StoreDelegate(idResolver))
        ) as Store<K, V>
    }

    private fun getStoreTypeParameters(type: Class<out Store<*, *>>): List<Type> {
        return type.genericInterfaces
            .filterIsInstance<ParameterizedType>()
            .first {
                //eg KeyValueStore or ValueStore
                Store::class.isAssignableFrom(it.rawType)
            }.actualTypeArguments.toList() //eg ValueStore<Data> => [Data]
    }

}
