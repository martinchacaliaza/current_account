package com.example.app.controllers;


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
import com.example.app.models.TypeCurrentAccount;
import com.example.app.service.ProductoService;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/ProductoBancario")
@RestController
public class ProductoControllers {

	@Autowired
	private ProductoService productoService;



	@ApiOperation(value = "Muestra todos las cuentas bancarias existentes", notes="")
	@GetMapping
	public Mono<ResponseEntity<Flux<CurrentAccount>>> findAll() {
		return Mono.just(
				ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(productoService.findAllProducto())

		);
	}

	@ApiOperation(value = "Filtra todas cuentas bancarias por id", notes="")
	@GetMapping("/{id}")
	public Mono<ResponseEntity<CurrentAccount>> viewId(@PathVariable String id) {
		return productoService.findByIdProducto(id)
				.map(p -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}


	@ApiOperation(value = "Actualiza cuenta bancaria", notes="")
	@PutMapping
	public Mono<CurrentAccount> updateProducto(@RequestBody CurrentAccount producto) {

		return productoService.saveProducto(producto);
	}

	@ApiOperation(value = "actualiza al momento de hacer la transaccion[RETIRO] desde servicio"
			+ " operaciones(movimientos)", notes="")
	@PutMapping("/retiro/{numero_cuenta}/{monto}/{comision}/{codigo_bancario}")
	public Mono<CurrentAccount> retiroBancario(@PathVariable Double monto, @PathVariable String numero_cuenta,
			@PathVariable Double comision, @PathVariable String codigo_bancario) {

		return productoService.retiro(monto, numero_cuenta, comision, codigo_bancario);
	}

	
	@ApiOperation(value = "Actualiza al momento de hacer la transaccion [DEPOSITO] desde servicio"
			+ " operaciones(movimientos)", notes="")
	@PutMapping("/depositoTransf/{numero_Cuenta}/{monto}/{codigo_bancario}")
	public Mono<CurrentAccount> despositoTransf(@PathVariable Double monto, @PathVariable String numero_Cuenta,
			@PathVariable Double comision,  @PathVariable String codigo_bancario) {

		return productoService.depositos(monto, numero_Cuenta, codigo_bancario);
	}

	@ApiOperation(value = "Actualiza al momento de hacer la transaccion [DEPOSITO] desde servicio"
			+ " operaciones(movimientos)  cobrando comision si realiza varias transacciones", notes="")
	@PutMapping("/deposito/{numero_Cuenta}/{monto}/{comision}/{codigo_bancario}")
	public Mono<CurrentAccount> despositoBancario(@PathVariable Double monto, @PathVariable String numero_Cuenta,
			@PathVariable Double comision,  @PathVariable String codigo_bancario) {

		return productoService.depositos(monto, numero_Cuenta, comision, codigo_bancario);
	}



	@ApiOperation(value = "GUARDA CON VALIDANDO SEGUN LAS REGLAS DEL NEGOCIO", notes="")
	@PostMapping
	public Flux<CurrentAccount> saveProducto(@RequestBody List<CurrentAccount> pro) {
		return productoService.saveProductoList(pro);
			
	}

	@ApiOperation(value = "Muestra la cuenta bancaria por el numero de tarjeta y entidad bancaria", notes="")
	@GetMapping("/numero_cuenta/{num}/{codigo_bancario}")
	public Mono<CurrentAccount> listProdNumTarj(@PathVariable String num, @PathVariable String codigo_bancario) {
		Mono<CurrentAccount> producto = productoService.listProdNumTarj(num, codigo_bancario);
		return producto;
	}
	
	@ApiOperation(value = "Muestra Cuentas Bancarias por el Dni Cliente y entidad bancaria", notes="")
	@GetMapping("/dni_codbanco/{dni}/{codigo_bancario}")
	public Flux<CurrentAccount> listProd(@PathVariable String dni, @PathVariable String codigo_bancario) {
		Flux<CurrentAccount> producto = productoService.listProd(dni, codigo_bancario);
		return producto;
	}

	@ApiOperation(value = "Muestra todos los poductos de cuentas de credito de un cliente", notes="")
	@GetMapping("/dni/{dni}")
	public Flux<CurrentAccount> listProductoByDicliente(@PathVariable String dni) {
		Flux<CurrentAccount> producto = productoService.findAllProductoByDniCliente(dni);
		return producto;
	}

	@ApiOperation(value = "Muestra los saldos de las cuentas de un cliente"
			+ " se consulta por el numero de cuenta", notes="")
	@GetMapping("/SaldosBancarios/{numero_cuenta}/{codigo_bancario}")
	public Mono<CurrentAccount> SaldosBancarios(@PathVariable String numero_cuenta, String codigo_bancario) {
		Mono<CurrentAccount> oper = productoService.listProdNumTarj(numero_cuenta, codigo_bancario);
		return oper.flatMap(c -> {
			CurrentAccount pp = new CurrentAccount();
			TypeCurrentAccount tp = new TypeCurrentAccount();
			tp.setIdTipo(c.getTipoProducto().getIdTipo());
			tp.setDescripcion(c.getTipoProducto().getDescripcion());
			pp.setDni(c.getDni());
			pp.setNumeroCuenta(c.getNumeroCuenta());
			pp.setSaldo(c.getSaldo());
			pp.setTipoProducto(tp);
			return Mono.just(pp);
		});

	}

	@ApiOperation(value = "REPORTE POR RANGO DE FECHAS", notes="")
	@GetMapping("consultaRangoFecha/{fecha1}/{codigo_banco}")
	public Flux<CurrentAccount> consultaProductosTiempo(@PathVariable String fecha1, @PathVariable String codigo_banco) throws ParseException{
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
		String f1 = fecha1.split("&&")[0]+" 00:00:00.000 +0000";
		Date from = format.parse(f1);
		Date to = format.parse(fecha1.split("&&")[1]+" 00:00:00.000 +0000");
		System.out.println(format.format(from));	
		return productoService.consultaProductosTiempo(from,to,codigo_banco);
			
		
		}
}

















