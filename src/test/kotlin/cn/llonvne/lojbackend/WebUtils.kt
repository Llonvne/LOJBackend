package cn.llonvne.lojbackend

import cn.llonvne.lojbackend.json.json
import cn.llonvne.lojbackend.json.jsonValue
import cn.llonvne.lojbackend.json.type
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*


/**
 * 此处配置了基于 Ktor 的 HttpClient
 * 基于某些原因暂未使用
 */
@Suppress("unused")
val client: HttpClient = HttpClient(CIO) {
    install(JsonFeature) {
        this.serializer = JacksonSerializer {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
}

/**
 * 此处的配置的 [MockHttpServletRequestDsl] 的拓展函数
 * 用于简化配置 post 请求
 *
 * 配置内容如下:
 * * 请求类型为 `application/json`
 * * 请求编码为 `UTF-8`
 *
 * 请在 post 方法 DSL 作用域内调用函数配置
 */
private val postJsonConfigure: MockHttpServletRequestDsl.() -> Unit = {
    contentType = MediaType.APPLICATION_JSON
    characterEncoding = Charsets.UTF_8.name()
}

fun MockMvc.postJson(urlTemplate: String, configure: MockHttpServletRequestDsl.() -> Unit = {}) =
    post(urlTemplate) {
        postJsonConfigure()
        configure()
    }

/**
 * 此处的配置的 [MockHttpServletRequestDsl] 的拓展函数
 * 是用于通过 json 作用域内的拓展属性 [jsonValue] 将传入[content]的转换为 json 字符串
 * 并赋值到 [MockHttpServletRequestDsl.content] 中
 */
fun MockHttpServletRequestDsl.jsonContent(content: Any) {
    this.content = json { content.jsonValue }
}

/**
 * 此处的配置的 [MvcResult] 的拓展函数
 * 是用于将 [MvcResult] 的 [MvcResult.getResponse] 的内容转换为 UTF-8 编码的字符串
 */
fun MvcResult.contextAsUTF8(): String {
    return response.getContentAsString(Charsets.UTF_8)
}

/**
 * 此处的配置的 [MvcResult] 的拓展函数
 * 是通过 [contextAsUTF8] 将 [MvcResult.getResponse] 的内容转换为 UTF-8 编码的字符串
 * 并通过 [json] 作用域内部 [type] 拓展函数将字符串转换为 [T] 类型
 */
inline fun <reified T> MvcResult.deserialize(): T = json {
    contextAsUTF8().type()
}

/**
 * 注意仅使用于异步请求，通常是 [post]
 * 此处的配置的 [ResultActionsDsl] 的拓展函数
 * 是先调用 [ResultActionsDsl.asyncDispatch] 方法来调用
 */
inline fun <reified T> ResultActionsDsl.asyncTo(afterDispatch: ResultActionsDsl.() -> Unit = {}) =
    asyncDispatch()
        .also(afterDispatch)
        .andReturn()
        .deserialize<T>()

/**
 * 注意仅使用于同步请求，通常是 [get]
 * 此处的配置的 [ResultActionsDsl] 的拓展函数
 * 是先调用 [ResultActionsDsl.asyncDispatch] 方法来调用
 */
inline fun <reified T> ResultActionsDsl.dispatchTo(afterDispatch: ResultActionsDsl.() -> Unit = {}) =
    this.also(afterDispatch)
        .andReturn()
        .deserialize<T>()




