package com.safi.managedBeans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.icefaces.ace.model.table.LazyDataModel;
import org.icefaces.ace.model.table.SortCriteria;
import com.safi.entity.Periodo;
import com.safi.facade.PeriodoFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Angel Alvarenga, Facundo Gonzalez
 */
@ManagedBean
@RequestScoped
public class PeriodoManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private PeriodoFacadeLocal periodoFacade;

    private Date fechaInicio;
    private Date fechaFin;
    private Periodo periodo;

    /**
     * Creates a new instance of PeriodoManagedBean
     */
    public PeriodoManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoPeriodo")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarPeriodo")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarPeriodo")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detallePeriodo"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<Periodo> lstPeriodosAux = new LazyDataModel<Periodo>() {

            @Override
            public List<Periodo> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<Periodo> lstPeriodosAux = periodoFacade.findAll(fechaInicio, fechaFin, first, pageSize);

                return lstPeriodosAux;
            }

        };
        lstPeriodosAux.setRowCount(periodoFacade.countAll(fechaInicio, fechaFin).intValue());
        return lstPeriodosAux;
    }

    @Override
    public void limpiar() {
        this.setFechaInicio(null);
        this.setFechaFin(null);
        this.setPeriodo(null);
    }

    @Override
    public String crear() {
        try {
            this.periodoFacade.create(this.getFechaInicio(), this.getFechaFin());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorPeriodo");
            this.setMsgSuccessError("El periodo ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorPeriodo");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {

            this.periodoFacade.create(this.getFechaInicio(), this.getFechaFin());
            this.setResultado("nuevoPeriodo");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorPeriodo");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idPeriodoAux = Long.parseLong(myRequest.getParameter("id"));
        Periodo periodoAux = this.periodoFacade.find(idPeriodoAux);
        this.setPeriodo(periodoAux);

    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idPeriodoAux = Long.parseLong(myRequest.getParameter("id"));
            this.periodoFacade.remove(idPeriodoAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("periodoConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorPeriodo");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idPeriodoAux = Long.parseLong(myRequest.getParameter("id"));
        Periodo periodoAux = this.periodoFacade.find(idPeriodoAux);
        this.setId(periodoAux.getId());
        this.setFechaInicio(periodoAux.getFechaInicio());
        this.setFechaFin(periodoAux.getFechaFin());
    }

    @Override
    public String guardarEdicion() {
        try {
            this.periodoFacade.edit(this.getId(), this.getFechaInicio(), this.getFechaFin());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorPeriodo");
            this.setMsgSuccessError("El periodo ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorPeriodo");
        }
        return this.getResultado();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
