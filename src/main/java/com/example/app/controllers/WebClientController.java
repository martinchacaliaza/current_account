package com.example.app.controllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.app.models.Client;
import com.example.app.models.CurrentAccount;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ProductoBancario")
public class WebClientController {
	
	@Value("${com.bootcamp.gateway.url}")
	String valor;
	
	
	WebClient webClient;
	@PostConstruct
	 public void init() { 
			 webClient = WebClient 
			.builder()
			.baseUrl("http://"+valor+"/clientes/api/ClientePersonal")
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
			.build(); 		 
	}
	
   @GetMapping("/ClientePersonal/{dniCliente}")
   public Flux<Client> getCliente(@PathVariable String dniCliente) 
   { 
		return webClient.get().uri("/dni/"+dniCliente).retrieve().bodyToFlux(Client.class); 
   }
  

}
