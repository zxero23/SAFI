package com.safi.managedBeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.icefaces.ace.model.table.LazyDataModel;
import com.safi.entity.ActionItem;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Alvarenga Angel
 */
public abstract class UtilManagedBean {

    private static Logger log = Logger.getLogger(UtilManagedBean.class);

    private String nombreOriginal;
    private List<ActionItem> lstActionItems = null;
    private boolean alta, baja, modificacion, detalle;
    private List<?> list;
    private Long id;
    private String resultado;
    private String title;
    private String images;
    private String msgSuccessError;
    private Integer page = 1;
    public boolean flag = true;
    public boolean busqueda = false;
    //control de que se realice solo 1 consulta a la base de datos,
    public boolean flagMovimiento = true;
    //control alta 
    public boolean controlAlta = true;
    //timer getlist
    private Double tiempoBsq;
    public boolean esCorrecto;
    /*
     * Logs
     */
    private String auditoriaAnterior;
    private String auditoriaActual;
    private Long idUsuarioResponsable;

    /**
     * @author Doroñuk Gustavo Devuelve un String con el año, mes, dia, hora,
     * minutos, segundos, milisegundos concatenados. (Ej.: 23/01/2020 16:58:09 >
     * 20200123165809578) Por ejemplo, sirve para agregar a los nombres de
     * archivos generados.
     */
    private String dateTime;

    public UtilManagedBean() {
    }

    /*Se cambia por lazyDataModel 
     Matias Zakowicz 08/05/2020 */
    public abstract LazyDataModel<?> getListElements();

    public abstract List<SelectItem> getSelectItems();

    public abstract void limpiar();

    public abstract String crear();

    public abstract String crearOtro();

    public abstract void verDetalle();

    public abstract void guardarBorrado();

    public abstract void prepararParaEditar();

    public abstract String guardarEdicion();

    public boolean isEsCorrecto() {
        return esCorrecto;
    }

    public void setEsCorrecto(boolean esCorrecto) {
        this.esCorrecto = esCorrecto;
    }

    public boolean isFlagMovimiento() {
        return flagMovimiento;
    }

    public void setFlagMovimiento(boolean flagMovimiento) {
        this.flagMovimiento = flagMovimiento;
    }

    public boolean isBusqueda() {
        return busqueda;
    }

    public void setBusqueda(boolean busqueda) {
        this.busqueda = busqueda;
    }

    public void setAlta(boolean alta) {
        this.alta = alta;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    public void setDetalle(boolean detalle) {
        this.detalle = detalle;
    }

    public void setModificacion(boolean modificacion) {
        this.modificacion = modificacion;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setMsgSuccessError(String msgSuccessError) {
        this.msgSuccessError = msgSuccessError;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public String getMsgSuccessError() {
        return msgSuccessError;
    }

    public String getTitle() {
        return title;
    }

    public String getResultado() {
        return resultado;
    }

    public Long getId() {
        return id;
    }

    public List<?> getList() {
        return list;
    }

    protected WebManagedBean getSessionBean() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            return (WebManagedBean) myRequest.getSession().getAttribute("webManagedBean");

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isAlta() {
        return alta;
    }

    public boolean isBaja() {
        return baja;
    }

    public boolean isDetalle() {
        return detalle;
    }

    public boolean isModificacion() {
        return modificacion;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }

    /**
     * @author Doroñuk Gustavo Devuelve un String con el año, mes, dia, hora,
     * minutos, segundos, milisegundos concatenados. (Ej.: 23/01/2020 16:58:09 >
     * 20200123165809578) Por ejemplo, sirve para agregar a los nombres de
     * archivos generados desde el XHTML.
     * @return
     */
    public String getDateTime() {
        this.dateTime = Utilidad.dateTimeString(new Date());
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Double getTiempoBsq() {
        return tiempoBsq;
    }

    public void setTiempoBsq(Double tiempoBsq) {
        this.tiempoBsq = tiempoBsq;
    }

    public boolean isControlAlta() {
        return controlAlta;
    }

    public void setControlAlta(boolean controlAlta) {
        this.controlAlta = controlAlta;
    }

    public String getAuditoriaAnterior() {
        return auditoriaAnterior;
    }

    public void setAuditoriaAnterior(String auditoriaAnterior) {
        this.auditoriaAnterior = auditoriaAnterior;
    }

    public String getAuditoriaActual() {
        return auditoriaActual;
    }

    public void setAuditoriaActual(String auditoriaActual) {
        this.auditoriaActual = auditoriaActual;
    }

    public Long getIdUsuarioResponsable() {
        return idUsuarioResponsable;
    }

    public void setIdUsuarioResponsable(Long idUsuarioResponsable) {
        this.idUsuarioResponsable = idUsuarioResponsable;
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        UtilManagedBean.log = log;
    }

    public List<ActionItem> getLstActionItems() {
        return lstActionItems;
    }

    public void setLstActionItems(List<ActionItem> lstActionItems) {
        this.lstActionItems = lstActionItems;
    }

    public void aplicarFiltro() {
        this.setPage(1);
        this.setFlag(true);
        this.setBusqueda(true);
        this.setFlagMovimiento(true);
        this.getListElements();
        
    }

    public void actualizar() {
        this.setNombreOriginal(null);
        this.setFlag(true);
        this.setFlagMovimiento(true);
        this.getListElements();
        this.setPage(1);
        this.setDateTime("");
    }

    public void limpiarAuditorias() {
        this.setAuditoriaActual(null);
        this.setAuditoriaAnterior(null);
    }

    public void validatorEspaciosEnBlanco(FacesContext context, UIComponent validate, Object value) {
        String evaluar = (String) value;
        if (evaluar.trim().isEmpty()) {
            FacesMessage msg = new FacesMessage("Debe ingresar un valor.");
            context.addMessage(validate.getClientId(context), msg);
            log.warn(msg);
            throw new ValidatorException(msg);
        }

    }

}
