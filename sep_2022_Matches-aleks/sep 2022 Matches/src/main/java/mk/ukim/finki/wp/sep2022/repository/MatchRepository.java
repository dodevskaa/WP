package mk.ukim.finki.wp.sep2022.repository;

import mk.ukim.finki.wp.sep2022.model.Match;
import mk.ukim.finki.wp.sep2022.model.MatchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match,Long> {
    public List<Match> findMatchByPriceLessThanAndType (Double price, MatchType type);
    public List<Match> findMatchByPriceLessThan (Double price);
    public List<Match> findMatchByType ( MatchType type);
    // listMatchesWithPriceLessThanAndType
}
