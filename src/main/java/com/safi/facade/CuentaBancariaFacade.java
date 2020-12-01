/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.Agente;
import com.safi.entity.Banco;
import com.safi.entity.CuentaBancaria;
import com.safi.entity.Ejercicio;
import com.safi.entity.EstadoCuentaBancaria;
import com.safi.entity.EstadoRepresentante;
import com.safi.entity.Representante;
import com.safi.entity.Rol;
import com.safi.entity.Servicio;
import com.safi.entity.TipoCuenta;
import com.safi.entity.TipoMoneda;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga
 */
@Stateless
public class CuentaBancariaFacade extends AbstractFacade<CuentaBancaria> implements CuentaBancariaFacadeLocal {
    
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    @EJB
    private EstadoCuentaBancariaFacadeLocal estadoCuentaFacade;
    @EJB
    private BancoFacadeLocal bancoFacade;
    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private AgenteFacadeLocal agenteFacade;
    @EJB
    private TipoCuentaFacadeLocal tipoCuentaFacade;
    @EJB
    private TipoMonedaFacadeLocal tipoMonedaFacade;
    @EJB
    private RepresentanteFacadeLocal representanteFacade;
    @EJB
    private EstadoRepresentanteFacadeLocal estadoRepresentanteFacade;
    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private RolFacadeLocal rolFacade;
    @EJB
    private SaldoAcumuladoFacadeLocal saldoFacade;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public CuentaBancariaFacade() {
        super(CuentaBancaria.class);
    }

