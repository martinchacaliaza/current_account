package com.example.app;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.app.dto.dtoCreditAccount;
import com.example.app.models.CurrentAccount;
import com.example.app.service.ProductoService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationCurrentTests {

	@Autowired
	private WebTestClient credit; 
	
	@Autowired
	ProductoService currentService;
	
	@Test
	void contextLoads() {
	}
	
	
	@Test
	public void listCredit() {
		credit.get().uri("/api/ProductoBancario/")
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
		.expectBodyList(dtoCreditAccount.class).consumeWith(response -> {
			List<dtoCreditAccount> credit = response.getResponseBody();
			
			credit.forEach(p -> {
				System.out.println(p.getDni());
			});
			
			Assertions.assertThat(credit.size()>0).isTrue();
		});;
	}
	
	@Test
	public void findByIdCurrent() {
		CurrentAccount cred = currentService.findByIdProducto("5e306641ccfe6770a8b39dac").block();
		credit.get().uri("/api/ProductoBancario/{id}", Collections.singletonMap("id", cred.getId()))
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
	}
	
	@Test
	public void findAllProductoByDniCliente() {
		CurrentAccount cred = currentService.findAllProductoByDniCliente("123456").blockFirst();
		credit.get().uri("/api/ProductoBancario/dni/{dni}", Collections.singletonMap("dni", cred.getDni()))
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
	}

}
