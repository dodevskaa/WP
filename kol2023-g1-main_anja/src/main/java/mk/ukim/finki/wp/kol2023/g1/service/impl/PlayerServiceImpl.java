package mk.ukim.finki.wp.kol2023.g1.service.impl;

import mk.ukim.finki.wp.kol2023.g1.model.Player;
import mk.ukim.finki.wp.kol2023.g1.model.PlayerPosition;
import mk.ukim.finki.wp.kol2023.g1.model.Team;
import mk.ukim.finki.wp.kol2023.g1.model.exceptions.InvalidPlayerIdException;
import mk.ukim.finki.wp.kol2023.g1.model.exceptions.InvalidTeamIdException;
import mk.ukim.finki.wp.kol2023.g1.repository.PlayerRepository;
import mk.ukim.finki.wp.kol2023.g1.repository.TeamRepository;
import mk.ukim.finki.wp.kol2023.g1.service.PlayerService;
import mk.ukim.finki.wp.kol2023.g1.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    private final TeamService teamService;

    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository, TeamService teamService) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.teamService = teamService;
    }

    @Override
    public List<Player> listAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);
    }

    @Override
    public Player create(String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
      Team teams=teamRepository.findById(team).orElseThrow(InvalidTeamIdException::new);
       Player player = new Player(name,bio,pointsPerGame,position,teams);
        return playerRepository.save(player);
    }

    @Override
    public Player update(Long id, String name, String bio, Double pointsPerGame, PlayerPosition position, Long team) {
        Player findPlayer = playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);
        Team teams=teamRepository.findById(team).orElseThrow(InvalidTeamIdException::new);
        findPlayer.setName(name);
        findPlayer.setBio(bio);
        findPlayer.setPosition(position);
        findPlayer.setPointsPerGame(pointsPerGame);
        findPlayer.setTeam(teams);
        return playerRepository.save(findPlayer);
    }

    @Override
    public Player delete(Long id) {
        Player toDelete = playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);
        playerRepository.delete(toDelete);
        return toDelete;
    }

    @Override
    public Player vote(Long id) {
        Player player = playerRepository.findById(id).orElseThrow(InvalidPlayerIdException::new);
        player.setVotes(player.getVotes()+1);
        return playerRepository.save(player);
    }

    @Override
    public List<Player> listPlayersWithPointsLessThanAndPosition(Double pointsPerGame, PlayerPosition position) {

       if(position==null && pointsPerGame==null){
          return listAllPlayers();
       }
       else if (position!=null && pointsPerGame!=null){
           return playerRepository.listPlayersWithPointsLessThanAndPositionContaining(pointsPerGame,position);
       }
       else if(position!=null){
           return playerRepository.listPlayersWithPositionContaining(position);
       }
       else
        return playerRepository.listPlayersWithPointsLessThan(pointsPerGame);
    }
}
