package br.com.jackson.braga.moviebattle.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import br.com.jackson.braga.moviebattle.enums.BattleStatus;
import br.com.jackson.braga.moviebattle.enums.RoundStatus;
import br.com.jackson.braga.moviebattle.exceptions.UnprocessableModelException;
import br.com.jackson.braga.moviebattle.model.Battle;
import br.com.jackson.braga.moviebattle.model.Movie;
import br.com.jackson.braga.moviebattle.model.Round;
import br.com.jackson.braga.moviebattle.repository.MovieRepository;
import br.com.jackson.braga.moviebattle.repository.RoundRepository;

@Service
public class RoundService {
	private Logger log = org.slf4j.LoggerFactory.getLogger(RoundService.class);

	@Autowired
	private RoundRepository roundRepository;
	@Autowired
	private MovieRepository movieRepository;

	public Optional<Round> findCurrentRound(Battle battle) {
		validateBattle(battle);
		return roundRepository.findByBattleAndStatus(battle, RoundStatus.CURRENT).stream().findFirst();
	}

	public Round createRound(Battle battle) {
		validateBattle(battle);

		var rounds = roundRepository.findByBattle(battle);
		var movies = movieRepository.findAll();

		Pair<Movie, Movie> pair = choicePairMovie(rounds, movies);

		var round = new Round();
		round.setBattle(battle);
		round.setFirst(pair.getFirst());
		round.setSecond(pair.getSecond());
		round.setStatus(RoundStatus.CURRENT);
		round = roundRepository.save(round);
		return round;
	}

	private Pair<Movie, Movie> choicePairMovie(List<Round> rounds, List<Movie> movies) {
		var usedPairs = usedPairs(rounds);

		Pair<Movie, Movie> pair;
		do {
			pair = randomPair(movies);
		} while (usedPairs.contains(createPair(pair.getFirst(), pair.getSecond())));

		return pair;
	}

	private Pair<Movie, Movie> randomPair(List<Movie> movies) {
		var size = movies.size();

		var numbers = new Random().ints(0, size).distinct().limit(2).sorted().boxed().collect(Collectors.toList());

		var first = numbers.get(0);
		var second = numbers.get(1);

		return Pair.of(movies.get(first), movies.get(second));
	}

	private Set<Pair<Long, Long>> usedPairs(List<Round> rounds) {
		return rounds.stream().map(round -> {
			var first = round.getFirst();
			var second = round.getSecond();
			return createPair(first, second);
		}).collect(Collectors.toCollection(() -> {
			return new TreeSet<>(Comparator.comparing(Pair::getFirst));
		}));

	}

	private Pair<Long, Long> createPair(Movie first, Movie second) {
		if (first.getId() > second.getId()) {
			return Pair.of(second.getId(), first.getId());
		}
		return Pair.of(first.getId(), second.getId());
	}

	private void validateBattle(Battle battle) {
		if (battle.getStatus() != BattleStatus.STARTED) {
			throw new UnprocessableModelException("Battle não iniciado.");
		}
	}

	public Optional<Round> findRound(Battle battle, Long roundId) {
		validateBattle(battle);
		return roundRepository.findByBattleAndId(battle, roundId);
	}

	public Round answer(Round round, Movie chosen) {
		if (round.getStatus() != RoundStatus.CURRENT) {
			throw new UnprocessableModelException("Round já respondida");
		}
		
		var status = answerRound(round, chosen);
		round.setStatus(status);
		
		movieRepository.findById(chosen.getId())
			.ifPresent(round::setChoice);
		
		round = roundRepository.save(round);
		
		return round;
	}

	private RoundStatus answerRound(Round round, Movie chosen) {
		var scoreFirst = calculateScore(round.getFirst());
		
		var scoreSecond = calculateScore(round.getSecond());
		
		Movie better = scoreFirst > scoreSecond ? round.getFirst() : round.getSecond();

		return better.equals(chosen) ? RoundStatus.SUCCESS : RoundStatus.FAILD;
	}

	private float calculateScore(Movie movie) {
		return movie.getRating() * movie.getVotes();
	}

}
