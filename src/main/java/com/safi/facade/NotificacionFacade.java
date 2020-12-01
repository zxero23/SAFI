package com.safi.facade;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import com.safi.entity.Usuario;
import com.safi.entity.Notificacion;

/**
 * @author Doroñuk Gustavo
 */
@Stateless
public class NotificacionFacade extends AbstractFacade<Notificacion> implements NotificacionFacadeLocal {
    
    private static Logger log = Logger.getLogger(NotificacionFacade.class);
    
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotificacionFacade() {
        super(Notificacion.class);
    }
    
    @Override
    public void create(Long idUsuario, String mensaje) throws Exception {
        try {
            log.debug("START create");
            Usuario usuario = this.usuarioFacade.find(idUsuario);
            Notificacion notificacion = new Notificacion();
            notificacion.setUsuarioEmisor(usuario);
            notificacion.setMensaje(mensaje);
            notificacion.setLeido(false);
            notificacion.setFecha(new Date());
            this.create(notificacion);
        } catch (Exception e) {
            log.error("Error al crear la notificación: " + e.getMessage());
            throw new Exception("Error al crear la notificación: " + e.getMessage());
        }
    }
    
    @Override
    public void leer(Long id) throws Exception {
        try {
            log.debug("START leer");
            Notificacion notificacion = this.find(id);
            notificacion.setLeido(true);
            this.edit(notificacion);
        } catch(Exception e) {
            log.error("Error al leer la notificación: " + e.getMessage());
            throw new Exception("Error al leer la notificación: " + e.getMessage());
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Notificacion> findAll(Long idServicio, int leido, Date fechaDesdeBsq, Date fechaHastaBsq, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Notificacion as o WHERE 1 = 1 ");
        if (idServicio != null && idServicio != 0L) {
            query.append(" AND o.usuarioEmisor.servicio.id = ").append(idServicio);
        }
        switch(leido) {
            case 0:
                break;
            case 1:
                query.append(" and o.leido = true");
                break;
            case 2:
                query.append(" and o.leido = false");
                break;
            default:
                break;
        }
        if (fechaDesdeBsq != null) {
            Timestamp fDesde = new Timestamp(fechaDesdeBsq.getTime());
            query.append("  and o.fecha >= '").append(fDesde).append("' ");
        }
        if (fechaHastaBsq != null) {
            Timestamp fHasta = new Timestamp(fechaHastaBsq.getTime());
            fHasta.setHours(23);
            fHasta.setMinutes(59);
            fHasta.setSeconds(59);
            query.append("  and o.fecha <= '").append(fHasta).append("' ");
        }
        query.append(" ORDER BY o.fecha DESC");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @Override
    public Long count(Long idServicio, int leido, Date fechaDesdeBsq, Date fechaHastaBsq) {
        StringBuilder query = new StringBuilder();
        query.append("select count(o) FROM Notificacion as o WHERE 1 = 1 ");
        if (idServicio != null && idServicio != 0L) {
            query.append(" AND o.usuarioEmisor.servicio.id = ").append(idServicio);
        }
        switch(leido) {
            case 0:
                break;
            case 1:
                query.append(" and o.leido = true");
                break;
            case 2:
                query.append(" and o.leido = false");
                break;
            default:
                break;
        }
        if (fechaDesdeBsq != null) {
            Timestamp fDesde = new Timestamp(fechaDesdeBsq.getTime());
            query.append("  and o.fecha >= '").append(fDesde).append("' ");
        }
        if (fechaHastaBsq != null) {
            Timestamp fHasta = new Timestamp(fechaHastaBsq.getTime());
            fHasta.setHours(23);
            fHasta.setMinutes(59);
            fHasta.setSeconds(59);
            query.append("  and o.fecha <= '").append(fHasta).append("' ");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }
    
}