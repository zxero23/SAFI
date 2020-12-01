package com.safi.facade;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Ejercicio;
import com.safi.entity.Servicio;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface EjercicioFacadeLocal {

    public void create(Ejercicio ejercicio);

    public void edit(Ejercicio ejercicio);

    public void remove(Ejercicio ejercicio);

    public Ejercicio find(Object id);

    public List<Ejercicio> findAll();

    public List<Ejercicio> findRange(int[] range);

    public List<Ejercicio> findAll(Integer anio, Long idEstadoEjer);
    
    public List<Ejercicio> findAllNuevoBancario(Integer anio);

    public int count();

    public void create(Integer anio, Date fechaComplementaria) throws Exception;

    public void remove(Long idEjercicio) throws Exception;

    public void edit(Long idEjercicio, Long idEstado, Date fechaComplementaria) throws Exception;

    public List<Ejercicio> findAll(boolean estado);

    public List<String> cerrarEjercicioServAdministrativo(Long idEjercicio, List<Long> lstIdServicios, Long idUsuario) throws Exception;

    public List<String> cerrarEjercicio(Long idEjercicio, Long idUsuario) throws Exception;
    
    public List<Servicio> findAllServ(Long idEjercicio,boolean estado) ;
    
    public Long ejercicioActual();

    public List<Ejercicio> findAll(Integer anio, Long idEstado, int first, int pageSize);

    public Long countAll(Integer anio, Long idEstado);

    public void reOpenExercise(Long idEjercicio, Long idServicio) throws Exception;
    
}
