package cn.llonvne.lojbackend.json

import cn.llonvne.lojbackend.dto.LoginUserDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JsonUtilsKtTest {
    /**
     * 测试对象转 Json 使用 .jsonValue 拓展属性
     * ```kotlin
     * LoginUserDto("123", "123")
     * ```
     *
     * 应该被转换为
     * ```json
     * {"username":"123","password":"123"}
     * ```
     *
     * 这只是一个简单的测试用于测试 json 模块是否工作，并非完整的测试
     */
    @Test
    fun `test object to Json`() {
        assertEquals("""{"username":"123","password":"123"}""",
            json {
                LoginUserDto("123", "123").jsonValue
            }
        )
    }
}