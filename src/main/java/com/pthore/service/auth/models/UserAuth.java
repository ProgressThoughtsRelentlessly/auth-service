package com.pthore.service.auth.models;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="user_auth")
public class UserAuth implements UserDetails {
	
	private static final long serialVersionUID = 8929406003912696796L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_user_sequence_generator")
	@SequenceGenerator(name="auth_user_sequence_generator", sequenceName = "user_auth_sequence")
	private Long id;
	
	@Column(nullable = false)
	private String firstname;
	
	@Column(nullable = true)
	private String lastname;
	
	@Column(nullable = false)
	private String email;
	
	private String password;
	
	private String matchingPassword;

	@CreationTimestamp
	private LocalDate joiningDate;
	
	private String mobile;
	
	@Lob
	private String jwtString; 
	
	private String pictureUrl;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<GrantedAuthority> authorities;
	
	private boolean isAccountNonExpired;
	private boolean isAccountNonBlocked;
	private boolean isCredentialsNonExpired; 
	private boolean isEnabled;
	
	public UserAuth() {
	}
	
	public UserAuth(String username, String password, List<GrantedAuthority> authorities,
			boolean isAccountNonExpired, boolean isAccountNonBlocked, 
			boolean isCredentialsNonExpired, boolean isEnabled) {
			
			this.authorities = authorities;
			this.email = username;
			this.password = password;
			this.isAccountNonBlocked = isAccountNonBlocked;
			this.isAccountNonExpired = isAccountNonExpired;
			this.isEnabled = isEnabled;
			this.isCredentialsNonExpired = isCredentialsNonExpired;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.isAccountNonBlocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getJwtString() {
		return jwtString;
	}

	public void setJwtString(String jwtString) {
		this.jwtString = jwtString;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public boolean isAccountNonBlocked() {
		return isAccountNonBlocked;
	}

	public void setAccountNonBlocked(boolean isAccountNonBlocked) {
		this.isAccountNonBlocked = isAccountNonBlocked;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public void setAccountNonExpired(boolean isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}

	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
}
