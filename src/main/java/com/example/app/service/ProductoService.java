package com.example.app.service;


import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.Query;
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
	
	Mono<CurrentAccount> listProdNumTarj(String num, String codigo_bancario);
	
	Mono<CurrentAccount> retiro(Double monto, String numero_cuenta, Double comision, String codigo_bancario);
	
	Mono<CurrentAccount> depositos(Double monto, String numero_cuenta, Double comision, String codigo_bancario);
	
	Mono<CurrentAccount> depositos(Double monto, String numero_cuenta, String codigo_bancario);
	
	Flux<CurrentAccount> consultaProductosTiempo(Date from, Date to, String codigo_banco);

	Flux<CurrentAccount> listProd(String dni, String codigo_bancario);

}
