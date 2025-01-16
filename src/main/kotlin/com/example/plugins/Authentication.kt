package com.example.plugins


import com.example.domain.repository.IUsersRepository
import com.example.route.util.toUUID
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import kotlin.text.Charsets.UTF_8


const val AUTH_BASIC = "auth-basic"

fun Application.configureBasicAuthentication() {
    val usersRep by inject<IUsersRepository>()
    install(Authentication) {
        basic(AUTH_BASIC) {
            realm = "Access to database"
            charset = UTF_8
            validate { credentials ->
                val user = usersRep.read(credentials.name, credentials.password)
                println(user)
                user ?: return@validate null
                UserIdPrincipal(user.id.toString())
            }
        }
    }
}

//const val AUTH_FORM = "auth-form"
//const val AUTH_SESSION = "auth-session"
//
//data class UserSession(val id: String) : Principal
//
//fun Application.configureBasicAuthentication() {
//    val usersRep by inject<IUsersRepository>()
//    install(Sessions) {
//        cookie<UserSession>("user_session") {
//            cookie.path = "/"
//            cookie.maxAgeInSeconds = 60 * 60
//            cookie.httpOnly = false
//        }
//    }
//    install(Authentication) {
//        form("auth-form") {
//            userParamName = "email"
//            passwordParamName = "password"
//            validate { credentials ->
//                val user = usersRep.read(credentials.name, credentials.password)
//                if (user != null) {
//                    UserIdPrincipal(user.id.toString())
//                } else {
//                    null
//                }
//            }
//            challenge {
//                call.respondRedirect("/logout")
//            }
//        }
//        session<UserSession>("auth-session") {
//            validate { session ->
//                if(usersRep.read(session.id.toUUID()) != null) {
//                    session
//                } else {
//                    null
//                }
//            }
//            challenge {
//                call.respondRedirect("/logout")
//            }
//        }
//    }
//}