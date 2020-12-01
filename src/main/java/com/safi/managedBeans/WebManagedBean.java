package com.safi.managedBeans;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;
import org.icefaces.ace.component.fileentry.FileEntryStatus;
import com.safi.entity.ActionItem;
import com.safi.entity.Grupo;
import com.safi.entity.Menu;
import com.safi.entity.Servicio;
import com.safi.entity.Usuario;
import com.safi.enums.AccionEnum;
import com.safi.facade.GrupoFacadeLocal;
import com.safi.facade.UsuarioFacadeLocal;
import com.safi.facade.FeriadoFacadeLocal;
import com.safi.enums.GrupoUsuarioEnum;
import static com.safi.enums.GrupoUsuarioEnum.ADMIN;
import static com.safi.enums.GrupoUsuarioEnum.DIRECCION_ANALISIS_INFORMATICA;
import static com.safi.enums.GrupoUsuarioEnum.SERVICIO_ADMINISTRATIVO;
import static com.safi.enums.GrupoUsuarioEnum.SUPER_ADMIN;
import com.safi.utilidad.MailSenderThread;
import com.safi.enums.MenuEnum;
import com.safi.facade.AuditoriaFacadeLocal;
import com.safi.facade.EjercicioFacadeLocal;

/**
 *
 * @author matias
 */
@ManagedBean
@SessionScoped
public class WebManagedBean implements Serializable {
    
    @EJB
    private EjercicioFacadeLocal ejercicioFacade;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private GrupoFacadeLocal grupoFacade;
    @EJB
    private AuditoriaFacadeLocal auditoriaFacade;
    @EJB
    private FeriadoFacadeLocal feriadoFacade;
    private String nombreUsuario;
    private String claveAcceso;
    private String resultado;
    private String msgSuccessError;
    private String pathArchivosCuentaBancaria = "/home/archivosEAppSAFI/CuentaBancaria/";
    private String carpetaArchivos = "/home/archivosEAppSAFI/FotoPerfil/";
    private String path;
    private String title;
    private String images;
    private String fotoPerfil;
    private Usuario usuario;
    private String nombreGrupo;
    private Servicio servicio;
    private List feriados;
    private String emailForgetPassword;
    private String usuarioForgetPassword;
    private boolean botonGuardar;
    private List<ActionItem> lstActionItems = new ArrayList<>();
    private List<Menu> menus = new ArrayList();
    private boolean esCorrecto;

    /**
     * Creates a new instance of WebManagedBean
     */
    public WebManagedBean() {
        
    }
    
    @PostConstruct
    public void init() {
        this.botonGuardar = true;
    }
    
    public boolean isEsCorrecto() {
        return esCorrecto;
    }
    
