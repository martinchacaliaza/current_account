package com.example.app.repository;


import java.util.Date;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.example.app.models.CurrentAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface ProductoDao extends ReactiveMongoRepository<CurrentAccount, String> {

	Flux<CurrentAccount> findByDni(String dni);

	Mono<CurrentAccount> findByNumeroCuentaAndCodigoBancario(String numero_cuenta, String codigo_bancario);
	
	Flux<CurrentAccount> findByDniAndCodigoBancario(String dni, String codigo_bancario);
	
	@Query("{ 'dni' : ?0 , 'tipoProducto.idTipo' : ?1 , 'codigoBancario': ?2 }")
	Flux<CurrentAccount> viewDniCliente2(String dni, String idTipo, String codigo_bancario);
	
	@Query("{'fecha_afiliacion' : {'$gt' : ?0, '$lt' : ?1}, 'codigoBancario' : ?2}")
	Flux<CurrentAccount> consultaProductoBanco(Date from, Date to, String codigo_bancario);

	
	
}
