package com.conexion.ventanas;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Empresa;
import com.conexion.entidades.Viaje;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class InformeClientes extends Composite {

	private static InformeClientesUiBinder uiBinder = GWT
			.create(InformeClientesUiBinder.class);

	@UiField
	FlexTable tablaBalance;
	@UiField
	Button btnSeleccionar;
	@UiField
	Button btnCobrar;
	@UiField
	Label lblNombreCliente;
	@UiField
	FlexTable tablaInforme;
	@UiField
	DialogBox dialogBox;
	@UiField
	Label lblMje;
	@UiField
	DateBox dbFechaDesde;
	@UiField
	DateBox dbFechaHasta;
	@UiField
	Button btnFiltrar;

	String saldoInicial = "0";
	String cli = "";
	String IVA = "1.22";
	Empresa empresaSel;
	Date fechaDesde;
	Date fechaHasta;
	BigDecimal bdSaldo;
	int i = 2;// Inicio en 2 para dejar la primer fila con el
	// saldo
	// inicial.
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface InformeClientesUiBinder extends UiBinder<Widget, InformeClientes> {
	}

	public InformeClientes() {
		initWidget(uiBinder.createAndBindUi(this));
		RootPanel.get("contenedorLink").clear();
		HTML h = new HTML(
				"<a download=\"InformeClientes.xls\" "
						+ "onclick=\"return ExcellentExport.excel(this, 'hola', 'InformeClientes');\">"
						+ "Exportar a Excel</a>");
		RootPanel.get("contenedorLink").add(h);
		tablaInforme.getElement().setId("hola");
		dialogBox.setVisible(false);
		cargarTabla();
		CheckBox chk = new CheckBox();
		chk.setEnabled(false);
		tablaBalance.setWidget(0, 0, chk);
		tablaBalance.setWidget(0, 1, new Label("Deudor"));
		tablaBalance.setWidget(0, 2, new Label("Saldo"));
		tablaBalance.getRowFormatter().setStyleName(0, "watchListHeader");
		tablaInforme.setWidget(0, 0, chk);
		tablaInforme.setWidget(0, 1, new Label("Fecha"));
		tablaInforme.setWidget(0, 2, new Label("Toneladas"));
		tablaInforme.setWidget(0, 3, new Label("Origen/proveedor"));
		tablaInforme.setWidget(0, 4, new Label("Fletero"));
		tablaInforme.setWidget(0, 5, new Label("Monto/ton"));
		tablaInforme.setWidget(0, 6, new Label("Importe"));
		tablaInforme.setWidget(0, 7, new Label("Saldo"));
		tablaInforme.getRowFormatter().setStyleName(0, "watchListHeader");
		lblMje.setText("");
		lblMje.setStyleName("serverResponseLabelError");
	}

	public int getClickedRow() {
		int selectedRow = -1;
		for (int i = 1; i < tablaBalance.getRowCount(); ++i) {
			CheckBox checkBox = (CheckBox) tablaBalance.getWidget(i, 0);
			if (checkBox.getValue()) {
				selectedRow = i;
				return selectedRow;
			}
		}
		return selectedRow;
	}
	
	public int getClickedCobroParcial() {
		int selectedRow = -1;
		for (int i = 1; i < tablaInforme.getRowCount(); ++i) {
			CheckBox checkBox = (CheckBox) tablaInforme.getWidget(i, 0);
			if (checkBox.getValue()) {
				selectedRow = i;
				return selectedRow;
			}
		}
		return selectedRow;
	}

	private void cargarTabla() {
		greetingService.getListaEmpresas(new AsyncCallback<List<Empresa>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error.");

			}

			@Override
			public void onSuccess(List<Empresa> result) {
				int i = 1;
				if (result != null) {
					empresaSel = result.get(0);
					int h;
					for (h = 0; h < result.size(); h++) {
						Empresa emp = result.get(h);
						if (emp.getTipo().equals("Cliente")) {
							tablaBalance.setWidget(i, 0, new CheckBox());
							tablaBalance.setWidget(i, 1,
									new Label(emp.getNombre()));
							tablaBalance.setWidget(i, 2,
									new Label(emp.getCuenta_corriente()));
							i++;
						}
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
				cargarTablaInforme(fechaDesde, fechaHasta);
			}
		}
	}

	@UiHandler("btnCobrar")
	void handleClickCobrar(ClickEvent e) {
		lblMje.setText("");
		if (getClickedRow() == -1) {
			Window.alert("Debe seleccionar un cliente.");
		} else {
			String empSel = tablaBalance.getText(getClickedRow(), 1);
			greetingService.getEmpresa(empSel, new AsyncCallback<Empresa>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error.");
				}

				@Override
				public void onSuccess(Empresa result) {
					PagarCobrarEmpresas cobrarCliente = new PagarCobrarEmpresas(
							result, "cobrar", InformeClientes.this, null);
					dialogBox = createDialogBox("Cobrar a cliente",
							cobrarCliente);
					dialogBox.center();
					dialogBox.show();

				}
			});
		}
	}

	@UiHandler("btnSeleccionar")
	void handleClickSeleccionar(ClickEvent e) {
		lblMje.setText("");
		if (getClickedRow() == -1) {
			Window.alert("Debe seleccionar un cliente.");
		} else {
			tablaInforme.clear();
			String empSel = tablaBalance.getText(getClickedRow(), 1);
			greetingService.getEmpresa(empSel, new AsyncCallback<Empresa>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error.");
				}

				@Override
				public void onSuccess(Empresa result) {
					lblNombreCliente.setText(result.getNombre());
					cli = result.getNombre();
					saldoInicial = result.getSaldoInicial();
					cargarTablaInforme(null, null);
				}
			});
		}
	}
	
	public void cerrarDialogBox(){
		this.dialogBox.hide();
		cargarTabla();
	}

	// Con esta funcion se carga la tabla de informes.
	private void cargarTablaInforme(final Date ctFechaDesde,
			final Date ctFechaHasta) {
		// Datos de la tabla.
		tablaInforme.removeAllRows();
		tablaInforme.clear();
		tablaInforme.getRowFormatter().setStyleName(0, "watchListHeader");
		tablaInforme.setWidget(0, 0, new Label("Fecha"));
		tablaInforme.setWidget(0, 1, new Label("Toneladas"));
		tablaInforme.setWidget(0, 2, new Label("Factura"));
		tablaInforme.setWidget(0, 3, new Label("Origen/proveedor"));
		tablaInforme.setWidget(0, 4, new Label("Fletero"));
		tablaInforme.setWidget(0, 5, new Label("Monto/ton"));
		tablaInforme.setWidget(0, 6, new Label("Importe"));
		tablaInforme.setWidget(0, 7, new Label("Saldo"));

		greetingService.getListaViajes(new AsyncCallback<List<Viaje>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error.");
			}

			@Override
			public void onSuccess(List<Viaje> result) {
				bdSaldo = new BigDecimal("0.00");
				bdSaldo = BigDecimal.valueOf(Double.parseDouble(saldoInicial));
				// Saldo inicial.
				tablaInforme.setWidget(1, 0, new Label(""));
				tablaInforme.setWidget(1, 1, new Label(""));
				tablaInforme.setWidget(1, 2, new Label("Saldo Inicial"));
				tablaInforme.setWidget(1, 3, new Label(""));
				tablaInforme.setWidget(1, 4, new Label(""));
				tablaInforme.setWidget(1, 5, new Label(""));
				tablaInforme.setWidget(1, 6, new Label(""));
				tablaInforme.setWidget(1, 7, new Label(saldoInicial));
				if (result != null) {
					int h;
					// Recorro toda la lista.
					for (h = 0; h < result.size(); h++) {
						Viaje vje = result.get(h);
						if (ctFechaDesde == null && ctFechaHasta == null) {
							llenoTabla(vje, cli);
							i++;
						} else { // HAGO EL FILTRO POR LAS FECHAS.
							DateTimeFormat formatoDelTexto = DateTimeFormat
									.getFormat("dd/MM/yy");
							Date fechaDate = null;
							fechaDate = formatoDelTexto.parse(vje.getFecha());
							if (((fechaDesde.before(fechaDate) || fechaDesde
									.equals(fechaDate)) && (fechaHasta
									.after(fechaDate) || fechaHasta
									.equals(fechaDate)))) {
								llenoTabla(vje, cli);
								i++;
							}
						}
					}
				}
			}
		});
	}

	// Deberia no pasar el i para que no se pisen los valor del i.
	private void llenoTabla(Viaje vje, String cliente) {
		if (vje.getCliente() != null) {
			if ((vje.getCliente().equals(cliente))
					&& (!vje.isPendienteDeDescarga())) {
				// Compruebo que el viaje no sea un cobro o pago
				if (!vje.getFacturado().equals("pago")) {
					double toneladasD = 0;
					if (vje.getKilosOrigenReales() != null)
						toneladasD = Double.parseDouble(vje
								.getKilosOrigenReales());
					else
						toneladasD = Double.parseDouble(vje.getKilos());
					BigDecimal bdToneladasDescarga = BigDecimal
							.valueOf(toneladasD);
					// El importe es la suma del precio que se
					// paga
					// la
					// madera al descargar con el monto del
					// fletero
					// iva incluido
					// por las toneladas compradas.
					// El precio final de la madera.
					double precioXTonD = 0;
					if (vje.getPrecioMaderaDescarga() != null)
						precioXTonD = Double.parseDouble(vje
								.getPrecioMaderaDescarga());
					else
						precioXTonD = Double.parseDouble(vje.getPrecio_kilo());
					BigDecimal bdprecioXTon = BigDecimal.valueOf(precioXTonD);
					// El precio final del flete y agrego el
					// IVA.
					BigDecimal bdIva = BigDecimal.valueOf(Double
							.parseDouble(IVA));
					double precioFleteD = 0;
					if (vje.getPrecioFleteroDescarga() == null)
						precioFleteD = Double.parseDouble(vje
								.getPrecioXtonelada_flete());
					else
						precioFleteD = Double.parseDouble(vje
								.getPrecioFleteroDescarga());
					BigDecimal bdprecioFlete = BigDecimal.valueOf(precioFleteD);
					// Calculo el precio del flete con iva
					// incluido.
					BigDecimal rPrecioFleteroIva = bdprecioFlete
							.multiply(bdIva);
					// Sumo el precio del flete con iva incluido
					// y
					// el
					// precio final de la
					// madera
					BigDecimal rSuma = new BigDecimal("0.00");
					rSuma = rPrecioFleteroIva.add(bdprecioXTon);
					// Multiplico las toneladas por la suma del
					// flete
					// con iva incluido y el
					// precio final de la madera
					BigDecimal bdImporte = new BigDecimal("0.00");
					bdImporte = bdToneladasDescarga.multiply(rSuma);
					bdImporte = bdImporte.setScale(2,
							BigDecimal.ROUND_UNNECESSARY);
					bdSaldo = bdSaldo.add(bdImporte);

					agregoRegistro(i, vje.getFecha(),
							String.valueOf(toneladasD), vje.getFacturado(),
							vje.getProveedor(), vje.getFletero(),
							String.valueOf(precioXTonD), bdImporte.toString(),
							bdSaldo.toString());
					// En caso de que el viaje fuera cobrado, se
					// agrega
					// un nuevo registro con el mismo importe
					// pero
					// negativo.
					if (!vje.isPendienteDeCobro()) {
						if (vje.getCobroParcial() != null) {
							bdImporte = BigDecimal.valueOf(Double
									.parseDouble(vje.getCobroParcial()));
						} else {
							BigDecimal negativo = new BigDecimal(-1);
							bdImporte = bdImporte.multiply(negativo);
						}
						// ME DIO UN ERROR POR
						// ESTO---------------------------------------------------18/02/16---
						// Redondeo para mejorar el número.
						// bdImporte = bdImporte.setScale(2,
						// BigDecimal.ROUND_UNNECESSARY);
						// FIN..ME DIO UN ERROR POR
						// ESTO--------------------------------------------
						// 18/02/16---
						bdSaldo = bdSaldo.add(bdImporte);
						i++;
						agregoRegistro(i, vje.getFecha(), "",
								"Cobro del viaje", "", "", "",
								bdImporte.toString(), bdSaldo.toString());
					}
				} else {
					if (vje.getCliente() != null
							&& vje.getPrecioMaderaDescarga() != null) {
						if (vje.getCliente().equals(cliente)) {
							// Muestro el cobro.
							BigDecimal pago = new BigDecimal("0.00");
							pago = BigDecimal.valueOf(Double.parseDouble(vje
									.getPrecioMaderaDescarga()));
							bdSaldo = bdSaldo.add(pago);
							// Arreglo los BigDecimal.
							pago = pago.setScale(2,
									BigDecimal.ROUND_UNNECESSARY);
							bdSaldo = bdSaldo.setScale(2,
									BigDecimal.ROUND_UNNECESSARY);
							agregoRegistro(i, vje.getFecha(), "",
									"Cobro parcial", "", "", "",
									pago.toString(), bdSaldo.toString());
						}
					}
				}
			}
		}
	}

	// Esta función agrega un nuevo registro a la tabla de informes.
	private void agregoRegistro(int i, String fecha, String toneladas,
			String facturado, String proveedor, String fletero,
			String precioXton, String importe, String saldo) {
		tablaInforme.setWidget(i, 0, new Label(fecha));
		tablaInforme.setWidget(i, 1, new Label(toneladas));
		tablaInforme.setWidget(i, 2, new Label(facturado));
		tablaInforme.setWidget(i, 3, new Label(proveedor));
		tablaInforme.setWidget(i, 4, new Label(fletero));
		tablaInforme.setWidget(i, 5, new Label(precioXton));
		tablaInforme.setWidget(i, 6, new Label(importe));
		tablaInforme.setWidget(i, 7, new Label(saldo));

	}

	private DialogBox createDialogBox(String titulo, Widget widget) {
		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		// Personalizo el dialogBox
		dialogBox.setText(titulo);
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setWidth("100%");
		dialogContents.setSpacing(4);
		dialogBox.setWidget(dialogContents);
		// Pone el Dialog box con el fondo en gris.
		dialogBox.setGlassEnabled(true);

		// Add a close button at the bottom of the dialog
		Button closeButton = new Button("Cerrar");
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				cargarTabla();
				tablaInforme.clear();
				tablaInforme.removeAllRows();
				tablaInforme.setWidget(0, 0, new Label("Fecha"));
				tablaInforme.setWidget(0, 1, new Label("Toneladas"));
				tablaInforme.setWidget(0, 2, new Label("Origen/proveedor"));
				tablaInforme.setWidget(0, 3, new Label("Fletero"));
				tablaInforme.setWidget(0, 4, new Label("Monto/ton"));
				tablaInforme.setWidget(0, 5, new Label("Importe"));
				tablaInforme.setWidget(0, 6, new Label("Saldo"));
				tablaInforme.getRowFormatter().setStyleName(0,
						"watchListHeader");
			}
		});
		closeButton.setWidth("250px");
		dialogContents
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dialogContents.add(widget);
		dialogContents.add(closeButton);
		// Return the dialog box
		return dialogBox;
	}
}