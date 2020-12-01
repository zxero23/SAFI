/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.safi.facade;

import java.util.ArrayList;
import org.safi.facade.AbstractFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.safi.entity.ClasificadorOrganismo;
import org.safi.entity.Organismo;
import org.safi.entity.OrganismoServicio;
import org.safi.entity.Servicio;
import org.safi.utilidad.Utilidad;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class OrganismoFacade extends AbstractFacade<Organismo> implements OrganismoFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    private ClasificadorOrganismoFacadeLocal clasificadorOrganismoFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrganismoFacade() {
        super(Organismo.class);
    }

    /**
     * @author Alvarenga Angel
     * @param estado
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Organismo> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Organismo as o WHERE o.estado = :p1 ORDER BY o.codigoOrganismo ASC");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    /**
     * @author Gonzalez Facundo
     * @param estado
     * @param idServicio
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Organismo> findAll(Long idServicio, boolean estado) {
        Query consulta = em.createQuery("select distinct object(o) from Organismo as o, in(o.servicios)a WHERE o.estado = :p1 and a.id = :p2 order by  CAST(o.id AS BIGINT)");
        consulta.setParameter("p1", estado);
        consulta.setParameter("p2", idServicio);

        return consulta.getResultList();
    }

    /**
     * @author Alvarenga Angel
     * @param codigo
     * @return Organismo
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Organismo findByCodigo(Long codigo) {
        Organismo organismoAux;
        try {
            Query consulta = em.createQuery("select object(o) from Organismo as o WHERE o.codigoOrganismo = :p1");
            consulta.setParameter("p1", codigo);
            organismoAux = (Organismo) consulta.getSingleResult();
        } catch (Exception e) {
            organismoAux = null;
        }
        return organismoAux;
    }

    /**
     * @author Matias Zakowicz Busca todos los organismos que no tengan asociado
     * un servicio
     * @param estado
     * @return List Organismo
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Organismo> findAllNotServicio(boolean estado) {
        Query consulta = em.createNativeQuery("select o.* from organismos o left join organismos_servicios os on o.id = os.organismo_id where o.estado = ? and os.organismo_id is null order by o.id asc",Organismo.class);
        consulta.setParameter(1, estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public boolean existsOrganismo(Long codigoOrganismo) {
        Query consulta = em.createQuery("select object(o) from Organismo as o WHERE o.estado = true and o.codigoOrganismo = :p1 ");
        consulta.setParameter("p1", codigoOrganismo);
        return consulta.getResultList().isEmpty();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Organismo> findAll(String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Organismo as o WHERE o.estado = true ");
        query.append("AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
        query.append(Utilidad.desacentuar(nombre).toUpperCase());
        query.append("%' ORDER BY o.nombre ");

        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    /**
     * @author Alvarenga Angel
     * @param organismosId
     * @return List Organismo
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Organismo> findAll(List<Long> organismosId) {
        String iDes = organismosId.toString().substring(1, organismosId.toString().length() - 1);
        return em.createQuery("select object (o) from Organismo as o where o.id in (" + iDes + ")").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Organismo> findAll(Long codigoOrganismo, String nombre,
            Long idOrganismoPadre, Long idClasificadorOrganismo) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Organismo as o WHERE o.estado = true ");

        if (codigoOrganismo != null) {
            query.append(" and o.codigoOrganismo =").append(codigoOrganismo);
        }
        if (nombre != null && !nombre.equals("")) {
            query.append(" and upper(FUNCTION('unaccent', o.nombre)) LIKE '%").append(Utilidad.desacentuar(nombre).toUpperCase()).append("%'");
        }
        if (idOrganismoPadre != null && !(idOrganismoPadre < 0L)) {
            query.append(" and o.organismoPadre.id =").append(idOrganismoPadre);
        }
        if (idClasificadorOrganismo != null && idClasificadorOrganismo != 0L) {
            query.append(" and o.clasificadorOrganismo.id =").append(idClasificadorOrganismo);
        }
        query.append(" ORDER BY o.codigoOrganismo ASC");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Organismo> findAll(Long codigoOrganismo, String nombre,
            Long idOrganismoPadre, Long idClasificadorOrganismo, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Organismo as o WHERE o.estado = true ");

        if (codigoOrganismo != null) {
            query.append(" and o.codigoOrganismo =").append(codigoOrganismo);
        }
        if (nombre != null && !nombre.equals("")) {
            query.append(" and upper(FUNCTION('unaccent', o.nombre)) LIKE '%").append(Utilidad.desacentuar(nombre).toUpperCase()).append("%'");
        }
        if (idOrganismoPadre != null && !(idOrganismoPadre < 0L)) {
            query.append(" and o.organismoPadre.id =").append(idOrganismoPadre);
        }
        if (idClasificadorOrganismo != null && idClasificadorOrganismo != 0L) {
            query.append(" and o.clasificadorOrganismo.id =").append(idClasificadorOrganismo);
        }
        query.append(" ORDER BY o.codigoOrganismo ASC");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Long codigoOrganismo, String nombre,
            Long idOrganismoPadre, Long idClasificadorOrganismo) {
        StringBuilder query = new StringBuilder();
        query.append("select count(o) FROM Organismo as o WHERE o.estado = true ");

        if (codigoOrganismo != null) {
            query.append(" and o.codigoOrganismo =").append(codigoOrganismo);
        }
        if (nombre != null && !nombre.equals("")) {
            query.append(" and upper(FUNCTION('unaccent', o.nombre)) LIKE '%").append(Utilidad.desacentuar(nombre).toUpperCase()).append("%'");
        }
        if (idOrganismoPadre != null && !(idOrganismoPadre < 0L)) {
            query.append(" and o.organismoPadre.id =").append(idOrganismoPadre);
        }
        if (idClasificadorOrganismo != null && idClasificadorOrganismo != 0L) {
            query.append(" and o.clasificadorOrganismo.id =").append(idClasificadorOrganismo);
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    /**
     * @author Matias Zakowicz
     * @Modificado Alvarenga Angel
     * @fecha 23/12/2019
     * @param idOrganismoPadre
     * @param idClasificadorOrganismo
     * @param codigoOrganismo
     * @param nombre
     * @param direccion
     * @param telefonos
     * @param correoOficial
     * @throws Exception
     */
    @Override
    public void create(Long idOrganismoPadre, Long idClasificadorOrganismo, Long codigoOrganismo,
            String nombre, String direccion, String telefonos, String correoOficial,Long idServicio) throws Exception {
        try {
            List<Organismo> organismoFindNombre = this.findAll(nombre);
//            if (organismoFindNombre.isEmpty()) {

            ClasificadorOrganismo clasificadorOrganismoAux = this.clasificadorOrganismoFacade.find(idClasificadorOrganismo);
            Organismo organismoAux = new Organismo();
            organismoAux.setCodigoOrganismo(codigoOrganismo);
            organismoAux.setNombre(nombre.toUpperCase());
            organismoAux.setDireccion(direccion);
            organismoAux.setTelefonos(telefonos);
            organismoAux.setCorreoOficial(correoOficial);
            organismoAux.setClasificadorOrganismo(clasificadorOrganismoAux);
            organismoAux.setEstado(true);
            Long idOrganismo = this.createEspecial(organismoAux).getId();
            OrganismoServicio organismoServicio = new OrganismoServicio();
            organismoServicio.setOrganismos_id(idOrganismo);
            organismoServicio.setServicios_id(idServicio);
            this.createOServicio(organismoServicio);
            if (idOrganismoPadre != -1L) {
                Organismo organismoPadre = this.find(idOrganismoPadre);
                organismoAux.setOrganismoPadre(organismoPadre);
                this.edit(organismoAux);
                organismoPadre.getOrganismos().add(organismoAux);
                this.edit(organismoPadre);
            } else if (organismoAux.getOrganismoPadre() != null) {
                Organismo organismoPadre = organismoAux.getOrganismoPadre();
                organismoAux.setOrganismoPadre(null);
                this.edit(organismoAux);
                organismoPadre.getOrganismos().remove(organismoAux);
                this.edit(organismoPadre);
            }
//            } else {
//                throw new Exception("El nombre de organismo ya se encuentra registrado. ");
//            }
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el organismo. " + e.getMessage());
        }
    }

    @Override
    public void remove(Long idOrganismo) throws Exception {
        try {
            Organismo organismoAux = this.find(idOrganismo);
            organismoAux.setEstado(false);
            OrganismoServicio organismoServicio = this.findOServicio(idOrganismo);
            this.removeOServicio(organismoServicio);
            this.edit(organismoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el organismo.");
        }
    }

    /**
     * @author Alvarenga Angel
     * @param id
     * @param idOrganismoPadre
     * @param idClasificadorOrganismo
     * @param codigoOrganismo
     * @param nombre
     * @param direccion
     * @param telefonos
     * @param correoOficial
     * @throws Exception
     */
    @Override
    public void edit(Long id, Long idOrganismoPadre, Long idClasificadorOrganismo,
            Long codigoOrganismo, String nombre, String direccion, String telefonos,
            String correoOficial,Long idServicio) throws Exception {
        try {
            Organismo organismoAuxPadre = null;
            if (idOrganismoPadre != -1) {
                organismoAuxPadre = this.find(idOrganismoPadre);
            }

            ClasificadorOrganismo clasificadorOrganismoAux = this.clasificadorOrganismoFacade.find(idClasificadorOrganismo);

            Organismo organismoAux = this.find(id);
            
            OrganismoServicio organismoServicio = new OrganismoServicio();
            organismoServicio.setOrganismos_id(id);
            organismoServicio.setServicios_id(idServicio);
            
            if ((organismoAux.getCodigoOrganismo().equals(codigoOrganismo))) {
                organismoAux.setNombre(nombre.toUpperCase());
                organismoAux.setDireccion(direccion);
                organismoAux.setTelefonos(telefonos);
                organismoAux.setCorreoOficial(correoOficial);
                organismoAux.setClasificadorOrganismo(clasificadorOrganismoAux);
                organismoAux.setOrganismoPadre(organismoAuxPadre);
            } else {
//                Organismo organismoFindAux = this.findByCodigo(codigoOrganismo);
//                if (organismoFindAux != null) {
//                    throw new Exception("El código de organismo ya se encuentra registrado.");
//                } else {
                organismoAux.setCodigoOrganismo(codigoOrganismo);
//                }
            }
            this.editOServicio(organismoServicio);
            this.edit(organismoAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el organismo." + e.getMessage());
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Organismo> findAllSelected(Servicio servicio) {
        Query consulta = em.createNativeQuery("select o.* from organismos o right join organismos_servicios os on o.id = os.organismo_id where o.estado = ? and os.servicios_id = ? order by o.id asc",Organismo.class);
        consulta.setParameter(1, true);
        consulta.setParameter(2,servicio.getId());
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void createOServicio(OrganismoServicio entity) {
        getEntityManager().persist(entity);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void removeOServicio(OrganismoServicio entity) {
        getEntityManager().remove(entity);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void editOServicio(OrganismoServicio entity) {
        getEntityManager().merge(entity);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public OrganismoServicio findOServicio(Long id) {
        return getEntityManager().find(OrganismoServicio.class, id);
    }
    
}
