package corp.phonebook.config

import org.springframework.stereotype.Service
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.http.MediaType
import jakarta.servlet.http.HttpServletResponse

@Service
class SecurityExceptionHandler {
    fun unauthorized(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { _, response, _ ->
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.writer.write(
                """
                {
                    "status": 401,
                    "error": "Unauthorized",
                    "message": "Authentication required. Please login",
                    "timestamp": ${System.currentTimeMillis()}
                }
                """.trimIndent()
            )
        }
    }

    fun accessDenied(): AccessDeniedHandler {
        return AccessDeniedHandler { _, response, accessDeniedException ->
            response.status = HttpServletResponse.SC_FORBIDDEN
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.writer.write(
                """
                {
                    "status": 403,
                    "error": "Access Denied",
                    "message": "${accessDeniedException.message ?: "You don't have permission to access this resource"}",
                    "timestamp": ${System.currentTimeMillis()}
                }
                """.trimIndent()
            )
        }
    }
}