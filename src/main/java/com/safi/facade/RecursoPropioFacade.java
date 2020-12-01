/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
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
import com.safi.entity.Caracter;
import com.safi.entity.ClasificadorRecursoPropio;
import com.safi.entity.Ejercicio;
import com.safi.entity.Organismo;
import com.safi.entity.RecursoPropio;
import com.safi.entity.Servicio;
import com.safi.entity.TipoMoneda;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga
 */
@Stateless
public class RecursoPropioFacade extends AbstractFacade<RecursoPropio> implements RecursoPropioFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    EjercicioFacadeLocal ejercicioFacade;
    @EJB
    ServicioFacadeLocal servicioFacade;
    @EJB
    OrganismoFacadeLocal organismoFacade;
    @EJB
    TipoMonedaFacadeLocal tipoMonedaFacade;
    @EJB
    ClasificadorRecursoPropioFacadeLocal clasificadorRPFacade;
    @EJB
    TipoClasificadorRecursoPropioFacadeLocal tipoClasificadorRecursoFacade;
    @EJB
    CaracterFacadeLocal caracterFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RecursoPropioFacade() {
        super(RecursoPropio.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<RecursoPropio> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from RecursoPropio as o WHERE o.estado = :p1 order by o.id");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<RecursoPropio> findAll(Long idEjercicio, Long idServicio, Long idOrganismo,
            Long idTipoMoneda, BigDecimal importeOriginalDesde, BigDecimal importeOriginalHasta, String descripcion,
            Long idClasificadoRecursoPropio, Long idTipoClasificadorRecursoPropio) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM RecursoPropio as o WHERE o.estado = true ");
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" and o.ejercicio.id = ").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L) {
            query.append(" and o.servicio.id = ").append(idServicio);
        }
        if (idOrganismo != null && idOrganismo != 0L) {
            query.append(" and o.organismo.id = ").append(idOrganismo);
        }
        if (idTipoMoneda != null && idTipoMoneda != 0L) {
            query.append(" and o.tipoMoneda.id = ").append(idTipoMoneda);
        }
        if (idClasificadoRecursoPropio != null && idClasificadoRecursoPropio != 0L) {
            query.append(" and o.clasificadorRecurso.id = ").append(idClasificadoRecursoPropio);
        }
        if (idTipoClasificadorRecursoPropio != null && idTipoClasificadorRecursoPropio != 0L) {
            query.append(" and o.clasificadorRecurso.tipoClasificadorRecursoPropio.id = ").append(idTipoClasificadorRecursoPropio);
        }
        if (importeOriginalDesde != null && importeOriginalHasta != null) {
            query.append(" and o.importeOriginal between ").append(importeOriginalDesde).append(" and ").append(importeOriginalHasta).append(" ");
        }
        if (descripcion != null && !descripcion.equals("")) {
            query.append(" AND upper(FUNCTION('unaccent', o.descripcion)) LIKE '%");
            query.append(Utilidad.desacentuar(descripcion.toUpperCase()));
            query.append("%' ");
        }
        query.append("  AND o.concepto != 'XX'");
        query.append(" ORDER BY o.ejercicio.anio DESC,");
        query.append(" CAST(o.servicio.codigo AS BIGINT),");
        query.append(" o.organismo.codigoOrganismo,");
        query.append(" o.caracter.id,");
        query.append(" o.clasificadorRecurso.tipoClasificadorRecursoPropio.id,");
        query.append(" o.concepto,");
        query.append(" o.descripcion");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<RecursoPropio> findAll(Long idEjercicio, Long idServicio, Long idOrganismo,
            Long idTipoMoneda, BigDecimal importeOriginalDesde, BigDecimal importeOriginalHasta, String descripcion,
            Long idClasificadoRecursoPropio, Long idTipoClasificadorRecursoPropio, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        List<RecursoPropio>recursoAux=new ArrayList<>();
        List<RecursoPropio>recursoAux2=new ArrayList<>();
        query.append("select object(o) FROM RecursoPropio as o WHERE o.estado = true ");
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" and o.ejercicio.id = ").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L) {
            query.append(" and o.servicio.id = ").append(idServicio);
        }
        if (idOrganismo != null && idOrganismo != 0L) {
            query.append(" and o.organismo.id = ").append(idOrganismo);
        }
        if (idTipoMoneda != null && idTipoMoneda != 0L) {
            query.append(" and o.tipoMoneda.id = ").append(idTipoMoneda);
        }
        if (idClasificadoRecursoPropio != null && idClasificadoRecursoPropio != 0L) {
            query.append(" and o.clasificadorRecurso.id = ").append(idClasificadoRecursoPropio);
        }
        if (idTipoClasificadorRecursoPropio != null && idTipoClasificadorRecursoPropio != 0L) {
            query.append(" and o.clasificadorRecurso.tipoClasificadorRecursoPropio.id = ").append(idTipoClasificadorRecursoPropio);
        }
        if (importeOriginalDesde != null && importeOriginalHasta != null) {
            query.append(" and o.importeOriginal between ").append(importeOriginalDesde).append(" and ").append(importeOriginalHasta).append(" ");
        }
        if (descripcion != null && !descripcion.equals("")) {
            query.append(" AND upper(FUNCTION('unaccent', o.descripcion)) LIKE '%");
            query.append(Utilidad.desacentuar(descripcion.toUpperCase()));
            query.append("%' ");
        }
        query.append("  AND o.concepto != 'XX'");
        query.append(" ORDER BY o.ejercicio.anio DESC,");
        query.append(" CAST(o.servicio.codigo AS BIGINT),");
        query.append(" o.organismo.codigoOrganismo,");
        query.append(" o.caracter.id,");
        query.append(" o.clasificadorRecurso.tipoClasificadorRecursoPropio.id,");
        query.append(" o.concepto,");
        query.append(" o.descripcion");
        Comparator<RecursoPropio> anio = Comparator.comparing(x -> x.getEjercicio().getAnio());
        Comparator<RecursoPropio> codigoServ = Comparator.comparing(x -> Integer.valueOf(x.getServicio().getCodigo()));
        Comparator<RecursoPropio> compararClaseRec = Comparator.comparing(x -> x.getClasificadorRecurso().getCodigo());
            Comparator<RecursoPropio> compararTipoRec = Comparator.comparing(x -> x.getClasificadorRecurso().getClasificadorRecursoPropio().getCodigo());
            Comparator<RecursoPropio> compararJur =  Comparator.comparing(x -> x.getClasificadorRecurso().getClasificadorRecursoPropio().getClasificadorRecursoPropio().getCodigo());
            Comparator<RecursoPropio> compararGenero = Comparator.comparing(x -> x.getClasificadorRecurso().getClasificadorRecursoPropio().getClasificadorRecursoPropio().getClasificadorRecursoPropio().getCodigo());
            Comparator<RecursoPropio> compararConcepto = Comparator.comparing(x -> Integer.valueOf(x.getConcepto()));
        Query consulta = em.createQuery(query.toString());
        recursoAux=(List<RecursoPropio>)consulta.getResultList().parallelStream()
                .sorted(anio.reversed() //ordena por anio desc
                                .thenComparing(codigoServ) //ordena por codigo servicio asc
                                .thenComparing(compararGenero) 
                                .thenComparing(compararJur)
                                .thenComparing(compararTipoRec)
                                .thenComparing(compararClaseRec)
                                .thenComparing(compararConcepto))
                .collect(Collectors.toList());
        if (recursoAux.size()>(first+pageSize)){
                recursoAux2=recursoAux.subList(first, (first+pageSize));
            }else if(recursoAux.size()<first){
                        recursoAux2=recursoAux.subList(0, recursoAux.size());
                  }else{
                        recursoAux2=recursoAux.subList(first, recursoAux.size());
        }
        return recursoAux2;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Long idEjercicio, Long idServicio, Long idOrganismo,
            Long idTipoMoneda, BigDecimal importeOriginalDesde, BigDecimal importeOriginalHasta, String descripcion,
            Long idClasificadoRecursoPropio, Long idTipoClasificadorRecursoPropio) {
        StringBuilder query = new StringBuilder();
        query.append("select count(o) FROM RecursoPropio as o WHERE o.estado = true ");

        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" and o.ejercicio.id = ").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L) {
            query.append(" and o.servicio.id = ").append(idServicio);
        }
        if (idOrganismo != null && idOrganismo != 0L) {
            query.append(" and o.organismo.id = ").append(idOrganismo);
        }
        if (idTipoMoneda != null && idTipoMoneda != 0L) {
            query.append(" and o.tipoMoneda.id = ").append(idTipoMoneda);
        }
        if (idClasificadoRecursoPropio != null && idClasificadoRecursoPropio != 0L) {
            query.append(" and o.clasificadorRecurso.id = ").append(idClasificadoRecursoPropio);
        }
        if (idTipoClasificadorRecursoPropio != null && idTipoClasificadorRecursoPropio != 0L) {
            query.append(" and o.clasificadorRecurso.tipoClasificadorRecursoPropio.id = ").append(idTipoClasificadorRecursoPropio);
        }
        if (importeOriginalDesde != null && importeOriginalHasta != null) {
            query.append(" and o.importeOriginal between ").append(importeOriginalDesde).append(" and ").append(importeOriginalHasta).append(" ");
        }
        if (descripcion != null && !descripcion.equals("")) {
            query.append(" AND upper(FUNCTION('unaccent', o.descripcion)) LIKE '%");
            query.append(Utilidad.desacentuar(descripcion.toUpperCase()));
            query.append("%' ");
        }
        query.append("  AND o.concepto != 'XX'");

        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<RecursoPropio> findAll(Long idEjercicio, Long idServicio) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM RecursoPropio as o WHERE o.estado = true ");
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" and o.ejercicio.id = ").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L) {
            query.append(" and o.servicio.id = ").append(idServicio);
        }
        query.append("  AND o.concepto != 'XX'");
        Query consulta = em.createQuery(query.toString());        
        return consulta.getResultList();
    }

    @Override
    public void create(Long idEjercicio, Long idServicio, Long idOrganismo,
            Long idTipoMoneda, BigDecimal importeOriginal, String descripcion, Long idClasificadoRecursoPropioPadre, String concepto, Long idCaracter) throws Exception {
        try {
            List<RecursoPropio> lstRPAux = this.findAll(idEjercicio, idServicio, idOrganismo, idCaracter, idClasificadoRecursoPropioPadre, null);
            if (!lstRPAux.isEmpty()) {
                Long nuevo = Long.parseLong(concepto);                
                for (RecursoPropio rp : lstRPAux) {                    
                    Long conceptoRP = !rp.getConcepto().equals("XX")?Long.parseLong(rp.getConcepto()):0;
                    if (nuevo.equals(conceptoRP)) {
                        throw new Exception("Ya existe el recurso propio.");
                    }
                }
            }
            /*Preparo los objetos de otras entidades*/
            Ejercicio ejercicioAux = this.ejercicioFacade.find(idEjercicio);
            Servicio servicioAux = this.servicioFacade.find(idServicio);
            Organismo organismoAux = this.organismoFacade.find(idOrganismo);
            TipoMoneda tipoMonedaAux = this.tipoMonedaFacade.find(idTipoMoneda);
            Caracter caracterAux = this.caracterFacade.find(idCaracter);
            /*Creo el recurso*/
            RecursoPropio recursoPropioAux = new RecursoPropio();
            recursoPropioAux.setImporteOriginal(importeOriginal);
            recursoPropioAux.setImporteActual(importeOriginal);
            recursoPropioAux.setDescripcion(descripcion.toUpperCase().trim());
            recursoPropioAux.setFechaAlta(new Date());
            recursoPropioAux.setTipoMoneda(tipoMonedaAux);            
            recursoPropioAux.setConcepto(this.completarConcepto(concepto));
            recursoPropioAux.setEstado(true);
            this.create(recursoPropioAux);
            /* Relaciono el recurso al ejecicio */
            recursoPropioAux.setEjercicio(ejercicioAux);
            ejercicioAux.getLstRecursoPropio().add(recursoPropioAux);
            this.ejercicioFacade.edit(ejercicioAux);
            /* Fin relación del recurso al ejecicio */

            /* Relaciono el recurso al servicio */
            recursoPropioAux.setServicio(servicioAux);
            servicioAux.getLstRecursoPropio().add(recursoPropioAux);
            this.servicioFacade.edit(servicioAux);
            /* Fin relación del recurso al servicio */

            /* Relaciono el recurso al organismo */
            recursoPropioAux.setOrganismo(organismoAux);
            organismoAux.getLstRecursoPropio().add(recursoPropioAux);
            this.organismoFacade.edit(organismoAux);
            /* Fin relación del recurso al organismo */

            //caracter
            recursoPropioAux.setCaracter(caracterAux);
            //fin caracter

            /*Creo el clasificador*/
            ClasificadorRecursoPropio clasificadorRecursoPropioAux = this.clasificadorRPFacade.find(idClasificadoRecursoPropioPadre);
            recursoPropioAux.setClasificadorRecurso(clasificadorRecursoPropioAux);
            /*Fin crear clasificador*/
            this.edit(recursoPropioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el recurso propio: " + e.getMessage());
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<RecursoPropio> findAll(Long idEjercicio, Long idServicio, Long idOrganismo, Long idCaracter, Long idClasificadoRecursoPropio, Long idRecursoPropio) {
        List<RecursoPropio> lista = new ArrayList<>();
        try {
            StringBuilder query = new StringBuilder();
            query.append("select object(o) FROM RecursoPropio as o WHERE o.estado = true ");
            query.append(" and o.ejercicio.id = ").append(idEjercicio);
            query.append(" and o.servicio.id = ").append(idServicio);
            query.append(" and o.organismo.id = ").append(idOrganismo);
            query.append(" and o.clasificadorRecurso.id = ").append(idClasificadoRecursoPropio);
            query.append(" and o.caracter.id = ").append(idCaracter);
            if (idRecursoPropio != null) {
                query.append("  and o.id !=  ").append(idRecursoPropio);
            }
            Query consulta = em.createQuery(query.toString());            
            lista = consulta.getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return lista;
    }

    @Override
    public void edit(Long idRecursoPropio, Long idEjercicio, Long idServicio, Long idOrganismo,
            Long idTipoMoneda, BigDecimal importeOriginal, String descripcion, Long idClasificadoRecursoPropioPadre, String concepto, Long idCaracter
    ) throws Exception {
        try {
            List<RecursoPropio> lstRPAux = this.findAll(idEjercicio, idServicio, idOrganismo, idCaracter, idClasificadoRecursoPropioPadre, idRecursoPropio);
            if (!lstRPAux.isEmpty()) {
                Long nuevo = Long.parseLong(concepto);
                for (RecursoPropio rp : lstRPAux) {
                    Long esta = 0L;
                    if (rp.getConcepto().matches("[0-9]*")) {
                        esta = Long.parseLong(rp.getConcepto());
                    }
                    if (nuevo.equals(esta)) {
                        throw new Exception("Ya existe el recurso propio.");
                    }
                }
            }

            /*Preparo los objetos de otras entidades*/
            Ejercicio ejercicioAux = this.ejercicioFacade.find(idEjercicio);
            Servicio servicioAux = this.servicioFacade.find(idServicio);
            Organismo organismoAux = this.organismoFacade.find(idOrganismo);
            TipoMoneda tipoMonedaAux = this.tipoMonedaFacade.find(idTipoMoneda);
            Caracter caracterAux = this.caracterFacade.find(idCaracter);
            /*Creo el recurso*/
            RecursoPropio recursoPropioAux = this.find(idRecursoPropio);
            recursoPropioAux.setImporteActual(recursoPropioAux.getImporteActual().subtract(recursoPropioAux.getImporteOriginal()));
            recursoPropioAux.setImporteOriginal(importeOriginal);
            recursoPropioAux.setImporteActual(recursoPropioAux.getImporteActual().add(recursoPropioAux.getImporteOriginal()));
            recursoPropioAux.setDescripcion(descripcion.toUpperCase().trim());
            recursoPropioAux.setTipoMoneda(tipoMonedaAux);
            recursoPropioAux.setConcepto(concepto);
            this.edit(recursoPropioAux);
            /* Relaciono el recurso al ejecicio */
            if (!recursoPropioAux.getEjercicio().equals(ejercicioAux)) {
                Ejercicio ejercicioEx = ejercicioAux;
                recursoPropioAux.setEjercicio(ejercicioAux);
                //Agrego al nuevo ejercicio
                ejercicioAux.getLstRecursoPropio().add(recursoPropioAux);
                this.ejercicioFacade.edit(ejercicioAux);
                //Edito el Ejercicio EX
                ejercicioEx.getLstRecursoPropio().remove(recursoPropioAux);
                this.ejercicioFacade.edit(ejercicioEx);
            }
            /* Fin relación del recurso al ejecicio */

            /* Relaciono el recurso al servicio */
            if (!recursoPropioAux.getServicio().equals(servicioAux)) {
                Servicio servicioEx = servicioAux;
                recursoPropioAux.setServicio(servicioAux);
                //Agrego al nuevo servicio
                servicioAux.getLstRecursoPropio().add(recursoPropioAux);
                this.servicioFacade.edit(servicioAux);
                //Edito el Servicio EX
                servicioEx.getLstRecursoPropio().remove(recursoPropioAux);
                this.servicioFacade.edit(servicioEx);
            }

            /* Fin relación del recurso al servicio */

            /* Relaciono el recurso al organismo */
            if (!recursoPropioAux.getOrganismo().equals(organismoAux)) {
                Organismo organismoEx = organismoAux;
                recursoPropioAux.setOrganismo(organismoAux);
                //Agrego al nuevo organismo
                organismoAux.getLstRecursoPropio().add(recursoPropioAux);
                this.organismoFacade.edit(organismoAux);
                //Edito el organismo EX
                organismoEx.getLstRecursoPropio().remove(recursoPropioAux);
                this.organismoFacade.edit(organismoEx);
            }

            /* Fin relación del recurso al organismo */

            /*Creo el clasificador*/
            ClasificadorRecursoPropio clasificadorRecursoPropioAux = this.clasificadorRPFacade.find(idClasificadoRecursoPropioPadre);
            recursoPropioAux.setClasificadorRecurso(clasificadorRecursoPropioAux);
            /*Fin crear clasificador*/
            recursoPropioAux.setCaracter(caracterAux);
            this.edit(recursoPropioAux);
        } catch (Exception e) {

            throw new Exception("Error al intentar crear el recurso propio: " + e.getMessage());
        }
    }

    @Override
    public void remove(Long idRecursoPropio) throws Exception {
        try {
            RecursoPropio recursoPropioAux = this.find(idRecursoPropio);
            recursoPropioAux.setEstado(false);
            this.edit(recursoPropioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el RecursoPropio");
        }
    }
    
    //si el concepto es menor a 10 lo completa con cero a la izquierda.
    private String completarConcepto(String concepto){        
        if (Integer.valueOf(concepto)<10 && concepto.length()<2){
            return (String) Utilidad.ceroIzquierda(Long.valueOf(concepto));
        }else{
            return concepto;
        }
        
    }

    
    
      @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<RecursoPropio> obtenerRecursosPropios(Long idClasificadorRecurso, Long idEjercicio, Long idServicio) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM RecursoPropio as o WHERE o.estado = true ");
        if (idServicio != null && idServicio != 0L) {
            query.append(" and o.servicio.id = ").append(idServicio);
        }
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" and o.ejercicio.id = ").append(idEjercicio);
        }
        query.append("  and o.clasificadorRecurso.id=").append(idClasificadorRecurso);
        query.append("  AND o.concepto != 'XX'");
        query.append("  order by o.concepto,o.descripcion");          
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

}
