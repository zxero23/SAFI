package com.safi.managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.safi.entity.EstadoCuentaBancaria;
import com.safi.facade.AuditoriaFacadeLocal;
import com.safi.facade.EstadoCuentaBancariaFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Matias Zakowicz
 */
@ManagedBean
@SessionScoped
public class EstadoCuentaBancariaManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private EstadoCuentaBancariaFacadeLocal estadoCuentaBancariaFacade;
    @EJB
    private AuditoriaFacadeLocal logAccionFacade;
    private String nombre;
    private String nombreBsq;

    /**
     * Creates a new instance of EstadoCuentaBancariaManagedBean
     */
    public EstadoCuentaBancariaManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoEstadoCuentaBancaria")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarEstadoCuentaBancaria")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarEstadoCuentaBancaria")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleEstadoCuentaBancaria"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public EstadoCuentaBancariaFacadeLocal getEstadoCuentaBancariaFacade() {
        return estadoCuentaBancariaFacade;
    }

    public void setEstadoCuentaBancariaFacade(EstadoCuentaBancariaFacadeLocal estadoCuentaBancariaFacade) {
        this.estadoCuentaBancariaFacade = estadoCuentaBancariaFacade;
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

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<EstadoCuentaBancaria> lstEstadoCuentaBancariasAux = new LazyDataModel<EstadoCuentaBancaria>() {

            @Override
            public List<EstadoCuentaBancaria> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<EstadoCuentaBancaria> lstEstadoCuentaBancariasAux = estadoCuentaBancariaFacade.findAll(nombreBsq, first, pageSize);

                return lstEstadoCuentaBancariasAux;
            }

        };
        lstEstadoCuentaBancariasAux.setRowCount(estadoCuentaBancariaFacade.countAll(nombreBsq).intValue());
        return lstEstadoCuentaBancariasAux;
    }

    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<EstadoCuentaBancaria> listEstadoCuentaBancaria = estadoCuentaBancariaFacade.findAll(true);
        if (!(listEstadoCuentaBancaria.isEmpty())) {
            for (EstadoCuentaBancaria estadoCuentaBancariaAux : listEstadoCuentaBancaria) {
                selectItems.add(new SelectItem(estadoCuentaBancariaAux.getId(), estadoCuentaBancariaAux.getNombre()));
            }
        }
        return selectItems;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
        this.setNombreOriginal(null);
    }

    @Override
    public void actualizar() {
        this.setNombreBsq(null);
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String crear() {
        try {
            estadoCuentaBancariaFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorEstadoCuentaBancaria");
            this.setMsgSuccessError("El estado de cuenta bancaria ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorEstadoCuentaBancaria");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            estadoCuentaBancariaFacade.create(this.getNombre());
            this.setResultado("nuevoEstadoCuentaBancaria");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorEstadoCuentaBancaria");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idEstadoCuentaBancariaAux = Long.parseLong(myRequest.getParameter("id"));
        EstadoCuentaBancaria estadoCuentaBancariaAux = this.estadoCuentaBancariaFacade.find(idEstadoCuentaBancariaAux);
        this.setId(estadoCuentaBancariaAux.getId());
        this.setNombre(estadoCuentaBancariaAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAccionAux = Long.parseLong(myRequest.getParameter("id"));
            this.estadoCuentaBancariaFacade.remove(idAccionAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("estadoCuentaBancariaConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successEstadoCuentaBancaria");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idEstadoCuentaBancariaAux = Long.parseLong(myRequest.getParameter("id"));
        EstadoCuentaBancaria estadoCuentaBancariaAux = this.estadoCuentaBancariaFacade.find(idEstadoCuentaBancariaAux);
        this.setId(estadoCuentaBancariaAux.getId());
        this.setNombre(estadoCuentaBancariaAux.getNombre());
        this.setNombreOriginal(estadoCuentaBancariaAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.estadoCuentaBancariaFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorEstadoCuentaBancaria");
            this.setMsgSuccessError("El estado de la Cuenta Bancaria ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorEstadoCuentaBancaria");
        }
        return this.getResultado();
    }

    /**
     * Comprueba que no exista el nombre del estado de cuenta bancaria en el
     * sistema.
     *
     * @author Doroñuk Gustavo
     * @param context
     * @param validate
     * @param value
     */
    public void validatorNombreEstadoCuentaBancaria(FacesContext context, UIComponent validate, Object value) {
        this.validatorEspaciosEnBlanco(context, validate, value);
        String evaluar = (String) value;
        List<EstadoCuentaBancaria> lstEstadoCuentaBancariaAux = this.estadoCuentaBancariaFacade.findByName(evaluar.toUpperCase());
        if (this.getNombreOriginal() != null) {
            if (!lstEstadoCuentaBancariaAux.isEmpty() && !this.getNombreOriginal().equalsIgnoreCase(evaluar)) {
                FacesMessage msg = new FacesMessage("Ya existe un estado de cuenta bancaria con el mismo nombre.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        } else {
            if (!lstEstadoCuentaBancariaAux.isEmpty()) {
                FacesMessage msg = new FacesMessage("Ya existe un estado de cuenta bancaria con el mismo nombre.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        }
    }

}
