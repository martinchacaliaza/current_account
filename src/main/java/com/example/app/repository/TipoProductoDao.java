package com.example.app.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.app.models.TypeCurrentAccount;

import reactor.core.publisher.Mono;

public interface TipoProductoDao extends ReactiveMongoRepository<TypeCurrentAccount, String> {

	
	@Query("{ 'idTipo' : ?0 }")
	Mono<TypeCurrentAccount> viewidTipo(String idTipo);
}
