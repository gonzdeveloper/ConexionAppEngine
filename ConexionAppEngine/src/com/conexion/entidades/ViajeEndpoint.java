package com.conexion.entidades;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@Api(name = "viajeendpoint", namespace = @ApiNamespace(ownerDomain = "conexion.com", ownerName = "conexion.com", packagePath = "entidades"))
public class ViajeEndpoint {

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listViaje")
	public CollectionResponse<Viaje> listViaje(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Viaje> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Viaje.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Viaje>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (Viaje obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Viaje> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method.
	 *
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getViaje")
	public Viaje getViaje(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Viaje viaje = null;
		try {
			viaje = mgr.getObjectById(Viaje.class, id);
		} finally {
			mgr.close();
		}
		return viaje;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
	 *
	 * @param viaje
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertViaje")
	public Viaje insertViaje(Viaje viaje) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsViaje(viaje)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(viaje);
		} finally {
			mgr.close();
		}
		return viaje;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
	 *
	 * @param viaje
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateViaje")
	public Viaje updateViaje(Viaje viaje) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsViaje(viaje)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(viaje);
		} finally {
			mgr.close();
		}
		return viaje;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 *
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeViaje")
	public void removeViaje(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Viaje viaje = mgr.getObjectById(Viaje.class, id);
			mgr.deletePersistent(viaje);
		} finally {
			mgr.close();
		}
	}

	private boolean containsViaje(Viaje viaje) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Viaje.class, viaje.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

	// //------------------------------------Inicio MisCambios
	@ApiMethod(name = "obtenerMaxId")
	public Viaje obtenerMaxId() {

		Collection<Viaje> viajes = this.listViaje(null, null).getItems();
		Viaje viaje = new Viaje();
		if (!viajes.isEmpty())
			viaje.setId(((Viaje) (viajes.toArray())[viajes.size() - 1]).getId());
		else
			viaje.setId(Long.valueOf(0));
		return viaje;
	}

	@ApiMethod(name = "obtenerViajesCargaPendiente")
	public Collection<Viaje> obtenerViajesCargaPendiente() {
		// PersistenceManager mgr = getPersistenceManager();

		Collection<Viaje> viajes = listViaje(null, null).getItems();
		Collection<Viaje> aux = new java.util.ArrayList<Viaje>();
		for (Viaje viaje : viajes) {
			if (viaje.isPendienteDeCobro() || viaje.isPendienteDePago()
					|| !viaje.isSePagoFletero() || !viaje.isSePagoDescargador())
				aux.add(viaje);
		}
		// Query q = mgr.newQuery(Viaje.class,
		// "(pendienteDeCobro == "+true+" || pendienteDePago == "+true+")");

		return aux;
	}

	@ApiMethod(name = "obtenerViajesDescargaPendiente")
	public Collection<Viaje> obtenerViajesDescargaPendiente() {
		// PersistenceManager mgr = getPersistenceManager();
		// Query q = mgr.newQuery(Viaje.class,
		// "pendienteCarga == "+false+" && pendienteDeCobro =="+true);
		Collection<Viaje> viajes = listViaje(null, null).getItems();
		Collection<Viaje> aux = new java.util.ArrayList<Viaje>();
		for (Viaje viaje : viajes) {
			if (!viaje.isPendienteCarga()
					&& (viaje.isPendienteDeCobro() || viaje.isPendienteDePago()
							|| !viaje.isSePagoFletero() || !viaje

					.isSePagoDescargador()))
				aux.add(viaje);
		}
		return aux;
	}

	@ApiMethod(name = "obtenerViajesPorFletero")
	public Collection<Viaje> obtenerViajesPorFletero(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Query q = mgr.newQuery(Viaje.class, "usuarioResponsable ==" + id + "");
		Collection<Viaje> viajes = (Collection<Viaje>) q.execute();
		return viajes;
	}

