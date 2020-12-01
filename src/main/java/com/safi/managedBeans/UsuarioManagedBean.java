package com.safi.managedBeans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
import com.safi.entity.Grupo;
import com.safi.entity.Usuario;
import com.safi.entity.Servicio;
import com.safi.enums.AccionEnum;
import com.safi.facade.GrupoFacadeLocal;
import com.safi.facade.UsuarioFacadeLocal;
import com.safi.enums.GrupoUsuarioEnum;
import com.safi.facade.AuditoriaFacadeLocal;
import com.safi.facade.EjercicioFacadeLocal;
import com.safi.facade.ServicioFacadeLocal;
import com.safi.utilidad.EncriptadorAES;

/**
 *
 * @author matias
 */
@ManagedBean
@SessionScoped
public class UsuarioManagedBean extends UtilManagedBean implements Serializable {

    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private GrupoFacadeLocal grupoFacade;
    @EJB
    private ServicioFacadeLocal servicioFacade;
    @EJB
    private AuditoriaFacadeLocal auditoriaFacade;
    private String apellidoNombre;
    private String uss;
    private String pass;
    private String passActual;
    private String passNueva;
    private String passConfirmar;
    private String email;
    private Long dni;
    private Date fechaAlta;
    private Date fechaModif;
    private Date ultimoLogin;
    private String fotoPerfil;
    private List<Long> idItems = new ArrayList<>();
    private List<Grupo> lstGrupos = new ArrayList<>();
    private List<SelectItem> selecteditemsIzquierda;
    private List<SelectItem> selecteditemsDerecha;
    private String servicio;
    private String carpetaArchivos = "/home/archivosEAppSAFI/FotoPerfil/";
    private String path;
    /*Atributos de Busqueda */
    private String apellidoNombreBsq = "";
    private String nombreUsuarioBsq = "";
    private Long idServicioBsq;
    private Long idGrupoBsq;
    private Long dniBsq;
    private Boolean gestionarUsuariosServicios = false;
    private boolean restorePassword;
    private Usuario usuario;
    /*
     Fin atributos de busqueda
     */
    //Para el control de nombre de email cuando edita o es nuevo
    private String emailOriginal;

    /**
     * Creates a new instance of UsuarioManagedBean
     */
    public UsuarioManagedBean() {
        this.selecteditemsDerecha = new ArrayList<>();
        this.selecteditemsIzquierda = new ArrayList<>();
    }

    @PostConstruct
    private void init() {
        WebManagedBean sessionBean = this.getSessionBean();
        this.setUsuario(sessionBean.getUsuario());
        if (sessionBean != null) {
            try {
                this.setLstActionItems(sessionBean.getLstActionItems());
                this.setId(sessionBean.getUsuario().getId());
                if (!(getLstActionItems().isEmpty())) {
                    getLstActionItems().stream().map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("nuevoUsuario")) {
                            this.setAlta(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("gestionarUsuariosServicios")) {
                            this.setGestionarUsuariosServicios(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("editarUsuario")) {
                            this.setModificacion(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("cambiarClave")) {
                            this.setRestorePassword(true);
                        }
                        return accionAux;
                    }).map((accionAux) -> {
                        if (accionAux.getNombre().equalsIgnoreCase("borrarUsuario")) {
                            this.setBaja(true);
                        }
                        return accionAux;
                    }).filter((accionAux) -> (accionAux.getNombre().equalsIgnoreCase("detalleUsuario"))).forEach((_item) -> {
                        this.setDetalle(true);
                    });
                }
            } catch (Exception e) {

            }
        }
    }

    public Boolean getGestionarUsuariosServicios() {
        return gestionarUsuariosServicios;
    }

