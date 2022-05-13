package com.s_m.backend.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "user_address")
public class UserAddress implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "mobile")
	private String mobileNumber;
	
	@Column(name = "address_line1")
	private String addressLineOne;
	
	@Column(name = "address_line2")
	private String addressLineTwo;
	
	@Column(name = "landmark")
	private String landmark;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "pincode")
	private String pincode;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "is_default")
	private int isDefault;
	
	// @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	// @JoinColumn(name = "fk_user_id")
	@JsonManagedReference
	@Column(name = "fk_user_id")
	private Long fkUser;

	public UserAddress() { }
	
	public UserAddress(String fullName, String mobileNumber, String addressLineOne, String addressLineTwo,
			String landmark, String city, String pincode, String state, String country, int isDefault) {
		this.fullName = fullName;
		this.mobileNumber = mobileNumber;
		this.addressLineOne = addressLineOne;
		this.addressLineTwo = addressLineTwo;
		this.landmark = landmark;
		this.city = city;
		this.pincode = pincode;
		this.state = state;
		this.country = country;
		this.isDefault = isDefault;
	}

	public UserAddress(long id, String fullName, String mobileNumber, String addressLineOne, String addressLineTwo,
			String landmark, String city, String pincode, String state, String country, int isDefault, Long fkUser) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.mobileNumber = mobileNumber;
		this.addressLineOne = addressLineOne;
		this.addressLineTwo = addressLineTwo;
		this.landmark = landmark;
		this.city = city;
		this.pincode = pincode;
		this.state = state;
		this.country = country;
		this.isDefault = isDefault;
		this.fkUser = fkUser;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAddressLineOne() {
		return addressLineOne;
	}

	public void setAddressLineOne(String addressLineOne) {
		this.addressLineOne = addressLineOne;
	}

	public String getAddressLineTwo() {
		return addressLineTwo;
	}

	public void setAddressLineTwo(String addressLineTwo) {
		this.addressLineTwo = addressLineTwo;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	public Long getFkUser() {
		return fkUser;
	}

	public void setFkUser(Long fkUser) {
		this.fkUser = fkUser;
	}

}
