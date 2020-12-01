package com.safi.facade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import com.safi.entity.Usuario;
import com.safi.entity.Funcion;
import com.safi.entity.Movimiento;

/**
 *
 * @author Angel Alvarenga
 */
@Local
public interface MovimientoFacadeLocal {
    

    public void create(Movimiento movimiento);

    public void edit(Movimiento movimiento);

    public void remove(Movimiento movimiento);

    public Movimiento find(Object id);

    public List<Movimiento> findAll();

    public List<Movimiento> findRange(int[] range);

    public int count();

    public List<Movimiento> findAll(boolean estado);
    
    public List<Movimiento> findAll(Long idEjercicio, Long idServicio, 
            Long idCuentaBancaria, String descripcion, Date fechaAltaDesde, 
            Date fechaAltaHasta, Date fechaComprobDesde, 
            Date fechaComprobHasta, BigDecimal importeDesde, 
            BigDecimal importeHasta, Long codigoOrganismo, Long numero, Long anio);

    public void create(Long idEjercicio, Long idCuentaBancaria, 
            Long idTipoOrdenGasto, Long idTipoFondo, Long idTipoImputacion, 
            Long nroOrden, Long nroPedidoFondo, Long nroEntregaFondo, 
            Long codigo, Long numero, Long anio, Long nroComprobante, 
            Date fechaComprobante, String descripcion, BigDecimal importe,
            Usuario usuario,Long idRecursoPropio, boolean revertido) throws Exception;

    public List<Movimiento> findByCuentaBancaria(Long idCuentaBancaria);

    public List<Long> findAllCodigoOrganismo(Long idEjercicio,Long idCuenta);
    
    public List<Movimiento> findSaldoByCuentaBancaria(Long idSaldoAcumulado) ;
    
    public List<Long> findAllExpedientes(Long idEjercicio,Long idCuenta,Long codigo) ;

    public List<Movimiento> findAll(Long idEjercicio, Long idServicio, Long idCuentaBancaria, String descripcion, 
            Date fechaAltaDesde, Date fechaAltaHasta, Date fechaComprobDesde, Date fechaComprobHasta, BigDecimal importeDesde, 
            BigDecimal importeHasta, Long codigoOrganismo, Long numero, Long anio,
            Long numeroOrdenBsq,Long numeroPedidoFondoBsq,Long numeroEntregaFondoBsq,Long tipoOrdenGasto,Long tipoFondo,Long tipoImputacion,
            int first, int pageSize);

    public Long countAll(Long idEjercicio, Long idServicio, Long idCuentaBancaria, String descripcion, 
            Date fechaAltaDesde, Date fechaAltaHasta, Date fechaComprobDesde, Date fechaComprobHasta, BigDecimal importeDesde, 
            BigDecimal importeHasta, Long codigoOrganismo, Long numero, Long anio,
            Long numeroOrdenBsq,Long numeroPedidoFondoBsq,
            Long numeroEntregaFondoBsq,Long tipoOrdenGasto,Long tipoFondo,Long tipoImputacion);
    
    
}
