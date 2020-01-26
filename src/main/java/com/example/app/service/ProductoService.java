package com.example.app.service;


import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.app.models.CurrentAccount;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

	Flux<CurrentAccount> findAllProducto();
	
	Mono<CurrentAccount> findByIdProducto(String id);
	
	Mono<CurrentAccount> saveProducto(CurrentAccount producto);

	Flux<CurrentAccount> findAllProductoByDniCliente(String dniCliente);

	Flux<CurrentAccount> saveProductoList(List<CurrentAccount> producto);
	
	Mono<CurrentAccount> listProdNumTarj(String num);
	
	Mono<CurrentAccount> retiro(Double monto, String numTarjeta, Double comision);
	
	Mono<CurrentAccount> depositos(Double monto, String numTarjeta, Double comision);



	
}
