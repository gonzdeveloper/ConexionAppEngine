package com.conexion.ventanas;

import java.util.ArrayList;
import java.util.List;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Viaje;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class EliminarPagoCobroParcial extends Composite {

	private static EliminarPagoCobroParcialUiBinder uiBinder = GWT
			.create(EliminarPagoCobroParcialUiBinder.class);

	interface EliminarPagoCobroParcialUiBinder extends
			UiBinder<Widget, EliminarPagoCobroParcial> {
	}

	@UiField
	Button btnAceptar;
	@UiField
	Label lblConf;
	List<Viaje> viajes;
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	public EliminarPagoCobroParcial(final Viaje viaje,
			final InformeProveedor informeProveedor,
			final InformeClientes informeClientes, final String accion) {
		initWidget(uiBinder.createAndBindUi(this));
		btnAceptar.setFocus(true);
		viajes = new ArrayList<Viaje>();
		if (accion.equals("pagar")) {
			lblConf.setText("¿Seguro que desea eliminar el pago parcial?");
		} else {
			lblConf.setText("¿Seguro que desea eliminar el cobro parcial?");
		}
		btnAceptar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				viajes.add(viaje);
				// Deshabilito el boton.
				btnAceptar.setEnabled(false);
				eliminarViaje(viajes);
			}

			private void eliminarViaje(List<Viaje> viaje) {
				greetingService.eliminarViajesSeleccionados(viaje,
						new AsyncCallback<String>() {
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Error.");
							}
							@Override
							public void onSuccess(String result) {
								if (accion.equals("pagar")) {
									informeProveedor.cerrarDialogBox();
								} else {
									informeClientes.cerrarDialogBox();
								}
							}
						});
			}
		});
	}
}
