package org.safi.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import org.safi.entity.Notificacion;
import org.safi.entity.Servicio;

/**
 * @author Doroñuk Gustavo
 */
@Local
public interface NotificacionFacadeLocal {

    void create(Notificacion notificacion);

    void edit(Notificacion notificacion);

    void remove(Notificacion notificacion);

    Notificacion find(Object id);

    List<Notificacion> findAll();

    List<Notificacion> findRange(int[] range);

    int count();

    public void create(Long idUsuario, String mensaje, Long servicioId) throws Exception;

    public void leer(Long id) throws Exception;

    public List<Notificacion> findAll(Long idServicio, int leido, Date fechaDesdeBsq, Date fechaHastaBsq, int first, int pageSize);
    
    Long count(Long idServicio, int leido, Date fechaDesdeBsq, Date fechaHastaBsq);

    public List<Servicio> findAllServ();
    
    public List<Notificacion> findAllEnviado(Long idServicio, int leido, Date fechaDesdeBsq, Date fechaHastaBsq,Long idUsuario, int first, int pageSize) ;
    
    public Long CountfindAllEnviado(Long idServicio, int leido, Date fechaDesdeBsq, Date fechaHastaBsq,Long idUsuario);
   
   
    
}