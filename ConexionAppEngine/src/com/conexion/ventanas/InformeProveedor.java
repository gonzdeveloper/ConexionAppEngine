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

public class InformeProveedor extends Composite {

	private static InformeProveedoresUiBinder uiBinder = GWT
			.create(InformeProveedoresUiBinder.class);
	@UiField
	FlexTable tablaBalance;
	@UiField
	Button btnSeleccionar;
	@UiField
	Button btnPagar;
	@UiField
	Button btnEliminarPago;
	@UiField
	Label lblNombreProveedor;
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

	String prov = "";
	String saldoInicial = "0";
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

	interface InformeProveedoresUiBinder extends
			UiBinder<Widget, InformeProveedor> {
	}

	public InformeProveedor() {
		initWidget(uiBinder.createAndBindUi(this));
		RootPanel.get("contenedorLink").clear();
		RootPanel.get("contenedorLink").clear();
		HTML h = new HTML(
				"<a download=\"InformeProveedores.xls\" "
						+ "onclick=\"return ExcellentExport.excel(this, 'hola', 'InformeProveedores');\">"
						+ "Exportar a Excel</a>");
		RootPanel.get("contenedorLink").add(h);
		tablaInforme.getElement().setId("hola");
		dialogBox.setVisible(false);
		cargarTabla();
		lblNombreProveedor.setText("");
		CheckBox chk = new CheckBox();
		chk.setEnabled(false);
		tablaBalance.setWidget(0, 0, chk);
		tablaBalance.setWidget(0, 1, new Label("Proveedor"));
		tablaBalance.setWidget(0, 2, new Label("Saldo"));
		tablaBalance.getRowFormatter().setStyleName(0, "watchListHeader");
		lblMje.setText("");
		lblMje.setStyleName("serverResponseLabelError");
	}

	//TODO no se esta seleccionando correctamente la fila que contiene el pago parcial
	public int getClickedPagoParcial() {
		int selectedRow = -1;
		Window.alert(tablaInforme.toString());
		for (int i = 2; i < tablaInforme.getRowCount(); ++i) {
			CheckBox checkBox = (CheckBox) tablaInforme.getWidget(i, 0);
			if (checkBox.getValue()) {
				selectedRow = i;
				return selectedRow;
			}
		}
		return selectedRow;
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

	private void cargarTabla() {
		lblMje.setText("");
		tablaInforme.removeAllRows();
		tablaInforme.clear();
		CheckBox chk = new CheckBox();
		chk.setEnabled(false);
		tablaInforme.setWidget(0, 0, chk);
		tablaInforme.setWidget(0, 1, new Label("Fecha"));
		tablaInforme.setWidget(0, 2, new Label("Toneladas"));
		tablaInforme.setWidget(0, 3, new Label("Lugar descarga"));
		tablaInforme.setWidget(0, 4, new Label("Monto/ton"));
		tablaInforme.setWidget(0, 5, new Label("Importe"));
		tablaInforme.setWidget(0, 6, new Label("Saldo"));
		tablaInforme.getRowFormatter().setStyleName(0, "watchListHeader");
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
						if (emp.getTipo().equals("Proveedor")) {
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
				cargarTablaInforme(empresaSel, fechaDesde, fechaHasta);
			}
		}
	}