	@ApiMethod(name = "modificarFechaDePagoMontoCuentaCorrienteORIGEN")
	public void modificarFechaDePagoMontoCuentaCorrienteORIGEN(
			@Named("id") Long id,
			@Named("fechaPagar_origen") String fechaPagar_origen,
			@Named("pago") String pago, @Named("kilos") String kilos,
			@Named("fechaDeCarga") String fechaDeCarga,
			@Named("pendienteDePago") boolean pendienteDePago) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Viaje viaje = mgr.getObjectById(Viaje.class, id);
			viaje.setPendienteCarga(false);
			viaje.setFechaDeCarga(fechaDeCarga);
			viaje.setPendienteDePago(pendienteDePago);
			viaje.setFechaPagar_origen(fechaPagar_origen);
			viaje.setPago(pago);
			viaje.setKilos(kilos);
			mgr.makePersistent(viaje);
		} finally {
			mgr.close();
		}
	}

	@ApiMethod(name = "modificarFechaDePagoMontoCuentaCorrienteDESTINO")
	public void modificarFechaDePagoMontoCuentaCorrienteDESTINO(
			@Named("id") Long id,
			@Named("fechaPagar_flete") String fechaPagar_origen,
			@Named("pago_flete") String pago_flete,
			@Named("kilos") String kilos,
			@Named("fechaDeDescarga") String fechaDeDescarga,
			@Named("pendienteDeCobro") boolean pendienteDeCobro) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Viaje viaje = mgr.getObjectById(Viaje.class, id);
			viaje.setPendienteDeDescarga(false);
			viaje.setFechaDeDescarga(fechaDeDescarga);
			viaje.setPendienteDeCobro(pendienteDeCobro);
			viaje.setFechaPago_flete(fechaPagar_origen);
			viaje.setPago_flete(pago_flete);
			viaje.setKilos(kilos);
			mgr.makePersistent(viaje);
		} finally {
			mgr.close();
		}
	}

	// Se debe modificar el n\FAmero de factura de todos los viajes de la lista.
	@ApiMethod(name = "facturarViajesSeleccionados")
	public void facturarViajesSeleccionados(@Named("cadena") String cadena,
			@Named("factura") String factura) {
		String[] viajes = cadena.split(";");
		PersistenceManager mgr = getPersistenceManager();
		try {
			for (int i = 0; i < viajes.length; i++) {
				Viaje viaje = mgr.getObjectById(Viaje.class,
						Long.parseLong(viajes[i]));
				viaje.setFacturado(factura);
				mgr.makePersistent(viaje);
			}
			;
		} finally {
			mgr.close();
		}
	}

	// TODO falta el fletero y el descargador.
	@ApiMethod(name = "eliminarViajes")
	public void eliminarViajes(@Named("cadena") String cadena) {
		String[] viajes = cadena.split(";");
		PersistenceManager mgr = getPersistenceManager();
		try {
			for (int i = 0; i < viajes.length; i++) {
				Viaje viaje = mgr.getObjectById(Viaje.class,
						Long.parseLong(viajes[i]));
				if (!viaje.isPendienteDePago() && !viaje.isPendienteDeCobro()) {
					// En caso de que este completado
					// Se debe verificar si fue pagado/cobrado por un viaje
					// fantasma.
					if (!fuePagadoPagoParcial(String.valueOf(viaje.getId()))
							&& !fueCobradoCobroParcial(String.valueOf(viaje
									.getId()))) {
						// No se modifica ninguna cuenta corriente.
					} else {
						if (fuePagadoPagoParcial(String.valueOf(viaje.getId()))) {
							// Si el viaje fue pagado por un pago parcial.
							// TODO se debe restar el saldo del pago parcial.
							// Estos pueden ser uno o mas pagos parciales.
							List<Viaje> viajesFantasmas = obtenerViajeFantasma(
									String.valueOf(viaje.getId()), "pagado");
							// TODO Se debe agregar el pagoParcial del viaje
							// original
							// al saldo del viaje fantasma.
						}
						if (fueCobradoCobroParcial(String
								.valueOf(viaje.getId()))) {
							// Si el viaje fue cobrado por un cobro parcial.
							// TODO se debe restar el saldo del cobro parcial.
							// Estos pueden ser uno o mas cobros parciales.
							List<Viaje> viajesFantasmas = obtenerViajeFantasma(
									String.valueOf(viaje.getId()), "cobrado");
							// TODO Se debe agregar el cobroParcial del viaje
							// original
							// al saldo del viaje fantasma.
						}
					}
				} else {// Cuando el viaje NO fue completado.
					if (!viaje.isPendienteCarga() && viaje.isPendienteDePago()) {
						// Si el viaje fue cargado pero no pago.
						// Se debe disminuir la CC del proveedor
						// con lo que tiene en pagoParcial.
						new EmpresaEndpoint().modificarCuentaCorrienteNombre(
								viaje.getProveedor(), viaje.getPagoParcial());
					} else if (!viaje.isPendienteCarga()
							&& !viaje.isPendienteDePago()) {
						// Si el viaje fue cargado y pago.
						// NO SE MODIFICA LA CC DEL PROVEEDOR.
					}
					if (!viaje.isPendienteDeDescarga()
							&& viaje.isPendienteDeCobro()) {
						// Si el viaje fue descargado pero no cobrado.
						// Se debe disminuir la CC del cliente
						// con lo que tiene en cobroParcial.
						new EmpresaEndpoint().modificarCuentaCorrienteNombre(
								viaje.getCliente(), viaje.getCobroParcial());
					} else if (!viaje.isPendienteDeDescarga()
							&& !viaje.isPendienteDeCobro()) {
						// Si el viaje fue descargado y cobrado.
						// NO SE MODIFICA LA CC DEL CLIENTE.
					}
					// Cuando el viaje no fue completado.
					if (fuePagadoPagoParcial(String.valueOf(viaje.getId()))) {
						// Si el viaje fue pagado por un pago parcial.
						// TODO se debe sumar el saldo del pago parcial.
						// Estos pueden ser uno o mas pagos parciales.
						List<Viaje> viajesFantasmas = obtenerViajeFantasma(
								String.valueOf(viaje.getId()), "pagado");
					}
					if (fueCobradoCobroParcial(String.valueOf(viaje.getId()))) {
						// Si el viaje fue cobrado por un cobro parcial.
						// TODO se debe sumar el saldo del cobro parcial.
						// Estos pueden ser uno o mas cobros parciales.
						List<Viaje> viajesFantasmas = obtenerViajeFantasma(
								String.valueOf(viaje.getId()), "cobrado");
					}

				}
				// Se debe controlar si fue pagado al fletero y al descargador.
				// Para el fletero.
				String pagoFletero = calcularPagoFletero(viaje);
				if (!viaje.isSePagoFletero()) {
					// Si no se paga al fletero se debe restar de su CC.
					pagoFletero = cambiarSigno(pagoFletero);
					new EmpresaEndpoint().modificarCuentaCorrienteNombre(
							viaje.getFletero(), pagoFletero);
				}
				// Para el descargador.
				String pagoDescargador = calcularPagoDescargador(viaje);
				if (!viaje.isSePagoDescargador()) {
					// Si no se paga al descargador se debe restar de su CC.
					pagoDescargador = cambiarSigno(pagoDescargador);
					new EmpresaEndpoint().modificarCuentaCorrienteNombre(
							viaje.getNombre_descargador(), pagoDescargador);
				}
				// Se elimina el viaje.
				mgr.deletePersistent(viaje);
			}
		} finally {
			mgr.close();
		}
	}

	// Retorna lo que se le debe al descargdor.
	private String calcularPagoDescargador(Viaje viaje) {
		if (viaje.getKilosOrigenReales() == null) {
			viaje.setKilosOrigenReales(viaje.getKilos());
		}
		BigDecimal bdPrecioXTonDescargador = new BigDecimal("0.00");
		BigDecimal bdTon = new BigDecimal("0.00");
		BigDecimal bdMontoDescargador = new BigDecimal("0.00");
		bdPrecioXTonDescargador = BigDecimal.valueOf(Double.parseDouble(viaje
				.getMonto_descargador()));
		bdTon = BigDecimal.valueOf(Double.parseDouble(viaje
				.getKilosOrigenReales()));
		bdMontoDescargador = bdPrecioXTonDescargador.multiply(bdTon);

		return bdMontoDescargador.toString();
	}

	// Cambia el signo de un string.
	private String cambiarSigno(String numero) {
		BigDecimal bdNum = new BigDecimal("0.00");
		bdNum = BigDecimal.valueOf(Double.valueOf(numero));
		bdNum = BigDecimal.valueOf(-1);
		return bdNum.toString();
	}

	// Retorna lo que se le debe al fletero.
	private String calcularPagoFletero(Viaje viaje) {
		if (viaje.getKilosOrigenReales() == null) {
			viaje.setKilosOrigenReales(viaje.getKilos());
		}
		BigDecimal bdPrecioXTonFletero = new BigDecimal("0.00");
		BigDecimal bdTon = new BigDecimal("0.00");
		BigDecimal bdMontoFletero = new BigDecimal("0.00");
		if (viaje.getPrecioFleteroDescarga() == null)
			bdPrecioXTonFletero = BigDecimal.valueOf(Double.parseDouble(viaje
					.getPrecioXtonelada_flete()));
		else
			bdPrecioXTonFletero = BigDecimal.valueOf(Double.parseDouble(viaje
					.getPrecioFleteroDescarga()));
		bdTon = BigDecimal.valueOf(Double.parseDouble(viaje
				.getKilosOrigenReales()));
		// Lo que se le debe al Fletero.
		bdMontoFletero = bdPrecioXTonFletero.multiply(bdTon);

		return bdMontoFletero.toString();
	}

	// Retorna el viaje fantasma que se utilizo para pagar o cobrar el viaje.
	private List<Viaje> obtenerViajeFantasma(String idViaje, String tipo) {
		List<Viaje> viajeFantasma = new FinanzasEndpoint()
				.obtenerViajeFantasma(idViaje, tipo);
		return viajeFantasma;
	}

	// Retorna true si el viaje fue cobrado por un cobro parcial.
	private boolean fueCobradoCobroParcial(String idViaje) {
		return new FinanzasEndpoint().fueCobradoCobroParcial(idViaje);
	}

	// Retorna true si el viaje fue pagado por un pago parcial.
	private boolean fuePagadoPagoParcial(String idViaje) {
		return new FinanzasEndpoint().fuePagadoPagoParcial(idViaje);
	}

	@ApiMethod(name = "obtenerViajesPendientesDeCobros")
	public Collection<Viaje> obtenerViajesPendientesDeCobros(
			@Named("cliente") String cliente) {
		PersistenceManager mgr = getPersistenceManager();
		Query q = mgr.newQuery(Viaje.class, "pendienteDeCobro ==" + true
				+ " && pendienteDeDescarga ==" + false + " && cliente == '"
				+ cliente + "'");
		Collection<Viaje> viajes = (Collection<Viaje>) q.execute();
		return viajes;
	}

	@ApiMethod(name = "obtenerViajesPendientesDePago")
	public Collection<Viaje> obtenerViajesPendientesDePago(
			@Named("proveedor") String proveedor) {
		PersistenceManager mgr = getPersistenceManager();
		Query q = mgr.newQuery(Viaje.class, "pendienteDePago ==" + true
				+ " && pendienteCarga ==" + false + " && proveedor == '"
				+ proveedor + "'");
		Collection<Viaje> viajes = (Collection<Viaje>) q.execute();
		return viajes;
	}

	// Obtengo todas las facturas de la empresa propia
	@ApiMethod(name = "obtenerFacturasDeEmpresaPropia")
	public List<String> obtenerFacturasDeEmpresaPropia(
			@Named("empresa") String empresa) {
		List<String> facturas = new ArrayList<String>();

		PersistenceManager mgr = getPersistenceManager();
		Query q = mgr.newQuery(Viaje.class, "empresaPropia == '" + empresa
				+ "'");
		@SuppressWarnings("unchecked")
		List<Viaje> viajes = (List<Viaje>) q.execute();
		// Para cada viaje obtengo la factura y luego si no est\E1 en la lista
		// la
		// agrego.
		for (int i = 0; i < viajes.size(); i++) {
			String factura = viajes.get(i).getFacturado();
			if (!facturas.contains(factura) && !factura.equals(""))
				facturas.add(factura);
		}
		return facturas;
	}

	// Para cada factura, obtengo las toneladas totales, fecha ultimo viaje,
	// precio del flete SIN IVA de cualuiera de ellos, precio final de la madera
	// de cualquiera de ellos y el total de pagos recibidos
	// CADA STRING QUE SE DEVUELVA VA A TENER SEPARADO POR ";" LOS DATOS ARRIBA
	// MENCIONADOS.

	@ApiMethod(name = "obtenerDatosFactura")
	public List<String> obtenerDatosFactura(@Named("factura") String factura) {
		List<String> res = new ArrayList<String>();
		String datos = "";
		float toneladas = 0;
		BigDecimal pagosRecibidos = new BigDecimal("0.00");
		pagosRecibidos = BigDecimal.valueOf(Double.parseDouble("0"));
		String fechaPagosRecibidos = "";

		PersistenceManager mgr = getPersistenceManager();
		Query q = mgr.newQuery(Viaje.class, "facturado == '" + factura + "'");
		List<Viaje> viajes = (List<Viaje>) q.execute();
		for (int i = 0; i < viajes.size(); i++) {
			Viaje viaje = viajes.get(i);
			// para cada viaje, sumo las toneladas descargadas.
			if (viaje.getKilosOrigenReales() != null) {
				float ton = Float.parseFloat(viaje.getKilosOrigenReales());
				toneladas += ton;
			}
			if (!viaje.isPendienteDeCobro()) {
				// Aca va la cuenta con lo que se cobra al cliente.
				// Hago el calculo de cuanto pago el cliente.
				// Inicializo las variables
				BigDecimal precioMadera = new BigDecimal("0.00");
				BigDecimal montoFletero = new BigDecimal("0.00");
				BigDecimal iva = new BigDecimal("0.00");
				BigDecimal ton = new BigDecimal("0.00");
				BigDecimal CC = new BigDecimal("0.00");
				// Seteo los valores
				precioMadera = BigDecimal.valueOf(Double.parseDouble(viaje
						.getPrecioMaderaDescarga()));
				montoFletero = BigDecimal.valueOf(Double.parseDouble(viaje
						.getPrecioFleteroDescarga()));
				iva = BigDecimal.valueOf(Double.parseDouble("1.22"));
				ton = BigDecimal.valueOf(Double.parseDouble(viaje
						.getKilosOrigenReales()));
				// Realizo el calculo
				CC = ton.multiply(precioMadera.add(montoFletero.multiply(iva)));
				CC = CC.setScale(2, BigDecimal.ROUND_UNNECESSARY);
				pagosRecibidos = pagosRecibidos.add(CC);
				fechaPagosRecibidos = viaje.getFechaPago_flete();
			}
		}
		// Las toneladas.
		datos += String.valueOf(toneladas);
		datos += ";";
		// La fecha del ultimo viaje.
		datos += viajes.get(viajes.size() - 1).getFecha();
		datos += ";";
		// El precio del flete SIN IVA.
		datos += viajes.get(viajes.size() - 1).getPrecioXtonelada_flete();
		datos += ";";
		// El precio final de la madera.
		datos += viajes.get(viajes.size() - 1).getPrecioMaderaDescarga();
		datos += ";";
		// Los pagos recibidos.
		datos += String.valueOf(pagosRecibidos);
		datos += ";";
		// El numero de factura
		datos += viajes.get(viajes.size() - 1).getFacturado();
		datos += ";";
		// El cliente
		datos += viajes.get(viajes.size() - 1).getCliente();
		datos += ";";
		// La fecha de los pagos recibidos.
		datos += String.valueOf(fechaPagosRecibidos);
		datos += ";";
		res.add(0, datos);
		return res;
	}

	@ApiMethod(name = "limpiarDatos")
	public void limpiarDatos() {
		List<Viaje> viajes = new ArrayList<Viaje>();
		viajes = (List<Viaje>) this.listViaje(null, null).getItems();
		PersistenceManager mgr = getPersistenceManager();
		try {
			for (int i = 0; i < viajes.size(); i++) {
				Viaje vje = viajes.get(i);
				mgr.makePersistent(vje);
			}
		} finally {
			mgr.close();
		}
	}

	// Se agregan los pagos o cobros parciales a la tabla facturacion.
	@ApiMethod(name = "pagarCobrarViajesPendientes")
	public List<String> pagarCobrarViajesPendientes(
			@Named("pendientes") String pendientes,
			@Named("monto") String monto, @Named("accion") String accion,
			@Named("viajeInvolucrado") String viajeInvolucrado) {
		List<String> result = new ArrayList<String>();
		String[] viajes = pendientes.split(";");
		Double montoD = Double.parseDouble(monto);
		PersistenceManager mgr = getPersistenceManager();
		try {
			int i = 0;
			if (accion.equals("cobrar")) {
				// Voy cobrando los viajes pendientes de cobro mientras el monto
				// sea > 0
				while ((i < viajes.length) && (montoD > 0)) {
					if (viajes[i] != null && viajes[i] != "") {
						Viaje viaje = mgr.getObjectById(Viaje.class,
								Long.parseLong(viajes[i]));
						// Aca va la cuenta con lo que se cobra al cliente.
						// Hago el calculo de cuanto pago el cliente.
						// Inicializo las variables
						BigDecimal precioMadera = new BigDecimal("0.00");
						BigDecimal montoFletero = new BigDecimal("0.00");
						BigDecimal iva = new BigDecimal("0.00");
						BigDecimal ton = new BigDecimal("0.00");
						BigDecimal totalCobrar = new BigDecimal("0.00");
						Double debe = (double) 0;
						// Seteo los valores
						precioMadera = BigDecimal.valueOf(Double
								.parseDouble(viaje.getPrecioMaderaDescarga()));
						montoFletero = BigDecimal.valueOf(Double
								.parseDouble(viaje.getPrecioFleteroDescarga()));
						iva = BigDecimal.valueOf(Double.parseDouble("1.22"));
						ton = BigDecimal.valueOf(Double.parseDouble(viaje
								.getKilosOrigenReales()));
						// Realizo el calculo
						totalCobrar = ton.multiply(precioMadera
								.add(montoFletero.multiply(iva)));
						totalCobrar = totalCobrar.setScale(2,
								BigDecimal.ROUND_UNNECESSARY);
						// Realizo la resta de lo que debe cobrar con el monto
						// cobrado.
						debe = totalCobrar.doubleValue();
						// En caso de que sea mayor el monto recibido que lo que
						// se debe, se modifica el montoD
						// y se deja el viaje como cobrado.
						if (montoD > debe) {
							montoD -= debe;
						} else {
							debe -= montoD;
							// Guardo el monto para luego crear el nuevo viaje.
							result.add(String.valueOf(montoD));
							// Le doy el valor -1 al monto para salir del while.
							montoD = (double) -1;
						}
						// Manejo el cobro como negativo
						debe = debe * -1;
						viaje.setCobroParcial(String.valueOf(debe));
						// Se utiliza el Saldo_descargador para hacer la
						// referencia.
						viaje.setSaldo_descargador(viajeInvolucrado);
						// En Observaciones se tiene que el viaje fue cobrado
						// mediante un cobro parcial.
						viaje.setObservaciones("cobroParcial");
						// Se agrega un registro del cobro parcial en la tabla
						// finanzas.
						Finanzas finanzas = new FinanzasEndpoint()
								.obtenerMaxId();
						finanzas.setId(finanzas.getId() + 1);
						finanzas.setIdViaje(viaje.getId().toString());
						finanzas.setIdViajeFantasma(viajeInvolucrado);
						finanzas.setTipo("cobrado");
						new FinanzasEndpoint().insertFinanzas(finanzas);
						mgr.makePersistent(viaje);
						i++;
					}
				}
			} else {
				// CUANDO SE PAGA UN VIAJE
				// Voy pagando los viajes pendientes de pago mientras el monto
				// sea > 0
				while ((i < viajes.length) && (montoD > 0)) {
					if (viajes[i] != null && viajes[i] != "") {
						Viaje viaje = mgr.getObjectById(Viaje.class,
								Long.parseLong(viajes[i]));
						// Calculo del monto que son las toneladas por el
						// precio
						// por tonelada original.
						// Inicializo todos los BigDecimal para darle
						BigDecimal bdTon = new BigDecimal("0.00");
						BigDecimal bdPrecioXton = new BigDecimal("0.00");
						BigDecimal bdMonto = new BigDecimal("0.00");
						// Obtengo los valores para luego realizar el calculo.
						if (viaje.getKilosOrigenReales() == null) {
							viaje.setKilosOrigenReales(viaje.getKilos());
						}
						bdTon = BigDecimal.valueOf(Double.parseDouble(viaje
								.getKilosOrigenReales()));
						bdPrecioXton = BigDecimal.valueOf(Double
								.parseDouble(viaje.getPrecio_kilo()));
						bdMonto = bdPrecioXton.multiply(bdTon);
						// En caso de que sea mayor el monto pagado que lo que
						// debi,
						// se modifica el montoD
						// y se deja el viaje como pagado.
						// La fecha de hoy.
						Date d = new Date();
						@SuppressWarnings("deprecation")
						String fecha = d.getDate() + "/" + (d.getMonth() + 1)
								+ "/" + (d.getYear() % 100);
						// Fin la fecha de hoy.
						// Se debe revisar lo que tenia en la CC el
						// proveedor
						// para asi saber cuanto debo.
						Double debo = (double) 0;
						debo = calcularDeudaCobrar(viaje, bdMonto.doubleValue());
						if (montoD >= debo) {
							montoD -= debo;
						} else {
							debo -= montoD;
							// Guardo el monto para luego crear el nuevo viaje.
							result.add(String.valueOf(montoD));
							// Le doy el valor -1 al monto para salir del while.
							montoD = (double) -1;
						}
						viaje.setPendienteDePago(false);
						// En Observaciones se tiene que el viaje fue pagado
						// mediante un pago parcial.
						// Se utiliza el Saldo_descargador para hacer la
						// referencia.
						if (debo != 0) {
							debo = debo * -1;
						}
						viaje.setSaldo_descargador(viajeInvolucrado);
						viaje.setObservaciones("pagoParcial");
						viaje.setPagoParcial(String.valueOf(debo));
						viaje.setFechaPagar_origen(fecha);
						// Se agrega un registro del pago parcial en la tabla
						// finanzas.
						Finanzas finanzas = new FinanzasEndpoint()
								.obtenerMaxId();
						finanzas.setId(finanzas.getId() + 1);
						finanzas.setIdViaje(viaje.getId().toString());
						finanzas.setIdViajeFantasma(viajeInvolucrado);
						finanzas.setTipo("pagado");
						new FinanzasEndpoint().insertFinanzas(finanzas);
						mgr.makePersistent(viaje);
						i++;
					}
				}
			}
		} finally {
			mgr.close();
		}
		if (montoD != -1) {
			if (montoD > 0) {
				montoD = montoD * -1;
			}
			result.add(String.valueOf(montoD));
		}
		return result;
	}

	// Se revisa la cuenta corriente del proveedor para calcular cuanto hay que
	// pagarle.
	private Double calcularDeudaCobrar(Viaje viaje, double costoViaje) {
		Double resultado;
		// Obtengo el proveedor
		Empresa prov = new EmpresaEndpoint().obtenerEmpresaByNombre(viaje
				.getProveedor());
		// El proveedor me debe plata.
		if (Double.parseDouble(prov.getCuenta_corriente()) >= 0) {
			// En este caso el pago parcial sera el costo total del viaje.
			resultado = costoViaje;
		} else {// CUANDO LA CC ES NEGATIVA
			// El proveedor tiene plata a favor.
			BigDecimal proveedorCC = new BigDecimal("0.00");
			BigDecimal saldoProveedorCC = new BigDecimal("0.00");
			proveedorCC = BigDecimal.valueOf(Double.parseDouble(prov
					.getCuenta_corriente()));
			// Realizo la resta de lo que tiene en la CC el proveedor con el
			// costo del viaje.
			saldoProveedorCC = proveedorCC.add(BigDecimal.valueOf(costoViaje));
			if (saldoProveedorCC.doubleValue() >= 0) {
				// El saldo que tenia el proveedor alcanza para pagar el viaje.
				// El proveedor no me deberia pagar y por eso se pasa "0"
				resultado = saldoProveedorCC.doubleValue();
			} else {
				// El saldo que tenia el proveedor NO es suficiente para pagar
				// el viaje.
				// por lo tanto el costo del viaje sera el resto que tiene que
				// pagar.
				resultado = (double) 0;
			}
		}
		return resultado;
	}
	// //------------------------------------Fin MisCambios
}