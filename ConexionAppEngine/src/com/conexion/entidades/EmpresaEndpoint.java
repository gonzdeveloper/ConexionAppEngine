package com.conexion.entidades;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@Api(name = "empresaendpoint", namespace = @ApiNamespace(ownerDomain = "conexion.com", ownerName = "conexion.com", packagePath = "entidades"))
public class EmpresaEndpoint {

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listEmpresa")
	public CollectionResponse<Empresa> listEmpresa(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Empresa> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Empresa.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Empresa>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.String
			for (Empresa obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Empresa> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method.
	 *
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getEmpresa")
	public Empresa getEmpresa(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Empresa empresa = null;
		try {
			empresa = mgr.getObjectById(Empresa.class, id);
		} finally {
			mgr.close();
		}
		return empresa;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
	 *
	 * @param empresa
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertEmpresa")
	public Empresa insertEmpresa(Empresa empresa) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsEmpresa(empresa)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(empresa);
		} finally {
			mgr.close();
		}
		return empresa;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
	 *
	 * @param empresa
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateEmpresa")
	public Empresa updateEmpresa(Empresa empresa) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsEmpresa(empresa)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(empresa);
		} finally {
			mgr.close();
		}
		return empresa;
	}

	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
	 *
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeEmpresa")
	public void removeEmpresa(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Empresa empresa = mgr.getObjectById(Empresa.class, id);
			mgr.deletePersistent(empresa);
		} finally {
			mgr.close();
		}
	}

	private boolean containsEmpresa(Empresa empresa) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Empresa.class, empresa.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

	// //------------------------------------Inicio MisCambios

	// Como no puedo pasar los parametros "long" creo un libro con el MAX id
	// hasta le momento

	@ApiMethod(name = "obtenerMaxId")
	public Empresa obtenerMaxId() {

		Collection<Empresa> empresas = this.listEmpresa(null, null).getItems();
		Empresa emp = new Empresa();
		if (!empresas.isEmpty())
			emp.setId(((Empresa) (empresas.toArray())[empresas.size() - 1])
					.getId());
		else
			emp.setId(Long.valueOf(0));
		return emp;
	}

	@ApiMethod(name = "modificarCuentaCorriente")
	public void modificarCuentaCorriente(@Named("id") Long id,
			@Named("cuenta_corriente") String cuenta_corriente) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Empresa empresa = mgr.getObjectById(Empresa.class, id);
			float fCC = Float.valueOf(empresa.getCuenta_corriente());
			float cCTotal = fCC + Float.valueOf(cuenta_corriente);
			empresa.setCuenta_corriente(String.valueOf(cCTotal));
			mgr.makePersistent(empresa);
		} finally {
			mgr.close();
		}
	}

	@ApiMethod(name = "modificarCuentaCorrienteNombre")
	public void modificarCuentaCorrienteNombre(@Named("nombre") String nombre,
			@Named("cuenta_corriente") String cuenta_corriente) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Empresa emp = null;
			Query q = mgr.newQuery(Empresa.class, "nombre == '" + nombre + "'");
			Collection<Empresa> empresas = (Collection<Empresa>) q.execute();
			emp = empresas.iterator().next();
			BigDecimal fCC = new BigDecimal("0.00");
			BigDecimal cCTotal = new BigDecimal("0.00");
			fCC = BigDecimal.valueOf(Double.parseDouble(emp
					.getCuenta_corriente()));
			cCTotal = fCC.add(BigDecimal.valueOf(Double
					.parseDouble(cuenta_corriente)));
			emp.setCuenta_corriente(cCTotal.toString());
			mgr.makePersistent(emp);
		} finally {
			mgr.close();
		}
	}

	// DANIEL
	@ApiMethod(name = "obtenerEmpresaByNombre")
	public Empresa obtenerEmpresaByNombre(@Named("nombre") String nombre) {
		PersistenceManager mgr = getPersistenceManager();
		Empresa empresa = null;
		try {
			Query q = mgr.newQuery(Empresa.class, "nombre == '" + nombre + "'");
			Collection<Empresa> empresas = (Collection<Empresa>) q.execute();
			empresa = empresas.iterator().next();
		} finally {
			mgr.close();
		}
		return empresa;
	}

	@ApiMethod(name = "obtenerEmpresasPorTipo")
	public Collection<Empresa> obtenerEmpresasPorTipo(@Named("tipo") String tipo) {
		PersistenceManager mgr = getPersistenceManager();
		Query q = mgr.newQuery(Empresa.class, "tipo == '" + tipo + "'");
		Collection<Empresa> empresas = (Collection<Empresa>) q.execute();
		return empresas;

	}

	@ApiMethod(name = "eliminarEmpresas")
	public void eliminarEmpresas(@Named("cadena") String cadena) {
		String[] empresas = cadena.split(";");
		PersistenceManager mgr = getPersistenceManager();
		try {
			for (int i = 0; i < empresas.length; i++) {
				Empresa empresa = mgr.getObjectById(Empresa.class,
						Long.parseLong(empresas[i]));
				mgr.deletePersistent(empresa);
			}
			;
		} finally {
			mgr.close();
		}
	}

	public void limpiarDatos() {
		List<Empresa> empresas = new ArrayList<Empresa>();
		empresas = (List<Empresa>) this.listEmpresa(null, null).getItems();
		PersistenceManager mgr = getPersistenceManager();
		try {
			for (int i = 0; i < empresas.size(); i++) {
				Empresa emp = empresas.get(i);
				emp.setCuenta_corriente("0");
				mgr.makePersistent(emp);
			}
		} finally {
			mgr.close();
		}
	}

	@ApiMethod(name = "obtenerCCdelCliente")
	public List<String> obtenerCCdelCliente(@Named("cliente") String cliente) {
		// TODO Auto-generated method stub
		List<String> resultado = new ArrayList<String>();
		PersistenceManager mgr = getPersistenceManager();
		Empresa empresa = null;
		try {
			Query q = mgr
					.newQuery(Empresa.class, "nombre == '" + cliente + "'");
			Collection<Empresa> empresas = (Collection<Empresa>) q.execute();
			empresa = empresas.iterator().next();
		} finally {
			mgr.close();
		}
		resultado.add(0, empresa.getCuenta_corriente());
		return resultado;
	}

	// //------------------------------------Fin MisCambios

}