    public void setEsCorrecto(boolean esCorrecto) {
        this.esCorrecto = esCorrecto;
    }
    
    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }
    
    public void setImages(String images) {
        this.images = images;
    }
    
    public void setMsgSuccessError(String msgSuccessError) {
        this.msgSuccessError = msgSuccessError;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }
    
    public String getNombreGrupo() {
        return nombreGrupo;
    }
    
    public String getClaveAcceso() {
        return claveAcceso;
    }
    
    public String getImages() {
        return images;
    }
    
    public String getMsgSuccessError() {
        return msgSuccessError;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public String getResultado() {
        return resultado;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public Servicio getServicio() {
        return servicio;
    }
    
    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }
    
    public String getPathArchivosCuentaBancaria() {
        return pathArchivosCuentaBancaria;
    }
    
    public void setPathArchivosCuentaBancaria(String pathArchivosCuentaBancaria) {
        this.pathArchivosCuentaBancaria = pathArchivosCuentaBancaria;
    }
    
    public String getEmailForgetPassword() {
        return emailForgetPassword;
    }
    
    public void setEmailForgetPassword(String emailForgetPassword) {
        this.emailForgetPassword = emailForgetPassword;
    }
    
    public String getFotoPerfil() {
        return fotoPerfil;
    }
    
    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
    
    public String getCarpetaArchivos() {
        return carpetaArchivos;
    }
    
    public void setCarpetaArchivos(String carpetaArchivos) {
        this.carpetaArchivos = carpetaArchivos;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public boolean isBotonGuardar() {
        return botonGuardar;
    }
    
    public void setBotonGuardar(boolean botonGuardar) {
        this.botonGuardar = botonGuardar;
    }
    
    public List getFeriados() {
        return feriados;
    }
    
    public void setFeriados(List feriados) {
        this.feriados = feriados;
    }
    
    public List<ActionItem> getLstActionItems() {
        return lstActionItems;
    }
    
    public void setLstActionItems(List<ActionItem> lstActionItems) {
        this.lstActionItems = lstActionItems;
    }
    
    public List<Menu> getMenus() {
        return menus;
    }
    
    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
    
    public String getUsuarioForgetPassword() {
        return usuarioForgetPassword;
    }
    
    public void setUsuarioForgetPassword(String usuarioForgetPassword) {
        this.usuarioForgetPassword = usuarioForgetPassword;
    }
    
    public void removeSessionBean(boolean isLogout) {
        {
            //managed beans base
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("accionManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("menuManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("grupoManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("usuarioManagedBean");

            //managed beans fondos
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cuentaBancariaManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("movimientoManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("periodoManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("servicioManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("servicioManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("funcionManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("expedienteManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("recursoPropioManagedBean");
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("reporteManagedBean");
            //managed beans proveedores
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("proveedorManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("tipoNormativaManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("actividadEconomicaManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cargoManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("estadoProveedorManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("estadoRequisitoPresentadoManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("feriadoManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("habilitacionMunicipalManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("impuestoIngresoBrutoManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("jurisdiccionManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("motivoDenegacionManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("normativaManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("reporteManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("requisitoManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("situacionAfipManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("tipoPersonaManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("tipoRequisitoManagedBean");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("tipoTramiteManagedBean");
            
            if (isLogout) { //Si es logout se limpia el managedBean sino tira error
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("webManagedBean");
            }
        }
    }
    
    public String login() {
        try {
            this.removeSessionBean(false); //Si no cierra sesión desde botón salir
            this.setLstActionItems(new ArrayList<>());
            this.setUsuario(null);
            this.setServicio(null);
            this.setMenus(new ArrayList());
            Usuario usuarioAux = usuarioFacade.find(this.getNombreUsuario(), this.getClaveAcceso());
            
            if (usuarioAux != null) {
                if (usuarioAux.isUpdatedPassword()) {
                    this.setNombreGrupo(usuarioAux.getGrupos().get(0).getNombre());
                    this.setUsuario(usuarioAux);
                    this.setServicio(usuarioAux.getServicio());
                    this.setResultado("configuracionConf");
                } else {
                    this.setNombreGrupo(usuarioAux.getGrupos().get(0).getNombre());
                    this.setUsuario(usuarioAux);
                    this.setServicio(usuarioAux.getServicio());
                    this.setMsgSuccessError("");
                    this.setResultado("administracion");
                    this.crearAuditoria(AccionEnum.LOGIN.getName(), usuarioAux, "-", this.devolverValorActual(usuarioAux.getApellidoNombre(), usuarioAux.getNombreUsuario(), usuarioAux.getEmail()), this.ejercicioFacade.ejercicioActual());
                    this.setMenus(this.menusUsuario());
                    List<Grupo> listGrupo = this.usuarioFacade.getGrupos(usuarioAux.getId());
                    if (!(listGrupo.isEmpty())) {
                        listGrupo.stream().forEach((grupoAux) -> {
                            this.setLstActionItems(this.grupoFacade.getAcciones(grupoAux.getId()));
                        });
                    }
                    this.lstActionItems.addAll(this.usuarioFacade.getAcciones(usuarioAux.getId()));
                    boolean EsAdmininistrador = false;
                    this.setFeriados(this.feriadoFacade.findAllFeriadoLogin(true));
                    if (this.getUsuario().getServicio() == null) {
                        if (!(listGrupo.isEmpty())) {
                            for (Grupo unGrupo : listGrupo) {
                                if (unGrupo.getId().equals(SUPER_ADMIN.getId()) || unGrupo.getId().equals(ADMIN.getId()) || unGrupo.getId().equals(DIRECCION_ANALISIS_INFORMATICA.getId())) {
                                    EsAdmininistrador = true;
                                }
                                if (EsAdmininistrador == false) {
                                    if (unGrupo.getId().equals(SERVICIO_ADMINISTRATIVO.getId())) {
                                        this.setEsCorrecto(false);
                                        this.setTitle("ADVERTENCIA");
                                        this.setImages("fa fa-exclamation-triangle");
                                        this.setResultado("successErrorReporte");
                                        this.setMsgSuccessError("Su usuario no esta asociado con ningún tipo de servicio administrativo. Por favor, contáctese con el administrador.");
                                        this.setResultado("successErrorUsuarioSinServicio");
                                        break;
                                    }
                                    
                                }
                                
                            }
                            
                        }
                        
                    }
                }
            } else {
                this.setResultado("successErrorLogin");
            }
            
        } catch (Exception ex) {
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorLogin");
        }
        return resultado;
    }
    
    public String logout() {
        this.setNombreUsuario(null);
        this.setClaveAcceso(null);
        this.setUsuario(null);
        this.setServicio(null);
        this.setLstActionItems(null);
        this.setServicio(null);
        this.removeSessionBean(true);
        return "index";
    }
    
    public String forgetPassword() {
        try {
            List<Usuario> lstUsuario = new ArrayList<>();
            lstUsuario = this.usuarioFacade.findByEmailAndUsuario(this.getEmailForgetPassword(), this.getUsuarioForgetPassword());
            if (lstUsuario.isEmpty()) {
                throw new Exception("No existe el usuario con ese correo en el sistema.");
            }
            this.usuarioFacade.restorePassword(lstUsuario.get(0).getId());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorForgetPassword");
            this.setMsgSuccessError("Se envió un e-mail con los datos de su cuenta. Revise su bandeja de entrada y/o spam.");
            this.setEsCorrecto(true);
        } catch (Exception e) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setResultado("successErrorForgetPassword");
            this.setMsgSuccessError("Error al recuperar credenciales de acceso: " + e.getMessage());
        }
        return this.getResultado();
    }
    
    public void limpiarFoto() {
        this.setImages(null);
        this.setPath(null);
    }
    
    public void limpiar() {
        this.setNombreUsuario(null);
        this.setClaveAcceso(null);
        this.setUsuario(null);
        this.setEmailForgetPassword(null);
        this.setTitle(null);
        this.setImages(null);
        this.setResultado(null);
        this.setMsgSuccessError(null);
    }
    
    public void prepararParaEditarPerfil(Long id) {
        Usuario usuarioAux = this.usuarioFacade.find(id);
        this.setUsuario(usuario);
        this.setFotoPerfil(usuarioAux.getFoto());
    }
    
    public String guardarEdicionFoto() {
        
        try {
            Usuario unUsuario = this.usuarioFacade.find(this.getUsuario().getId());
            usuarioFacade.editFotos(unUsuario.getId(), this.getFotoPerfil());
            this.setFotoPerfil(this.getFotoPerfil());
            this.getUsuario().setFoto(this.getFotoPerfil());
            this.setTitle("Proceso completo...");
            this.setImages("fa fa-check-circle-o");
            this.setResultado("successErrorWeb");
            this.setMsgSuccessError("La foto de perfil ha sido editada con éxito.");
            this.setEsCorrecto(true);
        } catch (Exception ex) {
            this.setEsCorrecto(false);
            this.setTitle("¡Error!");
            this.setImages("fa fa-times-circle-o");
            this.setMsgSuccessError(ex.getMessage());
            this.setResultado("successErrorWeb");
            
        }
        return this.getResultado();
    }
    
    public void customValidator(FileEntryEvent entryEvent) {
        
        FileEntryResults results = ((FileEntry) entryEvent.getComponent()).getResults();
        for (FileEntryResults.FileInfo file : results.getFiles()) {
            if (file.isSaved()) {
                boolean valido = false;
                String tipo = file.getContentType();
                String[] tipos = new String[]{"image/bmp", "image/gif",
                    "image/jpeg", "image/jpeg", "image/jpeg", "image/png", "image/tiff", "image/tiff"};
                
                for (String item : tipos) {
                    if (item.equals(tipo)) {
                        valido = true;
                        
                    }
                }
                
                if (!valido) {
                    file.updateStatus(new FileEntryStatus() {
                        @Override
                        public boolean isSuccess() {
                            return false;
                        }
                        
                        @Override
                        public FacesMessage getFacesMessage(
                                FacesContext facesContext, UIComponent fileEntry, FileEntryResults.FileInfo fi) {
                            return new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Solo se pueden adjuntar imágenes",
                                    "Solo se pueden adjuntar imágenes");
                            
                        }
                    }, true, true);
                    this.botonGuardar = true;
                } else {
                    try {
                        File miArchivo = new File(file.getFile().getAbsolutePath());
                        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        Date date = new Date();
                        String extension = "";
                        int i = file.getFileName().lastIndexOf('.');
                        int p = Math.max(file.getFileName().lastIndexOf('/'), file.getFileName().lastIndexOf('\\'));
                        if (i > p) {
                            extension = file.getFileName().substring(i + 1);
                        }
                        File miArchivoAux = new File(miArchivo.getParent() + "/" + dateFormat.format(date) + "." + extension);
                        miArchivo.renameTo(miArchivoAux);
                        File fil = new File(this.getCarpetaArchivos() + miArchivo.getName());
                        Path path = Paths.get(fil.getAbsolutePath());
                        this.setPath(miArchivoAux.getName());
                        this.setFotoPerfil(miArchivoAux.getName());
                        fil.delete();
                        file.updateStatus(new FileEntryStatus() {
                            @Override
                            public boolean isSuccess() {
                                return true;
                            }
                            
                            @Override
                            public FacesMessage getFacesMessage(FacesContext facesContext, UIComponent fileEntry, FileEntryResults.FileInfo fi) {
                                return new FacesMessage(FacesMessage.SEVERITY_INFO, "Se adjuntó el archivo.", "Se adjuntó el archivo.");
                            }
                        }, true, true);
                        this.botonGuardar = false;
                    } catch (Exception e) {
                        
                    }
                }
            }
        }
    }
    
    public List<Menu> menusUsuario() {
        List<Menu> listRetorno = new ArrayList<>();
        try {
            GrupoUsuarioEnum enumGrupoUsuarioServicioAdministrativo = GrupoUsuarioEnum.SERVICIO_ADMINISTRATIVO;
            GrupoUsuarioEnum enumGrupoUsuarioSuperAdmin = GrupoUsuarioEnum.SUPER_ADMIN;
            GrupoUsuarioEnum enumGrupoUsuarioAdmin = GrupoUsuarioEnum.ADMIN;
            GrupoUsuarioEnum enumGrupoUsuarioContaduriaGeneral = GrupoUsuarioEnum.DIRECCION_ANALISIS_INFORMATICA;
            MenuEnum menuReporte = MenuEnum.REPORTES;
            MenuEnum menuMovimientos = MenuEnum.MOVIMIENTOS;
            MenuEnum menuCuentasBancarias = MenuEnum.CUENTAS_BANCARIAS;
            
            List<Grupo> listGrupo = this.usuarioFacade.getGrupos(this.getUsuario().getId());
            boolean EsAdmininistrador = false;

            /*Agregar los menús que tiene por pertenecer a x grupo*/
            if (!(listGrupo.isEmpty())) {
                for (Grupo grupoAux : listGrupo) {
                    if (grupoAux.getId().equals(enumGrupoUsuarioSuperAdmin.getId())
                            || grupoAux.getId().equals(enumGrupoUsuarioAdmin.getId())
                            || grupoAux.getId().equals(enumGrupoUsuarioContaduriaGeneral.getId())) {
                        EsAdmininistrador = true;
                        break;
                    }
                }
            }
            
            if (EsAdmininistrador) {
                for (Grupo grupoAux : listGrupo) {
                    for (Menu menuAux : this.grupoFacade.getMenus(grupoAux.getId())) {
                        if (!listRetorno.contains(menuAux)) {
                            listRetorno.add(menuAux);
                        }
                    }
                    
                }
            } else {
                for (Grupo grupoAux : listGrupo) {
                    if (grupoAux.getId().equals(enumGrupoUsuarioServicioAdministrativo.getId())) {
                        for (Menu menuAux : this.grupoFacade.getMenus(grupoAux.getId())) {
                            if (this.getUsuario().getServicio() != null) {
                                if (!listRetorno.contains(menuAux)) {
                                    listRetorno.add(menuAux);
                                }
                            } else {
                                if (menuAux.getNombre().replace(" ", "_").equals(menuReporte.toString()) || menuAux.getNombre().replace(" ", "_").equals(menuMovimientos.toString()) || menuAux.getNombre().replace(" ", "_").equals(menuCuentasBancarias.toString()) || menuAux.getNombre().replace(" ", "_").equalsIgnoreCase(MenuEnum.EXPEDIENTES.toString()) || menuAux.getNombre().replace(" ", "_").equalsIgnoreCase(MenuEnum.NOTIFICACIONES.toString())) {
                                    
                                } else {
                                    if (!listRetorno.contains(menuAux)) {
                                        listRetorno.add(menuAux);
                                    }
                                }
                            }
                        }
                    } else {
                        for (Menu menuAux : this.grupoFacade.getMenus(grupoAux.getId())) {
                            if (!listRetorno.contains(menuAux)) {
                                listRetorno.add(menuAux);
                            }
                        }
                        
                    }
                }
                
            }
            /*Agregar los menús que se asignan a un usuario determinado*/
            if (!this.getUsuario().getMenus().isEmpty()) {
                for (Menu menuAux : this.usuarioFacade.getMenus(this.getUsuario().getId())) {
                    if (!listRetorno.contains(menuAux)) {
                        if (menuAux.getNombre().replace(" ", "_").equals(menuReporte.toString()) || menuAux.getNombre().replace(" ", "_").equals(menuMovimientos.toString()) || menuAux.getNombre().replace(" ", "_").equals(menuCuentasBancarias.toString())) {
                            if (this.getServicio() != null) {
                                listRetorno.add(menuAux);
                            }
                        } else {
                            listRetorno.add(menuAux);
                        }
                    }
                }
            }
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            
        }
        return listRetorno;
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
