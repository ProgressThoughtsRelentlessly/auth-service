package com.pthore.service.auth.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@JsonIdentityInfo( generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserProfile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="userProfileSequenceGenerator")
	@SequenceGenerator(
			name = "userProfileSequenceGenerator", 
			sequenceName = "user_profile_sequence",
			initialValue = 1,
			allocationSize = 1
		)
	private Long id;
	
	private String profilePicture;
	
	@Column(nullable = false)
	private String firstname;
	
	@Column(nullable = true)
	private String lastname;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = true)
	private String mobile;
	
	private String description;
	
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDate joiningDate;
	
	@UpdateTimestamp
	private LocalDate lastUpdateDate;
	
	/*
	 * metadata to be added/removed to/from UserPostMetadata.class This field should
	 * never be updated from this class.
	 */
	@OneToMany(orphanRemoval=true, mappedBy="userProfile", fetch = FetchType.LAZY)
	private List<UserPostMetadata> userPostMetadata;
	
	@OneToMany(orphanRemoval=true, mappedBy="user", fetch = FetchType.LAZY)
	private List<Follower> followers;

	/*
	 * data will be added from this class itself. Just remember to initialize the list before inserting for the firsttime/size == 0;
	*/
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name="users_followed_domains",
			joinColumns = {@JoinColumn(name = "user_id") },
			inverseJoinColumns= { @JoinColumn(name="domain_id")} 
			)
	private List<Domain> followedDomains;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name="user_group",
			joinColumns = {@JoinColumn(name = "user_id") },
			inverseJoinColumns= { @JoinColumn(name="group_id")} 
			)
	private List<PthoreGroup> followedGroups;
	

	
	
	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public List<Domain> getFollowedDomains() {
		return followedDomains;
	}

	public void setFollowedDomains(List<Domain> followedDomains) {
		this.followedDomains = followedDomains;
	}

	public List<PthoreGroup> getFollowedGroups() {
		return followedGroups;
	}

	public void setFollowedGroups(List<PthoreGroup> followedGroups) {
		this.followedGroups = followedGroups;
	}

	public List<Domain> getDomains() {
		return followedDomains;
	}

	public void setDomains(List<Domain> domains) {
		this.followedDomains = domains;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public List<UserPostMetadata> getUserPostMetadata() {
		return userPostMetadata;
	}

	public void setUserPostMetadata(List<UserPostMetadata> userPostMetadata) {
		this.userPostMetadata = userPostMetadata;
	}

	public UserProfile() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstname;
	}

	public void setFirstName(String firstName) {
		this.firstname = firstName;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public LocalDate getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDate lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public List<Follower> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Follower> followers) {
		this.followers = followers;
	}

}
