package com.s_m.backend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "user")
public class User implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile")
	private String mobile;
	
	@Column(name = "active")
	private int active;
	
	@Column(name = "created_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column(name = "modified_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedAt;
	
	@ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	//@JsonIgnoreProperties
	private Collection<Role> roles;
	
	
	/*
	 * @JsonBackReference helps in keeping lazy initialization feature because
	 * jackson would want userAddressses to be present at the time of response but
	 * because of lazy initialization it won't be available at that time.
	 * hence it ignores this property at the time of response and hence keeps the lazy nature.
	 * for more info {@link JsonBackReference}
	 */	
	// @OneToMany(mappedBy = "fkUser", cascade = CascadeType.ALL)
	@JsonBackReference
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_user_id")
	private Collection<UserAddress> userAddresses;
	
	public User() {
		
	}

	public User(long id, String username, String password, String firstName, String lastName, String email,
			String mobile, int active, Date createdAt, Date modifiedAt) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobile = mobile;
		this.active = active;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		userAddresses = new ArrayList<UserAddress>();
	}

	public User(long id, String username, String password, String firstName, String lastName, String email,
			String mobile, int active, Date createdAt, Date modifiedAt, Collection<Role> roles) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobile = mobile;
		this.active = active;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.roles = roles;
		userAddresses = new ArrayList<UserAddress>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}
	
	public Collection<UserAddress> getUserAddresses() {
		return userAddresses;
	}

	public void setUserAddresses(Collection<UserAddress> userAddresses) {
		this.userAddresses = userAddresses;
	}
	
	public void addUserAddress(UserAddress userAddress) {
		if(userAddresses == null) {
			userAddresses = new ArrayList<>();
		}
		userAddresses.add(userAddress);
		
		userAddress.setFkUser(this.getId());
	}

	@Override
	public String toString() {
		return  "User{" + "id=" + id + ", username='" + username + '\'' + ", password='" + "*********" + '\''
				+ ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\''
				+ ", roles=" + roles + '}';
	} 
	
}
