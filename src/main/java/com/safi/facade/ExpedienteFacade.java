/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.Expediente;
import com.safi.entity.Movimiento;
import com.safi.utilidad.ExpedienteAux;

/**
 *
 * @author Angel Alvarenga
 */
@Stateless
public class ExpedienteFacade extends AbstractFacade<Expediente> implements ExpedienteFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    private CuentaBancariaFacadeLocal cuentaFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExpedienteFacade() {
        super(Expediente.class);
    }

    @Override
    public List<Expediente> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Expediente as o WHERE o.estado = :p1 ORDER BY o.numero");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @Override
    public List<Expediente> findAll(Long numero) {
        return em.createQuery("select object(o) FROM Expediente as o WHERE o.estado = true AND o.numero = " + numero + " ORDER BY o.numero ").getResultList();
    }

    @Override
    public List<Movimiento> findAll(Long organismoExpediente,String numeroExpediente,String anio,Long idEjercicio,Long idCuentaBancaria) {
        StringBuilder consultaS = new StringBuilder();
        List<Movimiento> movimientosDelExpediente = new ArrayList();
        try {
            consultaS.append("select object(m) from Movimiento m where m.estado = true "); 
            if (idEjercicio != 0L && idEjercicio != null){
                consultaS.append(" AND m.ejercicio.id=").append(idEjercicio);
            }
            if (!numeroExpediente.equals("-1") && numeroExpediente != null) {
                consultaS.append("   AND m.expediente.numero=").append(numeroExpediente);
            }
            if (organismoExpediente != 0L){
                    consultaS.append("  AND m.expediente.codigo=").append(organismoExpediente);
            }
             if (!anio.equals("")){
                    consultaS.append("  AND m.expediente.anio=").append(anio);
            }
            if (idCuentaBancaria != null){
                    consultaS.append("  AND m.cuentaBancaria.id=").append(idCuentaBancaria);
            }
            consultaS.append(" ORDER BY m.fechaAlta DESC");   
            Query consulta = em.createQuery(consultaS.toString());   
            movimientosDelExpediente = (List<Movimiento>) consulta.getResultList();
        } catch (Exception e) {

        }
        return movimientosDelExpediente;
    }

      

  

    @Override
    public Expediente findPorNroExpediente(Long codigo, Long nroExpediente, String anio) {
        Expediente expediente;
        try {
            Query consulta = em.createQuery("select object(o) from Expediente as o where o.estado = true and o.numero = " + nroExpediente + " and o.codigo = " + codigo + " AND o.ano like '" + anio + "'");
            expediente = (Expediente) consulta.getSingleResult();
        } catch (Exception e) {
            expediente = null;
        }

        return expediente;
    }

    @Override
    public List<Movimiento> findAll(Long idEjercicio, Long idServicio) {

        List<Movimiento> movimientosDelExpediente = new ArrayList();
        try {
            Query consulta = em.createQuery("select object(m) from Movimiento m where m.ejercicio.id = " + idEjercicio + " AND m.cuentaBancaria.servicio.id = " + idServicio + " and m.estado = true order by m.fechaDeParte");

            movimientosDelExpediente = consulta.getResultList();
        } catch (Exception e) {

        }
        return movimientosDelExpediente;
    }
    
    
    
    @Override
    //Expedientes
    public List<ExpedienteAux> findExpedientes(Long idEjercicio, String numeroExpediente, Long idServicio, Long idCuenta, Long organismoExpediente,String anio) {
        try {
        StringBuilder consultaS = new StringBuilder(); 
        List<ExpedienteAux> listaExp=new ArrayList<>();
        List<Object[]> objetos;
            consultaS.append("select distinct(e.codigo,e.numero,e.anio,m.cuentabancaria_id),e.codigo,e.numero,e.anio,c.numero,c.descripcion from expedientes e   ");
            consultaS.append("  inner join movimientos m on m.expediente_id=e.id  ");
            consultaS.append("  inner join cuentas_bancarias c on m.cuentabancaria_id=c.id  ");
           if (idEjercicio != 0L && idEjercicio != null)consultaS.append(" where m.ejercicio_id=").append(idEjercicio);       
           if (!numeroExpediente.equals("-1") && numeroExpediente != null) consultaS.append(" and e.numero=").append(numeroExpediente); 
           if (idServicio != 0L && idServicio != null) consultaS.append(" and c.servicio_id=").append(idServicio);
           if (idCuenta != 0L)consultaS.append(" and m.cuentabancaria_id=").append(idCuenta);
           if (organismoExpediente != 0L) consultaS.append(" and e.codigo=").append(organismoExpediente);
           if (!anio.equals("")) consultaS.append(" and e.anio=").append(anio); 
            Query q = em.createNativeQuery(consultaS.toString());
            objetos=q.getResultList();
            for (Object[] obj:objetos){
                ExpedienteAux exp=new ExpedienteAux();
                exp.setDistinct(obj[0].toString());
                exp.setCodigo(obj[1].toString());
                exp.setNumero(obj[2].toString());
                exp.setAnio(obj[3].toString());
                exp.setNumeroCuenta(obj[4].toString());
                exp.setDescripcionCuenta(obj[5].toString());
                listaExp.add(exp);                
            }  
        return listaExp;
        }catch (Exception ex) {
            System.out.println("expe: "+ex.getMessage() );
         return null;               
        }
    }
    
    
    

    
    
    
}
