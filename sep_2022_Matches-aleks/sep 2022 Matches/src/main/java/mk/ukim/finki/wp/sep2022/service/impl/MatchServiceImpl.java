package mk.ukim.finki.wp.sep2022.service.impl;

import mk.ukim.finki.wp.sep2022.model.Match;
import mk.ukim.finki.wp.sep2022.model.MatchLocation;
import mk.ukim.finki.wp.sep2022.model.MatchType;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchIdException;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchLocationIdException;
import mk.ukim.finki.wp.sep2022.repository.MatchLocationRepository;
import mk.ukim.finki.wp.sep2022.repository.MatchRepository;
import mk.ukim.finki.wp.sep2022.service.MatchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchLocationRepository matchLocationRepository;
    private final MatchRepository matchRepository;

    public MatchServiceImpl(MatchLocationRepository matchLocationRepository, MatchRepository matchRepository) {
        this.matchLocationRepository = matchLocationRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public List<Match> listAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match findById(Long id) {
        return matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
    }

    @Override
    public Match create(String name, String description, Double price, MatchType type, Long location) {
      MatchLocation location1 = matchLocationRepository.findById(location).orElseThrow(InvalidMatchLocationIdException::new);
      Match match = new Match(name,description,price,type,location1);
        return matchRepository.save(match);
    }

    @Override
    public Match update(Long id, String name, String description, Double price, MatchType type, Long location) {
        MatchLocation location1 = matchLocationRepository.findById(location).orElseThrow(InvalidMatchLocationIdException::new);
        Match match = findById(id);
        match.setName(name);
        match.setDescription(description);
        match.setPrice(price);
        match.setType(type);
        match.setLocation(location1);
        return matchRepository.save(match);
    }

    @Override
    public Match delete(Long id) {
        Match toDelete = findById(id);
        matchRepository.delete(toDelete);
        return toDelete;
    }

    @Override
    public Match follow(Long id) {
        Match matchFollow = findById(id);
        matchFollow.setFollows(matchFollow.getFollows()+1);
        return matchRepository.save(matchFollow);
    }

    @Override
    public List<Match> listMatchesWithPriceLessThanAndType(Double price, MatchType type) {

        if(price==null && type==null){
            return listAllMatches();
        }
        else if (price!=null && type!=null){
            return matchRepository.findMatchByPriceLessThanAndType(price, type);
        }
        else if(price!=null ){
            return matchRepository.findMatchByPriceLessThan(price);

        }
        else {
            return matchRepository.findMatchByType(type);
        }




    }
}
