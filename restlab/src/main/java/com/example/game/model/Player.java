package com.example.game.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "player")
public class Player implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
	private String firstname;
    private String lastname;
    private String email;
    private String description;
    @OneToOne
    private Address address;
    @OneToOne
    private Sponsor sponsor;
    @ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.REMOVE)
    @JsonIgnore
    @JoinTable(name="opponents",
                joinColumns={@JoinColumn(name="PLAYER_ID")},
                inverseJoinColumns={@JoinColumn(name="OPPONENT_ID")})
    private List<Player> opponents = new ArrayList<Player>();
    
    public Player(){
    	
    }
    public Player(String firstname, String lastname, String email, String description, Address address, Sponsor sponsor) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.description = description;
		this.address = address;
		this.sponsor = sponsor;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Sponsor getSponsor() {
		return sponsor;
	}
	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}
	public List<Player> getOpponents() {
		return opponents;
	}
	public void setOpponents(List<Player> opponents) {
		this.opponents = opponents;
	}
	@Override
	public String toString() {
		return "Player [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", email=" + email
				+ ", description=" + description + ", address=" + address + ", sponsor=" + sponsor + ", opponents="
				+ opponents + "]";
	}
	
}
