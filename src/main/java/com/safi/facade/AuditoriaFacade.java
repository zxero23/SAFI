package com.safi.facade;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.Auditoria;
import com.safi.entity.Usuario;
import com.safi.entity.Servicio;
import com.safi.utilidad.Utilidad;
import org.apache.log4j.Logger;

/**
 *
 * @author Facundo Gonzalez
 */
@Stateless
public class AuditoriaFacade extends AbstractFacade<Auditoria> implements AuditoriaFacadeLocal {
    
    private static Logger log = Logger.getLogger(AuditoriaFacade.class);

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    UsuarioFacadeLocal usuarioFacade;
    @EJB
    EjercicioFacadeLocal ejercicioFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AuditoriaFacade() {
        super(Auditoria.class);
    }

    @Override
    public void create(String accion, Date fecha, Long idUsuario, String valorAnterior, String valorActual, Long idEjercicio) {
        try {
            log.debug("START create");
            Auditoria auditoriaAux = new Auditoria();
            auditoriaAux.setAccion(accion);
            auditoriaAux.setFecha(fecha);
            if (idEjercicio !=null){
                auditoriaAux.setEjercicio(this.ejercicioFacade.find(idEjercicio));                
            }
            Usuario usuario;
            if (idUsuario != null) {
                usuario = usuarioFacade.find(idUsuario);
                auditoriaAux.setUsuario(usuario);
                if (usuario != null){
                    auditoriaAux.setServicio(usuario.getServicio());
                }else{
                    auditoriaAux.setServicio(new Servicio());
                }
                        
            } else {
                usuario = new Usuario();
                usuario.setNombreUsuario("Usuario no identificado");
            }
            auditoriaAux.setValorAnterior(valorAnterior);
            auditoriaAux.setValorActual(valorActual);
            this.create(auditoriaAux);
        } catch (Exception e) {
            log.error("Error al crear la auditoría: " + e.getMessage());           
        }

    }

    @Override
    public List<Auditoria> findAll(Long idEjercicio, Long idServicio, Long idUsuario, String accion, String valor, Date fechaDesde, Date fechaHasta) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Auditoria as o where 1 = 1   ");
        if (fechaDesde != null) {
            Timestamp fDesde = new Timestamp(fechaDesde.getTime());
            query.append("  and o.fecha >= '").append(fDesde).append("' ");
        }
        if (fechaHasta != null) {
            Timestamp fHasta = new Timestamp(fechaHasta.getTime());
            fHasta.setHours(23);
            fHasta.setMinutes(59);
            fHasta.setSeconds(59);
            query.append("  and o.fecha <= '").append(fHasta).append("' ");
        }
        if (accion != null && !accion.isEmpty()) {
            query.append("  and upper(unaccent(auditoria.accion)) like '%").append(Utilidad.desacentuar(accion).toUpperCase()).append("%' ");
        }
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append("   and o.ejercicio.id = ").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L) {
            query.append("   and o.servicio.id = ").append(idServicio);
        }
        if (idUsuario != null && idUsuario != 0L) {
            query.append("   and o.usuario.id = ").append(idUsuario);
        }
        if (valor != null && !valor.isEmpty()) {
            query.append(" and (");
                query.append(" (upper(unaccent(auditoria.valoranterior)) like '%").append(Utilidad.desacentuar(valor).toUpperCase()).append("%') ");
                    query.append(" or");
                query.append(" (upper(unaccent(auditoria.valoractual)) like '%").append(Utilidad.desacentuar(valor).toUpperCase()).append("%') ");
            query.append(")");
        }
        query.append("   ORDER BY o.fecha DESC");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Auditoria> findAll(Long idEjercicio, Long idServicio, Long idUsuario, String accion, String valor, Date fechaDesde, Date fechaHasta, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Auditoria as o where 1 = 1   ");
        if (fechaDesde != null) {
            Timestamp fDesde = new Timestamp(fechaDesde.getTime());
            query.append("  and o.fecha >= '").append(fDesde).append("' ");
        }
        if (fechaHasta != null) {
            Timestamp fHasta = new Timestamp(fechaHasta.getTime());
            fHasta.setHours(23);
            fHasta.setMinutes(59);
            fHasta.setSeconds(59);
            query.append("  and o.fecha <= '").append(fHasta).append("' ");
        }
        if (accion != null && !accion.isEmpty()) {
            query.append("  and upper(FUNCTION('unaccent', o.accion)) like '%").append(Utilidad.desacentuar(accion).toUpperCase()).append("%' ");
        }
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append("   and o.ejercicio.id = ").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L) {
            query.append("   and o.servicio.id = ").append(idServicio);
        }
        if (idUsuario != null && idUsuario != 0L) {
            query.append("   and o.usuario.id = ").append(idUsuario);
        }
        if (valor != null && !valor.isEmpty()) {
            query.append(" and (");
                query.append(" (upper(FUNCTION('unaccent', o.valorAnterior)) like '%").append(Utilidad.desacentuar(valor).toUpperCase()).append("%') ");
                    query.append(" or");
                query.append(" (upper(FUNCTION('unaccent', o.valorActual)) like '%").append(Utilidad.desacentuar(valor).toUpperCase()).append("%') ");
            query.append(")");
        }
        query.append("   ORDER BY o.fecha DESC");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Long idEjercicio, Long idServicio, Long idUsuario, String accion, String valor, Date fechaDesde, Date fechaHasta) {
        StringBuilder query = new StringBuilder();
        query.append("select count(o) FROM Auditoria as o where 1 = 1   ");
        if (fechaDesde != null) {
            Timestamp fDesde = new Timestamp(fechaDesde.getTime());
            query.append("  and o.fecha >= '").append(fDesde).append("' ");
        }
        if (fechaHasta != null) {
            Timestamp fHasta = new Timestamp(fechaHasta.getTime());
            fHasta.setHours(23);
            fHasta.setMinutes(59);
            fHasta.setSeconds(59);
            query.append("  and o.fecha <= '").append(fHasta).append("' ");
        }
        if (accion != null && !accion.isEmpty()) {
            query.append("  and upper(FUNCTION('unaccent', o.accion)) like '%").append(Utilidad.desacentuar(accion).toUpperCase()).append("%' ");
        }
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append("   and o.ejercicio.id = ").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L) {
            query.append("   and o.servicio.id = ").append(idServicio);
        }
        if (idUsuario != null && idUsuario != 0L) {
            query.append("   and o.usuario.id = ").append(idUsuario);
        }
        if (valor != null && !valor.isEmpty()) {
            query.append(" and (");
                query.append(" (upper(FUNCTION('unaccent', o.valorAnterior)) like '%").append(Utilidad.desacentuar(valor).toUpperCase()).append("%') ");
                    query.append(" or");
                query.append(" (upper(FUNCTION('unaccent', o.valorActual)) like '%").append(Utilidad.desacentuar(valor).toUpperCase()).append("%') ");
            query.append(")");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @Override
    public List<Servicio> findServicios() {
        List<Servicio> lstServicios = new ArrayList<>();
        this.findAll().stream().filter((auditoriaAux) -> (auditoriaAux.getUsuario() != null && auditoriaAux.getUsuario().getServicio() != null)).forEach((auditoriaAux) -> {
            lstServicios.add(auditoriaAux.getUsuario().getServicio());
        });
        return lstServicios.stream()
                .sorted((s1, s2) -> (s1.getCodigo().compareTo(s2.getCodigo())))//ordena por codigo
                .filter(Utilidad.distinctByKey(p -> p.getId())) //se distingue entre id del servicio                  
                .collect(Collectors.toList());
    }      
}
