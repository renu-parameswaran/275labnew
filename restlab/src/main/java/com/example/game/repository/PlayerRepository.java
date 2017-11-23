package com.example.game.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.game.model.Player;


public interface PlayerRepository extends CrudRepository<Player,Long>  {
	
	@Query(value="select * from 275lab.player where sponsor_id = ?",nativeQuery=true)
	public List<Player> findPlayersWithSponsor(long id);
	
	@Query(value="select * from 275lab.player where email = ?",nativeQuery=true)
	public List<Player> findPlayerByEmail(String email);

}
