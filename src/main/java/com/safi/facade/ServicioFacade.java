/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import com.safi.facade.AbstractFacade;
import com.safi.facade.UsuarioFacadeLocal;
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
import com.safi.entity.Organismo;
import com.safi.entity.Servicio;
import com.safi.entity.Usuario;
import com.safi.entity.CodigoExpediente;
import com.safi.entity.CuentaBancaria;
import com.safi.entity.TipoServicio;
import com.safi.facade.AuditoriaFacadeLocal;
import com.safi.facade.GrupoFacadeLocal;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Angel Alvarenga
 */
@Stateless
public class ServicioFacade extends AbstractFacade<Servicio> implements ServicioFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @EJB
    UsuarioFacadeLocal usuarioFacade;
    @EJB
    GrupoFacadeLocal grupoFacade;
    @EJB
    OrganismoFacadeLocal organismoFacade;
    @EJB
    EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private CuentaBancariaFacadeLocal cuentaBancariaFacade;
    @EJB
    TipoServicioFacadeLocal tipoServicioFacade;
    @EJB
    CodigoExpedienteFacadeLocal codigoExpedienteFacade;
    @EJB
    private AuditoriaFacadeLocal auditoriaFacade;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ServicioFacade() {
        super(Servicio.class);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Servicio> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Servicio as o WHERE o.estado = :p1 order by CAST(o.codigo AS BIGINT)");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Servicio> findAll(Long idEjercicio) {
        
        StringBuilder query = new StringBuilder();
        query.append("select distinct object(o) from Servicio as o join o.ejercicio ejer join ejer.servicio ser where o.estado = true ");
        if (idEjercicio != null && idEjercicio != 0L) {
            query.append(" and ejer.id = ").append(idEjercicio);  
        } 
        query.append(" order by CAST(o.codigo AS BIGINT)"); 
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    /**
     * @author Matias
     * @param id
     * @param codigo
     * @param abreviatura
     * @param descripcion
     * @return List Servicio
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Servicio> findAll(Long id, String codigo, String abreviatura, String descripcion) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Servicio as o WHERE o.estado = true");

        if (codigo != null && !(codigo.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.codigo)) LIKE '%").append(Utilidad.desacentuar(codigo).toUpperCase()).append("%'");
        }
        if (abreviatura != null && !(abreviatura.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.abreviatura)) LIKE '%").append(Utilidad.desacentuar(abreviatura).toUpperCase()).append("%'");
        }

        if (descripcion != null && !(descripcion.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.descripcion)) LIKE '%").append(Utilidad.desacentuar(descripcion).toUpperCase()).append("%'");
        }

        if (id != null && id != 0L) {
            query.append(" and o.id =").append(id);
        }
        query.append(" ORDER BY cast ( o.codigo as int8)");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Servicio> findAll(Long id, String codigo, String abreviatura, String descripcion, Long idTipoServicio, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Servicio as o WHERE o.estado = true ");
        if (codigo != null && !(codigo.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.codigo)) LIKE '%").append(Utilidad.desacentuar(codigo).toUpperCase()).append("%'");
        }
        if (abreviatura != null && !(abreviatura.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.abreviatura)) LIKE '%").append(Utilidad.desacentuar(abreviatura).toUpperCase()).append("%'");
        }

        if (descripcion != null && !(descripcion.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.descripcion)) LIKE '%").append(Utilidad.desacentuar(descripcion).toUpperCase()).append("%'");
        }

        if (id != null && id != 0L) {
            query.append(" and o.id =").append(id);
        }
        if (idTipoServicio != null && idTipoServicio != 0L) {
            query.append(" and o.tipoServicio.id =").append(idTipoServicio);
        }
        query.append(" ORDER BY cast ( o.codigo as int8)");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Long id, String codigo, String abreviatura, String descripcion, Long idTipoServicio) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(o) FROM Servicio as o WHERE o.estado = true ");
        if (codigo != null && !(codigo.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.codigo)) LIKE '%").append(Utilidad.desacentuar(codigo).toUpperCase()).append("%'");
        }
        if (abreviatura != null && !(abreviatura.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.abreviatura)) LIKE '%").append(Utilidad.desacentuar(abreviatura).toUpperCase()).append("%'");
        }

        if (descripcion != null && !(descripcion.equals(""))) {
            query.append(" and upper(FUNCTION('unaccent', o.descripcion)) LIKE '%").append(Utilidad.desacentuar(descripcion).toUpperCase()).append("%'");
        }

        if (idTipoServicio != null && idTipoServicio != 0L) {
            query.append(" and o.tipoServicio.id =").append(idTipoServicio);
        }

        if (id != null && id != 0L) {
            query.append(" and o.id =").append(id);
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public boolean existsServicio(String codigo) {
        Query consulta = em.createQuery("select object(o) from Servicio as o WHERE o.estado = true and o.codigo = :p1 ");
        consulta.setParameter("p1", codigo);
        return consulta.getResultList().isEmpty();
    }

    @Override
    public void create(String codigo, String abreviatura, String descripcion, Long idTipoServicio,List<CodigoExpediente> lstCodigoExpediente) throws Exception {
        try {
            TipoServicio tipoServicioAux = this.tipoServicioFacade.find(idTipoServicio);
            Servicio servicioAux = new Servicio();
            servicioAux.setAbreviatura(abreviatura.toUpperCase());
            servicioAux.setCodigo(codigo);
            servicioAux.setDescripcion(descripcion.toUpperCase());
            servicioAux.setTipoServicio(tipoServicioAux);
            servicioAux.setLstCodigoExpediente(lstCodigoExpediente);
            servicioAux.setEstado(true);
            this.create(servicioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar crear el servicio administrativo.");
        }
    }

    /**
     * @autor Matias Zakowicz
     * @param idServicio
     * @throws Exception
     */
    @Override
    public void remove(Long idServicio) throws Exception {
        try {

            Servicio servicioAux = (Servicio) this.find(idServicio);
            servicioAux.setEstado(false);
            /*Quito servicios del organismo*/
            for (Organismo organismoAux : servicioAux.getOrganismos()) {
                organismoAux.getServicios().remove(servicioAux);
                this.organismoFacade.edit(organismoAux); //Edito el organismo
            }
            for (CuentaBancaria cuentaBancariaAux : servicioAux.getLstCuentaBnacaria()) {
                cuentaBancariaAux.setEstado(false);
                this.cuentaBancariaFacade.edit(cuentaBancariaAux);
            }
            servicioAux.setOrganismos(new ArrayList()); //Quito la relación los organismos que tenia ese servicio
            servicioAux.setLstCuentaBnacaria(new ArrayList());
            this.edit(servicioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el servicio administrativo.");
        }
    }

    @Override
    public void edit(Long idServicio, String codigo, String abreviatura, String descripcion, Long idTipoServicio,List<Long> lstCodigoExpediente) throws Exception {
        try {
            
            List<CodigoExpediente> listaCodigosIzquierda = new ArrayList();
            if (!lstCodigoExpediente.isEmpty()) {
                listaCodigosIzquierda = this.codigoExpedienteFacade.findAll(lstCodigoExpediente);
            }
            TipoServicio tipoServicioAux = this.tipoServicioFacade.find(idTipoServicio);
            Servicio servicioAux = (Servicio) this.find(idServicio);
            servicioAux.setCodigo(codigo);
            servicioAux.setAbreviatura(abreviatura.toUpperCase());
            servicioAux.setDescripcion(descripcion.toUpperCase());
            servicioAux.setTipoServicio(tipoServicioAux);
            servicioAux.setLstCodigoExpediente(listaCodigosIzquierda);
            this.edit(servicioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el servicio administrativo.");
        }
    }

    @Override
    public void editUsuariosServicio(Long id, List<Long> lstUsuariosIzquierda, List<Long> lstUsuariosDerecha) throws Exception {
        try {
            List<Usuario> listaUsuariosIzquierda = new ArrayList();
            List<Usuario> listaUsuariosDerecha = new ArrayList();
            if (!lstUsuariosIzquierda.isEmpty()) {
                listaUsuariosIzquierda = this.usuarioFacade.findAll(lstUsuariosIzquierda);
            }
            if (!lstUsuariosDerecha.isEmpty()) {
                listaUsuariosDerecha = this.usuarioFacade.findAll(lstUsuariosDerecha);
            }
            Servicio servicioAux = (Servicio) this.find(id);
            servicioAux.setUsuarios(listaUsuariosIzquierda);
            this.edit(servicioAux);
            for (Usuario usuarioAux : listaUsuariosIzquierda) {
                usuarioAux.setServicio(servicioAux);
                this.usuarioFacade.edit(usuarioAux);
            }
            for (Usuario usuarioAux : listaUsuariosDerecha) {
                usuarioAux.setServicio(null);
                this.usuarioFacade.edit(usuarioAux);
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el servicio administrativo.");
        }
    }
    
     public void crearAuditoria(String accion,Usuario usuarioAccion,String valorAnterior,String valorActual,Long idEjercicio){        
        this.auditoriaFacade.create(accion, new Date(), usuarioAccion.getId(), valorAnterior, valorActual,idEjercicio);
    }
    
    public String devolverValorActual(String apellidoNombre,String nombreUsuario,String email){        
        return  "Apellido y Nombre: "+apellidoNombre+ "\n"+                
                "Nombre de Usuario:"+nombreUsuario+ "\n"+
                "Email:"+email+ "\n";
                
    }

    @Override
    public void editOrganismosServicio(Long id, List<Long> lstOrganismos) throws Exception {
        try {
            Servicio servicioAux = (Servicio) this.find(id);
            /* Obtengo todos los organismos ya asignados y quito su servicio, ademas elimino los organismos del servicio */
            List<Organismo> listaOrganismosViejos = servicioAux.getOrganismos();
            for (Organismo organismoAux : listaOrganismosViejos) {
                organismoAux.getServicios().remove(servicioAux);
                this.organismoFacade.edit(organismoAux);
            }
            servicioAux.getOrganismos().clear();
            this.edit(servicioAux);

            if (!lstOrganismos.isEmpty()) {
                /* Busco todos los servicios ingresados por parametro y asigno al servicio. */
                List<Organismo> listaOrganismosNuevos = this.organismoFacade.findAll(lstOrganismos);
                servicioAux.setOrganismos(listaOrganismosNuevos);

                /*esta seccion agrega el servicio a los organismos */
                for (Organismo organismoAux : listaOrganismosNuevos) {
                    if (organismoAux.getServicios() != null) {
                        if (!organismoAux.getServicios().contains(servicioAux)) {
                            organismoAux.getServicios().add(servicioAux);
                            this.organismoFacade.edit(organismoAux);
                        }
                    } else {
                        List<Servicio> lstServicios = new ArrayList();
                        lstServicios.add(servicioAux);
                        organismoAux.setServicios(lstServicios);
                        this.organismoFacade.edit(organismoAux);
                    }
                }

            }
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el servicio administrativo.");
        }
    }

    /**
     * @author Facundo Gonzalez
     * @param idServicio
     * @param idEjercicio
     *
     * @return List Servicio
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Servicio> findAll(Long idServicio, Long idEjercicio) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Servicio as o WHERE  o.estado = true");

        if (idServicio != null || idServicio != 0L) {
            query.append(" and o.id=").append(idServicio);
        }
        if (idEjercicio != null || idEjercicio != 0L) {
            query.append(" and o.CuentaBancaria.Ejercicio.id=").append(idEjercicio);
        }

        query.append(" ORDER BY o.id");
        Query consulta = em.createQuery(query.toString());

        return consulta.getResultList();
    }
    
    
       /**
     * @author Facundo Gonzalez
     * @param idServicio
     * @param idEjercicio
     *
     * @return List Servicio
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Servicio> findAllServicio(Long idServicio,Long idEjercicio) {
       try{
        StringBuilder query = new StringBuilder();
        query.append("select Distinct(object(o)) FROM Servicio as o,o.lstCuentaBnacaria as c WHERE  o.estado = true");

        if (idServicio != null || idServicio != 0L) {
            query.append(" and o.id=").append(idServicio);
        }
       if (idEjercicio != null || idEjercicio != 0L) {
            query.append(" and c.ejercicio.id IN (").append(idEjercicio).append(")");
        }
        
        query.append(" ORDER BY o.id");
        Query consulta = em.createQuery(query.toString());

        return consulta.getResultList();
       }catch (Exception ex){
           System.out.println(ex.getMessage());
           return new ArrayList<>();
       }
        
        
        
    }
    

}
