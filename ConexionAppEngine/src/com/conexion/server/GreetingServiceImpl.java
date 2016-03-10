package com.conexion.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.conexion.client.GreetingService;
import com.conexion.entidades.Empresa;
import com.conexion.entidades.EmpresaEndpoint;
import com.conexion.entidades.Usuario;
import com.conexion.entidades.UsuarioEndpoint;
import com.conexion.entidades.Viaje;
import com.conexion.entidades.ViajeEndpoint;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	@Override
	public String greetServer(String valNombreEmpresa, String tipo,
			String valDireEmpresa, String valCCEmpresa) {

		Empresa emp = new EmpresaEndpoint().obtenerMaxId();
		emp.setId(emp.getId() + 1);
		emp.setTipo(tipo);
		emp.setNombre(valNombreEmpresa);
		emp.setDireccion(valDireEmpresa);
		emp.setCuenta_corriente(valCCEmpresa);
		emp.setSaldoInicial(valCCEmpresa);
		insertEmpresa(emp);

		return "Empresa creada.";
	}

	// Utilizo en EndPoint.
	public Empresa insertEmpresa(Empresa empresa) {
		return new EmpresaEndpoint().insertEmpresa(empresa);
	}

	@Override
	public String greetServerCrearUsuario(String valNombreUsuario,
			String valClaveUsuario, String valRolUsuario) {

		Usuario usu = new UsuarioEndpoint().obtenerMaxIdUsuario();
		usu.setId(usu.getId() + 1);
		usu.setNombre(valNombreUsuario);
		usu.setPass(valClaveUsuario);
		usu.setRol(valRolUsuario);
		insertUsuario(usu);

		return "Usuario creado.";
	}

	private Usuario insertUsuario(Usuario usu) {
		return new UsuarioEndpoint().insertUsuario(usu);
	}

	@Override
	public String greetServerLogin(String valLblUsuario, String valLblClave) {
		Usuario usu = new UsuarioEndpoint().getUsuarioNombrePass(valLblUsuario,
				valLblClave);
		if (usu != null)
			return usu.toString();
		else
			return "noEsta";

	}

	@Override
	public ArrayList<Usuario> getListaUsuarios() {
		CollectionResponse<Usuario> consultaBD = new UsuarioEndpoint()
				.listUsuario(null, null);
		Collection<Usuario> usuarios = consultaBD.getItems();
		ArrayList<Usuario> result = new ArrayList<Usuario>(usuarios);
		return result;
	}

	@Override
	public Usuario getUsuario(String usuSel) {
		Usuario usu = new UsuarioEndpoint().obtenertUsuarioByNombre(usuSel);
		return usu;
	}

	@Override
	public String ModificarUsuario(Usuario usu) {
		updateUsuario(usu);
		return "Usuario modificado.";
	}

	private Usuario updateUsuario(Usuario usuario) {
		return new UsuarioEndpoint().updateUsuario(usuario);
	}

	@Override
	public String eliminarUsuario(Usuario usuario) {
		new UsuarioEndpoint().removeUsuario(usuario.getId());
		return "Usuario eliminado.";
	}

	@Override
	public List<Empresa> getListaEmpresas() {
		CollectionResponse<Empresa> consultaBD = new EmpresaEndpoint()
				.listEmpresa(null, null);
		Collection<Empresa> empresas = consultaBD.getItems();
		ArrayList<Empresa> result = new ArrayList<Empresa>(empresas);

		return result;
	}

	@Override
	public Empresa getEmpresa(String empSel) {
		return new EmpresaEndpoint().obtenerEmpresaByNombre(empSel);
	}

	@Override
	public String ModificarEmpresa(Empresa empresaSel) {
		updateEmpresa(empresaSel);
		return "Empresa modificada.";
	}

	private Empresa updateEmpresa(Empresa empresaSel) {
		return new EmpresaEndpoint().updateEmpresa(empresaSel);
	}

	@Override
	public String eliminarEmpresa(Empresa empresa) {
		new EmpresaEndpoint().removeEmpresa(empresa.getId());
		return "Empresa eliminada.";
	}

	@Override
	public List<Viaje> getListaViajes() {
		CollectionResponse<Viaje> consultaBD = new ViajeEndpoint().listViaje(
				null, null);
		Collection<Viaje> viajes = consultaBD.getItems();
		ArrayList<Viaje> result = new ArrayList<Viaje>(viajes);

		return result;
	}

	@Override
	public String crearViaje(String fecha, String proveedor, String cliente,
			String kilos, String precioXKilo, String descargador,
			String montoDescargador, String fletero, String precioXFlete,
			String tipoCarga, String factura, Boolean cargado, Boolean pagado,
			String empresaPropia) {

		Viaje viaje = new ViajeEndpoint().obtenerMaxId();
		viaje.setId(viaje.getId() + 1);
		viaje.setFecha(fecha);
		viaje.setProveedor(proveedor);
		viaje.setCliente(cliente);
		viaje.setKilos(kilos);
		viaje.setKilosOrigenReales(kilos);
		viaje.setPrecio_kilo(precioXKilo);
		viaje.setNombre_descargador(descargador);
		viaje.setMonto_descargador(montoDescargador);
		viaje.setFletero(fletero);
		viaje.setPrecioXtonelada_flete(precioXFlete);
		viaje.setTipoCarga(tipoCarga);
		viaje.setFacturado(factura);
		viaje.setPendienteCarga(!cargado);
		viaje.setNombreEmpresaPropia(empresaPropia);
		viaje.setEmpresaPropia(empresaPropia);
		viaje.setSePagoFletero(false);
		viaje.setSePagoDescargador(false);
		viaje.setPagoParcial("0");
		viaje.setCobroParcial("0");
		viaje.setPrecioFleteroDescarga(precioXFlete);
		viaje.setPrecioMaderaDescarga(precioXKilo);
		if (cargado) {
			if (pagado) {
				viaje.setPendienteDePago(!pagado);
				// Calculo del monto que son las toneladas por el precio
				// por tonelada original.
				BigDecimal bdTon = BigDecimal
						.valueOf(Double.parseDouble(kilos));
				BigDecimal bdPrecioXton = BigDecimal.valueOf(Double
						.parseDouble(viaje.getPrecio_kilo()));
				BigDecimal bdMonto = bdPrecioXton.multiply(bdTon);
				viaje.setPago(bdMonto.toString());
				viaje.setFechaPagar_origen(fecha);
			} else {
				viaje.setPago("0");
				viaje.setFechaPagar_origen("-");
			}
		}
		viaje.setPendienteDeDescarga(true);

		new ViajeEndpoint().insertViaje(viaje);
		return "Viaje creado.";
	}

	@Override
	public Usuario getUsuarioById(Long usuarioResponsable) {
		return new UsuarioEndpoint().getUsuario(usuarioResponsable);
	}

	@Override
	public String eliminarViaje(Viaje viaje) {
		new ViajeEndpoint().removeViaje(viaje.getId());
		return "Viaje eliminado.";
	}

	@Override
	public Viaje getViaje(String viajeSel) {
		Long id = Long.parseLong(viajeSel);
		return new ViajeEndpoint().getViaje(id);
	}

	@Override
	public String ModificarViaje(Viaje vje, Boolean estabaCargado,
			Boolean estabaPago, Boolean estabaDescargado,
			Boolean estabaCobrado, Boolean estabaPagoFletero,
			Boolean estabaPagoDescargador) {
		// Primero modifico el viaje.
		Viaje viaje = new ViajeEndpoint().updateViaje(vje);
		// Luego modifico las CC que correspondan.
		// PARA LA CARGA.
		if (!estabaCargado) {
			if (!estabaPago) {// Cuando no estaba cargado ni pago.
				// Si se CARGA Y NO SE PAGA.
				if (!viaje.isPendienteCarga() && viaje.isPendienteDePago()) {
					cargarViaje(viaje, estabaPagoFletero, estabaPagoDescargador);
				} else
				// Si se CARGA Y PAGA.
				if (!viaje.isPendienteCarga() && !viaje.isPendienteDePago()) {
					cargarPagarViaje(viaje);
				}
			}
		} else // YA ESTABA CARGADO
		if (!estabaPago) {// ESTABA CARGADO PERO NO PAGADO.
			if (!viaje.isPendienteCarga() && !viaje.isPendienteDePago()) {
				// Se paga el viaje
				pagarViajeCargado(viaje);
			} else {// SI SE QUITA LA CARGA.
				if (viaje.isPendienteCarga() && viaje.isPendienteDePago()) {
					quitarViajeCargado(viaje);
				}
			}
		} else {// SI ESTABA CARGADO Y PAGO.
			// SI SE QUITA EL PAGO.
			if (!viaje.isPendienteCarga() && viaje.isPendienteDePago()) {
				// TODO revisar el pago parcial.
				quitarPagoViajeCargado(viaje);
			} else {
				// SI SE QUITA EL PAGO Y LA CARGA.
				if (viaje.isPendienteCarga()) {
					// TODO revisar el pago parcial.
					quitarViajeCargadoPago(viaje);
				}
			}
		}
		// FIN CARGA.
		// PARA EL FLETERO
		if (!estabaPagoFletero) {
			if (viaje.isSePagoFletero()) {
				pagarFletero(viaje);
			}
		} else {// SI ESTABA PAGO EL FLETERO.
			if (!viaje.isSePagoFletero()) {
				quitarPagoFletero(viaje);
			}
		}
		// FIN FLETERO
		// PARA EL DESCARGADOR
		if (!estabaPagoDescargador) {
			if (viaje.isSePagoDescargador()) {
				pagarDescargador(viaje);
			}
		} else {// SI ESTABA PAGO EL DESCARGADOR.
			if (!viaje.isSePagoDescargador()) {
				quitarPagoDescargador(viaje);
			}
		}
		// FIN DESCARGADOR
		// PARA LA DESCARGA
		if (!estabaDescargado) {
			if (!estabaCobrado) {
				// SI SE DESCARGA Y NO SE COBRA.
				if (!viaje.isPendienteDeDescarga()
						&& viaje.isPendienteDeCobro()) {
					descargarViaje(viaje);
				} else {
					// SI SE DESCARGA Y SE COBRA.
					if (!viaje.isPendienteDeDescarga()
							&& !viaje.isPendienteDeCobro()) {
						String saldoCC = new EmpresaEndpoint()
								.obtenerCCdelCliente(viaje.getCliente()).get(0);
						descargarCobrarViaje(viaje, saldoCC);
					}
				}
			}
		} else {// SI YA ESTABA DESCARGADO
			if (!estabaCobrado) {// SI ESTABA DESCARGADO Y NO COBRADO
				// SI SE COBRA EL VIAJE DESCARGADO
				if (!viaje.isPendienteDeDescarga()
						&& !viaje.isPendienteDeCobro()) {
					cobrarViajeDescargado(viaje);
				} else {
					// SI SE QUITA LA DESCARGA DEL VIAJE NO COBRADO
					if (viaje.isPendienteDeDescarga()) {
						quitarDescargaViaje(viaje);
					}
				}
			} else {// SI ESTABA DESCARGADO Y COBRADO.
				// SI SE QUITA EL COBRO DEL VIAJE DESCARGADO
				if (!viaje.isPendienteDeDescarga()
						&& viaje.isPendienteDeCobro()) {
					quitarCobroViajeDescargado(viaje);
				} else {
					// SI SE QUITA LA DESCARGA DEL VIAJE NO COBRADO NI
					// DESCARGADO
					if (viaje.isPendienteDeDescarga()) {
						quitarDescargaViajePagado(viaje);
					}
				}
			}

		}
		// FIN DESCARGA
		return "Viaje modificado.";
	}

	// Estaba Descargado y Cobrado pero se quita.
	// 22-02-16---Funciona ok para un solo viaje
	// modificar la CC del cliente.
	private void quitarDescargaViajePagado(Viaje viaje) {
		// Si el cobro parcial no era el valor del viaje
		// se debe restaurar el saldo que tenia en la CC.
		if (viaje.getKilosOrigenReales() == null) {
			viaje.setKilosOrigenReales(viaje.getKilos());
		}
		// Inicializo las variables
		BigDecimal precioMadera = new BigDecimal("0.00");
		BigDecimal montoFletero = new BigDecimal("0.00");
		BigDecimal iva = new BigDecimal("0.00");
		BigDecimal ton = new BigDecimal("0.00");
		BigDecimal costoViaje = new BigDecimal("0.00");
		// Seteo los valores
		precioMadera = BigDecimal.valueOf(Double.parseDouble(viaje
				.getPrecioMaderaDescarga()));
		montoFletero = BigDecimal.valueOf(Double.parseDouble(viaje
				.getPrecioFleteroDescarga()));
		iva = BigDecimal.valueOf(Double.parseDouble("1.22"));
		ton = BigDecimal.valueOf(Double.parseDouble(viaje
				.getKilosOrigenReales()));
		// Realizo el calculo
		costoViaje = ton.multiply(precioMadera.add(montoFletero.multiply(iva)));
		costoViaje = costoViaje.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		if (Double.parseDouble(viaje.getCobroParcial()) != costoViaje
				.doubleValue()) {
			BigDecimal CC = BigDecimal.valueOf(
					Double.parseDouble(viaje.getCobroParcial()))
					.add(costoViaje);
			CC = CC.multiply(BigDecimal.valueOf(-1));
			new EmpresaEndpoint().modificarCuentaCorrienteNombre(
					viaje.getCliente(), CC.toString());
		}
		// Cuando no hay nada seleccionado
		viaje.setCobroParcial("");
		new ViajeEndpoint().updateViaje(viaje);
	}

	// Estaba Descargado y Cobrado pero se quita el cobro.
	private void quitarCobroViajeDescargado(Viaje viaje) {
		// Aumenta la CC del Cliente.
		modificarCC_Iva(viaje.getCliente(), viaje.getPrecioFleteroDescarga(),
				viaje.getPrecioMaderaDescarga(), viaje.getKilosOrigenReales(),
				"1");
	}

	// Si el viaje estaba Descargado y se quita.
	private void quitarDescargaViaje(Viaje viaje) {
		// Disminuye la CC del Cliente.
		modificarCC_Iva(viaje.getCliente(), viaje.getPrecioFleteroDescarga(),
				viaje.getPrecioMaderaDescarga(), viaje.getKilosOrigenReales(),
				"-1");
	}

	// El viaje estaba descargado y no cobrado y se cobra.
	private void cobrarViajeDescargado(Viaje viaje) {
		// Asigno la diferencia que es el saldo del cliente.
		new EmpresaEndpoint().modificarCuentaCorrienteNombre(
				viaje.getCliente(), viaje.getCobroParcial());

	}

	// Se descarga y cobra el viaje.
	private void descargarCobrarViaje(Viaje viaje, String saldoCC) {
		// 1. Se agrege el cobro parcial.
		BigDecimal cobroParcial = new BigDecimal("0.00");
		BigDecimal costoViaje = new BigDecimal("0.00");
		Empresa cliente = new EmpresaEndpoint().obtenerEmpresaByNombre(viaje
				.getCliente());
		// Calculo el costo del viaje.
		costoViaje = costoViajeCobrar(viaje.getCliente(),
				viaje.getPrecioFleteroDescarga(),
				viaje.getPrecioMaderaDescarga(), viaje.getKilosOrigenReales(),
				"1");
		if (Double.parseDouble(cliente.getCuenta_corriente()) >= 0) {
			// El cliente me debe plata.
			// Se agrega como cobro parcial el costo del viaje de forma
			// negativa.
			cobroParcial = costoViaje.multiply(BigDecimal.valueOf(Double
					.parseDouble("-1")));
			viaje.setCobroParcial(cobroParcial.toString());
			new ViajeEndpoint().updateViaje(viaje);
			// En este caso NO se modifica la CC del cliente.
		} else {
			BigDecimal clienteCC = new BigDecimal("0.00");// La cuenta
															// corriente del
															// cliente
			BigDecimal saldoClienteCC = new BigDecimal("0.00");// El saldo de
																// la CC del
																// cliente.
			clienteCC = BigDecimal.valueOf(Double.parseDouble(cliente
					.getCuenta_corriente()));
			// Realizo la resta de lo que tiene en la CC el cliente con el
			// costo del viaje.
			saldoClienteCC = clienteCC.add(costoViaje);
			if (saldoClienteCC.doubleValue() >= 0) {
				// El saldo que tenia el cliente NO es suficiente para cobrar
				// el viaje.
				// por lo tanto el costo del viaje sera el resto que tiene que
				// pagar.
				saldoClienteCC = saldoClienteCC.multiply(BigDecimal
						.valueOf(Double.parseDouble("-1")));
				viaje.setCobroParcial(saldoClienteCC.toString());
				new ViajeEndpoint().updateViaje(viaje);
				// 2. Modificar la CC del Cliente
				// Asigno la diferencia que es el saldo del cliente.
				clienteCC = clienteCC.multiply(BigDecimal.valueOf(Double
						.parseDouble("-1")));
				clienteCC = clienteCC.setScale(2, BigDecimal.ROUND_UNNECESSARY);
				new EmpresaEndpoint().modificarCuentaCorrienteNombre(
						viaje.getCliente(), clienteCC.toString());
				// FIN 2.
			} else {
				// El saldo que tenia el cliente alcanza para pagar el viaje.
				// No se debe cobrar al cliente y por eso se pasa "0"
				viaje.setCobroParcial("0");
				new ViajeEndpoint().updateViaje(viaje);
				// 2. Modificar la CC del Ciente
				// Asigno la diferencia que es el saldo del cliente.
				costoViaje = costoViaje.setScale(2,
						BigDecimal.ROUND_UNNECESSARY);
				new EmpresaEndpoint().modificarCuentaCorrienteNombre(
						viaje.getCliente(), costoViaje.toString());
				// FIN 2.
			}
		}
		// FIN 1.
	}

	// Se descarga el viaje y no se cobra.
	private void descargarViaje(Viaje viaje) {
		// 1. Se agrege el cobro parcial.
		BigDecimal cobroParcial = new BigDecimal("0.00");
		BigDecimal costoViaje = new BigDecimal("0.00");
		Empresa cliente = new EmpresaEndpoint().obtenerEmpresaByNombre(viaje
				.getCliente());
		// Calculo el costo del viaje.
		costoViaje = costoViajeCobrar(viaje.getCliente(),
				viaje.getPrecioFleteroDescarga(),
				viaje.getPrecioMaderaDescarga(), viaje.getKilosOrigenReales(),
				"1");
		if (Double.parseDouble(cliente.getCuenta_corriente()) >= 0) {
			// El proveedor me debe plata.
			// Se agrega como pago parcial el costo del viaje de forma negativa.
			cobroParcial = costoViaje.multiply(BigDecimal.valueOf(Double
					.parseDouble("-1")));
			viaje.setCobroParcial(cobroParcial.toString());
			new ViajeEndpoint().updateViaje(viaje);
			// En este caso NO se modifica la CC del proveedor.
		} else {
			BigDecimal clienteCC = new BigDecimal("0.00");// La cuenta
															// corriente del
															// cliente
			BigDecimal saldoClienteCC = new BigDecimal("0.00");// El saldo de
																// la CC del
																// cliente.
			clienteCC = BigDecimal.valueOf(Double.parseDouble(cliente
					.getCuenta_corriente()));
			// Realizo la resta de lo que tiene en la CC el proveedor con el
			// costo del viaje.
			saldoClienteCC = clienteCC.add(costoViaje);
			if (saldoClienteCC.doubleValue() >= 0) {
				// El saldo que tenia el cliente NO es suficiente para cobrar
				// el viaje.
				// por lo tanto el costo del viaje seria el resto que tiene que
				// pagar.
				saldoClienteCC = saldoClienteCC.multiply(BigDecimal
						.valueOf(Double.parseDouble("-1")));
				viaje.setCobroParcial(saldoClienteCC.toString());
				new ViajeEndpoint().updateViaje(viaje);
			} else {
				// El saldo que tenia el cliente alcanza para cobrar el viaje.
				// No debo cobrar al cliente y por eso se pasa "0"
				viaje.setCobroParcial("0");
				new ViajeEndpoint().updateViaje(viaje);
			}
		}
		// FIN 1.
		// 2. Modificar la CC del Proveedor
		new EmpresaEndpoint().modificarCuentaCorrienteNombre(
				cliente.getNombre(), costoViaje.toString());
		// FIN 2.
	}

	private BigDecimal costoViajeCobrar(String cliente,
			String precioFleteroDescarga, String precioMaderaDescarga,
			String toneladas, String signo) {
		// Inicializo las variables
		BigDecimal precioMadera = new BigDecimal("0.00");
		BigDecimal montoFletero = new BigDecimal("0.00");
		BigDecimal iva = new BigDecimal("0.00");
		BigDecimal ton = new BigDecimal("0.00");
		BigDecimal CC = new BigDecimal("0.00");
		// Seteo los valores
		precioMadera = BigDecimal.valueOf(Double
				.parseDouble(precioMaderaDescarga));
		montoFletero = BigDecimal.valueOf(Double
				.parseDouble(precioFleteroDescarga));
		iva = BigDecimal.valueOf(Double.parseDouble("1.22"));
		ton = BigDecimal.valueOf(Double.parseDouble(toneladas));
		// Realizo el calculo
		CC = ton.multiply(precioMadera.add(montoFletero.multiply(iva)));
		// Dejo la CC en el signo que corresponda
		CC = CC.multiply(new BigDecimal(Double.parseDouble(signo)));
		CC = CC.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		return CC;
	}

	// Se quita el pago del Descargador.
	private void quitarPagoDescargador(Viaje viaje) {
		// Disminuye la CC del Descargador.
		BigDecimal montoDescargador = BigDecimal.valueOf(Double
				.parseDouble(viaje.getMonto_descargador()));
		modificarCC(viaje.getNombre_descargador(), montoDescargador.toString(),
				viaje.getKilosOrigenReales());
	}

	// Se paga al Descargador.
	private void pagarDescargador(Viaje viaje) {
		// Disminuye la CC del Descargador.
		BigDecimal montoDescargador = BigDecimal.valueOf(Double
				.parseDouble(viaje.getMonto_descargador()));
		montoDescargador = montoDescargador.multiply(new BigDecimal(Double
				.parseDouble("-1")));
		if (viaje.getKilosOrigenReales() != null) {
			modificarCC(viaje.getNombre_descargador(),
					montoDescargador.toString(), viaje.getKilosOrigenReales());
		} else {
			modificarCC(viaje.getNombre_descargador(),
					montoDescargador.toString(), viaje.getKilos());
		}
	}

	// Se quita el pago al fletero.
	private void quitarPagoFletero(Viaje viaje) {
		// Aumenta la CC del fletero.
		BigDecimal montoFletero = BigDecimal.valueOf(Double.parseDouble(viaje
				.getPrecioXtonelada_flete()));
		modificarCC(viaje.getFletero(), montoFletero.toString(),
				viaje.getKilosOrigenReales());
	}

	// Se paga al fletero.
	private void pagarFletero(Viaje viaje) {
		// Disminuye la CC del fletero de forma negativa.
		BigDecimal montoFletero = BigDecimal.valueOf(Double.parseDouble(viaje
				.getPrecioXtonelada_flete()));
		montoFletero = montoFletero.multiply(new BigDecimal(Double
				.parseDouble("-1")));
		if (viaje.getKilosOrigenReales() != null) {
			modificarCC(viaje.getFletero(), montoFletero.toString(),
					viaje.getKilosOrigenReales());
		} else {
			modificarCC(viaje.getFletero(), montoFletero.toString(),
					viaje.getKilos());
		}
	}

	// El viaje estaba cargado y pago y que quita.
	private void quitarViajeCargadoPago(Viaje viaje) {
		// 01-03-16------FUNCIONA OK en todos los casos.
		// Si el pago al descargador fue cubierto con el saldo de la CC
		// se debe restaurar la CC del proveedor.
		if (viaje.getKilosOrigenReales() == null) {
			viaje.setKilosOrigenReales(viaje.getKilos());
		}
		// Si el viaje fue pagado por el pago parcial.
		if (viaje.getObservaciones().equals("pagoParcial")) {
			// Se debe aumentar el precioMaderaDescarga
			// del viaje con el cual fue pagado
			Viaje viajePagoParcial = obtenerViajeQueLoPago(viaje);
			modificarMontoPagoParcial(viajePagoParcial, viaje.getPagoParcial());
		}
		Double costoViaje = Double.parseDouble(viaje.getKilosOrigenReales())
				* Double.parseDouble(viaje.getPrecio_kilo());
		Double CC = Double.parseDouble(viaje.getPagoParcial()) + costoViaje;
		CC = CC * -1;
		new EmpresaEndpoint().modificarCuentaCorrienteNombre(
				viaje.getProveedor(), CC.toString());
		// Cuando no hay nada seleccionado.
		viaje.setPagoParcial("");
		new ViajeEndpoint().updateViaje(viaje);
		// // Disminuye la CC del fletero y descargador.
		// quitarPagoFletero(viaje);
		// quitarPagoDescargador(viaje);
	}

	// El viaje estaba cargado y que quita.
	// 22-02-16------FUNCIONA OK en todos los casos.
	private void quitarViajeCargado(Viaje viaje) {
		// Disminuye la CC del proveedor, fletero y descargador.
		// Debo pasar el precio por toneladas de forma negativa.
		if (viaje.getKilosOrigenReales() == null) {
			viaje.setKilosOrigenReales(viaje.getKilos());
		}
		BigDecimal bdPrecioTon = BigDecimal.valueOf(Double.parseDouble(viaje
				.getPrecio_kilo()));
		bdPrecioTon = bdPrecioTon.multiply(new BigDecimal(Double
				.parseDouble("-1")));
		modificarCC(viaje.getProveedor(), bdPrecioTon.toString(),
				viaje.getKilosOrigenReales());
		// Cuando no hay nada seleccionado.
		viaje.setPagoParcial("");
		new ViajeEndpoint().updateViaje(viaje);
		// // Disminuye la CC del fletero y descargador.
		// quitarPagoFletero(viaje);
		// quitarPagoDescargador(viaje);
	}

	// Se quita el pago de un viaje que estaba cargado y pagado.
	// 22-02-16------FUNCIONA OK en todos los casos.
	private void quitarPagoViajeCargado(Viaje viaje) {
		// Aumenta la CC del proveedor.
		if (Double.parseDouble(viaje.getPagoParcial()) != 0) {
			if (viaje.getKilosOrigenReales() != null) {
				modificarCC(viaje.getProveedor(), viaje.getPrecio_kilo(),
						viaje.getKilosOrigenReales());
			} else {
				modificarCC(viaje.getProveedor(), viaje.getPrecio_kilo(),
						viaje.getKilos());
			}
			// Si el viaje fue pagado por el pago parcial.
			if (viaje.getObservaciones().equals("pagoParcial")) {
				// Se debe aumentar el precioMaderaDescarga
				// del viaje con el cual fue pagado
				Viaje viajePagoParcial = obtenerViajeQueLoPago(viaje);
				modificarMontoPagoParcial(viajePagoParcial,
						viaje.getPagoParcial());
			}
		}
	}

	// Modificara la variable precioMaderaDescarga con
	// lo que se le debolvio al quitar el pago.
	// Luego se debe guardar los cambios en la base de datos.
	// Se debe modificar la cuenta corriente del proveedor.
	private void modificarMontoPagoParcial(Viaje viajePagoParcial,
			String pagoParcial) {
		BigDecimal tenia = new BigDecimal("0.00");
		BigDecimal paga = new BigDecimal("0.00");
		BigDecimal total = new BigDecimal("0.00");
		tenia = BigDecimal.valueOf(Double.valueOf(viajePagoParcial
				.getPrecioMaderaDescarga()));
		paga = BigDecimal.valueOf(Double.valueOf(pagoParcial));
		total = tenia.add(paga);
		viajePagoParcial.setPrecioMaderaDescarga(total.toString());
		new ViajeEndpoint().updateViaje(viajePagoParcial);
		new EmpresaEndpoint().modificarCuentaCorrienteNombre(
				viajePagoParcial.getProveedor(), pagoParcial);
	}

	// Retornara el viaje que realizo el pago parcial.
	private Viaje obtenerViajeQueLoPago(Viaje viaje) {
		Long id = Long.parseLong(viaje.getSaldo_descargador());
		return new ViajeEndpoint().getViaje(id);
	}

	// Se paga un viaje que estaba cargado pero no pagado.
	private void pagarViajeCargado(Viaje viaje) {
		// Asigno la diferencia que es el saldo del cliente.
		// La fecha de hoy.
		Date d = new Date();
		@SuppressWarnings("deprecation")
		String fecha = d.getDate() + "/" + (d.getMonth() + 1) + "/"
				+ (d.getYear() % 100);
		// Fin la fecha de hoy.
		// Multiplico las toneladas por el precio por tonelada
		BigDecimal bdTon = new BigDecimal("0.00");
		if (viaje.getKilosOrigenReales() == null) {
			viaje.setKilosOrigenReales(viaje.getKilos());
		}
		bdTon = BigDecimal.valueOf(Double.parseDouble(viaje
				.getKilosOrigenReales()));
		BigDecimal bdPrecioXTon = BigDecimal.valueOf(Double.parseDouble(viaje
				.getPrecio_kilo()));
		BigDecimal bdPago = bdTon.multiply(bdPrecioXTon);
		viaje.setPago(bdPago.toString());
		viaje.setFechaPagar_origen(fecha);
		// Le modifico el pago y la fecha de pago.
		new ViajeEndpoint().updateViaje(viaje);
		new EmpresaEndpoint().modificarCuentaCorrienteNombre(
				viaje.getProveedor(), viaje.getPagoParcial());
	}

	// Se carga el viaje y se paga.
	private void cargarPagarViaje(Viaje viaje) {
		// Aumenta la CC del fletero y del Descargador.
		// NO SE MODIFICA LA CC DEL PROVEEDOR.
		// Se da el valor negativo del costo del viaje al pago parcial.
		// 1. Se agrege el pago parcial.
		BigDecimal pagoParcial = new BigDecimal("0.00");
		Empresa prov = new EmpresaEndpoint().obtenerEmpresaByNombre(viaje
				.getProveedor());
		BigDecimal bdPrecioTon = BigDecimal.valueOf(Double.parseDouble(viaje
				.getPrecio_kilo()));
		BigDecimal ton = new BigDecimal("0.00");
		BigDecimal costoViaje = new BigDecimal("0.00");
		if (viaje.getKilosOrigenReales() == null) {
			viaje.setKilosOrigenReales(viaje.getKilos());
		} 
		ton = BigDecimal.valueOf(Double.parseDouble(viaje
				.getKilosOrigenReales()));
		costoViaje = bdPrecioTon.multiply(ton);
		costoViaje = costoViaje.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		if (Double.parseDouble(prov.getCuenta_corriente()) >= 0) {
			// El proveedor me debe plata.
			// Se agrega como pago parcial el costo del viaje de forma negativa.
			pagoParcial = costoViaje.multiply(BigDecimal.valueOf(Double
					.parseDouble("-1")));
			viaje.setPagoParcial(pagoParcial.toString());
			new ViajeEndpoint().updateViaje(viaje);
			// En este caso NO se modifica la CC del proveedor.
		} else {
			BigDecimal proveedorCC = new BigDecimal("0.00");// La cuenta
															// corriente del
															// proveedor
			BigDecimal saldoProveedorCC = new BigDecimal("0.00");// El saldo de
																	// la CC del
																	// proveedor.
			proveedorCC = BigDecimal.valueOf(Double.parseDouble(prov
					.getCuenta_corriente()));
			// Realizo la resta de lo que tiene en la CC el proveedor con el
			// costo del viaje.
			saldoProveedorCC = proveedorCC.add(costoViaje);
			if (saldoProveedorCC.doubleValue() >= 0) {
				// El saldo que tenia el proveedor NO es suficiente para pagar
				// el viaje.
				// por lo tanto el costo del viaje será el resto que tiene que
				// pagar.
				saldoProveedorCC = saldoProveedorCC.multiply(BigDecimal
						.valueOf(Double.parseDouble("-1")));
				viaje.setPagoParcial(saldoProveedorCC.toString());
				new ViajeEndpoint().updateViaje(viaje);
				// 2. Modificar la CC del Proveedor
				// Asigno la diferencia que es el saldo del cliente.
				proveedorCC = proveedorCC.multiply(BigDecimal.valueOf(Double
						.parseDouble("-1")));
				new EmpresaEndpoint().modificarCuentaCorrienteNombre(
						viaje.getProveedor(), proveedorCC.toString());
				// FIN 2.
			} else {
				// El saldo que tenia el proveedor alcanza para pagar el viaje.
				// El proveedor no me deberia pagar y por eso se pasa "0"
				viaje.setPagoParcial("0");
				new ViajeEndpoint().updateViaje(viaje);
				// 2. Modificar la CC del Proveedor
				// Asigno la diferencia que es el saldo del cliente.
				new EmpresaEndpoint().modificarCuentaCorrienteNombre(
						viaje.getProveedor(), costoViaje.toString());
				// FIN 2.
			}
		}
		// FIN 1.
		modificarCC(viaje.getFletero(), viaje.getPrecioXtonelada_flete(),
				viaje.getKilos());
		modificarCC(viaje.getNombre_descargador(),
				viaje.getMonto_descargador(), viaje.getKilos());
	}

	// Se carga el viaje y no se paga.
	private void cargarViaje(Viaje viaje, Boolean estabaPagoFletero, Boolean estabaPagoDescargador) {
		// Se aumenta la CC del proveedor, del fletero y del descargador.
		modificarCCProveedor(viaje);
		modificarCC(viaje.getFletero(), viaje.getPrecioXtonelada_flete(),
				viaje.getKilos());
		modificarCC(viaje.getNombre_descargador(),
				viaje.getMonto_descargador(), viaje.getKilos());

	}

	// PAGGI
	private void modificarCCProveedor(Viaje viaje) {
		// 1. Se agrege el pago parcial.
		BigDecimal pagoParcial = new BigDecimal("0.00");
		Empresa prov = new EmpresaEndpoint().obtenerEmpresaByNombre(viaje
				.getProveedor());
		BigDecimal bdPrecioTon = BigDecimal.valueOf(Double.parseDouble(viaje
				.getPrecio_kilo()));
		BigDecimal ton = new BigDecimal("0.00");
		BigDecimal costoViaje = new BigDecimal("0.00");
		if (viaje.getKilosOrigenReales() != null) {
			ton = BigDecimal.valueOf(Double.parseDouble(viaje
					.getKilosOrigenReales()));
		} else {
			ton = BigDecimal.valueOf(Double.parseDouble(viaje.getKilos()));
		}
		costoViaje = bdPrecioTon.multiply(ton);
		costoViaje = costoViaje.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		if (Double.parseDouble(prov.getCuenta_corriente()) >= 0) { // El
																	// proveedor
																	// me debe
																	// plata
			// Se agrega como pago parcial el costo del viaje de forma negativa.
			pagoParcial = costoViaje.multiply(BigDecimal.valueOf(Double
					.parseDouble("-1")));
			viaje.setPagoParcial(pagoParcial.toString());
			new ViajeEndpoint().updateViaje(viaje);
		} else {
			BigDecimal proveedorCC = new BigDecimal("0.00");// La cuenta
															// corriente del
															// proveedor
			BigDecimal saldoProveedorCC = new BigDecimal("0.00");// El saldo de
																	// la CC del
																	// proveedor.
			proveedorCC = BigDecimal.valueOf(Double.parseDouble(prov
					.getCuenta_corriente()));
			// Realizo la resta de lo que tiene en la CC el proveedor con el
			// costo del viaje.
			saldoProveedorCC = proveedorCC.add(costoViaje);
			if (saldoProveedorCC.doubleValue() >= 0) {
				// El saldo que tenia el proveedor no es suficiente para pagar
				// el viaje.
				// por lo tanto el costo del viaje será el resto que tiene que
				// pagar.
				saldoProveedorCC = saldoProveedorCC.multiply(BigDecimal
						.valueOf(Double.parseDouble("-1")));
				viaje.setPagoParcial(saldoProveedorCC.toString());
				new ViajeEndpoint().updateViaje(viaje);
			} else {
				// El saldo que tenia el proveedor alcanza para pagar el viaje.
				// El proveedor no me deberia pagar y por eso se pasa "0"
				viaje.setPagoParcial("0");
				new ViajeEndpoint().updateViaje(viaje);
			}
		}
		// FIN 1.
		// 2. Modificar la CC del Proveedor
		new EmpresaEndpoint().modificarCuentaCorrienteNombre(prov.getNombre(),
				costoViaje.toString());
		// FIN 2.
	}

	private void modificarCC_Iva(String cliente, String precioFleteroDescarga,
			String precioMaderaDescarga, String toneladas, String signo) {
		// Inicializo las variables
		BigDecimal precioMadera = new BigDecimal("0.00");
		BigDecimal montoFletero = new BigDecimal("0.00");
		BigDecimal iva = new BigDecimal("0.00");
		BigDecimal ton = new BigDecimal("0.00");
		BigDecimal CC = new BigDecimal("0.00");
		// Seteo los valores
		precioMadera = BigDecimal.valueOf(Double
				.parseDouble(precioMaderaDescarga));
		montoFletero = BigDecimal.valueOf(Double
				.parseDouble(precioFleteroDescarga));
		iva = BigDecimal.valueOf(Double.parseDouble("1.22"));
		ton = BigDecimal.valueOf(Double.parseDouble(toneladas));
		// Realizo el calculo
		CC = ton.multiply(precioMadera.add(montoFletero.multiply(iva)));
		// Dejo la CC en el signo que corresponda
		CC = CC.multiply(new BigDecimal(Double.parseDouble(signo)));
		CC = CC.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		new EmpresaEndpoint().modificarCuentaCorrienteNombre(cliente,
				CC.toString());
	}

	private void modificarCC(String empresa, String precioToneladas,
			String toneladas) {
		BigDecimal ton = new BigDecimal("0.00");
		BigDecimal precioTonelada = new BigDecimal("0.00");
		BigDecimal CC = new BigDecimal("0.00");
		ton = BigDecimal.valueOf(Double.parseDouble(toneladas));
		precioTonelada = BigDecimal
				.valueOf(Double.parseDouble(precioToneladas));
		CC = precioTonelada.multiply(ton);
		CC = CC.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		new EmpresaEndpoint().modificarCuentaCorrienteNombre(empresa,
				CC.toString());
	}

	@Override
	public List<Empresa> getListaEmpresasPorTipo(String tipo) {
		Collection<Empresa> empresas = new EmpresaEndpoint()
				.obtenerEmpresasPorTipo(tipo);
		ArrayList<Empresa> result = new ArrayList<Empresa>(empresas);
		return result;
	}

	@Override
	public List<Usuario> getListaUsuariosSeleccionadosByNombres(
			List<String> lstUsuario) {
		CollectionResponse<Usuario> consultaBD = new UsuarioEndpoint()
				.listUsuario(null, null);
		Collection<Usuario> usuarios = consultaBD.getItems();
		ArrayList<Usuario> consulta = new ArrayList<Usuario>(usuarios);
		ArrayList<Usuario> result = new ArrayList<Usuario>();
		for (int i = 0; i < consulta.size(); i++) {
			Usuario u = consulta.get(i);
			if (lstUsuario.contains(u.getNombre())) {
				result.add(u);
			}
		}

		return result;
	}

	@Override
	public String eliminarUsuariosSeleccionados(List<Usuario> usuarios) {
		String cadena = "";
		for (int i = 0; i < usuarios.size(); i++) {
			cadena += String.valueOf(usuarios.get(i).getId());
			cadena += ";";
		}
		System.out.println(cadena);
		new UsuarioEndpoint().eliminarUsuarios(cadena);
		return "Usuarios eliminados.";
	}

	@Override
	public String eliminarEmpresasSeleccionadas(List<Empresa> empresas) {
		String cadena = "";
		for (int i = 0; i < empresas.size(); i++) {
			cadena += String.valueOf(empresas.get(i).getId().toString());
			cadena += ";";
		}
		new EmpresaEndpoint().eliminarEmpresas(cadena);
		if (empresas.size() == 1)
			return "Se elimina la empresa.";
		else
			return "Se eliminaron las empresas.";
	}

	@Override
	public String eliminarViajesSeleccionados(List<Viaje> viaje) {
		String cadena = "";
		for (int i = 0; i < viaje.size(); i++) {
			cadena += String.valueOf(viaje.get(i).getId().toString());
			cadena += ";";
		}
		new ViajeEndpoint().eliminarViajes(cadena);
		return "Viajes eliminados.";
	}

	@Override
	public List<Empresa> getListaEmpresasSeleccionadasByNombres(
			List<String> lstEmpresa) {
		CollectionResponse<Empresa> consultaBD = new EmpresaEndpoint()
				.listEmpresa(null, null);
		Collection<Empresa> empresas = consultaBD.getItems();
		ArrayList<Empresa> consulta = new ArrayList<Empresa>(empresas);
		ArrayList<Empresa> result = new ArrayList<Empresa>();
		for (int i = 0; i < consulta.size(); i++) {
			Empresa u = consulta.get(i);
			if (lstEmpresa.contains(u.getNombre())) {
				result.add(u);
			}
		}
		return result;
	}

	@Override
	public List<Viaje> getListaViajesSeleccionadosById(List<String> lstViajes) {
		CollectionResponse<Viaje> consultaBD = new ViajeEndpoint().listViaje(
				null, null);
		Collection<Viaje> viajes = consultaBD.getItems();
		ArrayList<Viaje> consulta = new ArrayList<Viaje>(viajes);
		ArrayList<Viaje> result = new ArrayList<Viaje>();
		for (int i = 0; i < consulta.size(); i++) {
			Viaje u = consulta.get(i);
			if (lstViajes.contains(String.valueOf(u.getId()))) {
				result.add(u);
			}
		}

		return result;
	}

	@Override
	public String facturarViajesSeleccionados(List<Viaje> viajes, String factura) {
		String cadena = "";
		for (int i = 0; i < viajes.size(); i++) {
			cadena += String.valueOf(viajes.get(i).getId().toString());
			cadena += ";";
		}
		new ViajeEndpoint().facturarViajesSeleccionados(cadena, factura);
		return "Se factura correctamente.";
	}

	@Override
	public String pagarCobrarEmpresa(Empresa empresa, String monto,
			String accion) {
		String mje;
		List<String> lstResult = new ArrayList<String>();
		List<Viaje> pendientes = new ArrayList<Viaje>();
		String strViajesPendientes = "";

		// Se debe modificar la CC de la empresa de forma negativa.
		BigDecimal CC = new BigDecimal("0.00");
		CC = BigDecimal.valueOf(Double.parseDouble(monto));
		CC = CC.multiply(new BigDecimal(Double.parseDouble("-1")));
		CC = CC.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		new EmpresaEndpoint().modificarCuentaCorrienteNombre(
				empresa.getNombre(), CC.toString());

		// Se va a crear un nuevo viaje que es el que solo contiene el monto a
		// cobrar/pagar para
		// que luego se vea en el informe.
		Viaje viaje = new ViajeEndpoint().obtenerMaxId();
		viaje.setId(viaje.getId() + 1);
		// La fecha de hoy.
		Date d = new Date();
		@SuppressWarnings("deprecation")
		String fecha = d.getDate() + "/" + (d.getMonth() + 1) + "/"
				+ (d.getYear() % 100);
		// Fin la fecha de hoy.
		viaje.setFecha(fecha);
		viaje.setFacturado("pago");
		viaje.setPendienteCarga(false);
		viaje.setPendienteDePago(false);
		viaje.setPendienteDeCobro(false);
		viaje.setPendienteDeDescarga(false);
		// Se va a guardar el monto inicial en la variable Observaciones.
		viaje.setObservaciones(monto);
		Double montoCobrarPagar = Double.parseDouble(monto);
		montoCobrarPagar = montoCobrarPagar * -1;
		if (accion.equals("cobrar")) {
			mje = "Se cobra al cliente.";
			viaje.setCliente(empresa.getNombre());
			viaje.setProveedor("");
			viaje.setCobroParcial(String.valueOf(montoCobrarPagar));
			// Se deben obtener todos los viajes pendientes de cobro para que se
			// vayan pagando.
			pendientes = (List<Viaje>) new ViajeEndpoint()
					.obtenerViajesPendientesDeCobros(empresa.getNombre());
		} else {
			mje = "Se paga al proveedor.";
			viaje.setProveedor(empresa.getNombre());
			viaje.setCliente("");
			viaje.setPagoParcial(String.valueOf(montoCobrarPagar));
			// Se deben obtener todos los viajes pendientes de pago para que se
			// vayan pagando.
			pendientes = (List<Viaje>) new ViajeEndpoint()
					.obtenerViajesPendientesDePago(empresa.getNombre());

		}
		if (pendientes.size() > 0) {
			for (int i = 0; i < pendientes.size(); i++) {
				strViajesPendientes += String.valueOf(pendientes.get(i).getId()
						.toString());
				strViajesPendientes += ";";
			}
			// Se pagara/cobraran los viajes pendientes.
			lstResult = new ViajeEndpoint().pagarCobrarViajesPendientes(
					strViajesPendientes, monto, accion, viaje.getId()
							.toString());
			// Esto maneja el monto pagado/cobrado.
			if (lstResult.size() > 0) {
				viaje.setPagoParcial(lstResult.get(0));
				viaje.setPrecioMaderaDescarga(lstResult.get(0));
			}
		} else {
			viaje.setPrecioMaderaDescarga(String.valueOf(montoCobrarPagar));
		}
		new ViajeEndpoint().insertViaje(viaje);
		return mje;
	}

	// Obtengo todas las facturas de la empresa propia
	// LUEGO Para cada factura, obtengo las toneladas totales, fecha ultimo
	// viaje,
	// precio del flete SIN IVA de cualuiera de ellos, precio final de la madera
	// de cualquiera de ellos y el total de pagos recibidos
	// CADA STRING QUE SE DEVUELVA VA A TENER SEPARADO POR ";" LOS DATOS ARRIBA
	// MENCIONADOS.
	@Override
	public List<String> getDatosEmpresaPropia(String nombre) {
		List<String> listaDatos = new ArrayList<String>();
		List<String> facturas = new ViajeEndpoint()
				.obtenerFacturasDeEmpresaPropia(nombre);
		for (int i = 0; i < facturas.size(); i++) {
			// Para cada factura, obtengo las toneladas totales, fecha ultimo
			// viaje,
			// precio del flete SIN IVA de cualuiera de ellos, precio final de
			// la madera
			// de cualquiera de ellos y el total de pagos recibidos
			List<String> res = new ViajeEndpoint().obtenerDatosFactura(facturas
					.get(i));
			String datos = res.get(0);
			listaDatos.add(datos);
		}
		return listaDatos;
	}

	@Override
	public String limpiarDatos() {
		new ViajeEndpoint().limpiarDatos();
		new EmpresaEndpoint().limpiarDatos();
		return "Ok";
	}

	@Override
	public String obtenerCCdelCliente(String cliente) {
		List<String> res = new EmpresaEndpoint().obtenerCCdelCliente(cliente);
		return res.get(0);
	}

	@Override
	public String ModificarViajeSaldoAPagar(Viaje vje, String pagar,
			String cobrarCiente, Boolean estabaCargado, Boolean estabaPago,
			Boolean estabaDescargado, Boolean estabaCobrado,
			Boolean estabaPagoFlete, Boolean estabaPagoDescargador) {
		vje.setCobroParcial(cobrarCiente);
		// Primero modifico el viaje.
		Viaje viaje = new ViajeEndpoint().updateViaje(vje);
		// Luego modifico las CC que correspondan.
		// PARA LA DESCARGA
		if (!estabaDescargado) {
			if (!estabaCobrado) {
				// SI SE DESCARGA Y SE COBRA.
				if (!viaje.isPendienteDeDescarga()
						&& !viaje.isPendienteDeCobro()) {
					// DANIEL
					// Falta revisar el saldo del cliente.
					descargarCobrarViajeSaldo(viaje.getCliente(), pagar);
				}
			}
		}
		return "Ok";
	}

	// Se paga al cliente el monto correspondiente.
	private void descargarCobrarViajeSaldo(String cliente, String pagar) {
		new EmpresaEndpoint().modificarCuentaCorrienteNombre(cliente, pagar);
	}
}