    public void setGestionarUsuariosServicios(Boolean gestionarUsuariosServicios) {
        this.gestionarUsuariosServicios = gestionarUsuariosServicios;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public List<Long> getIdItems() {
        return idItems;
    }

    public void setIdItems(List<Long> idItems) {
        this.idItems = idItems;
    }

    public String getApellidoNombre() {
        return apellidoNombre;
    }

    public void setApellidoNombre(String apellidoNombre) {
        this.apellidoNombre = apellidoNombre;
    }

    public String getUss() {
        return uss;
    }

    public void setUss(String uss) {
        this.uss = uss;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public UsuarioFacadeLocal getUsuarioFacade() {
        return usuarioFacade;
    }

    public void setUsuarioFacade(UsuarioFacadeLocal usuarioFacade) {
        this.usuarioFacade = usuarioFacade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaModif() {
        return fechaModif;
    }

    public void setFechaModif(Date fechaModif) {
        this.fechaModif = fechaModif;
    }

    public Date getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(Date ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }

    public GrupoFacadeLocal getGrupoFacade() {
        return grupoFacade;
    }

    public void setGrupoFacade(GrupoFacadeLocal grupoFacade) {
        this.grupoFacade = grupoFacade;
    }

    public List<Grupo> getLstGrupos() {
        return lstGrupos;
    }

    public void setLstGrupos(List<Grupo> lstGrupos) {
        this.lstGrupos = lstGrupos;
    }

    public List<SelectItem> getSelecteditemsIzquierda() {
        return selecteditemsIzquierda;
    }

    public void setSelecteditemsIzquierda(List<SelectItem> selecteditemsIzquierda) {
        this.selecteditemsIzquierda = selecteditemsIzquierda;
    }

    public List<SelectItem> getSelecteditemsDerecha() {
        return selecteditemsDerecha;
    }

    public void setSelecteditemsDerecha(List<SelectItem> selecteditemsDerecha) {
        this.selecteditemsDerecha = selecteditemsDerecha;
    }

    public String getPassActual() {
        return passActual;
    }

    public void setPassActual(String passActual) {
        this.passActual = passActual;
    }

    public String getPassConfirmar() {
        return passConfirmar;
    }

    public void setPassConfirmar(String passConfirmar) {
        this.passConfirmar = passConfirmar;
    }

    public String getApellidoNombreBsq() {
        return apellidoNombreBsq;
    }

    public void setApellidoNombreBsq(String apellidoNombreBsq) {
        this.apellidoNombreBsq = apellidoNombreBsq;
    }

    public String getNombreUsuarioBsq() {
        return nombreUsuarioBsq;
    }

    public void setNombreUsuarioBsq(String nombreUsuarioBsq) {
        this.nombreUsuarioBsq = nombreUsuarioBsq;
    }

    public Long getIdServicioBsq() {
        return idServicioBsq;
    }

    public void setIdServicioBsq(Long idServicioBsq) {
        this.idServicioBsq = idServicioBsq;
    }

    public Long getIdGrupoBsq() {
        return idGrupoBsq;
    }

    public void setIdGrupoBsq(Long idGrupoBsq) {
        this.idGrupoBsq = idGrupoBsq;
    }

    public String getPassNueva() {
        return passNueva;
    }

    public void setPassNueva(String passNueva) {
        this.passNueva = passNueva;
    }

    public String getEmailOriginal() {
        return emailOriginal;
    }

    public void setEmailOriginal(String emailOriginal) {
        this.emailOriginal = emailOriginal;
    }

    public String getCarpetaArchivos() {
        return carpetaArchivos;
    }

    public void setCarpetaArchivos(String carpetaArchivos) {
        this.carpetaArchivos = carpetaArchivos;
    }

    public boolean isRestorePassword() {
        return restorePassword;
    }

    public void setRestorePassword(boolean restorePassword) {
        this.restorePassword = restorePassword;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public Long getDniBsq() {
        return dniBsq;
    }

    public void setDniBsq(Long dniBsq) {
        this.dniBsq = dniBsq;
    }

    @Override
    public LazyDataModel getListElements() {
        Grupo grupoAnalisisInformatica = grupoFacade.find(GrupoUsuarioEnum.DIRECCION_ANALISIS_INFORMATICA.getId());
        LazyDataModel<Usuario> lstUsuariosAux = new LazyDataModel<Usuario>() {
            @Override
            public List<Usuario> load(int first, int pageSize,
                    final SortCriteria[] criteria, final Map<String, String> filters) {

                List<Long> lstGrupos = Arrays.asList(GrupoUsuarioEnum.DIRECCION_ANALISIS_INFORMATICA.getId(), GrupoUsuarioEnum.REPORTES.getId(),
                        GrupoUsuarioEnum.SERVICIO_ADMINISTRATIVO.getId(), GrupoUsuarioEnum.SERVICIOADMINISTRATIVOREPORTES.getId(),
                        GrupoUsuarioEnum.DIRECCION_GENERAL_DE_PRESUPUESTO.getId(), GrupoUsuarioEnum.DIRECTOR_REGISTRO_PROVEEDORES.getId(), GrupoUsuarioEnum.PROVEEDORES.getId());
                List<Usuario> lstUsuariosAux = usuarioFacade.findAll(gestionarUsuariosServicios, usuario.getGrupos().contains(grupoAnalisisInformatica) ? lstGrupos : new ArrayList(),
                        apellidoNombreBsq, nombreUsuarioBsq, idGrupoBsq, idServicioBsq, dniBsq, first, pageSize);
                return lstUsuariosAux;
            }

        };
        List<Long> lstGrupos = Arrays.asList(GrupoUsuarioEnum.DIRECCION_ANALISIS_INFORMATICA.getId(), GrupoUsuarioEnum.REPORTES.getId(),
                GrupoUsuarioEnum.SERVICIO_ADMINISTRATIVO.getId(), GrupoUsuarioEnum.SERVICIOADMINISTRATIVOREPORTES.getId(),
                GrupoUsuarioEnum.DIRECCION_GENERAL_DE_PRESUPUESTO.getId(), GrupoUsuarioEnum.DIRECTOR_REGISTRO_PROVEEDORES.getId(), GrupoUsuarioEnum.PROVEEDORES.getId());
        lstUsuariosAux.setRowCount(usuarioFacade.countAll(gestionarUsuariosServicios, usuario.getGrupos().contains(grupoAnalisisInformatica) ? lstGrupos : new ArrayList(),
                apellidoNombreBsq, nombreUsuarioBsq, idGrupoBsq, idServicioBsq, dniBsq).intValue());
        return lstUsuariosAux;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    @Override
    public void limpiar() {
        this.setApellidoNombre(null);
        this.setUss(null);
        this.setPass(null);
        this.getIdItems().clear();
        this.setEmail(null);
        this.setFechaAlta(null);
        this.setFechaModif(null);
        this.setUltimoLogin(null);
        this.setPassActual(null);
        this.setPassConfirmar(null);
        this.setServicio(null);
        this.setPassNueva(null);
        this.setNombreOriginal(null);
        this.setEmailOriginal(null);
        this.setDni(null);
    }

    public void limpiarGuardarYCrearOtro() {
        this.setApellidoNombre(null);
        this.setUss(null);
        this.setPass(null);
        this.setEmail(null);
        this.setFechaAlta(null);
        this.setFechaModif(null);
        this.setUltimoLogin(null);
        this.setPassActual(null);
        this.setPassConfirmar(null);
        this.setServicio(null);
        this.setPassNueva(null);
        this.setNombreOriginal(null);
        this.setEmailOriginal(null);
        this.setDni(null);
    }

    @Override
    public String crear() {
        try {
            if (idItems.isEmpty()) {
                throw new Exception("Debe seleccionar al menos un grupo.");
            }
            List<Grupo> lstAux = this.grupoFacade.finGrupos(idItems);
            this.usuarioFacade.create(this.getApellidoNombre(),
                    this.getUss(), this.getEmail(), lstAux, this.getUsuario().getId(), this.getDni());
            this.crearAuditoria(AccionEnum.ALTA_USUARIO.getName(), this.getUsuario(), "-", this.devolverValorActual(getApellidoNombre(), this.getUss(), this.getEmail()), this.ejercicioFacade.ejercicioActual());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorUsuario");
            this.setMsgSuccessError("El usuario ha sido generado con éxito. Se le ha enviado a su correo electrónico la información necesaria para iniciar sesión.");
            this.limpiar();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorUsuario");
        }
        return this.getResultado();
    }

    @Override
    public String crearOtro() {
        try {
            if (idItems.isEmpty()) {
                throw new Exception("Debe seleccionar al menos un grupo.");
            }
            List<Grupo> lstAux = this.grupoFacade.finGrupos(idItems);
            this.usuarioFacade.create(this.getApellidoNombre(),
                    this.getUss(), this.getEmail(), lstAux, this.getUsuario().getId(), this.getDni());
            this.crearAuditoria(AccionEnum.ALTA_USUARIO.getName(), this.getUsuario(), "-", this.devolverValorActual(getApellidoNombre(), this.getUss(), this.getEmail()), this.ejercicioFacade.ejercicioActual());
            this.setResultado("nuevoUsuario");
            this.limpiarGuardarYCrearOtro();
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorUsuario");
        }
        return this.getResultado();
    }

    @Override
    public void verDetalle() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAux = Long.parseLong(myRequest.getParameter("id"));
        Usuario usuarioAux = this.usuarioFacade.find(idAux);
        this.setId(usuarioAux.getId());
        this.setDni(usuarioAux.getDni());
        this.setApellidoNombre(usuarioAux.getApellidoNombre());
        this.setUss(usuarioAux.getNombreUsuario());
        this.setPass(usuarioAux.getClaveAcceso());
        this.setEmail(usuarioAux.getEmail());
        this.setFechaAlta(usuarioAux.getFechaAlta());
        this.setFechaModif(usuarioAux.getFechaModificacion());
        this.setUltimoLogin(usuarioAux.getUltimoLogin());
        this.setServicio(usuarioAux.getServicio() != null ? usuarioAux.getServicio().getCodigo() + " - " + usuarioAux.getServicio().getDescripcion() : "SIN ASIGNAR");
        this.setLstGrupos(usuarioAux.getGrupos());
        this.setDni(usuarioAux.getDni());
    }

    @Override
    public void guardarBorrado() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAux = Long.parseLong(myRequest.getParameter("id"));
            this.usuarioFacade.remove(idAux);
            this.setTitle(null);
            this.setImages(null);
            this.setMsgSuccessError(null);
            this.setResultado("usuarioConf");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorUsuario");
        }
    }

    @Override
    public void prepararParaEditar() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
        Long idAux = Long.parseLong(myRequest.getParameter("id"));
        Usuario usuarioAux = this.usuarioFacade.find(idAux);
        this.setId(usuarioAux.getId());
        this.setApellidoNombre(usuarioAux.getApellidoNombre());
        this.setUss(usuarioAux.getNombreUsuario());
        this.setPass(usuarioAux.getClaveAcceso());
        this.setEmail(usuarioAux.getEmail());
        this.gruposDisponibles(usuarioAux);
        this.gruposSeleccionados(usuarioAux);
        this.setNombreOriginal(usuarioAux.getNombreUsuario());
        this.setEmailOriginal(usuarioAux.getEmail());
        this.setDni(usuarioAux.getDni());
        this.setAuditoriaAnterior(this.devolverValorActual(usuarioAux.getApellidoNombre(), usuarioAux.getNombreUsuario(), usuarioAux.getEmail()));
    }

    public void gruposDisponibles(Usuario usuario) {
        Grupo grupoAnalisisInformatica = grupoFacade.find(GrupoUsuarioEnum.DIRECCION_ANALISIS_INFORMATICA.getId());
        this.selecteditemsDerecha.clear();
        List<Grupo> listGrupos = new ArrayList();
        if (this.getUsuario().getGrupos().contains(grupoAnalisisInformatica)) {
            List<Long> lstGrupos = Arrays.asList(GrupoUsuarioEnum.DIRECCION_ANALISIS_INFORMATICA.getId(), GrupoUsuarioEnum.REPORTES.getId(),
                    GrupoUsuarioEnum.SERVICIO_ADMINISTRATIVO.getId(), GrupoUsuarioEnum.SERVICIOADMINISTRATIVOREPORTES.getId(),
                    GrupoUsuarioEnum.DIRECCION_GENERAL_DE_PRESUPUESTO.getId(), GrupoUsuarioEnum.DIRECTOR_REGISTRO_PROVEEDORES.getId(), GrupoUsuarioEnum.PROVEEDORES.getId());

            listGrupos = this.grupoFacade.finGrupos(lstGrupos);
            listGrupos.removeAll(usuario.getGrupos());
        } else {
            listGrupos = this.grupoFacade.findAll(true);
            listGrupos.removeAll(usuario.getGrupos());
        }
        if (!(listGrupos.isEmpty())) {
            listGrupos.stream().forEach((grupoAux) -> {
                this.selecteditemsDerecha.add(new SelectItem(grupoAux.getId(), grupoAux.getNombre()));
            });
        }
    }

    public void gruposSeleccionados(Usuario usuario) {
        this.selecteditemsIzquierda.clear();
        List<Grupo> listGrupos = usuario.getGrupos();
        if (!(listGrupos.isEmpty())) {
            listGrupos.stream().forEach((grupoAux) -> {
                this.selecteditemsIzquierda.add(new SelectItem(grupoAux.getId(), grupoAux.getNombre()));
            });
        }
    }

    @Override
    public String guardarEdicion() {
        try {
            List<Long> lstAux = new ArrayList<>();
            this.selecteditemsIzquierda.stream().forEach((object) -> {
                lstAux.add((Long) object.getValue());
            });

            usuarioFacade.edit(this.getId(), this.getApellidoNombre(),
                    this.getUss(), this.getEmailOriginal(), this.getEmail(), lstAux, this.getDni());
            this.crearAuditoria(AccionEnum.EDICION_USUARIO.getName(), this.getUsuario(), this.getAuditoriaAnterior(), this.devolverValorActual(getApellidoNombre(), this.getUss(), this.getEmail()), this.ejercicioFacade.ejercicioActual());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorUsuario");
            this.setMsgSuccessError("El usuario ha sido editado con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorUsuario");
        }
        return this.getResultado();
    }

    /**
     * Controla que el grupo direccion analisis cree solo usuarios con grupo
     * servicio administrativo
     *
     * @author Gonzalez Facundo
     */
    @Override
    public List<SelectItem> getSelectItems() {
        List<SelectItem> returnList = new ArrayList();
        Grupo grupoAnalisisInformatica = grupoFacade.find(GrupoUsuarioEnum.DIRECCION_ANALISIS_INFORMATICA.getId());

        this.selecteditemsDerecha.clear();
        List<Grupo> listGrupos = new ArrayList();
        if (this.getUsuario().getGrupos().contains(grupoAnalisisInformatica)) {
            List<Long> lstGrupos = Arrays.asList(GrupoUsuarioEnum.DIRECCION_ANALISIS_INFORMATICA.getId(), GrupoUsuarioEnum.REPORTES.getId(),
                    GrupoUsuarioEnum.SERVICIO_ADMINISTRATIVO.getId(), GrupoUsuarioEnum.SERVICIOADMINISTRATIVOREPORTES.getId(),
                    GrupoUsuarioEnum.DIRECCION_GENERAL_DE_PRESUPUESTO.getId(), GrupoUsuarioEnum.DIRECTOR_REGISTRO_PROVEEDORES.getId(), GrupoUsuarioEnum.PROVEEDORES.getId());

            listGrupos = this.grupoFacade.finGrupos(lstGrupos);
        } else {
            listGrupos = this.grupoFacade.findAll(true);
        }

        if (!(listGrupos.isEmpty())) {
            listGrupos.stream().forEach((grupoAux) -> {
                returnList.add(new SelectItem(grupoAux.getId(), grupoAux.getNombre()));
            });
        }
        return returnList;
    }

    public List<SelectItem> getSelectItemsServicio() {
        try {
            List<SelectItem> selectItems = new ArrayList<>();
            List<Servicio> lstServicioAux = this.servicioFacade.findAll(true);
            if (!(lstServicioAux.isEmpty())) {
                lstServicioAux.stream().sorted((p1, p2) -> (Integer.valueOf(p1.getCodigo()).compareTo(Integer.valueOf(p2.getCodigo())))).forEach((servicioAux) -> {
                    selectItems.add(new SelectItem(servicioAux.getId(), servicioAux.getCodigo() + " - " + servicioAux.getAbreviatura()));
                });
            }
            return selectItems;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void prepararParaEditarContraseña(Long id) {
        Usuario usuarioAux = this.usuarioFacade.find(id);
        this.setId(usuarioAux.getId());
        this.setApellidoNombre(usuarioAux.getApellidoNombre());
        this.setUss(usuarioAux.getNombreUsuario());
        this.setEmail(usuarioAux.getEmail());
        this.gruposDisponibles(usuarioAux);
        this.gruposSeleccionados(usuarioAux);
    }

    public String guardarConfiguracion() {
        try {
            usuarioFacade.edit(this.getId(), this.getPassActual(), this.getPassNueva(), this.getPassConfirmar());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorConfiguracion");
            this.setMsgSuccessError("El usuario ha sido editado con éxito.");
            this.crearAuditoria(AccionEnum.CAMBIO_CONTRASENIA.getName(), this.getUsuario(), "-", "-", this.ejercicioFacade.ejercicioActual());
            this.setEsCorrecto(true);
        } catch (Exception e) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(e.getMessage());
            this.setResultado("successErrorConfiguracion");
        }

        return this.getResultado();

    }

    @Override
    public void actualizar() {
        this.setApellidoNombreBsq("");
        this.setNombreUsuarioBsq("");
        this.setIdGrupoBsq(0L);
        this.setIdServicioBsq(0L);
        this.setDniBsq(null);
        super.actualizar();
    }

    public void coincidenciaClaveVieja(final FacesContext context, final UIComponent validate, final Object value) throws FileNotFoundException, IOException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        String claveVieja = (String) value;
        WebManagedBean sessionBean = this.getSessionBean();
        Usuario usuarioAux = this.usuarioFacade.find(sessionBean.getUsuario().getId());
        EncriptadorAES encriptador = new EncriptadorAES();
        Properties prop = new Properties();
        File propsFile = new File("/home/config.properties");
        try (InputStream is = new FileInputStream(propsFile)) {
            if (is != null) { //Evalua que el archivo exista
                prop.load(is);
            } else {
                throw new FileNotFoundException("Archivo properties '" + propsFile + "' no encontrado.");
            }
            is.close();
        }
        //Recueperando los generos
        String claveSecretaUsuario = prop.getProperty("claveSecretaUsuario");
        this.setId(usuarioAux.getId());
        this.setPass(encriptador.desencriptar(usuarioAux.getClaveAcceso().trim(), claveSecretaUsuario));
        if (!claveVieja.equalsIgnoreCase(this.getPass())) {
            final FacesMessage msg = new FacesMessage("La contraseña actual no es válida.");
            context.addMessage(validate.getClientId(context), msg);
            throw new ValidatorException(msg);
        }
    }

    public void coincidenciaClaveNueva(final FacesContext context, final UIComponent validate, final Object value) {
        String claveRepite = (String) value;
        WebManagedBean sessionBean = this.getSessionBean();
        Usuario usuarioAux = this.usuarioFacade.find(sessionBean.getUsuario().getId());
        this.setId(usuarioAux.getId());
        this.setPass(usuarioAux.getClaveAcceso());
        if (!claveRepite.equalsIgnoreCase(this.getPassNueva())) {
            final FacesMessage msg = new FacesMessage("No coinciden las contraseñas.");
            context.addMessage(validate.getClientId(context), msg);
            throw new ValidatorException(msg);
        }
    }

    public void validarUsuarioNuevo(final FacesContext context, final UIComponent validate, final Object value) {
        this.validatorEspaciosEnBlanco(context, validate, value);
        String userAux = (String) value;
        List<Usuario> lstUsuarioAux = this.usuarioFacade.findAll("", userAux.trim());
        if (!lstUsuarioAux.isEmpty()) {
            final FacesMessage msg = new FacesMessage("El nombre de usuario \"" + userAux + "\" ya existe en el sistema.");
            context.addMessage(validate.getClientId(context), msg);
            throw new ValidatorException(msg);
        }
    }

    public void validarUsuarioEditar(final FacesContext context, final UIComponent validate, final Object value) {
        String userAux = (String) value;
        List<Usuario> lstUsuarioAux = this.usuarioFacade.findAll("", userAux);
        if (!lstUsuarioAux.isEmpty() && !userAux.equalsIgnoreCase(this.getNombreOriginal())) {
            final FacesMessage msg = new FacesMessage("El nombre de usuario \"" + userAux + "\" ya existe en el sistema.");
            context.addMessage(validate.getClientId(context), msg);
            throw new ValidatorException(msg);
        }
    }

    public String retorePassword() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest myRequest = (HttpServletRequest) context.getExternalContext().getRequest();
            Long idAux = Long.parseLong(myRequest.getParameter("id"));
            this.usuarioFacade.restorePassword(idAux);
            Usuario usuarioAux = this.usuarioFacade.find(idAux);
            this.crearAuditoria(AccionEnum.CAMBIO_CONTRASENIA.getName(), this.getUsuario(), "-", this.devolverValorActual(usuarioAux.getApellidoNombre(), usuarioAux.getNombreUsuario(), usuarioAux.getEmail()), this.ejercicioFacade.ejercicioActual());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setMsgSuccessError("Se ha restaurado la contraseña con éxito. Se le ha enviado a su correo electrónico la información para iniciar sesión.");
            this.setResultado("successErrorUsuario");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorUsuario");
        }
        return this.getResultado();
    }

    public void crearAuditoria(String accion, Usuario usuarioAccion, String valorAnterior, String valorActual, Long idEjercicio) {
        this.auditoriaFacade.create(accion, new Date(), usuarioAccion.getId(), valorAnterior, valorActual, idEjercicio);
    }

    public String devolverValorActual(String apellidoNombre, String nombreUsuario, String email) {
        return "Apellido y Nombre: " + apellidoNombre + "\n"
                + "Nombre de Usuario:" + nombreUsuario + "\n"
                + "Email:" + email + "\n";

    }

}
