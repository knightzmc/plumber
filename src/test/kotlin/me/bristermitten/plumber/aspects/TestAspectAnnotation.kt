package me.bristermitten.plumber.aspects

import me.bristermitten.plumber.PlumberExtension
import me.bristermitten.plumber.newaspect.Aspect
import me.bristermitten.plumber.newaspect.AspectAnnotation
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

class AnnotationEnabledAspect : Aspect {
    override fun enable() {
        enabled = true
    }

    override fun disable() {
    }

    companion object {
        var enabled = false
    }
}

@AspectAnnotation(AnnotationEnabledAspect::class)
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestAspectAnnotation


@TestAspectAnnotation
@ExtendWith(PlumberExtension::class)
class TestAspectAnnotationAspectTests {

    @Test
    fun `Test Annotated Aspect Enabled with AspectAnnotation present`() {
        assertTrue(AnnotationEnabledAspect.enabled)
    }
}