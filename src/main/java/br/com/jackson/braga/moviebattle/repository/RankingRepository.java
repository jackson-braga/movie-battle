package br.com.jackson.braga.moviebattle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.jackson.braga.moviebattle.model.Player;
import br.com.jackson.braga.moviebattle.model.Ranking;

public interface RankingRepository extends JpaRepository<Ranking, Long>{

	Optional<Ranking> findByPlayer(Player player);

	@Query(value = "select * "
			+ "from ranking "
			+ "order by score desc "
			+ "limit :limit", nativeQuery = true)
	List<Ranking> findRanking(long limit);

}
