
package org.springframework.samples.flatbook.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author japarejo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;


	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/resources/**","/webjars/**","/h2-console/**").permitAll()
				.antMatchers(HttpMethod.GET, "/","/oups").permitAll()
				.antMatchers("/users/new").permitAll()
				.antMatchers("/users/**").authenticated()
				.antMatchers("/admin/**").hasAnyAuthority("admin")
                .antMatchers("/advertisements/{advertisementId:[0-9]+}").permitAll()
                .antMatchers("/advertisements/{advertisementId:[0-9]+}/edit").hasAnyAuthority("HOST", "admin")
                .antMatchers("/advertisements/{advertisementId:[0-9]+}/delete").hasAnyAuthority("HOST", "admin")
                .antMatchers("/advertisements/{advertisementId:[0-9]+}/requests/new").hasAnyAuthority("TENANT")
                .antMatchers("/advertisements/{advertisementId:[0-9]+}/requests/list").hasAnyAuthority("HOST", "admin")
                .antMatchers("/advertisements/{advertisementId:[0-9]+}/requests/{requestId:[0-9]+}/**").hasAnyAuthority("HOST", "admin")
                .antMatchers("/requests/{requestId:[0-9]+}/**").fullyAuthenticated()
                .antMatchers("/requests/list").hasAuthority("TENANT")
                .antMatchers("/advertisements/**").permitAll()
                .antMatchers("/flats/{flatId:[0-9]+}/reviews/list").permitAll()
                .antMatchers("/flats/{flatId:[0-9]+}/reviews/**").hasAnyAuthority("TENANT")
				.antMatchers("/flats/{flatId:[0-9]+}").hasAnyAuthority("HOST", "admin")
                .antMatchers("/flats/{flatId:[0-9]+}/edit").hasAnyAuthority("HOST", "admin")
                .antMatchers("/flats/{flatId:[0-9]+}/images/{imageId:[0-9]+}/delete").hasAnyAuthority("HOST", "admin")
                .antMatchers("/flats/new").hasAnyAuthority("HOST")
                .antMatchers("/flats/my-flats").hasAuthority("HOST")
                .antMatchers("/flats/{flatId:[0-9]+}/advertisements/new").hasAnyAuthority("HOST")
				.antMatchers("/messages/**").authenticated()
				.antMatchers("/tasks/**").hasAuthority("TENANT")
				.antMatchers("/tenants/**/reviews/list").permitAll()
				.antMatchers("/tenants/**").fullyAuthenticated()
				.antMatchers("/logout").permitAll()
				.anyRequest().denyAll()
				.and()
				 	.formLogin()
				 	/*.loginPage("/login")*/
				 	.failureUrl("/login-error")
				.and()
					.logout()
						.logoutSuccessUrl("/");
                // Configuración para que funcione la consola de administración
                // de la BD H2 (deshabilitar las cabeceras de protección contra
                // ataques de tipo csrf y habilitar los framesets si su contenido
                // se sirve desde esta misma página.
                http.csrf().ignoringAntMatchers("/h2-console/**");
                http.headers().frameOptions().sameOrigin();
	}

	@Override
	public void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(this.dataSource).usersByUsernameQuery("select username,password,enabled " + "from users " + "where username = ?")
			.authoritiesByUsernameQuery("select username, authority " + "from authorities " + "where username = ?").passwordEncoder(this.passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
		return encoder;
	}

}
