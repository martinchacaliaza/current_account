package com.example.app.dao;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.app.models.CurrentAccount;
import com.example.app.models.TypeCurrentAccount;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TipoProductoDao extends ReactiveMongoRepository<TypeCurrentAccount, String> {

	
	@Query("{ 'idTipo' : ?0 }")
	Mono<TypeCurrentAccount> viewidTipo(String idTipo);
}
