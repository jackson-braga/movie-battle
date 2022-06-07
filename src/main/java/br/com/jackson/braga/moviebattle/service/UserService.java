package br.com.jackson.braga.moviebattle.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.jackson.braga.moviebattle.exceptions.NullModelException;
import br.com.jackson.braga.moviebattle.model.User;
import br.com.jackson.braga.moviebattle.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User upsert(User user) {
		if (user == null) {
			throw new NullModelException("User is null");
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		return repository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User invalid"));
	}

}
