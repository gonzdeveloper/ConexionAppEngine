package com.conexion.ventanas;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Usuario;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CrearUsuarioWeb extends Composite {

	private static CrearUsuarioWebUiBinder uiBinder = GWT
			.create(CrearUsuarioWebUiBinder.class);

	@UiField
	Button btnAceptar;
	@UiField
	Button btnModificar;
	@UiField
	TextBox tbNombreUsuario;
	@UiField
	PasswordTextBox tbClaveUsuario;
	@UiField
	Label errorLabel;
	@UiField
	ListBox tbRol;
	ListarUsuarios listarUsuarios;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface CrearUsuarioWebUiBinder extends UiBinder<Widget, CrearUsuarioWeb> {
	}

	public CrearUsuarioWeb(final Usuario usuarioSel, final ListarUsuarios listarUsuarios) {
		initWidget(uiBinder.createAndBindUi(this));
		errorLabel.addStyleName("serverResponseLabelError");
		this.listarUsuarios = listarUsuarios;
		// En caso de que se pase un usuario.
		if (usuarioSel != null) {
			// Muestro el botón modificar y oculto el botón aceptar.
			btnModificar.setVisible(true);
			btnAceptar.setVisible(false);
			tbNombreUsuario.setText(usuarioSel.getNombre());
			tbClaveUsuario.setText(usuarioSel.getPass());
			if (usuarioSel.getRol().equals("Administrador"))
				tbRol.setSelectedIndex(1);
			else
				tbRol.setSelectedIndex(0);

			// Manejo los eventos del enter
			tbNombreUsuario.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						modificarUsuario(usuarioSel);
					}
				}
			});
			tbClaveUsuario.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						btnModificar.setEnabled(false);
						modificarUsuario(usuarioSel);
					}
				}
			});
			btnModificar.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					btnModificar.setEnabled(false);
					modificarUsuario(usuarioSel);
				}
			});
		} else {
			btnModificar.setVisible(false);
			btnAceptar.setVisible(true);
			// Manejo los eventos del enter
			tbNombreUsuario.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						agregarUsuarioWeb();
					}
				}
			});
			tbClaveUsuario.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						btnAceptar.setEnabled(false);
						agregarUsuarioWeb();
					}
				}
			});
		}
	}

	private void modificarUsuario(Usuario usuarioSel) {
		// Realizo los controles de los campos.
		String valNombreUsuario = tbNombreUsuario.getText();
		String valClaveUsuario = tbClaveUsuario.getText();
		if ((!FieldVerifier.isValidName(valNombreUsuario))
				|| (!FieldVerifier.isValidName(valClaveUsuario))) {
			errorLabel.setText("El usuario o clave no pueden ser vacios.");
			return;
		}
		// Tomo los valores de los campos.
		usuarioSel.setNombre(tbNombreUsuario.getText());
		usuarioSel.setPass(tbClaveUsuario.getText());
		usuarioSel.setRol(tbRol.getItemText(tbRol.getSelectedIndex()));
		// Llamo al greetingService
		greetingService.ModificarUsuario(usuarioSel,
				new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						errorLabel
								.setText("Error al modificar el nuevo usuario.");
					}

					@Override
					public void onSuccess(String result) {
//						Window.alert("Se modifica el usuario correctamente.");
						listarUsuarios.cerrarDialogBox();

					}
				});

	}

	private void limpiarCampos() {
		errorLabel.setText("");
		tbNombreUsuario.setText("");
		tbClaveUsuario.setText("");
		tbRol.setSelectedIndex(0);
	}

	@UiHandler("btnAceptar")
	void handleClickAceptar(ClickEvent e) {
		btnAceptar.setEnabled(false);
		agregarUsuarioWeb();
		limpiarCampos();
	}

	private void agregarUsuarioWeb() {
		// Tomo los valores de los campos.
		String valNombreUsuario = tbNombreUsuario.getText();
		String valClaveUsuario = tbClaveUsuario.getText();
		String valRolUsuario = tbRol.getItemText(tbRol.getSelectedIndex());
		// Realizo los controles de los campos.
		if ((!FieldVerifier.isValidName(valNombreUsuario))
				|| (!FieldVerifier.isValidName(valClaveUsuario))) {
			errorLabel.setText("El usuario o clave no pueden ser vacios.");
			return;
		}
		// Llamo al greetingService
		greetingService.greetServerCrearUsuario(valNombreUsuario,
				valClaveUsuario, valRolUsuario, new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						errorLabel
								.setText("Error al agregar el nuevo usuario.");
					}

					@Override
					public void onSuccess(String result) {
//						Window.alert("Se agrego el usuario correctamente.");
						listarUsuarios.cerrarDialogBox();

					}
				});
	}
}
