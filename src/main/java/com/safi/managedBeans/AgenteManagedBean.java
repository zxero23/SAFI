package com.safi.managedBeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import com.safi.entity.Agente;
import com.safi.facade.AgenteFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Matias Zakowicz
 *
 */
@ManagedBean
@SessionScoped
public class AgenteManagedBean extends UtilManagedBean implements Serializable {

    /*Inicio Atributos */
    @EJB
    private AgenteFacadeLocal agenteFacade;
    private Long dni;
    private Integer legajo;
    private String apellidoNombre;

    /* Atributos de búsqueda */
    private Long dniBsq;
    private Integer legajoBsq;
    private String apellidoNombreBsq;
 
  
  
    /**
     * Creates a new instance of AgenteManagedBean
     */
   
    public AgenteManagedBean() {
    }

    public AgenteFacadeLocal getAgenteFacade() {
        return agenteFacade;
    }

    public void setAgenteFacade(AgenteFacadeLocal agenteFacade) {
        this.agenteFacade = agenteFacade;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public Integer getLegajo() {
        return legajo;
    }

    public void setLegajo(Integer legajo) {
        this.legajo = legajo;
    }

    public String getApellidoNombre() {
        return apellidoNombre;
    }

    public void setApellidoNombre(String apellidoNombre) {
        this.apellidoNombre = apellidoNombre;
    }

    public Long getDniBsq() {
        return dniBsq;
    }

    public void setDniBsq(Long dniBsq) {
        this.dniBsq = dniBsq;
    }

    public Integer getLegajoBsq() {
        return legajoBsq;
    }

    public void setLegajoBsq(Integer legajoBsq) {
        this.legajoBsq = legajoBsq;
    }

    public String getApellidoNombreBsq() {
        return apellidoNombreBsq;
    }

    public void setApellidoNombreBsq(String apellidoNombreBsq) {
        this.apellidoNombreBsq = apellidoNombreBsq;
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoAgente")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarAgente")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarAgente")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleAgente"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<Agente> lstAgentesAux = new LazyDataModel<Agente>() {

            @Override
            public List<Agente> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<Agente> lstAgentesAux = agenteFacade.findAll(dni, legajo, apellidoNombre, first, pageSize);

                return lstAgentesAux;
            }

        };
        lstAgentesAux.setRowCount(agenteFacade.countAll(dni, legajo, apellidoNombre).intValue());
        return lstAgentesAux;
    }

    @Override
    public String crear() {
        try {
            this.agenteFacade.create(this.getDni(), this.getLegajo(), this.getApellidoNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorAgente");
            this.setMsgSuccessError("El agente ha sido generado con éxito");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorAgente");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.agenteFacade.create(this.getDni(), this.getLegajo(), this.getApellidoNombre());
            this.setResultado("nuevoAgente");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorAgente");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAgenteAux = Long.parseLong(myRequest.getParameter("id"));
        Agente agenteAux = this.agenteFacade.find(idAgenteAux);
        this.setId(agenteAux.getId());
        this.setDni(agenteAux.getDni());
        this.setLegajo(agenteAux.getLegajo());
        this.setApellidoNombre(agenteAux.getApellidoNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAgenteAux = Long.parseLong(myRequest.getParameter("id"));
            this.agenteFacade.remove(idAgenteAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("agenteConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorAgente");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAgenteAux = Long.parseLong(myRequest.getParameter("id"));
        Agente agenteAux = this.agenteFacade.find(idAgenteAux);
        this.setId(agenteAux.getId());
        this.setDni(agenteAux.getDni());
        this.setLegajo(agenteAux.getLegajo());
        this.setApellidoNombre(agenteAux.getApellidoNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.agenteFacade.edit(this.getId(), this.getDni(), this.getLegajo(), this.getApellidoNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorAgente");
            this.setMsgSuccessError("El agente ha sido editado con éxito");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorAgente");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void limpiar() {
        this.setDni(null);
        this.setLegajo(null);
        this.setApellidoNombre(null);
    }

    @Override
    public void actualizar() {
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
        this.setDniBsq(null);
        this.setLegajoBsq(null);
        this.setApellidoNombreBsq("");
    }

}