    /**
     *
     * @param estado
     * @autor Matias Zakowicz
     * @return Listado de Cuentas Bancarias que están activas
     *
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<CuentaBancaria> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from CuentaBancaria as o WHERE o.estado = :p1 order by o.servicio.id, o.numero");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    /**
     * @author Alvarenga Angel
     * @param idEjercicio
     * @param idServicio
     * @param idEstadoCuenta
     * @return List CuentaBancaria Busca todas las cuentas bancaria en estado
     * activo para el ejercicio de un servicio en particular
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<CuentaBancaria> findAll(Long idEjercicio, Long idServicio, Long idEstadoCuenta) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT object(o) FROM CuentaBancaria AS o WHERE o.estado = true");
        
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" AND o.ejercicio.id =").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L && idServicio != -1) {
            query.append(" AND o.servicio.id =").append(idServicio);
        }
        if (idEstadoCuenta != null && idEstadoCuenta != 0L) {
            query.append(" AND o.estadoCuentaBancaria.id =").append(idEstadoCuenta);
        }
        query.append(" ORDER BY o.servicio.id, o.numero");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<CuentaBancaria> findAll(Long idServicio, Long idBanco, Long idEstado,
            Long numero, String descripcion, int saldoCero, Long idEjercicio, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM CuentaBancaria as o WHERE  o.estado = true");
        
        if (idServicio != null && idServicio != 0L) {
            query.append(" and o.servicio.id =").append(idServicio);
        }
        
        if (idBanco != null && idBanco != 0L) {
            query.append(" and o.banco.id =").append(idBanco);
        }
        
        if (idEstado != null && idEstado != 0L) {
            query.append(" and o.estadoCuentaBancaria.id =").append(idEstado);
        }
        
        if (numero != null && numero != 0L) {
            query.append(" and o.numero =").append(numero);
        }
        
        if (descripcion != null && !(descripcion.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.descripcion)) LIKE '%").append(Utilidad.desacentuar(descripcion).toUpperCase()).append("%'");
        }
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" and o.ejercicio.id =").append(idEjercicio);
        }
        switch (saldoCero) {
            case 0:
                break;
            case 1:
                query.append(" and o.saldo = 0");
                break;
            case 2:
                query.append(" and o.saldo != 0");
                break;
            default:
                break;
        }
        query.append(" ORDER BY o.ejercicio.anio DESC, CAST(o.servicio.codigo AS BIGINT) ASC, o.numero ASC");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Long idServicio, Long idBanco, Long idEstado,
            Long numero, String descripcion, int saldoCero, Long idEjercicio) {
        StringBuilder query = new StringBuilder();
        query.append("select count(o) FROM CuentaBancaria as o WHERE  o.estado = true");
        
        if (idServicio != null && idServicio != 0L) {
            query.append(" and o.servicio.id =").append(idServicio);
        }
        
        if (idBanco != null && idBanco != 0L) {
            query.append(" and o.banco.id =").append(idBanco);
        }
        
        if (idEstado != null && idEstado != 0L) {
            query.append(" and o.estadoCuentaBancaria.id =").append(idEstado);
        }
        
        if (numero != null && numero != 0L) {
            query.append(" and o.numero =").append(numero);
        }
        
        if (descripcion != null && !(descripcion.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.descripcion)) LIKE '%").append(Utilidad.desacentuar(descripcion).toUpperCase()).append("%'");
        }
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" and o.ejercicio.id =").append(idEjercicio);
        }
        switch (saldoCero) {
            case 0:
                break;
            case 1:
                query.append(" and o.saldo = 0");
                break;
            case 2:
                query.append(" and o.saldo != 0");
                break;
            default:
                break;
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    /**
     *
     * @param idServicio
     * @param numero
     * @param idEstado
     * @param idBanco
     * @param descripcion
     * @autor Matias Zakowicz
     * @return Listado de Cuentas Bancarias para los filtros de búsqueda
     *
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<CuentaBancaria> findAll(Long idServicio, Long idBanco, Long idEstado,
            Long numero, String descripcion) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM CuentaBancaria as o WHERE  o.estado = true");
        
        if (idServicio != null && idServicio != 0L) {
            query.append(" and o.servicio.id =").append(idServicio);
        }
        
        if (idBanco != null && idBanco != 0L) {
            query.append(" and o.banco.id =").append(idBanco);
        }
        
        if (idEstado != null && idEstado != 0L) {
            query.append(" and o.estadoCuentaBancaria.id =").append(idEstado);
        }
        
        if (numero != null && numero != 0L) {
            query.append(" and o.numero =").append(numero);
        }
        
        if (descripcion != null && !(descripcion.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.descripcion)) LIKE '%").append(Utilidad.desacentuar(descripcion).toUpperCase()).append("%'");
        }
        query.append(" ORDER BY o.servicio.descripcion, o.numero");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    /**
     *
     * @param idEjercicio
     * @param idServicio
     * @param idBanco
     * @param fechaAlta
     * @param nroCuenta
     * @param descripcion
     * @param cbu
     * @param alias
     * @param idTipoMoneda
     * @param idTipoCuenta
     * @param url
     * @autor Matias Zakowicz,gonzalez Facundo
     * @throws Exception Modificado por eugenio en sprint 2.
     */
    @Override
    public void create(Long idEjercicio, Long idServicio, Long idBanco, Date fechaAlta, Long nroCuenta,
            String descripcion, String cbu, String alias,
            Long idTipoMoneda, Long idTipoCuenta, String url) throws Exception {
        try {
            EstadoCuentaBancaria estadoCuentaAux = this.estadoCuentaFacade.find(1L);//Estado Activo ver BD
            TipoCuenta tipoCuentaAux = this.tipoCuentaFacade.find(idTipoCuenta);
            Ejercicio ejercicioAux = this.ejercicioFacade.find(idEjercicio);
            TipoMoneda tipoMonedaAux = this.tipoMonedaFacade.find(idTipoMoneda);
            Servicio servicioAux = this.servicioFacade.find(idServicio);
            Banco bancoAux = this.bancoFacade.find(idBanco);
            CuentaBancaria cuentaBancariaAux = new CuentaBancaria();
            cuentaBancariaAux.setFechaAlta(fechaAlta);
            cuentaBancariaAux.setNumero(nroCuenta);
            cuentaBancariaAux.setDescripcion(descripcion.toUpperCase());
            cuentaBancariaAux.setCbu(cbu);
            cuentaBancariaAux.setEstadoCuentaBancaria(estadoCuentaAux);
            cuentaBancariaAux.setBanco(bancoAux);
            cuentaBancariaAux.setUrl(url);
            cuentaBancariaAux.setSaldo(BigDecimal.ZERO);
            cuentaBancariaAux.setServicio(servicioAux);
            cuentaBancariaAux.setAlias(alias);
            cuentaBancariaAux.setTipoCuenta(tipoCuentaAux);
            cuentaBancariaAux.setTipoMoneda(tipoMonedaAux);
            cuentaBancariaAux.setEjercicio(ejercicioAux);
            cuentaBancariaAux.setEstado(true);
            this.create(cuentaBancariaAux);
            this.saldoFacade.create(cuentaBancariaAux);//crea los 17 saldos acumulados para la cuenta creada
            servicioAux.getLstCuentaBnacaria().add(cuentaBancariaAux);
            this.servicioFacade.edit(servicioAux);
            ejercicioAux.getLstCuentasBancarias().add(cuentaBancariaAux);
            this.ejercicioFacade.edit(ejercicioAux);
            
        } catch (Exception e) {
            throw new Exception("Error al intentar crear la cuenta bancaria.");
        }
    }

