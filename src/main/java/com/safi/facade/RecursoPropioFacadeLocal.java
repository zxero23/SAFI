/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Usuario;
import com.safi.entity.RecursoPropio;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface RecursoPropioFacadeLocal {

   public  void create(RecursoPropio recursoPropio);

    public void edit(RecursoPropio recursoPropio);

    public void remove(RecursoPropio recursoPropio);

    public RecursoPropio find(Object id);

    public List<RecursoPropio> findAll();

    public List<RecursoPropio> findRange(int[] range);

    public int count();

    public List<RecursoPropio> findAll(boolean estado);

    public void remove(Long idRecursoPropio) throws Exception;

    public void create(Long idEjercicio, Long idServicio, Long idOrganismo,
            Long idTipoMoneda, BigDecimal importeOriginal, String descripcion,
            Long idClasificadoRecursoPropioPadr,String concepto,Long idCaracter) throws Exception;

    public List<RecursoPropio> findAll(Long idEjercicio, Long idServicio, 
            Long idOrganismo, Long idTipoMoneda, BigDecimal importeOriginalDesde,
            BigDecimal importeOriginalHasta, String descripcion,
            Long idClasificadoRecursoPropio, Long idTipoClasificadorRecursoPropio);

    public void edit(Long idRecursoPropio, Long idEjercicio, Long idServicio, 
            Long idOrganismo, Long idTipoMoneda, BigDecimal importeOriginal, 
            String descripcion,Long idClasificadoRecursoPropioPadre,String concepto,Long idCaracter) throws Exception;

    public List<RecursoPropio> findAll(Long idEjercicio, Long idServicio, Long idOrganismo, Long idCaracter, Long idClasificadoRecursoPropio,Long idRecursoPropio);
    
    
    public List<RecursoPropio> findAll(Long idEjercicio, Long idServicio);

    public List<RecursoPropio> findAll(Long idEjercicio, Long idServicio, Long idOrganismo,
            Long idTipoMoneda, BigDecimal importeOriginalDesde, BigDecimal importeOriginalHasta,
            String descripcion, Long idClasificadoRecursoPropio, Long idTipoClasificadorRecursoPropio, int first, int pageSize);

    public Long countAll(Long idEjercicio, Long idServicio, Long idOrganismo, 
            Long idTipoMoneda, BigDecimal importeOriginalDesde, BigDecimal importeOriginalHasta, 
            String descripcion, Long idClasificadoRecursoPropio, Long idTipoClasificadorRecursoPropio);
           
        public List<RecursoPropio> obtenerRecursosPropios(Long idClasificadorRecurso,Long idEjercicio,Long idServicio);
}
