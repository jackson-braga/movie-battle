package br.com.jackson.braga.moviebattle.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.jackson.braga.moviebattle.exceptions.NullModelException;
import br.com.jackson.braga.moviebattle.model.User;
import br.com.jackson.braga.moviebattle.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService service;
	
	@MockBean
	private UserRepository repository;
	
	@Test
	public void shouldReturnNewUser() {
		var newUser = new User(null, "teste1", "123");
		var userReturned = new User(1L, "teste1", "123");
		
		Mockito.when(repository.save(Mockito.any())).thenReturn(userReturned);
		
		var user = service.upsert(newUser);
		
		Assertions.assertNotNull(user);
		Assertions.assertNotNull(user.getId());
		Assertions.assertEquals(1L, user.getId());
	}
	
	@Test
	public void shouldReturnExcpeptionWhenUserIsNull() {
		Assertions.assertThrows(NullModelException.class, () -> service.upsert(null));
	}
	
	@Test
	public void shouldReturnUserDetails() {
		var userReturned = new User(1L, "teste1", "123");
		
		Mockito.when(repository.findByUsername(Mockito.any())).thenReturn(Optional.of(userReturned));
		
		var user = service.loadUserByUsername("teste1");
		
		Assertions.assertNotNull(user);
		Assertions.assertNotNull(((User) user).getId());
		Assertions.assertEquals(1L, ((User) user).getId());
	}
	
	@Test
	public void shoulRetrunUserDetailsEmpty() {
		
		Assertions.assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("teste"));
	}
}
