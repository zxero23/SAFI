package com.safi.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Notificacion;

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

    public void create(Long idUsuario, String mensaje) throws Exception;

    public void leer(Long id) throws Exception;

    public List<Notificacion> findAll(Long idServicio, int leido, Date fechaDesdeBsq, Date fechaHastaBsq, int first, int pageSize);
    
    Long count(Long idServicio, int leido, Date fechaDesdeBsq, Date fechaHastaBsq);
    
}