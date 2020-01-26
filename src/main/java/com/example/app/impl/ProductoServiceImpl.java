package com.example.app.impl;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators.ToDate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.app.dao.ProductoDao;
import com.example.app.dao.TipoProductoDao;
import com.example.app.models.Client;
import com.example.app.models.CreditAccount;
import com.example.app.models.CurrentAccount;
import com.example.app.models.TypeCurrentAccount;
import com.example.app.service.ProductoService;
import com.example.app.service.TipoProductoService;
import com.sistema.app.exception.RequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Value("${com.bootcamp.gateway.url}")
	String valor;

	@Autowired
	public ProductoDao productoDao;

	@Autowired
	public TipoProductoDao tipoProductoDao;

	@Autowired
	private TipoProductoService tipoProductoService;

	@Override
	public Flux<CurrentAccount> findAllProducto() {
		return productoDao.findAll();

	}

	@Override
	public Mono<CurrentAccount> findByIdProducto(String id) {
		return productoDao.findById(id);

	}

	@Override
	public Flux<CurrentAccount> findAllProductoByDniCliente(String dniCliente) {

		return productoDao.viewDniCliente(dniCliente);
	}

	@Override
	public Mono<CurrentAccount> retiro(Double monto, String numTarjeta, Double comision) {

		return productoDao.viewNumTarjeta(numTarjeta).flatMap(c -> {

			if (monto < c.getSaldo()) {
				c.setSaldo((c.getSaldo() - monto) - comision);

				return productoDao.save(c);
			}
			return Mono.error(new InterruptedException("No tiene el saldo suficiente para retirar"));
		});
	}

	@Override
	public Mono<CurrentAccount> depositos(Double monto, String numTarjeta, Double comision) {
		return productoDao.viewNumTarjeta(numTarjeta).flatMap(c -> {
			c.setSaldo((c.getSaldo() + monto) - comision);
			return productoDao.save(c);
		});
	}

	@Override
	public Flux<CurrentAccount> saveProductoList(List<CurrentAccount> producto) {

		Flux<CurrentAccount> fMono = Flux.fromIterable(producto);
		return fMono.filter(ff -> {
			if (ff.getTipoProducto().getIdTipo().equalsIgnoreCase("1")
					|| ff.getTipoProducto().getIdTipo().equalsIgnoreCase("2")
					|| ff.getTipoProducto().getIdTipo().equalsIgnoreCase("3")
					|| ff.getTipoProducto().getIdTipo().equalsIgnoreCase("4")
					|| ff.getTipoProducto().getIdTipo().equalsIgnoreCase("5")
					|| ff.getTipoProducto().getIdTipo().equalsIgnoreCase("6")
					|| ff.getTipoProducto().getIdTipo().equalsIgnoreCase("7")
					|| ff.getTipoProducto().getIdTipo().equalsIgnoreCase("8")) {
				return true;
			}
			return false;

		}).flatMap(f -> {	
			Mono<CreditAccount> cred = WebClient.builder()
					.baseUrl("http://" + valor + "/productos_creditos/api/ProductoCredito/").build().get()
					.uri("/dni/"+f.getDni()).retrieve().bodyToMono(CreditAccount.class).log();
			
			
			return cred.flatMap(cre -> {

				Mono<CreditAccount> operas = WebClient.builder()
						.baseUrl("http://" + valor + "/productos_creditos/api/" + "OperCuentasCorrientes/").build().get()
						.uri("/consultaRangoFecha/" + cre.getFecha_cierre() +
								"&&"+cre.getFecha_cierre()).retrieve().bodyToMono(CreditAccount.class).log();
				
				
				
				
				return operas.flatMap(oper -> {
				
				Mono<Client> cli = WebClient.builder().baseUrl("http://" + valor + "/clientes/api/Clientes/").build()
						.get().uri("/dni/" + f.getDni()).retrieve().bodyToMono(Client.class).log();
				return cli.flatMap(p -> {

					if (!p.getCodigo_bancario().equalsIgnoreCase(f.getCodigo_bancario())) {

						throw new RequestException("el cliente no pertenece a la entedida bancaria del producto");

					} else {

						if (p.getTipoCliente().getIdTipo().equalsIgnoreCase("1")) {

							Mono<Long> valor = productoDao.viewDniCliente2(f.getDni(), f.getTipoProducto().getIdTipo())
									.count();

							return valor.flatMap(f2 -> {
								if (f2 >= 1) {
									if (!f.getTipoProducto().getIdTipo().equalsIgnoreCase("1")
											&& !f.getTipoProducto().getIdTipo().equalsIgnoreCase("2")
											&& !f.getTipoProducto().getIdTipo().equalsIgnoreCase("3")) {

										/*
										 * CurrentAccount f1 = new CurrentAccount(); f1.setDni(f.getDni());
										 * f1.setNumero_cuenta(f.getNumero_cuenta());
										 * f1.setFecha_afiliacion(f.getFecha_afiliacion());
										 * f1.setFecha_caducidad(f.getFecha_caducidad()); f1.setSaldo(f.getSaldo());
										 * f1.setUsuario(f.getClave()); f1.setClave(f.getClave());
										 * f1.setCodigo_bancario(f.getCodigo_bancario());
										 */

										TypeCurrentAccount t = new TypeCurrentAccount();
										t.setIdTipo(f.getTipoProducto().getIdTipo());
										t.setDescripcion(f.getTipoProducto().getDescripcion());

										f.setTipoProducto(t);
										return productoDao.save(f);

									} else {
										throw new RequestException(
												"El Cliente ya tiene una cuenta bancaria de ese tipo");
									}
								} else {

									/*
									 * CurrentAccount f1 = new CurrentAccount();
									 * 
									 * f1.setDni(f.getDni()); f1.setNumero_cuenta(f.getNumero_cuenta());
									 * f1.setFecha_afiliacion(f.getFecha_afiliacion());
									 * f1.setFecha_caducidad(f.getFecha_caducidad()); f1.setSaldo(f.getSaldo());
									 * f1.setUsuario(f.getClave()); f1.setClave(f.getClave());
									 * f1.setCodigo_bancario(f.getCodigo_bancario());
									 */

									TypeCurrentAccount t = new TypeCurrentAccount();
									t.setIdTipo(f.getTipoProducto().getIdTipo());
									t.setDescripcion(f.getTipoProducto().getDescripcion());
									f.setTipoProducto(t);

									return productoDao.save(f);

								}
							});

						} else if (p.getTipoCliente().getIdTipo().equalsIgnoreCase("2")) {

							if (!f.getTipoProducto().getIdTipo().equalsIgnoreCase("2")) {

								throw new RequestException("Cliente Empresarial no puede tener cuenta de ese tipo");
							}

							TypeCurrentAccount t = new TypeCurrentAccount();
							t.setIdTipo(f.getTipoProducto().getIdTipo());
							t.setDescripcion(f.getTipoProducto().getDescripcion());
							f.setTipoProducto(t);

							return productoDao.save(f);

						} else if (p.getTipoCliente().getIdTipo().equalsIgnoreCase("3")) {

							if (!f.getTipoProducto().getIdTipo().equalsIgnoreCase("4")
									&& !f.getTipoProducto().getIdTipo().equalsIgnoreCase("5")
									&& !f.getTipoProducto().getIdTipo().equalsIgnoreCase("8")) {

								throw new RequestException(
										"Un Cliente Personal VIP" + " no puede tener este tipo de cuenta");

							} else if (!(f.getSaldo() >= 20)) {

								throw new RequestException("La cuenta se apertura con un saldo mayor a S/.20");
							} else {

								TypeCurrentAccount t = new TypeCurrentAccount();
								t.setIdTipo(f.getTipoProducto().getIdTipo());
								t.setDescripcion(f.getTipoProducto().getDescripcion());
								f.setTipoProducto(t);
								return productoDao.save(f);

							}
						} else if (p.getTipoCliente().getIdTipo().equalsIgnoreCase("4")) {

							if (!f.getTipoProducto().getIdTipo().equalsIgnoreCase("6")) {

								throw new RequestException(
										"Un Cliente Empresarial PYME" + " no puede tener este tipo de cuenta");

							} else if (!(f.getSaldo() >= 50)) {

								throw new RequestException("La cuenta se apertura con un saldo mayor a S/.50");

							} else {

								TypeCurrentAccount t = new TypeCurrentAccount();
								t.setIdTipo(f.getTipoProducto().getIdTipo());
								t.setDescripcion(f.getTipoProducto().getDescripcion());
								f.setTipoProducto(t);
								return productoDao.save(f);

							}
						} else if (p.getTipoCliente().getIdTipo().equalsIgnoreCase("5")) {

							if (!f.getTipoProducto().getIdTipo().equalsIgnoreCase("7")) {

								throw new RequestException(
										"Un Cliente Empresarial Corporativo" + " no puede tener este tipo de cuenta");

							} else if (!(f.getSaldo() >= 100)) {

								throw new RequestException("La cuenta se apertura con un saldo mayor a S/.100");

							} else {

								TypeCurrentAccount t = new TypeCurrentAccount();
								t.setIdTipo(f.getTipoProducto().getIdTipo());
								t.setDescripcion(f.getTipoProducto().getDescripcion());
								f.setTipoProducto(t);
								return productoDao.save(f);

							}
						}
					}

					return Mono.empty();

				});
			});
		});
			});

	}

	@Override
	public Mono<CurrentAccount> listProdNumTarj(String num) {

		return productoDao.viewNumTarjeta(num);
	}

	@Override
	public Mono<CurrentAccount> saveProducto(CurrentAccount producto) {
		// TODO Auto-generated method stub
		return productoDao.save(producto);
	}

}
