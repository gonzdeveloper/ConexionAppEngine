package com.conexion.ventanas;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Empresa;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class ListarEmpresasPropias extends Composite {

	private static ListarEmpresasPropiasUiBinder uiBinder = GWT
			.create(ListarEmpresasPropiasUiBinder.class);
	@UiField
	Button btnSelEmpresa;
	@UiField
	ListBox cbEmpresas;
	@UiField
	DateBox dbFechaDesde;
	@UiField
	DateBox dbFechaHasta;
	@UiField
	Button btnFiltrar;
	@UiField
	FlexTable tablaDatosEmpresaPropia;
	@UiField
	DialogBox dialogBox;
	@UiField
	Label lblMje;
	Empresa empresaSel;
	Date fechaDesde;
	Date fechaHasta;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface ListarEmpresasPropiasUiBinder extends
			UiBinder<Widget, ListarEmpresasPropias> {
	}

	public ListarEmpresasPropias() {
		initWidget(uiBinder.createAndBindUi(this));
		dialogBox.setVisible(false);
		// Para las fechas
		// Creo un formato para la fecha.
		@SuppressWarnings("deprecation")
		DateTimeFormat dateFormat = DateTimeFormat.getShortDateFormat();
		dbFechaDesde.setFormat(new DateBox.DefaultFormat(dateFormat));
		dbFechaDesde.getDatePicker().setYearArrowsVisible(true);
		dbFechaHasta.setFormat(new DateBox.DefaultFormat(dateFormat));
		dbFechaHasta.getDatePicker().setYearArrowsVisible(true);
		lblMje.setText("");
		lblMje.setStyleName("serverResponseLabelError");
		cargarComboEmpresas();
		tablaDatosEmpresaPropia.setWidget(0, 0, new Label("Dia"));
		tablaDatosEmpresaPropia.setWidget(0, 1, new Label("Mes"));
		tablaDatosEmpresaPropia.setWidget(0, 2, new Label("Anio"));
		tablaDatosEmpresaPropia.setWidget(0, 3, new Label("Nro Fact"));
		tablaDatosEmpresaPropia.setWidget(0, 4, new Label("CLIENTE"));
		tablaDatosEmpresaPropia.setWidget(0, 5, new Label("Concepto"));
		tablaDatosEmpresaPropia.setWidget(0, 6, new Label("Ton"));
		tablaDatosEmpresaPropia.setWidget(0, 7, new Label("IVA"));
		tablaDatosEmpresaPropia.setWidget(0, 8, new Label("Gravado"));
		tablaDatosEmpresaPropia.setWidget(0, 9, new Label("No gravado"));
		tablaDatosEmpresaPropia.setWidget(0, 10, new Label("Monto Total"));
		tablaDatosEmpresaPropia.setWidget(0, 11, new Label("Pagos recibidos"));
		tablaDatosEmpresaPropia.setWidget(0, 12, new Label("Fecha"));
		tablaDatosEmpresaPropia.setWidget(0, 13, new Label("Nro Rec"));
		tablaDatosEmpresaPropia.setWidget(0, 14, new Label("Saldos"));
		tablaDatosEmpresaPropia.setWidget(0, 15, new Label("Vendedor"));
		tablaDatosEmpresaPropia.setWidget(0, 16, new Label("Comision"));
		tablaDatosEmpresaPropia.setWidget(0, 17, new Label("Pago vendedor"));
		// Add styles to elements in the stock list table.
		tablaDatosEmpresaPropia.getRowFormatter().setStyleName(0,
				"watchListHeader");
	}

	private void cargarComboEmpresas() {
		greetingService.getListaEmpresasPorTipo("Empresa propia",
				new AsyncCallback<List<Empresa>>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error al cargar las empresas.");
					}

					@Override
					public void onSuccess(List<Empresa> result) {
						if (result != null) {
							empresaSel = result.get(0);
							int h;
							for (h = 0; h < result.size(); h++) {
								Empresa emp = result.get(h);
								cbEmpresas.addItem(emp.getNombre());
							}
						}
					}
				});
	}

	// Eventos de los botones
	@UiHandler("btnFiltrar")
	void handleClickFiltrar(ClickEvent e) {
		fechaDesde = dbFechaDesde.getValue();
		fechaHasta = dbFechaHasta.getValue();
		if (dbFechaDesde.getValue() == null || dbFechaHasta.getValue() == null) {
			Window.alert("Error: Las fechas no pueden ser vacias.");
		} else {
			if (fechaHasta.before(fechaDesde)) {
				Window.alert("Error: La fecha final no puede ser mayor que la inicial.");
			} else {
				lblMje.setText("Se filtran los resultados.");
				cargarTabla(empresaSel, fechaDesde, fechaHasta);
			}
		}
	}

	@UiHandler("btnSelEmpresa")
	void handleClickSelEmpresa(ClickEvent e) {
		lblMje.setText("");
		tablaDatosEmpresaPropia.clear();
		String empSel = cbEmpresas.getItemText(cbEmpresas.getSelectedIndex());
		greetingService.getEmpresa(empSel, new AsyncCallback<Empresa>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error.");
			}

			@Override
			public void onSuccess(Empresa result) {
				empresaSel = result;
				cargarTabla(empresaSel, null, null);
			}
		});
	}

	private void cargarTabla(Empresa emp, final Date ctFechaDesde,
			final Date ctFechaHasta) {
		tablaDatosEmpresaPropia.removeAllRows();
		tablaDatosEmpresaPropia.clear();
		tablaDatosEmpresaPropia.setWidget(0, 0, new Label("Dia"));
		tablaDatosEmpresaPropia.setWidget(0, 1, new Label("Mes"));
		tablaDatosEmpresaPropia.setWidget(0, 2, new Label("Anio"));
		tablaDatosEmpresaPropia.setWidget(0, 3, new Label("Nro Fact"));
		tablaDatosEmpresaPropia.setWidget(0, 4, new Label("CLIENTE"));
		tablaDatosEmpresaPropia.setWidget(0, 5, new Label("Concepto"));
		tablaDatosEmpresaPropia.setWidget(0, 6, new Label("Ton"));
		tablaDatosEmpresaPropia.setWidget(0, 7, new Label("IVA"));
		tablaDatosEmpresaPropia.setWidget(0, 8, new Label("Gravado"));
		tablaDatosEmpresaPropia.setWidget(0, 9, new Label("No gravado"));
		tablaDatosEmpresaPropia.setWidget(0, 10, new Label("Monto Total"));
		tablaDatosEmpresaPropia.setWidget(0, 11, new Label("Pagos recibidos"));
		tablaDatosEmpresaPropia.setWidget(0, 12, new Label("Fecha"));
		tablaDatosEmpresaPropia.setWidget(0, 13, new Label("Nro Rec"));
		tablaDatosEmpresaPropia.setWidget(0, 14, new Label("Saldos"));
		tablaDatosEmpresaPropia.setWidget(0, 15, new Label("Vendedor"));
		tablaDatosEmpresaPropia.setWidget(0, 16, new Label("Comision"));
		tablaDatosEmpresaPropia.setWidget(0, 17, new Label("Pago vendedor"));
		// Add styles to elements in the stock list table.
		tablaDatosEmpresaPropia.getRowFormatter().setStyleName(0,
				"watchListHeader");
		// Obtengo todas las facturas de la empresa propia
		greetingService.getDatosEmpresaPropia(emp.getNombre(),
				new AsyncCallback<List<String>>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error.");
					}

					@Override
					public void onSuccess(List<String> result) {
						// Para cada factura, obtengo las toneladas totales,
						// fecha ultimo viaje,
						// precio del flete SIN IVA de cualuiera de ellos,
						// precio final de la madera
						// de cualquiera de ellos y el total de pagos recibidos
						if (result != null) {
							int h;
							int pos = 1;
							if (ctFechaDesde == null && ctFechaHasta == null) {
								for (h = 0; h < result.size(); h++) {
									// Debo desarmar este String por ";" para
									// poder
									// llenar la tabla.
									String[] datosEmpresaPropia = result.get(h)
											.split(";");
									String toneladas = datosEmpresaPropia[0];
									String fecha = datosEmpresaPropia[1];
									String precioFlete = datosEmpresaPropia[2];
									String precioMadera = datosEmpresaPropia[3];
									String pagosRecibidos = datosEmpresaPropia[4];
									String factura = datosEmpresaPropia[5];
									String cliente = datosEmpresaPropia[6];
									String fechaPagosRecibidos = datosEmpresaPropia[7];
									agregoRegistro(toneladas, fecha,
											precioFlete, precioMadera,
											pagosRecibidos, factura, cliente,
											fechaPagosRecibidos, pos);
									pos++;
								}

							} else {
								// Realizo el mismo filtro pero solo con el
								// filtro aplicado.
								for (h = 0; h < result.size(); h++) {
									// Debo desarmar este String por ";" para
									// poder
									// llenar la tabla.
									String[] datosEmpresaPropia = result.get(h)
											.split(";");
									String toneladas = datosEmpresaPropia[0];
									String fecha = datosEmpresaPropia[1];
									String precioFlete = datosEmpresaPropia[2];
									String precioMadera = datosEmpresaPropia[3];
									String pagosRecibidos = datosEmpresaPropia[4];
									String factura = datosEmpresaPropia[5];
									String cliente = datosEmpresaPropia[6];
									String fechaPagosRecibidos = datosEmpresaPropia[7];

									DateTimeFormat formatoDelTexto = DateTimeFormat
											.getFormat("dd/MM/yy");
									String strFecha = fecha;
									Date fechaDate = null;
									fechaDate = formatoDelTexto.parse(strFecha);
									if (((fechaDesde.before(fechaDate) || fechaDesde
											.equals(fechaDate)) && (fechaHasta
											.after(fechaDate) || fechaHasta
											.equals(fechaDate)))) {
										agregoRegistro(toneladas, fecha,
												precioFlete, precioMadera,
												pagosRecibidos, factura,
												cliente, fechaPagosRecibidos,
												pos);
										pos++;
									}
								}
							}

						}
					}
				});
	}

	private void agregoRegistro(String toneladas, String fecha,
			String precioFlete, String precioMadera, String pagosRecibidos,
			String factura, String cliente, String fechaPagosRecibidos, int pos) {
		BigDecimal iva = new BigDecimal("0.00");
		BigDecimal gravado = new BigDecimal("0.00");
		BigDecimal noGravado = new BigDecimal("0.00");
		BigDecimal montoTotal = new BigDecimal("0.00");
		BigDecimal saldos = new BigDecimal("0.00");
		BigDecimal bdIva = new BigDecimal("0.00");
		BigDecimal bdToneladas = new BigDecimal("0.00");
		BigDecimal bdPagosRecibidos = new BigDecimal("0.00");

		// Separo la fecha en dia/mes y anio.
		String dia = fecha.substring(0, fecha.indexOf("/"));
		fecha = fecha.substring(fecha.indexOf("/") + 1, fecha.length());
		String mes = fecha.substring(0, fecha.indexOf("/"));
		fecha = fecha.substring(fecha.indexOf("/") + 1, fecha.length());
		String anio = fecha;
		lblMje.setText("");

		// Realizo los calculos necesarios.
		bdIva = BigDecimal.valueOf(Double.parseDouble("0.22"));
		bdToneladas = BigDecimal.valueOf(Double.parseDouble(toneladas));
		bdPagosRecibidos = BigDecimal.valueOf(Double
				.parseDouble(pagosRecibidos));
		gravado = BigDecimal.valueOf(Double.parseDouble(precioFlete));
		noGravado = BigDecimal.valueOf(Double.parseDouble(precioMadera));
		gravado = gravado.multiply(bdToneladas);
		noGravado = noGravado.multiply(bdToneladas);
		iva = gravado.multiply(bdIva);
		montoTotal = gravado.add(noGravado);
		montoTotal = montoTotal.add(iva);
		saldos = montoTotal.subtract(bdPagosRecibidos);

		// Arreglo los BigDecimal.
		iva = iva.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		gravado = gravado.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		noGravado = noGravado.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		montoTotal = montoTotal.setScale(2, BigDecimal.ROUND_UNNECESSARY);
		saldos = saldos.setScale(2, BigDecimal.ROUND_UNNECESSARY);

		// Lleno la tabla.
		tablaDatosEmpresaPropia.setWidget(pos, 0, new Label(dia));
		tablaDatosEmpresaPropia.setWidget(pos, 1, new Label(mes));
		tablaDatosEmpresaPropia.setWidget(pos, 2, new Label(anio));
		tablaDatosEmpresaPropia.setWidget(pos, 3, new Label(factura));
		tablaDatosEmpresaPropia.setWidget(pos, 4, new Label(cliente));
		tablaDatosEmpresaPropia.setWidget(pos, 5, new Label(""));
		tablaDatosEmpresaPropia.setWidget(pos, 6, new Label(toneladas));
		tablaDatosEmpresaPropia.setWidget(pos, 7, new Label(iva.toString()));
		tablaDatosEmpresaPropia
				.setWidget(pos, 8, new Label(gravado.toString()));
		tablaDatosEmpresaPropia.setWidget(pos, 9,
				new Label(noGravado.toString()));
		tablaDatosEmpresaPropia.setWidget(pos, 10,
				new Label(montoTotal.toString()));
		tablaDatosEmpresaPropia.setWidget(pos, 11, new Label(pagosRecibidos));
		tablaDatosEmpresaPropia.setWidget(pos, 12, new Label(
				fechaPagosRecibidos));
		tablaDatosEmpresaPropia.setWidget(pos, 13, new Label(""));
		// Arreglo el saldo para el caso de que sea
		// cero.
		if (saldos.toString().equals("0")) {
			tablaDatosEmpresaPropia.setWidget(pos, 14, new Label("-"));
		} else {
			tablaDatosEmpresaPropia.setWidget(pos, 14,
					new Label(saldos.toString()));
		}
		tablaDatosEmpresaPropia.setWidget(pos, 15, new Label(""));
		tablaDatosEmpresaPropia.setWidget(pos, 16, new Label(""));
		tablaDatosEmpresaPropia.setWidget(pos, 17, new Label(""));
	}
}
