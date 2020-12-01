package com.safi.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Auditoria;
import com.safi.entity.Servicio;

/**
 *
 * @author Facundo Gonzalez
 */
@Local
public interface AuditoriaFacadeLocal {

    void create(Auditoria auditoriaAux);

    void edit(Auditoria auditoriaAux);

    void remove(Auditoria auditoriaAux);

    Auditoria find(Object id);

    List<Auditoria> findAll();

    List<Auditoria> findRange(int[] range);

    int count();
    
    public void create(String descripcion,Date fecha,Long idUsuario,String valorAnterior,String valorActual, Long idEjercicio);

    public List<Servicio> findServicios();

    public List<Auditoria> findAll(Long idEjercicio, Long idServicio, Long idUsuario, String accion, String valor, Date fechaDesde, Date fechaHasta);

    public List<Auditoria> findAll(Long idEjercicio, Long idServicio, Long idUsuario, String accion, String valor, Date fechaDesde, Date fechaHasta, int first, int pageSize);

    public Long countAll(Long idEjercicio, Long idServicio, Long idUsuario, String accion, String valor, Date fechaDesde, Date fechaHasta);
}
