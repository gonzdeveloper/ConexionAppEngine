package com.conexion.entidades;

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

@Api(name = "finanzasendpoint", namespace = @ApiNamespace(ownerDomain = "conexion.com", ownerName = "conexion.com", packagePath = "entidades"))
public class FinanzasEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listFinanzas")
	public CollectionResponse<Finanzas> listFinanzas(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Finanzas> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Finanzas.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Finanzas>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Finanzas obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Finanzas> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getFinanzas")
	public Finanzas getFinanzas(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Finanzas finanzas = null;
		try {
			finanzas = mgr.getObjectById(Finanzas.class, id);
		} finally {
			mgr.close();
		}
		return finanzas;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param finanzas the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertFinanzas")
	public Finanzas insertFinanzas(Finanzas finanzas) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsFinanzas(finanzas)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(finanzas);
		} finally {
			mgr.close();
		}
		return finanzas;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param finanzas the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateFinanzas")
	public Finanzas updateFinanzas(Finanzas finanzas) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsFinanzas(finanzas)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(finanzas);
		} finally {
			mgr.close();
		}
		return finanzas;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeFinanzas")
	public void removeFinanzas(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Finanzas finanzas = mgr.getObjectById(Finanzas.class, id);
			mgr.deletePersistent(finanzas);
		} finally {
			mgr.close();
		}
	}

	private boolean containsFinanzas(Finanzas finanzas) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Finanzas.class, finanzas.getId());
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
	@ApiMethod(name = "obtenerMaxId")
	public Finanzas obtenerMaxId() {
		Collection<Finanzas> finanzas = this.listFinanzas(null, null).getItems();
		Finanzas fin = new Finanzas();
		if (!finanzas.isEmpty())
			fin.setId(((Finanzas) (finanzas.toArray())[finanzas.size() - 1])
					.getId());
		else
			fin.setId(Long.valueOf(0));
		return fin;
	}
	
	// Retorna el viaje fantasma utilizado para pagar/cobrar un viaje dado.
	@ApiMethod(name = "obtenerViajeFantasma")
	public List<Viaje> obtenerViajeFantasma(@Named("idViaje") String idViaje, @Named("tipo") String tipo) {		
		PersistenceManager mgr = getPersistenceManager();
		List<Viaje> viajesFantasmas = new ArrayList<Viaje>();
		try {
			Query q = mgr.newQuery(Finanzas.class, "idViaje == '" + idViaje
					+ "' && tipo == '" + tipo + "'");
			@SuppressWarnings("unchecked")
			List<Finanzas> finanzas = (List<Finanzas>) q.execute();
			for (int i = 0; i < finanzas.size(); i++) {
				Finanzas fin = finanzas.get(i);
				Viaje viaje = new ViajeEndpoint().getViaje(Long.parseLong(fin.getIdViajeFantasma()));
				viajesFantasmas.add(viaje);
			}
		} finally {
			mgr.close();
		}
		return viajesFantasmas;
	}
	
	// Retorna true si el viaje fue cobrado por algun cobro parcial.
	@SuppressWarnings("unchecked")
	public boolean fueCobradoCobroParcial(String idViaje) {
		PersistenceManager mgr = getPersistenceManager();
		List<Finanzas> finanzas = new ArrayList<Finanzas>();
		try {
			Query q = mgr.newQuery(Finanzas.class, "idViaje == '" + idViaje
					+ "' && tipo == 'cobrado'");
			finanzas = (List<Finanzas>) q.execute();
		} finally {
			mgr.close();
		}
		return (finanzas.size() != 0);
	}

	// Retorna true si el viaje fue cobrado por algun cobro parcial.
	@SuppressWarnings("unchecked")
	public boolean fuePagadoPagoParcial(String idViaje) {
		PersistenceManager mgr = getPersistenceManager();
		List<Finanzas> finanzas = new ArrayList<Finanzas>();
		try {
			Query q = mgr.newQuery(Finanzas.class, "idViaje == '" + idViaje
					+ "' && tipo == 'pagado'");
			finanzas = (List<Finanzas>) q.execute();
		} finally {
			mgr.close();
		}
		return (finanzas.size() != 0);
	}
	// //------------------------------------Fin MisCambios
}
