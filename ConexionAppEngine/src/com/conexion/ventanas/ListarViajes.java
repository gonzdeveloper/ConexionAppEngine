package com.conexion.ventanas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Viaje;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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

public class ListarViajes extends Composite {

	private static ListarViajesUiBinder uiBinder = GWT
			.create(ListarViajesUiBinder.class);

	@UiField
	FlexTable tablaViajes;
	@UiField
	Button btnAceptar;
	@UiField
	Button btnModificar;
	@UiField
	Button btnEliminar;
	@UiField
	Button btnFacturar;
	@UiField
	Button btnRestaurar;
	@UiField
	DialogBox dialogBox;

	double toneladasD;
	double montoD;
	// Cargo la tabla
	int i = 1;
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface ListarViajesUiBinder extends UiBinder<Widget, ListarViajes> {
	}

	public ListarViajes() {
		initWidget(uiBinder.createAndBindUi(this));
		RootPanel.get("contenedorLink").clear();
		HTML h = new HTML(
				"<a download=\"Viajes.xls\" "
						+ "onclick=\"return ExcellentExport.excel(this, 'hola', 'Viajes');\">"
						+ "Exportar a Excel</a>");
		RootPanel.get("contenedorLink").add(h);
		tablaViajes.getElement().setId("hola");
		dialogBox.setVisible(false);
		btnRestaurar.setVisible(false);
		cargarTabla();
	}

	public int getClickedRow() {
		int selectedRow = -1;
		for (int i = 1; i < tablaViajes.getRowCount(); ++i) {
			CheckBox checkBox = (CheckBox) tablaViajes.getWidget(i, 0);
			if (checkBox.getValue()) {
				selectedRow = i;
				return selectedRow;
			}
		}

		return selectedRow;
	}

