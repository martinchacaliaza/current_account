package com.example.app.dao;


import java.util.Date;

import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.app.models.CreditAccount;
import com.example.app.models.CurrentAccount;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface ProductoDao extends ReactiveMongoRepository<CurrentAccount, String> {

	Flux<CurrentAccount> findByDni(String dni);
	
	
	@Query("{ 'numero_cuenta' : ?0 , 'codigo_bancario' : ?1}")
	Mono<CurrentAccount> consultaNumCuentaByCodBanc(String numero_cuenta, String codigo_bancario);
	
	@Query("{ 'dni' : ?0 , 'tipoProducto.idTipo' : ?1 , 'codigo_bancario': ?2 }")
	Flux<CurrentAccount> viewDniCliente2(String dni, String idTipo, String codigo_bancario);
	
	
	@Query("{'fecha_afiliacion' : {'$gt' : ?0, '$lt' : ?1}, 'codigo_bancario' : ?2}")
	Flux<CurrentAccount> consultaProductoBanco(Date from, Date to, String codigo_bancario);
	
}
