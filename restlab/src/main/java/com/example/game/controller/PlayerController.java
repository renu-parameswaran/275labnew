package com.example.game.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import com.example.game.model.Address;
import com.example.game.model.Player;
import com.example.game.model.Sponsor;
import com.example.game.service.AddressService;
import com.example.game.service.PlayerService;
import com.example.game.service.SponsorService;

@RestController
public class PlayerController {
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private SponsorService sponsorService;
	
	@Autowired
	private AddressService addressService;
	
	@PostMapping("/player")
	public @ResponseBody ModelAndView createPlayer(HttpServletResponse response, @RequestParam(value="firstname",required=true) String firstname,
			@RequestParam(value="lastname",required=true) String lastname,
			@RequestParam(value="email",required=true) String email,
			@RequestParam(value="description",required=false) String description,
			@RequestParam(value="street",required=false) String street,
			@RequestParam(value="city",required=false) String city,
			@RequestParam(value="state",required=false) String state,
			@RequestParam(value="zip",required=false) String zip,
			@RequestParam(value="sponsor",required=false) Long sponsor
			) throws IOException
	{
		ModelMap map = new ModelMap();
		if(firstname==null || firstname.equals("") || lastname==null || lastname.equals("") || lastname==null || lastname.equals("") || email==null || email.equals("") ){
			map.addAttribute("error","Required Parameter Missing");
			response.setStatus(400);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else if(playerService.findPlayerByEmail(email)!=null && playerService.findPlayerByEmail(email).size()>=1){
			map.addAttribute("error","Email Id already Exists.");
			response.setStatus(400);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else{
			Address address = new Address(street,city,state,zip);
			addressService.saveAddress(address);
			Sponsor sponsorData = null;
			if(!((Long)sponsor==null)){
				sponsorData = sponsorService.getSponsor(sponsor);
			}
			Player player = new Player(firstname,lastname,email,description,address,sponsorData);
			playerService.savePlayer(player);
			response.setStatus(200);
			map.addAttribute("player",player);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}
	}
	
	@GetMapping("player/{id}")
	public @ResponseBody ModelAndView getPlayer(HttpServletResponse response,@PathVariable(value="id") long id){
		ModelMap map = new ModelMap();
		Player player = playerService.getPlayer(id);
		if((Long)id==null || player == null ){
			map.addAttribute("error","Player ID does not Exist");
			response.setStatus(404);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else{
			response.setStatus(200);
			map.addAttribute("player",player);
			map.addAttribute("opponents",player.getOpponents());
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}
	}
	
	@PostMapping("/player/{id}")
	public @ResponseBody ModelAndView updateplayer(HttpServletResponse response, @PathVariable(value="id") long id,
			@RequestParam(value="firstname",required=true) String firstname,
			@RequestParam(value="lastname",required=true) String lastname,
			@RequestParam(value="email",required=true) String email,
			@RequestParam(value="description") String description,
			@RequestParam(value="street") String street,
			@RequestParam(value="city") String city,
			@RequestParam(value="state") String state,
			@RequestParam(value="zip") String zip,
			@RequestParam(value="sponsor") long sponsorId
			) throws IOException
	{
		ModelMap map = new ModelMap();
		Player player = playerService.getPlayer(id);
		if(player==null || player.equals("")){
			map.addAttribute("error","No Player Exist");
			response.setStatus(404);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else if(firstname==null || firstname.equals("") || lastname==null || lastname.equals("") || lastname==null || lastname.equals("") ){
			map.addAttribute("error","Required Parameter Missing");
			response.setStatus(400);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else{
			Address address = player.getAddress();
			address.setStreet(street);
			address.setCity(city);
			address.setState(state);
			address.setZip(zip);
			addressService.saveAddress(address);
			player.setAddress(address);
			player.setDescription(description);
			player.setFirstname(firstname);
			player.setLastname(lastname);
			player.setEmail(email);
			playerService.savePlayer(player);
			response.setStatus(200);
			map.addAttribute("player",player);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}
	}
	
	@DeleteMapping("player/{id}")
	public @ResponseBody ModelAndView deletePlayer(HttpServletResponse response,@PathVariable(value="id") long id){
		ModelMap map = new ModelMap();
		Player player = playerService.getPlayer(id);
		if((Long)id==null || player == null ){
			map.addAttribute("error","Player ID does not Exist");
			response.setStatus(404);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else{
			response.setStatus(200);
			map.addAttribute("player",player);
			playerService.deletePlayersOppents(player);
			playerService.deleteAllOpponents(player);
			playerService.deletePlayer(player);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}
	}
	
	@PutMapping("opponents/{id1}/{id2}")
	public @ResponseBody ModelAndView addOpponents(HttpServletResponse response,@PathVariable(value="id1") long id1,
			@PathVariable(value="id2") long id2){
		ModelMap map = new ModelMap();
		Player player1 = playerService.getPlayer(id1);
		Player player2 = playerService.getPlayer(id2);

		if(player2==null || player1 == null ){
			map.addAttribute("error","Player ID does not Exist");
			response.setStatus(404);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else{
			List<Player> players = player1.getOpponents();
			if(players==null){
				players = new ArrayList<>();
			}
			players.add(player2);
			player1.setOpponents(players);
			playerService.savePlayer(player1);
			List<Player> playersInverse = player2.getOpponents();
			if(playersInverse==null){
				playersInverse =  new ArrayList<>();
			}
			playersInverse.add(player1);
			player2.setOpponents(playersInverse);
			playerService.savePlayer(player2);
			response.setStatus(200);
			map.addAttribute("message","Opponent added.");
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}
	}
	@DeleteMapping("opponents/{id1}/{id2}")
	public @ResponseBody ModelAndView deleteOpponents(HttpServletResponse response,@PathVariable(value="id1") long id1,
			@PathVariable(value="id2") long id2){
		ModelMap map = new ModelMap();
		Player player1 = playerService.getPlayer(id1);
		Player player2 = playerService.getPlayer(id2);
		//Logic for checking the opponents relationship
		boolean contains = false;
		List<Player> opponents = player1.getOpponents();
		for(Player player: opponents){
			if(player==player2)
				contains = true;
		}	
		if(player2==null || player1 == null ){
			map.addAttribute("error","Player ID does not Exist");
			response.setStatus(404);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else if(!contains){
			map.addAttribute("error","Opponent Relationship does not exist");
			response.setStatus(404);
			return new ModelAndView(new MappingJackson2JsonView(), map);
			
		}else{
			response.setStatus(200);
			playerService.deleteOpponent(id1,id2);
			map.addAttribute("message","Opponent Relationship deleted.");
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}
	}

}