    /**
     *
     * @param idCuentaBancaria
     * @param idEjercicio
     * @param numero
     * @param descripcion
     * @param CBU
     * @param fechaAlta
     * @param fechaBaja
     * @param idEstadoCuenta
     * @param alias
     * @param idTipoMoneda
     * @param idTipoCuenta
     * @param url
     * @autor Matias Zakowicz
     * @throws Exception
     */
    @Override
    public void edit(Long idCuentaBancaria, Long idEjercicio, Long numero, String descripcion,
            String CBU, Date fechaAlta, Date fechaBaja, Long idEstadoCuenta, String alias,
            Long idTipoMoneda, Long idTipoCuenta, String url, Long idBanco) throws Exception {
        try {
            EstadoCuentaBancaria estadoCuentaAux = this.estadoCuentaFacade.find(idEstadoCuenta);
            Banco unBanco = this.bancoFacade.find(idBanco);
            TipoCuenta tipoCuentaAux = this.tipoCuentaFacade.find(idTipoCuenta);
            TipoMoneda tipoMonedaAux = this.tipoMonedaFacade.find(idTipoMoneda);
            CuentaBancaria cuentaBancariaAux = this.find(idCuentaBancaria);
            cuentaBancariaAux.setNumero(numero);
            cuentaBancariaAux.setDescripcion(descripcion.toUpperCase());
            cuentaBancariaAux.setCbu(CBU);
            cuentaBancariaAux.setAlias(alias);
            cuentaBancariaAux.setFechaBaja(fechaBaja);
            cuentaBancariaAux.setFechaAlta(fechaAlta);
            cuentaBancariaAux.setEstadoCuentaBancaria(estadoCuentaAux);
            cuentaBancariaAux.setTipoCuenta(tipoCuentaAux);
            cuentaBancariaAux.setTipoMoneda(tipoMonedaAux);
            cuentaBancariaAux.setUrl(url);
            cuentaBancariaAux.setBanco(unBanco);
            this.edit(cuentaBancariaAux);
            
        } catch (Exception e) {
            throw new Exception("Error al intentar editar la cuenta.");
        }
    }

