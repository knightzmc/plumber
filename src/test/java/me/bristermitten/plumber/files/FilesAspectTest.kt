package me.bristermitten.plumber.files

import me.bristermitten.plumber.PlumberTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class FilesAspectTest : PlumberTest()
{

    @Test
    fun `Test Correct Saving and Reloading`() {
        val type = TestStore::class.java
        val store = plugin.getInstance(type)

        val element = Data(id = 3, name = "Test")
        store.add(element)
        store.flush()

        store.clear()

        assertEquals(0, store.size)
        store.reload()

        assertEquals(1, store.size)
        assertEquals(element, store[0])
        assertEquals(listOf(element), store)
        assertFalse(element === store[0])
    }

    @MappedTo("test.yml")
    interface TestStore : ValueStore<Data>

    @MappedTo("config-test.yml")
    class TestConfig  {
        @ConfigKey("name")
        var name: String = ""
    }
}
