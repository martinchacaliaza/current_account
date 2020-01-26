package com.example.app.dao;


import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.app.models.CurrentAccount;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface ProductoDao extends ReactiveMongoRepository<CurrentAccount, String> {

	
	@Query("{ 'dni' : ?0 , 'tipoProducto.idTipo' : ?1, 'codigo_bancario': ?2}")
	Flux<CurrentAccount> viewDniCliente2(String dni, String idTipo);
	
	
	@Query("{ 'dni' : ?0 }")
	Flux<CurrentAccount> viewDniCliente(String dni);

	@Query("{ 'numero_cuenta' : ?0 }")
	Mono<CurrentAccount> viewNumTarjeta(String numTarjeta);
	

	/*@Query("{ 'numeroTarjeta' : ?0 }")
	Mono<Producto> viewNumTarjeta2(String numTarjeta);*/
	
}
