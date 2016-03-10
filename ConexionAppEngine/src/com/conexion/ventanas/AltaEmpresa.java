package com.conexion.ventanas;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Empresa;
import com.conexion.shared.FieldVerifier;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AltaEmpresa extends Composite {

	private static AltaEmpresaUiBinder uiBinder = GWT
			.create(AltaEmpresaUiBinder.class);
	@UiField
	TextBox tbNombreEmpresa;
	@UiField
	ListBox lbTipo;
	@UiField
	TextBox tbDireccionEmpresa;
	@UiField
	TextBox tbCCEmpresa;
	@UiField
	Button btnAceptar;
	@UiField
	Button btnModificar;
	@UiField
	Label errorLabel;
	ListarEmpresa le;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface AltaEmpresaUiBinder extends UiBinder<Widget, AltaEmpresa> {
	}

	public AltaEmpresa(final Empresa empresaSel, ListarEmpresa listarEmpresa) {
		initWidget(uiBinder.createAndBindUi(this));
		tbNombreEmpresa.setFocus(true);
		le = listarEmpresa;
		errorLabel.addStyleName("serverResponseLabelError");
		// Para el modificar empresa.
		if (empresaSel != null) {
			// Muestro el botón modificar y oculto el botón aceptar.
			btnModificar.setVisible(true);
			btnAceptar.setVisible(false);
			// DANIEL
			// Esto se debe descomentar para poder subir.
			// tbCCEmpresa.setEnabled(false);
			tbNombreEmpresa.setText(empresaSel.getNombre());
			if (empresaSel.getTipo() != null) {
				if (empresaSel.getTipo().equals("Proveedor")) {
					lbTipo.setSelectedIndex(0);
				} else if (empresaSel.getTipo().equals("Cliente")) {
					lbTipo.setSelectedIndex(1);
				} else {
					lbTipo.setSelectedIndex(2);
				}
			}
			tbDireccionEmpresa.setText(empresaSel.getDireccion());
			tbCCEmpresa
					.setText(String.valueOf(empresaSel.getCuenta_corriente()));

			// Manejo los eventos del enter
			tbNombreEmpresa.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						modificarUsuario(empresaSel);
					}
				}
			});
			tbDireccionEmpresa.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						modificarUsuario(empresaSel);
					}
				}
			});
			tbCCEmpresa.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						modificarUsuario(empresaSel);
					}
				}
			});

			btnModificar.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// Deshabilito el bot�n.
					btnModificar.setEnabled(false);
					modificarUsuario(empresaSel);
				}
			});
		} else {
			// Para el alta de empresas.
			btnModificar.setVisible(false);
			btnAceptar.setVisible(true);
			// Manejo los eventos del enter
			tbNombreEmpresa.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						agregarEmpresa();
						limpiarCampos();
					}
				}
			});
			tbDireccionEmpresa.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						agregarEmpresa();
						limpiarCampos();
					}
				}
			});
			tbCCEmpresa.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						agregarEmpresa();
						limpiarCampos();
					}
				}
			});
		}
	}

	@UiHandler("btnAceptar")
	void handleClickAceptar(ClickEvent e) {
		errorLabel.setText("");
		// Deshabilito el bot�n.
		btnAceptar.setEnabled(false);
		agregarEmpresa();
		limpiarCampos();
	}

	private void agregarEmpresa() {
		// Limpio el error label.
		errorLabel.setText("");
		// Tomo los valores de los campos.
		String valNombreEmpresa = tbNombreEmpresa.getText();
		String tipo = lbTipo.getItemText(lbTipo.getSelectedIndex());
		String valDireccionEmpresa = tbDireccionEmpresa.getText();
		String valCCEmpresa = "0";
		if (tbCCEmpresa.getText() != null && !tbCCEmpresa.getText().equals(""))
			valCCEmpresa = tbCCEmpresa.getText();
		// Compruebo que el saldo de la CC sea un numero.
		if (FieldVerifier.isNumeric(valCCEmpresa)) {
			greetingService.greetServer(valNombreEmpresa, tipo,
					valDireccionEmpresa, valCCEmpresa,
					new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {
							errorLabel
									.setText("Error al agregar la nueva empresa.");
						}

						@Override
						public void onSuccess(String result) {
							// Window.alert("Se agrega la empresa correctamente.");
							le.cerrarDialogBox();
						}
					});
		} else {
			Window.alert("El valor de la cuenta corriente debe ser un numero.");
		}
		// Vuelvo a habilitar el bot�n.
		// btnAceptar.setEnabled(true);
	}

	private void modificarUsuario(Empresa empresaSel) {
		// Tomo los valores de los campos.
		empresaSel.setNombre(tbNombreEmpresa.getText());
		empresaSel.setTipo(lbTipo.getItemText(lbTipo.getSelectedIndex()));
		empresaSel.setDireccion(tbDireccionEmpresa.getText());
		String valCCEmpresa = "0";
		if (tbCCEmpresa.getText() != null && !tbCCEmpresa.getText().equals(""))
			valCCEmpresa = tbCCEmpresa.getText();
		empresaSel.setCuenta_corriente(valCCEmpresa);
		empresaSel.setSaldoInicial(valCCEmpresa);
		// Llamo al greetingService
		greetingService.ModificarEmpresa(empresaSel,
				new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						errorLabel.setText("Error al modificar la empresa.");
					}

					@Override
					public void onSuccess(String result) {
						// Window.alert("Se modifico la empresa correctamente.");
						le.cerrarDialogBox();
					}
				});
		// Vuelvo a habilitar el bot�n.
		// btnModificar.setEnabled(true);
	}

	// Limpio los campos.
	private void limpiarCampos() {
		tbNombreEmpresa.setText("");
		lbTipo.setSelectedIndex(0);
		tbDireccionEmpresa.setText("");
		tbCCEmpresa.setText("");
	}

}
