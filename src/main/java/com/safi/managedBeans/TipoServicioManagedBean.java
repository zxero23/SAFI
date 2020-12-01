/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.managedBeans;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import com.safi.entity.TipoServicio;
import com.safi.facade.TipoServicioFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Matias Zakowicz
 */
@ManagedBean
@SessionScoped
public class TipoServicioManagedBean extends UtilManagedBean {

    @EJB
    private TipoServicioFacadeLocal tipoServicioFacade;
    private String nombre;
    private String nombreBsq;

    public TipoServicioManagedBean() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreBsq() {
        return nombreBsq;
    }

    public void setNombreBsq(String nombreBsq) {
        this.nombreBsq = nombreBsq;
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoTipoServicio")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarTipoServicio")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarTipoServicio")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleTipoServicio"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<TipoServicio> lstTipoServiciosAux = new LazyDataModel<TipoServicio>() {

            @Override
            public List<TipoServicio> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<TipoServicio> lstTipoServiciosAux = tipoServicioFacade.findAll(nombreBsq, first, pageSize);

                return lstTipoServiciosAux;
            }

        };
        lstTipoServiciosAux.setRowCount(tipoServicioFacade.countAll(nombreBsq).intValue());
        return lstTipoServiciosAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
    }

    @Override
    public String crear() {
        try {
            this.tipoServicioFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoServicio");
            this.setMsgSuccessError("El tipo de servicio ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoServicio");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.tipoServicioFacade.create(this.getNombre());
            this.setResultado("nuevoTipoServicio");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoServicio");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoServicioAux = Long.parseLong(myRequest.getParameter("id"));
        TipoServicio tipoServicioAux = this.tipoServicioFacade.find(idTipoServicioAux);
        this.setId(tipoServicioAux.getId());
        this.setNombre(tipoServicioAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idTipoServicioAux = Long.parseLong(myRequest.getParameter("id"));
            this.tipoServicioFacade.remove(idTipoServicioAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("tipoServicioConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoServicio");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoServicioAux = Long.parseLong(myRequest.getParameter("id"));
        TipoServicio tipoServicioAux = this.tipoServicioFacade.find(idTipoServicioAux);
        this.setId(tipoServicioAux.getId());
        this.setNombre(tipoServicioAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.tipoServicioFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoServicio");
            this.setMsgSuccessError("El tipo de servicio ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoServicio");
        }
        return this.getResultado();
    }

    public void validatorNombreTipoServicio(FacesContext context, UIComponent validate, Object value) {
        this.validatorEspaciosEnBlanco(context, validate, value);
        String evaluar = (String) value;
        List<TipoServicio> lstTipoServicioAux = this.tipoServicioFacade.findByName(evaluar.toUpperCase());
        if (this.getNombreOriginal() != null) {
            if (!lstTipoServicioAux.isEmpty() && !this.getNombreOriginal().equalsIgnoreCase(evaluar)) {
                FacesMessage msg = new FacesMessage("Ya existe un tipo de servicio con el mismo nombre.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        } else {
            if (!lstTipoServicioAux.isEmpty()) {
                FacesMessage msg = new FacesMessage("Ya existe un tipo de servicio con el mismo nombre.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        }
    }

    @Override
    public void actualizar() {
        this.setNombreBsq("");
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
