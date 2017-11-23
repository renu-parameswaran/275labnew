package com.example.game.service;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.game.model.Player;
import com.example.game.model.Sponsor;
import com.example.game.repository.PlayerRepository;

@Transactional
@Service
public class PlayerService {
	
	private final PlayerRepository playerRepository;
	
	public PlayerService(PlayerRepository playerRepository) {
	this.playerRepository = playerRepository;
	}
	
	public void savePlayer(Player player){
		playerRepository.save(player);
	}
	
	public Player getPlayer(Long id){
		return playerRepository.findOne(id);
	}
	
	public void deletePlayer(Player player){
		playerRepository.delete(player);
	}
	
	public void deleteOpponent(Long id1,Long id2){
		
		Player player1 = playerRepository.findOne(id1);
		Player player2 = playerRepository.findOne(id2);
		List<Player> opponents1 = player1.getOpponents();
		List<Player> opponents2 = player2.getOpponents();
		opponents1.remove(player2);
		opponents2.remove(player1);
		playerRepository.save(player1);
		playerRepository.save(player2);

	}
	
	public void deletePlayersOppents(Player player){
		List<Player> opponents = player.getOpponents();
		for(Player opponent: opponents){
			List<Player> updatedOpponents = opponent.getOpponents();
			updatedOpponents.remove(player);
			playerRepository.save(player);
		}
	}
	
	public List<Player> getPlayersBySponsorId(long id){
		return playerRepository.findPlayersWithSponsor(id);
	}
	
	public void deleteAllOpponents(Player player){
		List<Player> opponents = player.getOpponents();
		opponents.clear();
		playerRepository.save(player);
	}
	
	public List<Player> findPlayerByEmail(String email){
		return playerRepository.findPlayerByEmail(email);
	}
}
