package com.safi.managedBeans;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
import com.safi.entity.Usuario;
import com.safi.entity.CodigoExpediente;
import com.safi.entity.Ejercicio;
import com.safi.entity.Servicio;
import com.safi.facade.CodigoExpedienteFacadeLocal;
import com.safi.facade.EjercicioFacadeLocal;
import com.safi.facade.ServicioFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 *
 * @author González Facundo
 */
@ManagedBean
@SessionScoped
public class CodigoExpedienteManagedBean extends UtilManagedBean {

    @EJB
    private CodigoExpedienteFacadeLocal codigoExpedienteFacade;
    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    private String codigo;
    private String codigoBsq;
    private List<Servicio> lstServicio = new ArrayList<>();
    private List<SelectItem> selectItemsEjercicio;
    private Long idEjercicio = 0L;

    public CodigoExpedienteManagedBean() {
    }

    public List<CodigoExpediente> getLstCodigoExpediente() {
        return this.codigoExpedienteFacade.findAll().stream().filter(x -> x.isEstado()).collect(Collectors.toList());
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<CodigoExpediente> lstCodigoExpedienteAux = new LazyDataModel<CodigoExpediente>() {
            @Override
            public List<CodigoExpediente> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {
                List<CodigoExpediente> lstCodigoExpedienteAux = codigoExpedienteFacade.findAll(codigoBsq, first, pageSize);
                return lstCodigoExpedienteAux;
            }

        };
        lstCodigoExpedienteAux.setRowCount(codigoExpedienteFacade.countAll(codigoBsq).intValue());
        return lstCodigoExpedienteAux;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoBsq() {
        return codigoBsq;
    }

    public void setCodigoBsq(String codigoBsq) {
        this.codigoBsq = codigoBsq;
    }

    public List<Servicio> getLstServicio() {
        return lstServicio;
        
    }
      public List<Servicio> getLstServicioSorted() {
        return lstServicio.stream()
                .sorted((s1, s2) -> (Integer.valueOf(s1.getCodigo()).compareTo(Integer.valueOf(s2.getCodigo()))))
                .collect(Collectors.toList());
        
    }

    public void setLstServicio(List<Servicio> lstServicio) {
        this.lstServicio = lstServicio;
    }

    public Long getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(Long idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoCodigoExpediente")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarCodigoExpediente")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarCodigoExpediente")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleCodigoExpediente"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void limpiar() {
        this.setCodigo(null);
    }

    @Override
    public String crear() {
        try {
            CodigoExpediente codigoExpediente = new CodigoExpediente();
            codigoExpediente.setEstado(true);
            codigoExpediente.setCodigo(this.getCodigo());
            this.codigoExpedienteFacade.create(codigoExpediente);
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorCodigoExpediente");
            this.setMsgSuccessError("El código de expediente ha sido generado con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCodigoExpediente");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            CodigoExpediente codigoExpediente = new CodigoExpediente();
            codigoExpediente.setCodigo(this.getCodigo());
            codigoExpediente.setEstado(true);
            this.codigoExpedienteFacade.create(codigoExpediente);
            this.setResultado("nuevoCodigoExpediente");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCodigoExpediente");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idCodigoExpedienteAux = Long.parseLong(myRequest.getParameter("id"));
        CodigoExpediente codigoExpedienteAux = this.codigoExpedienteFacade.find(idCodigoExpedienteAux);
        this.setId(codigoExpedienteAux.getId());
        this.setCodigo(codigoExpedienteAux.getCodigo());
        this.setIdEjercicio(this.ejercicioFacade.ejercicioActual());
        mostrarExpedienteSegunEjercicio(this.getIdEjercicio());
    }

    public List<SelectItem> getSelectItemsEjercicio() {
        this.selectItemsEjercicio = new ArrayList<>();
        List<Ejercicio> lstEjercicioAux = this.ejercicioFacade.findAll(0, 0L);
        if (!(lstEjercicioAux.isEmpty())) {
            lstEjercicioAux.stream().forEach((ejercicioAux) -> {
                this.selectItemsEjercicio.add(new SelectItem(ejercicioAux.getId(), ejercicioAux.getAnio().toString()));
            });
        }
        return this.selectItemsEjercicio;
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idCodigoExpedienteAux = Long.parseLong(myRequest.getParameter("id"));
            CodigoExpediente codigoExpediente = new CodigoExpediente();
            codigoExpediente = this.codigoExpedienteFacade.find(idCodigoExpedienteAux);
            codigoExpediente.setEstado(false);
            this.codigoExpedienteFacade.edit(codigoExpediente);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("codigoExpedienteConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCodigoExpediente");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idCodigoExpedienteAux = Long.parseLong(myRequest.getParameter("id"));
        CodigoExpediente codigoExpedienteAux = this.codigoExpedienteFacade.find(idCodigoExpedienteAux);
        this.setId(codigoExpedienteAux.getId());
        this.setCodigo(codigoExpedienteAux.getCodigo());
    }

    @Override
    public String guardarEdicion() {
        try {
            CodigoExpediente codigoExpediente = new CodigoExpediente();
            codigoExpediente.setCodigo(this.getCodigo());
            codigoExpediente.setId(this.getId());
            codigoExpediente.setEstado(true);
            this.codigoExpedienteFacade.edit(codigoExpediente);
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorCodigoExpediente");
            this.setMsgSuccessError("El código de expediente ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorCodigoExpediente");
        }
        return this.getResultado();
    }

    @Override
    public void actualizar() {
        this.setCodigoBsq("");
        super.actualizar(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void validarCodigo(final FacesContext context, final UIComponent validate, final Object value) {
        this.validatorEspaciosEnBlanco(context, validate, value);
        List<CodigoExpediente> lstCodigosExpediente = this.codigoExpedienteFacade.findAll();
        for (CodigoExpediente codigoExp : lstCodigosExpediente) {
            Long c = Long.parseLong(codigoExp.getCodigo());
            if (c.equals(Long.parseLong(value.toString().trim()))) {
                final FacesMessage msg = new FacesMessage("El código de expediente ya existe.");
                context.addMessage(validate.getClientId(context), msg);
                throw new ValidatorException(msg);
            }
        }
    }

    public void mostrarExpedienteSegunEjercicio(Long idEjercicio) {
        this.getLstServicio().clear();
        Ejercicio unEjercicio = this.ejercicioFacade.find(idEjercicio);
        if (unEjercicio != null) {
            for (Servicio servicio : unEjercicio.getLstServicios()) {
                for (CodigoExpediente codigoExp : servicio.getLstCodigoExpediente()) {
                    if (codigoExp.getId().equals(this.getId())) {
                        this.getLstServicio().add(servicio);

                    }
                }
            }
        }

    }
}
