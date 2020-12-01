/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.ArrayList;
import com.safi.entity.TipoClasificadorRecursoPropio;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionAttribute;
import javax.persistence.Query;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.ejb.Stateless;
import com.safi.entity.ClasificadorRecursoPropio;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga, Zakowicz Matias
 */
@Stateless
public class ClasificadorRecursoPropioFacade extends AbstractFacade<ClasificadorRecursoPropio> implements ClasificadorRecursoPropioFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    TipoClasificadorRecursoPropioFacadeLocal tipoClasificadorRecursoPropioFacade;

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    public ClasificadorRecursoPropioFacade() {
        super((Class) ClasificadorRecursoPropio.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<ClasificadorRecursoPropio> findAll(final boolean estado) {
        final Query consulta = this.em.createQuery("select object(o) from ClasificadorRecursoPropio as o WHERE o.estado = :p1");
        consulta.setParameter("p1", (Object) estado);
        return (List<ClasificadorRecursoPropio>) consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<ClasificadorRecursoPropio> findAll(final String nombre) {
        return (List<ClasificadorRecursoPropio>) this.em.createQuery("select object(o) FROM ClasificadorRecursoPropio as o WHERE o.estado = true AND o.nombre LIKE '%" + nombre + "%' ORDER BY o.id ").getResultList();
    }

    @Override
    public ClasificadorRecursoPropio create(final String nombre, final Long idClasificadorPadre, final Long idTipoClasificadorRecursoPropio) throws Exception {
        ClasificadorRecursoPropio clasificadorRecursoPropioAux = new ClasificadorRecursoPropio();
        try {
            TipoClasificadorRecursoPropio tipoClasificadorRecursoPropioAux = this.tipoClasificadorRecursoPropioFacade.find((Object) idTipoClasificadorRecursoPropio);

            clasificadorRecursoPropioAux.setNombre(nombre.toUpperCase());
            clasificadorRecursoPropioAux.setTipoClasificadorRecursoPropio(tipoClasificadorRecursoPropioAux);
            clasificadorRecursoPropioAux.setCodigo(this.findUltimoNumeroDisponible(idClasificadorPadre));
            clasificadorRecursoPropioAux.setEstado(true);
            this.create(clasificadorRecursoPropioAux);
            if (idClasificadorPadre != null && idClasificadorPadre != 0L) {
                final ClasificadorRecursoPropio clasificadorPadre = (ClasificadorRecursoPropio) this.find((Object) idClasificadorPadre);
                clasificadorRecursoPropioAux.setClasificadorRecursoPropio(clasificadorPadre);
                clasificadorPadre.getClasificadoresRecursoPropio().add(clasificadorRecursoPropioAux);
                this.edit(clasificadorPadre);
                this.edit(clasificadorRecursoPropioAux);
            }

        } catch (Exception e) {
            throw new Exception("Error al intentar crear el Clasificador de Recurso Propio");
        }

        return clasificadorRecursoPropioAux;
    }

    @Override
    public void edit(final Long idClasificadorRecursoPropio, final String nombre, final Long idClasificadorPadre, final Long idTipoClasificadorRecursoPropio) throws Exception {
        try {
            final TipoClasificadorRecursoPropio tipoClasificadorRecursoPropioAux = this.tipoClasificadorRecursoPropioFacade.find((Object) idTipoClasificadorRecursoPropio);
            final ClasificadorRecursoPropio clasificadorRecursoPropioAux = (ClasificadorRecursoPropio) this.find((Object) idClasificadorRecursoPropio);
            clasificadorRecursoPropioAux.setTipoClasificadorRecursoPropio(tipoClasificadorRecursoPropioAux);
            clasificadorRecursoPropioAux.setNombre(nombre.toUpperCase());
            this.edit(clasificadorRecursoPropioAux);
            if (idClasificadorPadre != null && idClasificadorPadre != 0L) {
                final ClasificadorRecursoPropio clasificadorPadreEx = clasificadorRecursoPropioAux.getClasificadorRecursoPropio();
                clasificadorPadreEx.getClasificadoresRecursoPropio().remove(clasificadorRecursoPropioAux);
                this.edit(clasificadorPadreEx);
                final ClasificadorRecursoPropio clasificadorPadre = (ClasificadorRecursoPropio) this.find((Object) idClasificadorPadre);
                clasificadorRecursoPropioAux.setClasificadorRecursoPropio(clasificadorPadre);
                clasificadorPadre.getClasificadoresRecursoPropio().add(clasificadorRecursoPropioAux);
                this.edit(clasificadorPadre);
                this.edit(clasificadorRecursoPropioAux);
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el Clasificador de Recurso Propio");
        }
    }

    @Override
    public void remove(final Long idClasificadorRecursoPropio) throws Exception {
        try {
            final ClasificadorRecursoPropio clasificadorRecursoPropioAux = (ClasificadorRecursoPropio) this.find((Object) idClasificadorRecursoPropio);
            clasificadorRecursoPropioAux.setEstado(false);
            this.edit(clasificadorRecursoPropioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el Clasificador de Recurso Propio");
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private Long findUltimoNumeroDisponible(final Long idClasificadorPadre) {
        Long ultimoCod = 1L;
        try {
            if (idClasificadorPadre != null && idClasificadorPadre != 0L) {
                ultimoCod = (Long) this.em.createQuery("select CAST(MAX(o.codigo) AS BigInt) FROM ClasificadorRecursoPropio as o WHERE o.estado = true AND o.clasificadorRecursoPropio.id = " + idClasificadorPadre ).getResultList().get(0);
                ++ultimoCod;
            } else if (idClasificadorPadre == null || idClasificadorPadre == 0L) {
                ultimoCod = (Long) this.em.createQuery("select CAST(MAX(o.codigo) AS BigInt) FROM ClasificadorRecursoPropio as o LEFT JOIN o.clasificadorRecursoPropio oo WHERE o.estado = true AND oo IS NULL ").getResultList().get(0);
                ++ultimoCod;
            }
        } catch (Exception e) {
            
            return 1L;
        }
        return ultimoCod;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<ClasificadorRecursoPropio> findAll(final Long idClasificiadorPropio, final Long codigo, final Long idTipoClasificadorRecursoPropio, final String nombre) {
        final StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM ClasificadorRecursoPropio as o LEFT JOIN o.clasificadorRecursoPropio oo WHERE o.estado =true ");
        if (idClasificiadorPropio != null && idClasificiadorPropio != 0L) {
            query.append(" and o.clasificadorRecursoPropio.id = ").append(idClasificiadorPropio);
        }
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre.toUpperCase()));
            query.append("%' ");
        }
        if (codigo != null && codigo != 0L) {
            query.append(" and o.codigo = ").append(codigo);
        }
        if (idTipoClasificadorRecursoPropio != null && idTipoClasificadorRecursoPropio != 0L) {
            query.append(" and o.tipoClasificadorRecursoPropio.id = ").append(idTipoClasificadorRecursoPropio);
        }            
        query.append(" group by oo.codigo,o.id order by oo.codigo,o.codigo ");
        final Query consulta = this.em.createQuery(query.toString());
        return (List<ClasificadorRecursoPropio>) consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<ClasificadorRecursoPropio> findAll(Long idClasificiadorPropio, Long codigo,Long idTipoClasificadorRecursoPropio, String nombre, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM ClasificadorRecursoPropio as o LEFT JOIN o.clasificadorRecursoPropio oo WHERE o.estado =true ");
        if (idClasificiadorPropio != null && idClasificiadorPropio != 0L) {
            query.append(" and o.clasificadorRecursoPropio.id = ").append(idClasificiadorPropio);
        }
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre.toUpperCase()));
            query.append("%' ");
        }
        if (codigo != null && codigo != 0L) {
            query.append(" and o.codigo = ").append(codigo);
        }
        if (idTipoClasificadorRecursoPropio != null && idTipoClasificadorRecursoPropio != 0L) {
            query.append(" and o.tipoClasificadorRecursoPropio.id = ").append(idTipoClasificadorRecursoPropio);
        }
       query.append(" group by oo.codigo,o.id order by oo.codigo,o.codigo ");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Long idClasificiadorPropio, Long codigo,Long idTipoClasificadorRecursoPropio,String nombre) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(o) FROM ClasificadorRecursoPropio as o WHERE o.estado = true ");
        if (idClasificiadorPropio != null && idClasificiadorPropio != 0L) {
            query.append(" and o.clasificadorRecursoPropio.id = ").append(idClasificiadorPropio);
        }
        if (nombre != null && !nombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombre)) LIKE '%");
            query.append(Utilidad.desacentuar(nombre.toUpperCase()));
            query.append("%' ");
        }
        if (codigo != null && codigo != 0L) {
            query.append(" and o.codigo = ").append(codigo);
        }
        if (idTipoClasificadorRecursoPropio != null && idTipoClasificadorRecursoPropio != 0L) {
            query.append(" and o.tipoClasificadorRecursoPropio.id = ").append(idTipoClasificadorRecursoPropio);
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public boolean exists(final Long idClasificiadorPropio, final Long codigo, final Long idTipoClasificadorRecursoPropio) {
        final StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM ClasificadorRecursoPropio as o LEFT JOIN o.clasificadorRecursoPropio oo WHERE o.estado =true ");
        if (idClasificiadorPropio != null && idClasificiadorPropio != 0L) {
            query.append(" and o.clasificadorRecursoPropio.id = ").append(idClasificiadorPropio);
        }
        if (codigo != null && codigo != 0L) {
            query.append(" and o.codigo = ").append(codigo);
        }
        if (idTipoClasificadorRecursoPropio != null && idTipoClasificadorRecursoPropio != 0L) {
            query.append(" and o.tipoClasificadorRecursoPropio.id = ").append(idTipoClasificadorRecursoPropio);
        }
        query.append(" group by oo.codigo,o.id order by oo.codigo,o.codigo ");
        final Query consulta = this.em.createQuery(query.toString());
        return !consulta.getResultList().isEmpty();
    }
    
    
    @Override
    //Seleccion de sector
    public List<String> findAllSelectItems(Long ejercicio_id, Long servicio_id) {
        List<String> sectores = new ArrayList();
        try {
            //Ver como concatenar para armar el sector (clainst + organismo + caracter)
            String queryNative = "select distinct (select o.clasificadororganismo_id from organismos o "
                    + "where o.id = r.organismo_id )::character varying||organismo_id||caracter_id sector "
                    + "from recursos_propios r where r.servicio_id="+servicio_id+" and r.ejercicio_id="+ejercicio_id;
            List<Object> listaResult = (List<Object>) em.createNativeQuery(queryNative).getResultList();
            for (Object object : listaResult) {
                if (object != null) {
                    sectores.add(object.toString());
                }
            }
        } catch (Exception e) {
            
        }
        return sectores;
    }
    
    
     @Override
    //Seleccion de genero
    public List<String> findAllSelectItems(Long ejercicio_id, Long servicio_id,String sector) {
         List<String> generos = new ArrayList();
         List<String> generosAux = new ArrayList();
         ClasificadorRecursoPropio clasificadorAux= new ClasificadorRecursoPropio();
        try {
            if (sector != null) {                               
                Query consulta;
                consulta = em.createQuery("select c.clasificadorrecurso_id from recursos_propios c where\n" +
                                            "(c.caracter_id::character varying ||\n" +
                                            "c.organismo_id::character varying ||\n" +
                                            "(select o.clasificadororganismo_id from organismos o where o.id=c.organismo_id)::character varying  ) ="+sector+"\n" +
                                            "and\n" +
                                            "c.servicio_id="+servicio_id+" and c.ejercicio_id="+ejercicio_id);
                generos = consulta.getResultList();
            }
            for (String genero:generos){                    
                    ClasificadorRecursoPropio clasificador=this.find(Long.valueOf(genero)); 
                    generosAux.add(clasificadorAux.obtenerCodigoCompleto(clasificador));
                   
            }
        } catch (Exception e) {
           
        }
        return generosAux;
    }
    
    
    
    
    
}