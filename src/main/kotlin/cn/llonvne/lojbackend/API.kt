package cn.llonvne.lojbackend

object API {

    const val ROOT_URL = "http://localhost:9000"

    object LoginModule {
        const val LOGIN_API = "$ROOT_URL/login"

        const val LOGOUT_API = "$ROOT_URL/logout"

        const val HELLO_API = "$ROOT_URL/hello"
    }
}
