package com.safi.managedBeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import com.safi.entity.TipoCuenta;
import com.safi.facade.TipoCuentaFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Matias Zakowicz
 */
@ManagedBean
@RequestScoped
public class TipoCuentaManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private TipoCuentaFacadeLocal tipoCuentaFacade;
    private String nombre;
    private String nombreBsq;

    /**
     * Creates a new instance of TipoCuentaManagedBean
     */
    public TipoCuentaManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoTipoCuenta")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarTipoCuenta")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarTipoCuenta")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleTipoCuenta"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
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
        LazyDataModel<TipoCuenta> lstTipoCuentasAux = new LazyDataModel<TipoCuenta>() {

            @Override
            public List<TipoCuenta> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<TipoCuenta> lstTipoCuentasAux = tipoCuentaFacade.findAll(nombreBsq, first, pageSize);

                return lstTipoCuentasAux;
            }

        };
        lstTipoCuentasAux.setRowCount(tipoCuentaFacade.countAll(nombreBsq).intValue());
        return lstTipoCuentasAux;
    }

    @Override
    public void limpiar() {
        this.setNombre(null);
        this.setNombreOriginal(null);
    }

    @Override
    public String crear() {
        try {
            this.tipoCuentaFacade.create(this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoCuenta");
            this.setMsgSuccessError("El tipo de cuenta bancaria ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoCuenta");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.tipoCuentaFacade.create(this.getNombre());
            this.setResultado("nuevoTipoCuenta");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoCuenta");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoCuentaAux = Long.parseLong(myRequest.getParameter("id"));
        TipoCuenta tipoCuentaAux = this.tipoCuentaFacade.find(idTipoCuentaAux);
        this.setId(tipoCuentaAux.getId());
        this.setNombre(tipoCuentaAux.getNombre());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idTipoCuentaAux = Long.parseLong(myRequest.getParameter("id"));
            this.tipoCuentaFacade.remove(idTipoCuentaAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("tipoCuentaConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoCuenta");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idTipoCuentaAux = Long.parseLong(myRequest.getParameter("id"));
        TipoCuenta tipoCuentaAux = this.tipoCuentaFacade.find(idTipoCuentaAux);
        this.setId(tipoCuentaAux.getId());
        this.setNombre(tipoCuentaAux.getNombre());
        this.setNombreOriginal(tipoCuentaAux.getNombre());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.tipoCuentaFacade.edit(this.getId(), this.getNombre());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorTipoCuenta");
            this.setMsgSuccessError("El tipo de cuenta ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorTipoCuenta");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Comprueba que no exista el nombre del tipo de cuenta en el sistema.
     *
     * @author Doroñuk Gustavo
     * @param context
     * @param validate
     * @param value
     */
    public void validatorNombreTipoCuenta(FacesContext context, UIComponent validate, Object value) {
        this.validatorEspaciosEnBlanco(context, validate, value);
        String evaluar = (String) value;
        List<TipoCuenta> lstTipoCuentaAux = this.tipoCuentaFacade.findByName(evaluar.toUpperCase());
        if (this.getNombreOriginal() != null) {
            if (!lstTipoCuentaAux.isEmpty() && !this.getNombreOriginal().equalsIgnoreCase(evaluar)) {
                FacesMessage msg = new FacesMessage("Ya existe un tipo de cuenta con el mismo nombre.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        } else {
            if (!lstTipoCuentaAux.isEmpty()) {
                FacesMessage msg = new FacesMessage("Ya existe un tipo de cuenta con el mismo nombre.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        }
    }

}
