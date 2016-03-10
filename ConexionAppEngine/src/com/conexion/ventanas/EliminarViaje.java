package com.conexion.ventanas;

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

public class EliminarViaje extends Composite {

	private static EliminarViajeUiBinder uiBinder = GWT
			.create(EliminarViajeUiBinder.class);

	@UiField
	Button btnAceptar;
	@UiField
	Label lblConf;
	@UiField
	Label lblSeg;
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface EliminarViajeUiBinder extends UiBinder<Widget, EliminarViaje> {
	}

	public EliminarViaje(final List<Viaje> viajes,
			final ListarViajes listarViajes) {
		initWidget(uiBinder.createAndBindUi(this));
		btnAceptar.setFocus(true);
		if (viajes.size() == 1) {
			lblConf.setText("¿Seguro que desea eliminar el viaje?");
			if (!viajes.get(0).isPendienteDeCobro()
					&& !viajes.get(0).isPendienteDePago()) {
				// Si el viaje que se va a eliminar ya fue completado, no se
				// modifica la CC.
				lblSeg.setText("No se modificara la cuenta corriente.");
			} else {
				lblSeg.setText("El viaje no fue completado, se restauran las CC.");
			}

		} else {
			lblConf.setText("¿Seguro que desea eliminar los viajes?");
			lblSeg.setText("Si los viajes no fueron completados se resturan las CC.");
		}
		lblSeg.addStyleName("serverResponseLabelError");
		btnAceptar.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
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
								listarViajes.cerrarDialogBox();
							}
						});
			}
		});
	}

}
