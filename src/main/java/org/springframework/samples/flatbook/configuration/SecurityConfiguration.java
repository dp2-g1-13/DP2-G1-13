
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
				.antMatchers("/users/list").hasAnyAuthority("ADMIN")
				.antMatchers("/users/{username:[0-9a-zA-Z]{5,}}").permitAll()
				.antMatchers("/users/{username:[0-9a-zA-Z]{5,}}/unban").hasAnyAuthority("ADMIN")
				.antMatchers("/users/{username:[0-9a-zA-Z]{5,}}/ban").hasAnyAuthority("ADMIN")
				.antMatchers("/users/{username:[0-9a-zA-Z]{5,}}/edit").hasAnyAuthority("HOST", "TENANT")
				.antMatchers("/users/{username:[0-9a-zA-Z]{5,}}/editPassword").hasAnyAuthority("HOST", "TENANT")
                .antMatchers("/advertisements/{advertisementId:[0-9]+}").permitAll()
                .antMatchers("/advertisements").permitAll()
                .antMatchers("/advertisements/{advertisementId:[0-9]+}/edit").hasAnyAuthority("HOST", "ADMIN")
                .antMatchers("/advertisements/{advertisementId:[0-9]+}/delete").hasAnyAuthority("HOST", "ADMIN")
                .antMatchers("/flats/{flatId:[0-9]+}/requests/new").hasAnyAuthority("TENANT")
                .antMatchers("/flats/{flatId:[0-9]+}/requests/list").hasAnyAuthority("HOST")
                .antMatchers("/flats/{flatId:[0-9]+}/requests/{requestId:[0-9]+}/**").hasAnyAuthority("HOST")
                .antMatchers("/requests/list").hasAuthority("TENANT")
				.antMatchers("/flats/{flatId:[0-9]+}").hasAnyAuthority("HOST", "ADMIN", "TENANT")
                .antMatchers("/flats/{flatId:[0-9]+}/advertisements/new").hasAnyAuthority("HOST")
                .antMatchers("/flats/{flatId:[0-9]+}/**").hasAnyAuthority("HOST", "ADMIN")
                .antMatchers("/flats/new").hasAnyAuthority("HOST")
                .antMatchers("/flats/list").hasAuthority("HOST")
                .antMatchers("/reviews/**").fullyAuthenticated()
				.antMatchers("/messages/**").hasAnyAuthority("HOST", "TENANT")
				.antMatchers("/tasks/**").hasAuthority("TENANT")
				.antMatchers("/tenants/**").fullyAuthenticated()
				.antMatchers("/logout").permitAll()
				.antMatchers("/reports/{username:[0-9a-zA-Z]{5,}}/new").hasAnyAuthority("HOST", "TENANT")
				.antMatchers("/statistics").hasAnyAuthority("ADMIN")
				.antMatchers("/reports/list").hasAnyAuthority("ADMIN")
				.antMatchers("/reports/{username:[0-9a-zA-Z]{5,}}/list").hasAnyAuthority("ADMIN")
				.antMatchers("/reports/{reportId:[0-9]+}/delete").hasAnyAuthority("ADMIN")
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
