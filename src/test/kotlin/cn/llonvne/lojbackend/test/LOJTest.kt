package cn.llonvne.lojbackend.test

import cn.llonvne.lojbackend.TestLojBackendApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.lang.annotation.Inherited

@Inherited
@ContextConfiguration(classes = [TestLojBackendApplication::class])
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@TestPropertySource(locations = ["classpath:test.properties"])
/**
 * 这个注解用于测试类，用于标记这个类是一个LOJBackend测试类
 * 会自动加载测试配置，包括数据库和Redis，以及其他的一些配置
 *
 * 主要目的是为了减少测试的类注解数量，并且统一配置测试类注解
 */
annotation class LOJTest
