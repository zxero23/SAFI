package com.safi.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.CodigoExpediente;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Facundo Gonzalez
 */
@Stateless
public class CodigoExpedienteFacade extends AbstractFacade<CodigoExpediente> implements CodigoExpedienteFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CodigoExpedienteFacade() {
        super(CodigoExpediente.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<CodigoExpediente> findCodigos(List<Long> codigosId) {
        String iDes = codigosId.toString().substring(1, codigosId.toString().length() - 1);
        return em.createQuery("select object (g) from CodigoExpediente as g where g.id in (" + iDes + " ) ").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<CodigoExpediente> findAll(List<Long> codigoId) {
        String iDes = codigoId.toString().substring(1, codigoId.toString().length() - 1);
        return em.createQuery("select object (u) from CodigoExpediente as u where u.id in (" + iDes + ")").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<CodigoExpediente> findAll(String codExpediente, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select DISTINCT  object(o) FROM CodigoExpediente as o where o.estado=true ");

        if (codExpediente != null) {
            query.append(" AND upper(FUNCTION('unaccent', o.codigo)) LIKE '%");
            query.append(Utilidad.desacentuar(codExpediente).toUpperCase());
            query.append("%' ");
        }
        query.append(" ORDER BY CAST(o.codigo AS integer) asc");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<CodigoExpediente> findAll(boolean estado) {
        StringBuilder query = new StringBuilder();
        query.append("select DISTINCT  object(o) FROM CodigoExpediente as o where o.estado=true ORDER BY CAST(o.codigo AS integer) asc ");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(String codExpediente) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(DISTINCT(o)) FROM CodigoExpediente as o where o.estado=true");
        if (codExpediente != null) {
            query.append(" AND upper(FUNCTION('unaccent', o.codigo)) LIKE '%");
            query.append(Utilidad.desacentuar(codExpediente).toUpperCase());
            query.append("%' ");
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();

    }
}