	@UiHandler("btnPagar")
	void handleClickPagar(ClickEvent e) {
		lblMje.setText("");
		if (getClickedRow() == -1) {
			Window.alert("Debe seleccionar un proveedor.");
		} else {
			String empSel = tablaBalance.getText(getClickedRow(), 1);
			greetingService.getEmpresa(empSel, new AsyncCallback<Empresa>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error.");
				}

				@Override
				public void onSuccess(Empresa result) {
					PagarCobrarEmpresas pagarProveedor = new PagarCobrarEmpresas(
							result, "pagar", null, InformeProveedor.this);
					dialogBox = createDialogBox("Pagar a Proveedor",
							pagarProveedor);
					dialogBox.center();
					dialogBox.show();

				}
			});
		}
	}

	@UiHandler("btnSeleccionar")
	void handleClickSeleccionar(ClickEvent e) {
		lblMje.setText("");
		tablaInforme.removeAllRows();
		tablaInforme.clear();
		String empSel = tablaBalance.getText(getClickedRow(), 1);
		greetingService.getEmpresa(empSel, new AsyncCallback<Empresa>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error.");
			}
			@Override
			public void onSuccess(Empresa result) {
				lblNombreProveedor.setText(result.getNombre());
				prov = result.getNombre();
				saldoInicial = result.getSaldoInicial();
				cargarTablaInforme(result, null, null);
			}
		});
	}
	
	@UiHandler("btnEliminarPago")
	void handleClickbtnEliminarPago(ClickEvent e) {
		btnEliminarPago.setEnabled(false);
		lblMje.setText("");
		if (getClickedPagoParcial() == -1) {
			Window.alert("Debe seleccionar un pago parcial.");
		} else {
			String vjeSel  = tablaInforme.getText(getClickedRow(), 1);
			greetingService.getViaje(vjeSel, new AsyncCallback<Viaje>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error.");
				}
				@Override
				public void onSuccess(Viaje result) {
					EliminarPagoCobroParcial elimCobroPagoParcial = new EliminarPagoCobroParcial(result, InformeProveedor.this, null, "pagar");
					dialogBox = createDialogBox("Eliminar pago parcial",
							elimCobroPagoParcial);
					dialogBox.center();
					dialogBox.show();
				}
			});
		}
	}

	public void cerrarDialogBox(){
		this.dialogBox.hide();
		cargarTabla();
	}
	
	// Con esta funcion se carga la tabla de informes.
	private void cargarTablaInforme(final Empresa proveedor,
			final Date ctFechaDesde, final Date ctFechaHasta) {
		tablaInforme.removeAllRows();
		tablaInforme.clear();
		CheckBox chk = new CheckBox();
		chk.setEnabled(false);
		tablaInforme.setWidget(0, 0, chk);
		tablaInforme.getRowFormatter().setStyleName(0, "watchListHeader");
		tablaInforme.setWidget(0, 1, new Label("Fecha"));
		tablaInforme.setWidget(0, 2, new Label("Toneladas"));
		tablaInforme.setWidget(0, 3, new Label("Lugar descarga"));
		tablaInforme.setWidget(0, 4, new Label("Monto/ton"));
		tablaInforme.setWidget(0, 5, new Label("Importe"));
		tablaInforme.setWidget(0, 6, new Label("Saldo"));
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
				CheckBox chk = new CheckBox();
				chk.setEnabled(false);
				tablaInforme.setWidget(1, 0, chk);
				tablaInforme.setWidget(1, 1, new Label(""));
				tablaInforme.setWidget(1, 2, new Label(""));
				tablaInforme.setWidget(1, 3, new Label("Saldo Inicial"));
				tablaInforme.setWidget(1, 4, new Label(""));
				tablaInforme.setWidget(1, 5, new Label(""));
				tablaInforme.setWidget(1, 6, new Label(saldoInicial));
				// Falta mostrar el pago del proveedor como un nuevo registro.
				if (result != null) {
					int h;
					for (h = 0; h < result.size(); h++) {
						Viaje vje = result.get(h);
						if (ctFechaDesde == null && ctFechaHasta == null) {
							llenoTabla(vje, prov);
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
								llenoTabla(vje, prov);
								i++;
							}
						}
					}
				}
			}
		});
	}

	private void llenoTabla(Viaje vje, String prov) {
		// Compruebo que el viaje no sea un cobro o pago
		Double negativoD = Double.parseDouble("-1");
		BigDecimal negativo = new BigDecimal(negativoD);
		if (vje.getFacturado().equals("pago")) {
			if (vje.getProveedor() != null) {
				if (vje.getProveedor().equals(prov)) {
					BigDecimal pago = new BigDecimal("0.00");
					pago = BigDecimal.valueOf(0);
					if (vje.getPrecioMaderaDescarga() != null)
						pago = BigDecimal.valueOf(Double.parseDouble(vje
								.getPrecioMaderaDescarga()));
					bdSaldo = bdSaldo.add(pago);
					// Arreglo los BigDecimal.
					pago = pago.setScale(2, BigDecimal.ROUND_UNNECESSARY);
					bdSaldo = bdSaldo.setScale(2, BigDecimal.ROUND_UNNECESSARY);
					agregoRegistro(i, vje.getFecha(), "", "Pago parcial", "",
							pago.toString(), bdSaldo.toString(), true);
				}
			} else {
				i--;
			}
		} else {
			// PROVEEDOR
			if ((vje.getProveedor().equals(prov)) && (!vje.isPendienteCarga())) {
				double toneladasD = 0;
				if (vje.getKilosOrigenReales() != null)
					toneladasD = Double.parseDouble(vje.getKilosOrigenReales());
				else
					toneladasD = Double.parseDouble(vje.getKilos());
				// El importe es el precio de la madera
				// original
				// multiplicado
				// por las toneladas compradas.
				// Defino los bigDecimal para un correcto
				// formato.
				BigDecimal bdTon = new BigDecimal("0.00");
				BigDecimal bdPrecioXTon = new BigDecimal("0.00");
				BigDecimal bdImporte = new BigDecimal("0.00");
				bdTon = BigDecimal.valueOf(toneladasD);
				bdPrecioXTon = BigDecimal.valueOf(Double.parseDouble(vje
						.getPrecio_kilo()));
				bdImporte = bdPrecioXTon.multiply(bdTon);
				bdImporte = bdImporte.setScale(2, BigDecimal.ROUND_UNNECESSARY);
				bdSaldo = bdSaldo.add(bdImporte);
				agregoRegistro(i, vje.getFecha(), String.valueOf(toneladasD),
						vje.getCliente(), vje.getPrecio_kilo(),
						bdImporte.toString(), bdSaldo.toString(),false);
				// Si el viaje fue pagado, se debe crear un
				// nuevo
				// registro.
				if (!vje.isPendienteDePago()) {
					if (vje.getPagoParcial() != null && vje.getPagoParcial() != "") {
						bdImporte = BigDecimal.valueOf(Double.parseDouble(vje
								.getPagoParcial()));
					} else {
						bdImporte = bdImporte.multiply(negativo);
					}
					bdImporte = bdImporte.setScale(2,
							BigDecimal.ROUND_UNNECESSARY);
					bdSaldo = bdSaldo.add(bdImporte);
					i++;
					agregoRegistro(i, vje.getFecha(), "", "Pago del viaje", "",
							bdImporte.toString(), bdSaldo.toString(),false);
				}
			}
			// FLETERO
			if ((vje.getFletero().equals(prov)) && (!vje.isPendienteCarga())) {
				if (vje.getFletero().equals(vje.getProveedor())) {
					i++;
				}
				double toneladasD = 0;
				if (vje.getKilosOrigenReales() != null)
					toneladasD = Double.parseDouble(vje.getKilosOrigenReales());
				else
					toneladasD = Double.parseDouble(vje.getKilos());
				// El importe es el precio que se le paga al
				// fletero
				// originalmente por cada tonelada
				// multiplicado
				// por las toneladas compradas.
				BigDecimal bdTon = new BigDecimal("0.00");
				BigDecimal bdPrecioXTon = new BigDecimal("0.00");
				BigDecimal bdImporte = new BigDecimal("0.00");
				bdTon = BigDecimal.valueOf(toneladasD);
				bdPrecioXTon = BigDecimal.valueOf(Double.parseDouble(vje
						.getPrecioXtonelada_flete()));
				bdImporte = bdPrecioXTon.multiply(bdTon);
				bdImporte = bdImporte.setScale(2, BigDecimal.ROUND_UNNECESSARY);
				bdSaldo = bdSaldo.add(bdImporte);
				agregoRegistro(i, vje.getFecha(), String.valueOf(toneladasD),
						vje.getCliente(), vje.getPrecioXtonelada_flete(),
						bdImporte.toString(), bdSaldo.toString(),false);
				// Si el viaje se descarga se le debe pagar
				// al
				// fletero, por lo tanto,
				// se crea un nuevo registro.
				if (vje.isSePagoFletero() && !vje.isPendienteDeDescarga()) {
					bdImporte = bdImporte.multiply(negativo);
					bdImporte = bdImporte.setScale(2,
							BigDecimal.ROUND_UNNECESSARY);
					bdSaldo = bdSaldo.add(bdImporte);
					i++;
					agregoRegistro(i, vje.getFecha(), "", "Pago del fletero",
							"", bdImporte.toString(), bdSaldo.toString(),false);
				}
			}
			// DESCARGADOR
			if ((vje.getNombre_descargador().equals(prov))
					&& (!vje.isPendienteCarga())) {
				if (vje.getNombre_descargador().equals(vje.getProveedor())) {
					i++;
				} else if (vje.getNombre_descargador().equals(vje.getFletero())) {
					i++;
				}
				double toneladasD = 0;
				if (vje.getKilosOrigenReales() != null)
					toneladasD = Double.parseDouble(vje.getKilosOrigenReales());
				else
					toneladasD = Double.parseDouble(vje.getKilos());
				// El importe es el precio que se le paga al
				// descargador por cada tonelada
				// multiplicado
				// por las toneladas compradas.
				BigDecimal bdTon = new BigDecimal("0.00");
				BigDecimal bdPrecioXTon = new BigDecimal("0.00");
				BigDecimal bdImporte = new BigDecimal("0.00");
				bdTon = BigDecimal.valueOf(toneladasD);
				bdPrecioXTon = BigDecimal.valueOf(Double.parseDouble(vje
						.getMonto_descargador()));
				bdImporte = bdPrecioXTon.multiply(bdTon);
				bdImporte = bdImporte.setScale(2, BigDecimal.ROUND_UNNECESSARY);
				bdSaldo = bdSaldo.add(bdImporte);
				agregoRegistro(i, vje.getFecha(), String.valueOf(toneladasD),
						vje.getCliente(), vje.getMonto_descargador(),
						bdImporte.toString(), bdSaldo.toString(),false);
				// Si el viaje se descarga se le debe pagar
				// al
				// fletero, por lo tanto,
				// se crea un nuevo registro.
				if (vje.isSePagoDescargador() && !vje.isPendienteDeDescarga()) {
					bdImporte = bdImporte.multiply(negativo);
					bdImporte = bdImporte.setScale(2,
							BigDecimal.ROUND_UNNECESSARY);
					bdSaldo = bdSaldo.add(bdImporte);
					i++;
					agregoRegistro(i, vje.getFecha(), "",
							"Pago del descargador", "", bdImporte.toString(),
							bdSaldo.toString(),false);
				}
			}
		}

	}

	// Esta funcion agrega un nuevo registro a la tabla de informes.
	private void agregoRegistro(int i, String fecha, String toneladas,
			String cliente, String precio_kilo, String importe, String saldo, boolean adelanto) {
		CheckBox chk = new CheckBox();
		chk.setEnabled(adelanto);
		tablaInforme.setWidget(i, 0, chk);
		tablaInforme.setWidget(i, 1, new Label(fecha));
		tablaInforme.setWidget(i, 2, new Label(toneladas));
		tablaInforme.setWidget(i, 3, new Label(cliente));
		tablaInforme.setWidget(i, 4, new Label(precio_kilo));
		tablaInforme.setWidget(i, 5, new Label(importe));
		tablaInforme.setWidget(i, 6, new Label(saldo));
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
