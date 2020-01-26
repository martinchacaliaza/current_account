package com.example.app.service;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.app.models.TypeCurrentAccount;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TipoProductoService {
	
	Flux<TypeCurrentAccount> findAllTipoproducto();
	Mono<TypeCurrentAccount> findByIdTipoProducto(String id);
	Mono<TypeCurrentAccount> saveTipoProducto(TypeCurrentAccount tipoProducto);
	Mono<Void> deleteTipo(TypeCurrentAccount tipoProducto);
	
}
