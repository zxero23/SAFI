/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.Departamento;
import com.safi.entity.Domicilio;
import com.safi.entity.Municipio;
import com.safi.entity.Provincia;

/**
 *
 * @author ASC Doroñuk Gustavo
 */
@Stateless
public class DomicilioFacade extends AbstractFacade<Domicilio> implements DomicilioFacadeLocal {
    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;
    
    @EJB
    private ProvinciaFacadeLocal provinciaFacade;
    @EJB
    private DepartamentoFacadeLocal departamentoFacade;
    @EJB
    private MunicipioFacadeLocal municipioFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DomicilioFacade() {
        super(Domicilio.class);
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Domicilio> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Domicilio as o WHERE o.estado = :p1 ORDER BY o.provincia.nombre, o.departamento.nombre, o.municipio.nombre, o.domicilio");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }
    
    @Override
    public void create(String direccion, Long idMunicipio, Long idDepartamento, Long idProvincia) throws Exception {
        try {
            boolean direccionRepetido = false;
            boolean municipioRepetido = false;
            List <Domicilio> listDomicilio = this.findAll(true);
            for (Domicilio dom : listDomicilio) {
                if (dom.getDireccion().equalsIgnoreCase(direccion)) {
                    direccionRepetido = true;
                    if (dom.getMunicipio().getId().equals(idMunicipio)) {
                        municipioRepetido = true;
                        break;
                    }
                }
            }
            if (direccionRepetido && municipioRepetido) {
                throw new Exception("El Domicilio ya existe");
            }
            Provincia provinciaAux = this.provinciaFacade.find(idProvincia);
            Departamento departamentoAux = this.departamentoFacade.find(idDepartamento);
            Municipio municipioAux = this.municipioFacade.find(idMunicipio);
            Domicilio domicilioAux = new Domicilio();
            domicilioAux.setDireccion(direccion.toUpperCase());
            domicilioAux.setProvincia(provinciaAux);
            domicilioAux.setDepartamento(departamentoAux);
            domicilioAux.setMunicipio(municipioAux);
            domicilioAux.setEstado(true);
            this.create(domicilioAux);
        } catch(Exception e) {
            throw new Exception("Error al intentar crear el domicilio: " + e.getMessage());
        }
    }
    
    @Override
    public void edit(Long id, String direccion, Long idMunicipio, Long idDepartamento, Long idProvincia) throws Exception {
        try {
            Domicilio domicilioAux = this.find(id);
            domicilioAux.setDireccion(direccion.toUpperCase());
            if (domicilioAux.getMunicipio().getId() != idMunicipio) {
                Municipio muniAux = municipioFacade.find(idMunicipio);
                domicilioAux.setMunicipio(muniAux);
            }
            if (domicilioAux.getDepartamento().getId() != idDepartamento) {
                Departamento deptoAux = departamentoFacade.find(idDepartamento);
                domicilioAux.setDepartamento(deptoAux);
            }
            if (domicilioAux.getProvincia().getId() != idProvincia) {
                Provincia provAux = provinciaFacade.find(idProvincia);
                domicilioAux.setProvincia(provAux);
            }
            this.edit(domicilioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el domicilio");
        }
    }
    
    @Override
    public void remove(Long id) throws Exception {
        try {
            Domicilio domAux = this.find(id);
            domAux.setEstado(false);
            this.edit(domAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar el domicilio");
        }
    }
    
}
