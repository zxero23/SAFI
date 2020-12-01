package com.safi.managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.safi.entity.Funcion;
import com.safi.entity.TipoAcumulador;
import com.safi.entity.TipoFondo;
import com.safi.entity.TipoImputacion;
import com.safi.entity.TipoOrdenGasto;
import com.safi.facade.FuncionFacadeLocal;
import com.safi.facade.TipoAcumuladorFacadeLocal;
import com.safi.facade.TipoFondoFacadeLocal;
import com.safi.facade.TipoImputacionFacadeLocal;
import com.safi.facade.TipoOrdenGastoFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author Angel Alvarenga
 */
@ManagedBean
@SessionScoped
public class FuncionManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private TipoOrdenGastoFacadeLocal tipoOrdenGastoFacade;
    @EJB
    private TipoFondoFacadeLocal tipoFondoFacade;
    @EJB
    private TipoImputacionFacadeLocal tipoImputacionFacade;
    @EJB
    private TipoAcumuladorFacadeLocal tipoAcumuladorFacade;
    @EJB
    private FuncionFacadeLocal funcionFacade;
    private Long idTipoOrdenGasto;
    private Long idTipoFondo;
    private Long idTipoImputacion;
    private Long idTipoAcumulador;
    private Integer tipoOperacion;
    private Funcion funcion;
    private Long idTipoOrdenGastoBsq = 0L;
    private Long idTipoFondoBsq = 0L;
    private Long idTipoImputacionBsq = 0L;
    private Long idTipoAcumuladorBsq = 0L;

    /**
     * Creates a new instance of FuncionManagedBean
     */
    public FuncionManagedBean() {
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevaFuncion")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarFuncion")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarFuncion")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleFuncion"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }


    public Long getIdTipoOrdenGastoBsq() {
        return idTipoOrdenGastoBsq;
    }

    public void setIdTipoOrdenGastoBsq(Long idTipoOrdenGastoBsq) {
        this.idTipoOrdenGastoBsq = idTipoOrdenGastoBsq;
    }

    public Long getIdTipoFondoBsq() {
        return idTipoFondoBsq;
    }

    public void setIdTipoFondoBsq(Long idTipoFondoBsq) {
        this.idTipoFondoBsq = idTipoFondoBsq;
    }

    public Long getIdTipoImputacionBsq() {
        return idTipoImputacionBsq;
    }

    public void setIdTipoImputacionBsq(Long idTipoImputacionBsq) {
        this.idTipoImputacionBsq = idTipoImputacionBsq;
    }

    public Long getIdTipoAcumuladorBsq() {
        return idTipoAcumuladorBsq;
    }

    public void setIdTipoAcumuladorBsq(Long idTipoAcumuladorBsq) {
        this.idTipoAcumuladorBsq = idTipoAcumuladorBsq;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public Long getIdTipoOrdenGasto() {
        return idTipoOrdenGasto;
    }

    public void setIdTipoOrdenGasto(Long idTipoOrdenGasto) {
        this.idTipoOrdenGasto = idTipoOrdenGasto;
    }

    public Long getIdTipoFondo() {
        return idTipoFondo;
    }

    public void setIdTipoFondo(Long idTipoFondo) {
        this.idTipoFondo = idTipoFondo;
    }

    public Long getIdTipoImputacion() {
        return idTipoImputacion;
    }

    public void setIdTipoImputacion(Long idTipoImputacion) {
        this.idTipoImputacion = idTipoImputacion;
    }

    public Long getIdTipoAcumulador() {
        return idTipoAcumulador;
    }

    public void setIdTipoAcumulador(Long idTipoAcumulador) {
        this.idTipoAcumulador = idTipoAcumulador;
    }

    public Integer getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(Integer tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<Funcion> lstFuncionsAux = new LazyDataModel<Funcion>() {

            @Override
            public List<Funcion> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<Funcion> lstFuncionsAux = funcionFacade.findAll(idTipoOrdenGastoBsq,
                        idTipoFondoBsq, idTipoImputacionBsq, idTipoAcumuladorBsq, first, pageSize);

                return lstFuncionsAux;
            }

        };
        lstFuncionsAux.setRowCount(funcionFacade.countAll(idTipoOrdenGastoBsq,
                idTipoFondoBsq, idTipoImputacionBsq, idTipoAcumuladorBsq).intValue());
        return lstFuncionsAux;
    }

    @Override
    public void limpiar() {
        this.setIdTipoOrdenGasto(null);
        this.setIdTipoAcumulador(null);
        this.setIdTipoImputacion(null);
        this.setIdTipoFondo(null);
        this.setTipoOperacion(null);
    }

    @Override
    public String crear() {
        try {
            this.funcionFacade.create(this.getIdTipoOrdenGasto(),
                    this.getIdTipoFondo(), this.getIdTipoImputacion(),
                    this.getIdTipoAcumulador(), this.getTipoOperacion());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorFuncion");
            this.setMsgSuccessError("La función ha sido generada con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorFuncion");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            this.funcionFacade.create(this.getIdTipoOrdenGasto(),
                    this.getIdTipoFondo(), this.getIdTipoImputacion(),
                    this.getIdTipoAcumulador(), this.getTipoOperacion());
            this.setResultado("nuevaFuncion");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorFuncion");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idFuncionAux = Long.parseLong(myRequest.getParameter("id"));
        this.setFuncion(this.funcionFacade.find(idFuncionAux));
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idFuncionAux = Long.parseLong(myRequest.getParameter("id"));
            this.funcionFacade.remove(idFuncionAux);
            this.setResultado("funcionAuxConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorFuncion");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idFuncionAux = Long.parseLong(myRequest.getParameter("id"));
        Funcion funcionAux = this.funcionFacade.find(idFuncionAux);
        this.setId(funcionAux.getId());
        this.setIdTipoOrdenGasto(funcionAux.getTipoOrdenGasto().getId());
        this.setIdTipoFondo(funcionAux.getTipoFondo().getId());
        this.setIdTipoImputacion(funcionAux.getTipoImputacion().getId());
        this.setIdTipoAcumulador(funcionAux.getTipoAcumulador().getId());
        this.setTipoOperacion(funcionAux.getTipoOperacion());

    }

    @Override
    public String guardarEdicion() {
        try {
            this.funcionFacade.edit(this.getId(), this.getIdTipoOrdenGasto(),
                    this.getIdTipoFondo(), this.getIdTipoImputacion(),
                    this.getIdTipoAcumulador(), this.getTipoOperacion());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorFuncion");
            this.setMsgSuccessError("La función ha sido editada con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorFuncion");
        }
        return this.getResultado();
    }

    public List<SelectItem> getSelectItemsTipoOrdenGasto() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<TipoOrdenGasto> lstTipoOrdenGasto = this.tipoOrdenGastoFacade.findAll(true);
        if (!(lstTipoOrdenGasto.isEmpty())) {
            lstTipoOrdenGasto.stream().forEach((tipoOrdenGastoAux) -> {
                selectItems.add(new SelectItem(tipoOrdenGastoAux.getId(), tipoOrdenGastoAux.getId() + "-" + tipoOrdenGastoAux.getNombre()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsTipoFondo() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<TipoFondo> lstTipoFondo = this.tipoFondoFacade.findAll(true);
        if (!(lstTipoFondo.isEmpty())) {
            lstTipoFondo.stream().forEach((tipoFondoAux) -> {
                selectItems.add(new SelectItem(tipoFondoAux.getId(), tipoFondoAux.getId() + "-" + tipoFondoAux.getNombre()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsTipoAcumulador() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<TipoAcumulador> lstTipoAcumulador = this.tipoAcumuladorFacade.findAll(true);
        if (!(lstTipoAcumulador.isEmpty())) {
            lstTipoAcumulador.stream().forEach((tipoAcumuladorAux) -> {
                selectItems.add(new SelectItem(tipoAcumuladorAux.getId(), tipoAcumuladorAux.getId() + "-" + tipoAcumuladorAux.getNombre()));
            });
        }
        return selectItems;
    }

    public List<SelectItem> getSelectItemsTipoImputacion() {
        List<SelectItem> selectItems = new ArrayList<>();
        List<TipoImputacion> lstTipoImputacion = this.tipoImputacionFacade.findAll(true);
        if (!(lstTipoImputacion.isEmpty())) {
            lstTipoImputacion.stream().forEach((tipoImputacionAux) -> {
                selectItems.add(new SelectItem(tipoImputacionAux.getId(), tipoImputacionAux.getId() + "-" + tipoImputacionAux.getNombre()));
            });
        }
        return selectItems;
    }

    @Override
    public void actualizar() {
        this.setIdTipoOrdenGastoBsq(0L);
        this.setIdTipoFondoBsq(0L);
        this.setIdTipoImputacionBsq(0L);
        this.setIdTipoAcumuladorBsq(0L);
        super.actualizar();
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
