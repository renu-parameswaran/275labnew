package com.example.game.service;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.game.model.Address;
import com.example.game.repository.AddressRepository;

@Transactional
@Service
public class AddressService {
	
	private final AddressRepository addressRepository;
	
	public AddressService(AddressRepository addressRepository) {
	this.addressRepository = addressRepository;
	}
	
	public void saveAddress(Address address){
		addressRepository.save(address);
	}
	
	public Address getAddress(int id){
		return addressRepository.findOne(id);
	}

}
