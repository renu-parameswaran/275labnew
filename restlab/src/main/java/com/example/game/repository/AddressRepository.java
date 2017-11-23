package com.example.game.repository;

import org.springframework.data.repository.CrudRepository;
import com.example.game.model.Address;

public interface AddressRepository extends CrudRepository<Address, Integer> {

}
