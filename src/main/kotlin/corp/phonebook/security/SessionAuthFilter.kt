package corp.phonebook.security

import org.springframework.stereotype.Component
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import corp.phonebook.data.repository.AuthRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

@Component
class SessionAuthFilter(
    private val authRepository: AuthRepository,
    private val sessionService: SessionService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.requestURI

        if (path.contains("/auth/register") || path.contains("/auth/login")) {
            filterChain.doFilter(request, response)
            return
        }

        val sessionCookie = request.cookies?.find { it.name == "SESSION_ID" }

        if (sessionCookie == null) {
            filterChain.doFilter(request, response)
            return
        }

        val sessionId = sessionCookie.value

        if (sessionService.isSessionExpired(sessionId)) {
            filterChain.doFilter(request, response)
            return
        }

        val userOptional = authRepository.findBySessionId(sessionId)
        if (userOptional.isEmpty) {
            filterChain.doFilter(request, response)
            return
        }

        val user = userOptional.get()

        val authentication = UsernamePasswordAuthenticationToken(
            user,
            null,
            user.authorities
        )

        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }
}