    /**
     * @author Matias Zakowicz
     * @param id
     * @param idAgente
     * @param idRol
     * @param fechaIncio
     * @param fechaFin
     * @throws Exception
     *
     * Método para agregar representantes a la cuenta bancaria
     */
    @Override
    public void edit(Long id, Long idAgente, Long idRol, Date fechaIncio, Date fechaFin) throws Exception {
        try {
            CuentaBancaria cuentaBancariaAux = this.find(id);
            EstadoRepresentante estadoRepresentante = this.estadoRepresentanteFacade.find(1L);// 1 estado activo ver DB
            Agente agenteAux = this.agenteFacade.find(idAgente);
            Rol rolAux = this.rolFacade.find(idRol);

            /*Valido que el agente ya no se encuentre como representante*/
            for (Representante representanteAux : cuentaBancariaAux.getRepresentantes()) {
                if (representanteAux.getAgente().getId().equals(idAgente)) {
                    /*throw new Exception("El agente ya se encuentra como representante de la cuenta.");*/
                    /*Valido que el representante no se pise el periodo que ya tiene*/
                    if (Utilidad.isDentroRangoFecha(fechaIncio, representanteAux.getFechaInicio(), representanteAux.getFechaFin())
                            || Utilidad.isDentroRangoFecha(fechaFin, representanteAux.getFechaInicio(), representanteAux.getFechaFin())) {
                        throw new Exception("El agente ya se encuentra como representante de la cuenta en el rango de fechas solicitado.");
                    }
                }
            }
            /*Crago los datos del representante*/
            Representante representanteAux = new Representante();
            representanteAux.setAgente(agenteAux);
            representanteAux.setEstado(true);
            representanteAux.setEstadoRepresentante(estadoRepresentante);
            representanteAux.setFechaInicio(fechaIncio);
            representanteAux.setFechaFin(fechaFin);
            representanteAux.setRol(rolAux);
            this.representanteFacade.create(representanteAux);
            /*Compruebo que la lista no esté vacía y no haya más de dos representantes*/
            if (cuentaBancariaAux.getRepresentantes() != null) {
                if (cuentaBancariaAux.getRepresentantes().size() < 2) {
                    cuentaBancariaAux.getRepresentantes().add(representanteAux);
                } else {
                    throw new Exception("Solo se pueden agregar hasta 2 representantes.");
                }
                
            } else {
                cuentaBancariaAux.setRepresentantes(new ArrayList<>());
                cuentaBancariaAux.getRepresentantes().add(representanteAux);
            }
            this.edit(cuentaBancariaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar la cuenta. ".concat(e.getMessage()));
        }
    }

    /* 
     * Método para quitar los representantes de la cuenta bancaria
     */
    @Override
    public void edit(Long id, List<Long> lstIdRepresentantes) throws Exception {
        try {
            CuentaBancaria cuentaBancariaAux = this.find(id);
            EstadoRepresentante estadoInactivo = this.estadoRepresentanteFacade.find(2L); //2L estado inactivo ver BD
            List<Representante> lstRepresentante = this.representanteFacade.findAll(lstIdRepresentantes);
            for (Representante representanteAux : lstRepresentante) {
                representanteAux.setEstadoRepresentante(estadoInactivo);
                representanteAux.setFechaFin(new Date());
                this.representanteFacade.edit(representanteAux);
            }
            cuentaBancariaAux.getRepresentantes().removeAll(lstRepresentante);
            
            this.edit(cuentaBancariaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar la cuenta. ".concat(e.getMessage()));
        }
    }

    /**
     *
     * @param idCuentaBancaria
     * @throws Exception
     * @autor Matias Zakowicz Método para eliminar cuentas bancarias (lógico)
     *
     */
    @Override
    public void remove(Long idCuentaBancaria) throws Exception {
        try {
            CuentaBancaria cuentaBancariaAux = this.find(idCuentaBancaria);
            cuentaBancariaAux.setEstado(false);
            this.edit(cuentaBancariaAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar la cuenta bancaria.");
        }
    }

    /**
     * @author Gonzalez Facundo
     * @param idEjercicio
     * @param idServicio
     * @return List CuentaBancaria Busca todas las cuentas bancaria para el
     * ejercicio de un servicio en particular
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<CuentaBancaria> findAll(Long idEjercicio, Long idServicio) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT object(o) FROM CuentaBancaria AS o WHERE o.estado = true");
        
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" AND o.ejercicio.id =").append(idEjercicio);
        }
        if (idServicio != null && idServicio != 0L) {
            query.append(" AND o.servicio.id =").append(idServicio);
        }
        
        query.append(" ORDER BY o.servicio.id, o.numero");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }
    
}
