package com.example.game.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.example.game.model.Address;
import com.example.game.model.Sponsor;
import com.example.game.service.AddressService;
import com.example.game.service.PlayerService;
import com.example.game.service.SponsorService;

@RestController
public class SponsorController {
	
	@Autowired
	private SponsorService sponsorService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private AddressService addressService;
	
	@PostMapping("/sponsor")
	public @ResponseBody ModelAndView createSponsor(HttpServletResponse response, @RequestParam(value="name",required=true) String name,
			@RequestParam(value="description") String description,
			@RequestParam(value="street") String street,
			@RequestParam(value="city") String city,
			@RequestParam(value="state") String state,
			@RequestParam(value="zip") String zip
			) throws IOException
	{
		ModelMap map = new ModelMap();
		if(name==null || name.equals("")){
			map.addAttribute("error","No Sponsor Name");
			response.setStatus(400);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else{
			Address address = new Address(street,city,state,zip);
			addressService.saveAddress(address);
			Sponsor sponsor = new Sponsor(name,description,address);
			sponsorService.saveSponsor(sponsor);
			response.setStatus(200);
			map.addAttribute("sponsor",sponsor);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}
	}
	
	@GetMapping("sponsor/{id}")
	public @ResponseBody ModelAndView getSponsor(HttpServletResponse response,@PathVariable(value="id") long id){
		ModelMap map = new ModelMap();
		Sponsor sponsor = sponsorService.getSponsor(id);
		if((Long)id==null || sponsor == null ){
			map.addAttribute("error","Sponsor ID does not Exist");
			response.setStatus(404);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else{
			response.setStatus(200);
			map.addAttribute("sponsor",sponsor);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}
	}
	
	@PostMapping("/sponsor/{id}")
	public @ResponseBody ModelAndView updateSponsor(HttpServletResponse response, @PathVariable(value="id") long id,
			@RequestParam(value="name",required=true) String name,
			@RequestParam(value="description") String description,
			@RequestParam(value="street") String street,
			@RequestParam(value="city") String city,
			@RequestParam(value="state") String state,
			@RequestParam(value="zip") String zip
			) throws IOException
	{
		ModelMap map = new ModelMap();
		Sponsor sponsor = sponsorService.getSponsor(id);
		if(name==null || name.equals("")){
			map.addAttribute("error","No Sponsor Name");
			response.setStatus(400);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else if(sponsor == null){
			map.addAttribute("error","Sponsor ID does not Exist");
			response.setStatus(404);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else{
			Address address = sponsor.getAddress();
			address.setStreet(street);
			address.setCity(city);
			address.setState(state);
			address.setZip(zip);
			addressService.saveAddress(address);
			sponsor.setAddress(address);
			sponsor.setDescription(description);
			sponsor.setName(name);
			sponsorService.saveSponsor(sponsor);
			response.setStatus(200);
			map.addAttribute("sponsor",sponsor);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}
	}
	
	@DeleteMapping("sponsor/{id}")
	public @ResponseBody ModelAndView deleteSponsor(HttpServletResponse response,@PathVariable(value="id") long id){
		ModelMap map = new ModelMap();
		Sponsor sponsor = sponsorService.getSponsor(id);

		if((Long)id==null || sponsor == null ){
			map.addAttribute("error","Sponsor ID does not Exist");
			response.setStatus(404);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else if(playerService.getPlayersBySponsorId(id)!=null && playerService.getPlayersBySponsorId(id).size()>0){
			map.addAttribute("error","Players attached to the Sponsor");
			response.setStatus(400);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}else{
			response.setStatus(200);
			map.addAttribute("sponsor",sponsor);
			sponsorService.deleteSponsor(sponsor);
			return new ModelAndView(new MappingJackson2JsonView(), map);
		}
	}
	
}
