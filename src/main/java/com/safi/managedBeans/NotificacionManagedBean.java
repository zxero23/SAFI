package com.safi.managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.safi.entity.Usuario;
import com.safi.entity.Notificacion;
import com.safi.facade.NotificacionFacadeLocal;
import com.safi.managedBeans.UtilManagedBean;
import com.safi.managedBeans.WebManagedBean;

/**
 * @author Doroñuk Gustavo
 */
@ManagedBean
@RequestScoped
public class NotificacionManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private NotificacionFacadeLocal notificacionFacade;

    private String mensaje;
    private Usuario usuario;
    private int leidoBsq;
    private Date fechaDesdeBsq;
    private Date fechaHastaBsq;


    public NotificacionManagedBean() {
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaDesdeBsq() {
        return fechaDesdeBsq;
    }

    public void setFechaDesdeBsq(Date fechaDesdeBsq) {
        this.fechaDesdeBsq = fechaDesdeBsq;
    }

    public Date getFechaHastaBsq() {
        return fechaHastaBsq;
    }

    public void setFechaHastaBsq(Date fechaHastaBsq) {
        this.fechaHastaBsq = fechaHastaBsq;
    }

    public int getLeidoBsq() {
        return leidoBsq;
    }

    public void setLeidoBsq(int leidoBsq) {
        this.leidoBsq = leidoBsq;
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                this.setUsuario(sessionBean.getUsuario());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevaNotificacion")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("leerNotificacion"))).forEach((_item) -> {
                        this.setBaja(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public LazyDataModel getListElements() {
        LazyDataModel<Notificacion> lstNotificacionAux = new LazyDataModel<Notificacion>() {
            @Override
            public List<Notificacion> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {
                List<Notificacion> lstNotificacionAux = new ArrayList<>();
                if (usuario.getServicio() != null) {
                    lstNotificacionAux = notificacionFacade.findAll(usuario.getServicio().getId(), leidoBsq, fechaDesdeBsq, fechaHastaBsq, first, pageSize);
                } else {
                    lstNotificacionAux = notificacionFacade.findAll(0L, leidoBsq, fechaDesdeBsq, fechaHastaBsq, first, pageSize);
                }
                return lstNotificacionAux;
            }
        };
        if (usuario.getServicio() != null) {
            lstNotificacionAux.setRowCount(notificacionFacade.count(usuario.getServicio().getId(), leidoBsq, fechaDesdeBsq, fechaHastaBsq).intValue());
        } else {
            lstNotificacionAux.setRowCount(notificacionFacade.count(0L, leidoBsq, fechaDesdeBsq, fechaHastaBsq).intValue());
        }
        return lstNotificacionAux;
    }

    @Override
    public List<SelectItem> getSelectItems() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void limpiar() {
        this.setMensaje(null);
        this.setLeidoBsq(0);
        this.setFechaDesdeBsq(null);
        this.setFechaHastaBsq(null);
    }

    @Override
    public void actualizar() {
        this.setLeidoBsq(0);
        this.setFechaDesdeBsq(null);
        this.setFechaHastaBsq(null);
        super.actualizar();
    }

    @Override
    public String crear() {
        try {
            this.notificacionFacade.create(this.getUsuario().getId(), this.getMensaje());
            this.setTitle("Proceso completo");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorNotificacion");
            this.setMsgSuccessError("La notificación ha sido enviada con éxito.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception e) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(e.getMessage());
            this.setResultado("successErrorNotificacion");
        }
        return this.getResultado();
    }

    public void leer() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAux = Long.parseLong(myRequest.getParameter("id"));
            this.notificacionFacade.leer(idAux);
        } catch (Exception e) {
        }
    }

    @Override
    public String crearOtro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void verDetalle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardarBorrado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void prepararParaEditar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String guardarEdicion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
