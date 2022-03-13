package com.flysafe.FlySafe.Database.DataStore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Role class models entries in the Role table in the database.
 */
@Entity
@Table(name = "authorities")
public class Role {
	
	/**
	 * Default Constructor.
	 */
	public Role() {		
	}
	
	/**
	 * Parameterized constructor.
	 * 
	 * @param name The name of the role
	 */
	public Role(String name) {
		this.name = name;
	}
	
	/**
	 * (Primary Key) The unique identifier for the role.
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
	private long roleID;
	
	/**
	 * The name of the role.
	 */
	private String name;
	
	/**
	 * The User assigned the given role.
	 */
	@ManyToOne
    @JoinColumn(name = "user_id")
	private User user;

	/**
	 * Getter for the roleID.
	 * 
	 * @return roleID
	 */
	public long getRoleID() {
		return roleID;
	}
	
	/**
	 * Getter for the role name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter for the user assigned the role.
	 * 
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Setter for the user assigned the role.
	 * 
	 * @param user The user being assigned the role
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
