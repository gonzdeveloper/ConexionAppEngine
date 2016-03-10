package com.conexion.ventanas;

import java.util.ArrayList;
import java.util.List;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Usuario;
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

public class ListarUsuarios extends Composite {

	private static ListarUsuariosUiBinder uiBinder = GWT
			.create(ListarUsuariosUiBinder.class);
	@UiField
	FlexTable contactsTable;
	@UiField
	Button addButton;
	@UiField
	Button modifyButton;
	@UiField
	Button deleteButton;
	@UiField
	DialogBox dialogBox;
	int apear = 0;

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface ListarUsuariosUiBinder extends UiBinder<Widget, ListarUsuarios> {
	}

	public ListarUsuarios() {
		initWidget(uiBinder.createAndBindUi(this));
		RootPanel.get("contenedorLink").clear();
		HTML h = new HTML(
				"<a download=\"Usuarios.xls\" "
						+ "onclick=\"return ExcellentExport.excel(this, 'hola', 'Usuarios');\">"
						+ "Exportar a Excel</a>");
		RootPanel.get("contenedorLink").add(h);
		contactsTable.getElement().setId("hola");
		apear = 0;
		dialogBox.setVisible(false);
		cargarTabla();
	}

	public int getClickedRow() {
		int selectedRow = -1;
		for (int i = 1; i < contactsTable.getRowCount(); ++i) {
			CheckBox checkBox = (CheckBox) contactsTable.getWidget(i, 0);
			if (checkBox.getValue()) {
				selectedRow = i;
				return selectedRow;
			}
		}

		return selectedRow;
	}

	private void cargarTabla() {
		//Habilito los botones
		addButton.setEnabled(true);
		modifyButton.setEnabled(true);
		deleteButton.setEnabled(true);
		contactsTable.removeAllRows();
		contactsTable.clear();
		CheckBox chk = new CheckBox();
		chk.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				seleccionarTodos(event);

			}
		});
		contactsTable.setWidget(0, 0, chk);
		contactsTable.setWidget(0, 1, new Label("Nombre"));
		contactsTable.setWidget(0, 2, new Label("Rol"));

		// Add styles to elements in the stock list table.
		contactsTable.getRowFormatter().setStyleName(0, "watchListHeader");

		greetingService.getListaUsuarios(new AsyncCallback<List<Usuario>>() {
			@Override
			public void onSuccess(List<Usuario> result) {
				// Cargo la tabla
				int i = 1;
				if (result != null) {
					int h;
					for (h = 0; h < result.size(); h++) {
						Usuario usu = result.get(h);
						contactsTable.setWidget(i, 0, new CheckBox());
						contactsTable.setText(i, 1, usu.getNombre());
						contactsTable.setText(i, 2, usu.getRol());
						i++;
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error.");
			}
		});
	}

	// Eventos de los botones
	@UiHandler("addButton")
	void handleClickAddButton(ClickEvent e) {
		addButton.setEnabled(false);
		CrearUsuarioWeb crearUsu = new CrearUsuarioWeb(null, ListarUsuarios.this);
		dialogBox = createDialogBox("Alta de usuario", crearUsu);
		dialogBox.center();
		dialogBox.show();
	}

	@UiHandler("modifyButton")
	void handleClickModifyButton(ClickEvent e) {
		modifyButton.setEnabled(false);
		String usuSel = contactsTable.getText(getClickedRow(), 1);
		// Debo hallar el usuario por el nombre
		greetingService.getUsuario(usuSel, new AsyncCallback<Usuario>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error.");
			}

			@Override
			public void onSuccess(Usuario result) {
				CrearUsuarioWeb crearUsu = new CrearUsuarioWeb(result, ListarUsuarios.this);
				dialogBox = createDialogBox("Editar usuario", crearUsu);
				dialogBox.center();
				dialogBox.show();

			}
		});
	}

	@UiHandler("deleteButton")
	void handleClickDeleteButton(ClickEvent e) {
		deleteButton.setEnabled(false);
		List<Integer> lstSel = this.getSelectedRows();
		List<String> lstUsuario = new ArrayList<String>();
		int posicion = 0;
		for (int j = 0; j < lstSel.size(); j++) {
			posicion = lstSel.get(j);
			String usuSel = contactsTable.getText(posicion, 1);
			lstUsuario.add(usuSel);
		}
		// Debo hallar los usuarios por su nombre
		greetingService.getListaUsuariosSeleccionadosByNombres(lstUsuario,
				new AsyncCallback<List<Usuario>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error.");
					}

					@Override
					public void onSuccess(List<Usuario> result) {
						EliminarUsuario eliminarUsuario = new EliminarUsuario(
								result, ListarUsuarios.this);
						dialogBox = createDialogBox("Eliminar usuario",
								eliminarUsuario);
						dialogBox.center();
						dialogBox.show();
					}
				});
	}

	private DialogBox createDialogBox(String titulo, Widget widget) {
		// Create a dialog box and set the caption text
		final DialogBox dialogBox = new DialogBox();
		apear = 1;
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
	
	
	public void cerrarDialogBox(){
		this.dialogBox.hide();
		cargarTabla();
	}


	private void seleccionarTodos(ValueChangeEvent<Boolean> event) {
		for (int i = 1; i < contactsTable.getRowCount(); i++) {
			CheckBox c = (CheckBox) contactsTable.getWidget(i, 0);
			c.setValue(event.getValue());
			contactsTable.setWidget(i, 0, c);
		}
	}

	public List<Integer> getSelectedRows() {
		List<Integer> selectedRows = new ArrayList<Integer>();

		for (int i = 1; i < contactsTable.getRowCount(); ++i) {
			CheckBox checkBox = (CheckBox) contactsTable.getWidget(i, 0);
			if (checkBox.getValue()) {
				selectedRows.add(i);
			}
		}

		return selectedRows;
	}
}
