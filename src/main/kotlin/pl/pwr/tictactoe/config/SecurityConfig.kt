package pl.pwr.tictactoe.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder

@EnableWebSecurity
class SecurityConfig(
    @Value("\${auth0.audience}")
    private val audience: String,

    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private val issuer: String
) : WebSecurityConfigurerAdapter() {

    private val error = OAuth2Error("invalid_token", "The required audience is missing", null)

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .mvcMatchers("/game").authenticated()
            .anyRequest().permitAll()
            .and()
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .oauth2ResourceServer().jwt()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder = (JwtDecoders.fromOidcIssuerLocation(issuer) as NimbusJwtDecoder).apply {
        this.setJwtValidator(
            DelegatingOAuth2TokenValidator(
                JwtValidators.createDefaultWithIssuer(issuer),
                OAuth2TokenValidator { jwt ->
                    if (jwt.audience.contains(audience)) {
                        OAuth2TokenValidatorResult.success()
                    } else {
                        OAuth2TokenValidatorResult.failure(error)
                    }
                }
            )
        )
    }

}
