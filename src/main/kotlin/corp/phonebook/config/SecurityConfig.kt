package corp.phonebook.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.context.annotation.Bean
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import corp.phonebook.security.SessionAuthFilter
import corp.phonebook.constants.Constants.VERSION

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig(
    private val sessionAuthFilter: SessionAuthFilter,
    private val securityExceptionHandler: SecurityExceptionHandler
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .logout { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("$VERSION/auth/register", "$VERSION/auth/login").permitAll()
                    .requestMatchers("$VERSION/auth/**").authenticated()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(sessionAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling { exceptions ->
                exceptions
                    .authenticationEntryPoint(securityExceptionHandler.unauthorized())
                    .accessDeniedHandler(securityExceptionHandler.accessDenied())
            }

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}