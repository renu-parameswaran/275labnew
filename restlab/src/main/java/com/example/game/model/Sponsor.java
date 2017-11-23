package com.example.game.model;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "sponsor")
public class Sponsor implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @OneToOne
    private Address address;
    
    public Sponsor() {
	}

    public Sponsor(String name, String description, Address address) {
		super();
		this.name = name;
		this.description = description;
		this.address= address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "Sponsor [id=" + id + ", name=" + name + ", description=" + description + ", address=" + address + "]";
	}


}
