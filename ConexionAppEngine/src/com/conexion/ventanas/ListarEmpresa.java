package com.conexion.ventanas;

import java.util.ArrayList;
import java.util.List;

import com.conexion.client.GreetingService;
import com.conexion.client.GreetingServiceAsync;
import com.conexion.entidades.Empresa;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListarEmpresa extends Composite {

	private static ListarEmpresaUiBinder uiBinder = GWT
			.create(ListarEmpresaUiBinder.class);
	@UiField
	Button btnAgregar;
	@UiField
	Button btnModificar;
	@UiField
	Button btnEliminar;
	@UiField
	FlexTable tablaEmpresas;
	@UiField
	DialogBox dialogBox;
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	interface ListarEmpresaUiBinder extends UiBinder<Widget, ListarEmpresa> {
	}

	public ListarEmpresa() {
		initWidget(uiBinder.createAndBindUi(this));
		RootPanel.get("contenedorLink").clear();
		HTML h = new HTML(
				"<a download=\"Empresas.xls\" "
						+ "onclick=\"return ExcellentExport.excel(this, 'hola', 'Empresas');\">"
						+ "Exportar a Excel</a>");
		RootPanel.get("contenedorLink").add(h);
		tablaEmpresas.getElement().setId("hola");
		dialogBox.setVisible(false);
		cargarTabla();
	}

	public int getClickedRow() {
		int selectedRow = -1;
		for (int i = 1; i < tablaEmpresas.getRowCount(); ++i) {
			CheckBox checkBox = (CheckBox) tablaEmpresas.getWidget(i, 0);
			if (checkBox.getValue()) {
				selectedRow = i;
				return selectedRow;
			}
		}
		return selectedRow;
	}

	private void cargarTabla() {
		//Habilito los botones
		btnAgregar.setEnabled(true);
		btnModificar.setEnabled(true);
		btnEliminar.setEnabled(true);
		tablaEmpresas.removeAllRows();
		tablaEmpresas.clear();
		tablaEmpresas.getElement().setId("hola");
		CheckBox chk = new CheckBox();
		chk.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				seleccionarTodos(event);

			}
		});
		tablaEmpresas.setWidget(0, 0, chk);
		tablaEmpresas.setWidget(0, 1, new Label("Nombre"));
		tablaEmpresas.setWidget(0, 2, new Label("Tipo"));
		tablaEmpresas.setWidget(0, 3, new Label("Direccion"));
		tablaEmpresas.setWidget(0, 4, new Label("Cuenta corriente"));
		tablaEmpresas.getRowFormatter().setStyleName(0, "watchListHeader");

		greetingService.getListaEmpresas(new AsyncCallback<List<Empresa>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error.");

			}

			@Override
			public void onSuccess(List<Empresa> result) {
				int i = 1;
				if (result != null) {
					int h;
					for (h = 0; h < result.size(); h++) {
						Empresa emp = result.get(h);
						tablaEmpresas.setWidget(i, 0, new CheckBox());
						tablaEmpresas.setWidget(i, 1,
								new Label(emp.getNombre()));
						tablaEmpresas.setWidget(i, 2, new Label(emp.getTipo()));
						tablaEmpresas.setWidget(i, 3,
								new Label(emp.getDireccion()));
						tablaEmpresas.setWidget(i, 4,
								new Label(emp.getCuenta_corriente()));
						i++;
					}
				}
			}
		});
	}

	// Eventos de los botones
	@UiHandler("btnAgregar")
	void handleClickAgregar(ClickEvent e) {
		btnAgregar.setEnabled(false);
		AltaEmpresa crearEmp = new AltaEmpresa(null, ListarEmpresa.this);
		dialogBox = createDialogBox("Alta de empresa.", crearEmp);
		dialogBox.center();
		dialogBox.show();		
	}

	@UiHandler("btnModificar")
	void handleClickModificar(ClickEvent e) {
		String empSel = tablaEmpresas.getText(getClickedRow(), 1);
		greetingService.getEmpresa(empSel, new AsyncCallback<Empresa>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error.");
			}

			@Override
			public void onSuccess(Empresa result) {
				btnModificar.setEnabled(false);
				AltaEmpresa modifEmp = new AltaEmpresa(result, ListarEmpresa.this);
				dialogBox = createDialogBox("Editar usuario", modifEmp);
				dialogBox.center();
				dialogBox.show();				

			}
		});
	}

	@UiHandler("btnEliminar")
	void handleClickCancelar(ClickEvent e) {
		List<Integer> lstSel = this.getSelectedRows();
		List<String> lstEmpresa = new ArrayList<String>();
		int posicion = 0;
		for (int j = 0; j < lstSel.size(); j++) {
			posicion = lstSel.get(j);
			String usuSel = tablaEmpresas.getText(posicion, 1);
			lstEmpresa.add(usuSel);
		}
		greetingService.getListaEmpresasSeleccionadasByNombres(lstEmpresa,
				new AsyncCallback<List<Empresa>>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error.");
					}

					@Override
					public void onSuccess(List<Empresa> result) {
						btnEliminar.setEnabled(false);
						EliminarEmpresa elimEmp = new EliminarEmpresa(result, ListarEmpresa.this);
						dialogBox = createDialogBox("Eliminar empresa.",
								elimEmp);
						dialogBox.center();
						dialogBox.show();

					}
				});
	}

	private DialogBox createDialogBox(String titulo, Widget widget) {
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
		dialogContents.add(widget);
		dialogContents.add(closeButton);
		return dialogBox;
	}

	public void cerrarDialogBox(){
		this.dialogBox.hide();
		cargarTabla();
	}
	
	private void seleccionarTodos(ValueChangeEvent<Boolean> event) {
		for (int i = 1; i < tablaEmpresas.getRowCount(); i++) {
			CheckBox c = (CheckBox) tablaEmpresas.getWidget(i, 0);
			c.setValue(event.getValue());
			tablaEmpresas.setWidget(i, 0, c);
		}
	}

	public List<Integer> getSelectedRows() {
		List<Integer> selectedRows = new ArrayList<Integer>();
		for (int i = 1; i < tablaEmpresas.getRowCount(); ++i) {
			CheckBox checkBox = (CheckBox) tablaEmpresas.getWidget(i, 0);
			if (checkBox.getValue()) {
				selectedRows.add(i);
			}
		}
		return selectedRows;
	}
}
