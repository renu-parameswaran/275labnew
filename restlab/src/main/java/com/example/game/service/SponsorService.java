package com.example.game.service;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.example.game.model.Sponsor;
import com.example.game.repository.SponsorRepository;

@Transactional
@Service
public class SponsorService {
	
	private final SponsorRepository sponsorRepository;
	
	public SponsorService(SponsorRepository sponsorRepository){
		this.sponsorRepository= sponsorRepository;
	}
	
	public void saveSponsor(Sponsor sponsor){
		sponsorRepository.save(sponsor);
	}
	
	public Sponsor getSponsor(Long id){
		return sponsorRepository.findOne(id);	
	}
	
	public void deleteSponsor(Sponsor sponsor){
		sponsorRepository.delete(sponsor);
	}

}