	private void cargarTabla() {
		// Habilito los botones
		btnAceptar.setEnabled(true);
		btnModificar.setEnabled(true);
		btnEliminar.setEnabled(true);
		btnFacturar.setEnabled(true);
		tablaViajes.removeAllRows();
		tablaViajes.clear();
		CheckBox chk = new CheckBox();
		chk.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				seleccionarTodos(event);

			}
		});
		tablaViajes.setWidget(0, 0, chk);
		tablaViajes.setWidget(0, 1, new Label("Id"));
		tablaViajes.setWidget(0, 2, new Label("Dia"));
		tablaViajes.setWidget(0, 3, new Label("Mes"));
		tablaViajes.setWidget(0, 4, new Label("Anio"));
		tablaViajes.setWidget(0, 5, new Label("Toneladas"));
		tablaViajes.setWidget(0, 6, new Label("Origen"));
		tablaViajes.setWidget(0, 7, new Label("Monto"));
		tablaViajes.setWidget(0, 8, new Label("Fecha"));
		tablaViajes.setWidget(0, 9, new Label("Pago"));
		tablaViajes.setWidget(0, 10, new Label("Saldo"));
		tablaViajes.setWidget(0, 11, new Label("Descarga"));
		tablaViajes.setWidget(0, 12, new Label("Monto"));
		tablaViajes.setWidget(0, 13, new Label("Fecha"));
		tablaViajes.setWidget(0, 14, new Label("Pago"));
		tablaViajes.setWidget(0, 15, new Label("Saldo"));
		tablaViajes.setWidget(0, 16, new Label("Flete"));
		tablaViajes.setWidget(0, 17, new Label("Monto"));
		tablaViajes.setWidget(0, 18, new Label("Fecha"));
		tablaViajes.setWidget(0, 19, new Label("Pago"));
		tablaViajes.setWidget(0, 20, new Label("Saldo"));
		tablaViajes.setWidget(0, 21, new Label("Destino"));
		tablaViajes.setWidget(0, 22, new Label("Facturado"));
		tablaViajes.getRowFormatter().setStyleName(0, "watchListHeader");

		greetingService.getListaViajes(new AsyncCallback<List<Viaje>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error.");
			}

			@Override
			public void onSuccess(List<Viaje> result) {
				int i = 1;
				if (result != null) {
					int h;
					for (h = 0; h < result.size(); h++) {
						Viaje viaje = result.get(h);
						// Compruebo que el viaje no sea un cobro o pago
						if (!viaje.getFacturado().equals("pago")) {
							// Seteo los colores de la fila.
							if (viaje.isPendienteCarga()) {
								tablaViajes.getRowFormatter().setStyleName(i,
										"filaNormal");
							} else {
								if (!viaje.isPendienteCarga()
										&& viaje.isPendienteDePago()) {
									tablaViajes.getRowFormatter().setStyleName(
											i, "filaCargada");
								} else if (!viaje.isPendienteCarga()
										&& !viaje.isPendienteDePago()) {
									tablaViajes.getRowFormatter().setStyleName(
											i, "filaCargadaPaga");
								}
								if (!viaje.isPendienteDeDescarga()
										&& viaje.isPendienteDeCobro()) {
									tablaViajes.getRowFormatter().setStyleName(
											i, "filaDescargada");
								} else if (!viaje.isPendienteDeDescarga()
										&& !viaje.isPendienteDeCobro()) {
									tablaViajes.getRowFormatter().setStyleName(
											i, "filaDescargarCobrar");
								}
								if (!viaje.isPendienteCarga()
										&& !viaje.isPendienteDeDescarga()
										&& !viaje.getFacturado().equals("")
										&& viaje.getFacturado() != null) {
									tablaViajes.getRowFormatter().setStyleName(
											i, "filaViajeFacturado");
								}
							}
							// Fin seteo los colores de la fila.
							tablaViajes.setWidget(i, 0, new CheckBox());
							String fecha = viaje.getFecha();
							String dia = fecha.substring(0, fecha.indexOf("/"));
							fecha = fecha.substring(fecha.indexOf("/") + 1,
									fecha.length());
							String mes = fecha.substring(0, fecha.indexOf("/"));
							String teneladasS = "0";
							fecha = fecha.substring(fecha.indexOf("/") + 1,
									fecha.length());
							String anio = fecha;
							String idS = String.valueOf(viaje.getId());
							tablaViajes.setWidget(i, 1, new Label(idS));
							tablaViajes.setWidget(i, 2, new Label(dia));
							tablaViajes.setWidget(i, 3, new Label(mes));
							tablaViajes.setWidget(i, 4, new Label(anio));
							tablaViajes.setWidget(i, 6,
									new Label(viaje.getProveedor()));
							if (viaje.getKilosOrigenReales() == null)
								teneladasS = viaje.getKilos();
							else
								teneladasS = viaje.getKilosOrigenReales();
							tablaViajes.setWidget(i, 5,
									new Label(String.valueOf(teneladasS)));
							toneladasD = Double.parseDouble(teneladasS);
							// Calculo del monto que son las toneladas por el
							// precio
							// por tonelada original.
							// Inicializo todos los BigDecimal para darle
							BigDecimal bdTon = new BigDecimal("0.00");
							BigDecimal bdPrecioXton = new BigDecimal("0.00");
							BigDecimal bdMonto = new BigDecimal("0.00");
							bdTon = BigDecimal.valueOf(toneladasD);
							bdPrecioXton = BigDecimal.valueOf(Double
									.parseDouble(viaje.getPrecio_kilo()));
							bdMonto = bdPrecioXton.multiply(bdTon);
							tablaViajes.setWidget(i, 7,
									new Label(bdMonto.toString()));

							// Controlo que la fecha de pago del monto no sea
							// null
							if (viaje.getFechaPagar_origen() != null) {
								tablaViajes.setWidget(i, 8,
										new Label(viaje.getFechaPagar_origen()));
							}
							if (viaje.getPago() != null) {
								tablaViajes.setWidget(i, 9,
										new Label(viaje.getPago()));
								// Calculo el saldo del proveedor.
								BigDecimal bdPago = BigDecimal.valueOf(Double
										.parseDouble(viaje.getPago()));
								BigDecimal bdSaldo = bdMonto.subtract(bdPago);
								if (!bdSaldo.toString().equals("0"))
									tablaViajes.setWidget(i, 10, new Label(
											bdSaldo.toString()));
								else
									tablaViajes
											.setWidget(i, 10, new Label("-"));
							}
							tablaViajes.setWidget(i, 11,
									new Label(viaje.getNombre_descargador()));
							BigDecimal bdMontoDesc = BigDecimal.valueOf(Double
									.parseDouble(viaje.getMonto_descargador()));
							BigDecimal bdMontoDescXTon = bdMontoDesc
									.multiply(bdTon);
							tablaViajes.setWidget(i, 12, new Label(
									bdMontoDescXTon.toString()));
							if (viaje.getFecha_pago_descargador() != null) {
								tablaViajes.setWidget(
										i,
										13,
										new Label(viaje
												.getFecha_pago_descargador()));
							}
							if (viaje.getPago_descargados() != null
									&& !(viaje.getPago_descargados().equals(""))) {
								// Lo que se le paga al Descargador.
								BigDecimal bdPagoDesc = BigDecimal
										.valueOf(Double.parseDouble(viaje
												.getPago_descargados()));
								BigDecimal bdPagoDescXTon = new BigDecimal(
										"0.00");
								bdPagoDescXTon = bdPagoDesc.multiply(bdTon);
								tablaViajes.setWidget(i, 14, new Label(
										bdPagoDescXTon.toString()));
								BigDecimal bdSaldo = bdMontoDescXTon
										.subtract(bdPagoDescXTon);
								if (!bdSaldo.toString().equals("0"))
									tablaViajes.setWidget(i, 15, new Label(
											bdSaldo.toString()));
								else
									tablaViajes
											.setWidget(i, 15, new Label("-"));
							}
							tablaViajes.setWidget(i, 16,
									new Label(viaje.getFletero()));
							// Para el fletero
							if (viaje.getPrecioXtonelada_flete() != null) {
								BigDecimal bdPrecioXTonFletero = new BigDecimal(
										"0.00");
								if (viaje.getPrecioFleteroDescarga() == null)
									bdPrecioXTonFletero = BigDecimal.valueOf(Double.parseDouble(viaje
											.getPrecioXtonelada_flete()));
								else
									bdPrecioXTonFletero = BigDecimal.valueOf(Double.parseDouble(viaje
											.getPrecioFleteroDescarga()));
								// Lo que se le debe al Fletero.
								BigDecimal bdMontoFletero = bdPrecioXTonFletero
										.multiply(bdTon);
								tablaViajes.setWidget(i, 17, new Label(
										bdMontoFletero.toString()));
								if (viaje.getPago_flete() != null
										&& !(viaje.getPago_flete().equals(""))) {
									tablaViajes.setWidget(i, 18, new Label(
											viaje.getFechaPago_flete()));

									tablaViajes.setWidget(i, 19, new Label(
											bdMontoFletero.toString()));
									tablaViajes
											.setWidget(i, 20, new Label("-"));
								}
							}
							tablaViajes.setWidget(i, 21,
									new Label(viaje.getCliente()));
							tablaViajes.setWidget(i, 22,
									new Label(viaje.getFacturado()));
							i++;
						}
					}
				}
			}
		});
	}

	// Eventos de los botones
	@UiHandler("btnAceptar")
	void handleClickAgregar(ClickEvent e) {
		btnAceptar.setEnabled(false);
		CrearViaje crearViaje = new CrearViaje(null, ListarViajes.this);
		dialogBox = createDialogBox("Crear viaje.", crearViaje);
		dialogBox.center();
		dialogBox.show();
	}

	@UiHandler("btnModificar")
	void handleClickModificar(ClickEvent e) {
		if (getClickedRow() == -1) {
			Window.alert("Debe seleccionar un viaje.");
		} else {
			String viajeSel = tablaViajes.getText(getClickedRow(), 1);
			greetingService.getViaje(viajeSel, new AsyncCallback<Viaje>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Error.");
				}

				@Override
				public void onSuccess(Viaje result) {
					btnModificar.setEnabled(false);
					ModificarViaje modViaje = new ModificarViaje(result,
							ListarViajes.this);
					dialogBox = createDialogBox("Modificar viaje.", modViaje);
					dialogBox.center();
					dialogBox.show();
				}
			});
		}
	}

	public void cerrarDialogBox() {
		this.dialogBox.hide();
		cargarTabla();
	}

	@UiHandler("btnFacturar")
	void handleClickFacturar(ClickEvent e) {
		if (getClickedRow() == -1) {
			Window.alert("Debe seleccionar un viaje.");
		} else {
			List<Integer> lstSel = this.getSelectedRows();
			List<String> lstViajes = new ArrayList<String>();
			int posicion = 0;
			for (int j = 0; j < lstSel.size(); j++) {
				posicion = lstSel.get(j);
				String vjeSel = tablaViajes.getText(posicion, 1);
				if (!tablaViajes.getText(posicion, 22).equals("")) {
					Window.alert("El viaje " + vjeSel + " ya estaba facturado.");
				}
				lstViajes.add(vjeSel);
			}
			greetingService.getListaViajesSeleccionadosById(lstViajes,
					new AsyncCallback<List<Viaje>>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Error.");
						}

						@Override
						public void onSuccess(List<Viaje> result) {
							btnFacturar.setEnabled(false);
							Facturar facturar = new Facturar(result,
									ListarViajes.this);
							dialogBox = createDialogBox("Facturar.", facturar);
							dialogBox.center();
							dialogBox.show();
						}
					});
		}
	}

	// Se va a usar este boton para eliminar todos los viajes existentes en la
	// base de datos.
	@UiHandler("btnRestaurar")
	void handleClickRestaurar(ClickEvent e) {
		greetingService.limpiarDatos(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error.");
			}

			@Override
			public void onSuccess(String result) {
				Window.alert(result);
			}
		});
	}

	@UiHandler("btnEliminar")
	void handleClickCancelar(ClickEvent e) {
		if (getClickedRow() == -1) {
			Window.alert("Debe seleccionar un viaje.");
		} else {
			List<Integer> lstSel = this.getSelectedRows();
			List<String> lstViajes = new ArrayList<String>();
			int posicion = 0;
			for (int j = 0; j < lstSel.size(); j++) {
				posicion = lstSel.get(j);
				String vjeSel = tablaViajes.getText(posicion, 1);
				lstViajes.add(vjeSel);
			}
			greetingService.getListaViajesSeleccionadosById(lstViajes,
					new AsyncCallback<List<Viaje>>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Error.");
						}

						@Override
						public void onSuccess(List<Viaje> result) {
							btnEliminar.setEnabled(false);
							EliminarViaje elimViaje = new EliminarViaje(result,
									ListarViajes.this);
							dialogBox = createDialogBox("Eliminar viaje.",
									elimViaje);
							dialogBox.center();
							dialogBox.show();
						}
					});
		}
	}

	private DialogBox createDialogBox(String titulo, Widget widget) {
		final DialogBox dialogBox = new DialogBox();
		// Personalizo el dialogBox
		dialogBox.setText(titulo);
		// Create a table to layout the content
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.setSpacing(4);
		dialogBox.setWidget(dialogContents);
		// Pone el Dialog box con el fondo en gris y centrado
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		// Boton para cerrar.
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
		return dialogBox;
	}

	private void seleccionarTodos(ValueChangeEvent<Boolean> event) {
		for (int i = 1; i < tablaViajes.getRowCount(); i++) {
			CheckBox c = (CheckBox) tablaViajes.getWidget(i, 0);
			c.setValue(event.getValue());
			tablaViajes.setWidget(i, 0, c);
		}
	}

	public List<Integer> getSelectedRows() {
		List<Integer> selectedRows = new ArrayList<Integer>();

		for (int i = 1; i < tablaViajes.getRowCount(); ++i) {
			CheckBox checkBox = (CheckBox) tablaViajes.getWidget(i, 0);
			if (checkBox.getValue()) {
				selectedRows.add(i);
			}
		}

		return selectedRows;
	}
}
