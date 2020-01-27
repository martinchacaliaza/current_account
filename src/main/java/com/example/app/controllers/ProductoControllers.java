package com.example.app.controllers;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.app.models.CurrentAccount;

import com.example.app.models.dtoCurrentAccount;
import com.example.app.models.TypeCurrentAccount;
import com.example.app.service.ProductoService;
import com.example.app.service.TipoProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/ProductoBancario")
@RestController
public class ProductoControllers {

	
	
	@Autowired
	private ProductoService productoService;

	@Autowired
	private TipoProductoService tipoProductoService;

	// Muestra todos las cuentas bancarias existentes
	@GetMapping
	public Mono<ResponseEntity<Flux<CurrentAccount>>> findAll() {
		return Mono.just(
				ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(productoService.findAllProducto())

		);
	}

	// Filtra todas cuentas bancarias por id
	@GetMapping("/{id}")
	public Mono<ResponseEntity<CurrentAccount>> viewId(@PathVariable String id) {
		return productoService.findByIdProducto(id)
				.map(p -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	// actualiza cuenta bancaria
	@PutMapping
	public Mono<CurrentAccount> updateProducto(@RequestBody CurrentAccount producto) {

		return productoService.saveProducto(producto);
	}

	// actualiza al momento de hacer la transaccion desde servicio
	// operaciones(movimientos)
	@PutMapping("/retiro/{numero_cuenta}/{monto}/{comision}/{codigo_bancario}")
	public Mono<CurrentAccount> retiroBancario(@PathVariable Double monto, @PathVariable String numero_cuenta,
			@PathVariable Double comision, @PathVariable String codigo_bancario) {

		return productoService.retiro(monto, numero_cuenta, comision, codigo_bancario);
	}

	// actualiza al momento de hacer la transaccion desde servicio
	// operaciones(movimientos)
	@PutMapping("/deposito/{numero_Cuenta}/{monto}/{comision}/{codigo_bancario}")
	public Mono<CurrentAccount> despositoBancario(@PathVariable Double monto, @PathVariable String numero_Cuenta,
			@PathVariable Double comision,  @PathVariable String codigo_bancario) {

		return productoService.depositos(monto, numero_Cuenta, comision, codigo_bancario);
	}

	/*
	 * Guarda o Crea una tarjeta bancaria(tipo: ahorro, plazo fijo ....) - si el
	 * cliente ya tiene un tipo de cuenta bancaria no debe de registrarlo
	 */
	@PostMapping
	public Flux<CurrentAccount> saveProducto(@RequestBody List<CurrentAccount> pro) {
		return productoService.saveProductoList(pro);
	}
	
	// Muestra la cuenta bancaria por el numero de tarjeta y entidad bancaria
	@GetMapping("/numero_cuenta/{num}/{codigo_bancario}")
	public Mono<CurrentAccount> listProdNumTarj(@PathVariable String num, @PathVariable String codigo_bancario) {
		Mono<CurrentAccount> producto = productoService.listProdNumTarj(num, codigo_bancario);
		return producto;
	}

	// Muestra todos los poductos de cuentas de credito de un cliente
	@GetMapping("/dni/{dni}")
	public Flux<CurrentAccount> listProductoByDicliente(@PathVariable String dni) {
		Flux<CurrentAccount> producto = productoService.findAllProductoByDniCliente(dni);
		return producto;
	}

	// Muestra los saldos de las cuentas de un cliente
	// se consulta por el numero de cuenta
	@GetMapping("/SaldosBancarios/{numero_cuenta}/{codigo_bancario}")
	public Mono<dtoCurrentAccount> SaldosBancarios(@PathVariable String numero_cuenta, String codigo_bancario) {

		Mono<CurrentAccount> oper = productoService.listProdNumTarj(numero_cuenta, codigo_bancario);

		return oper.flatMap(c -> {

			dtoCurrentAccount pp = new dtoCurrentAccount();
			TypeCurrentAccount tp = new TypeCurrentAccount();
			
			tp.setIdTipo(c.getTipoProducto().getIdTipo());
			tp.setDescripcion(c.getTipoProducto().getDescripcion());
			
			
			pp.setDni(c.getDni());
			pp.setNumero_cuenta(c.getNumero_cuenta());
			pp.setSaldo(c.getSaldo());
			pp.setTipoProducto(tp);

			return Mono.just(pp);
		});

	}
	
	//Reporte 
	@GetMapping("consultaRangoFecha/{fecha1}/{codigo_banco}")
	public Flux<CurrentAccount> consultaProductosTiempo(@PathVariable String fecha1, @PathVariable String codigo_banco) throws ParseException{
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
		String f1 = fecha1.split("&&")[0]+" 00:00:00.000 +0000";
		Date from = format.parse(f1);
		Date to = format.parse(fecha1.split("&&")[1]+" 00:00:00.000 +0000");
		System.out.println(format.format(from));
		return productoService.consultaProductosTiempo(from,to, codigo_banco);
	
		}
}

















