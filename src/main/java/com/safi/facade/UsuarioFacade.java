/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.safi.facade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.safi.entity.ActionItem;
import com.safi.entity.Grupo;
import com.safi.entity.Menu;
import com.safi.entity.Usuario;
import com.safi.facade.MailFacadeLocal;
import com.safi.utilidad.EncriptadorAES;
import com.safi.utilidad.MailSenderThread;
import com.safi.utilidad.Utilidad;

/**
 *
 * @author Matias Zakowicz
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {

    @PersistenceContext(unitName = "EAppSAFI-ejbPU")
    private EntityManager em;

    @EJB
    private GrupoFacadeLocal grupoFacade;
    @EJB
    private ActionItemFacadeLocal actionFacade;
    @EJB
    private MenuFacadeLocal menuFacade;
    @EJB
    private MailFacadeLocal mailFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    @Override
    public Usuario find(String nombreUsuario, String claveAcceso) throws Exception {
        Usuario usuarioRetorno = null;
        try {
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
            Query consulta = em.createQuery("select u from Usuario u where u.nombreUsuario = :p1 and u.claveAcceso = :p2 and u.estado = true");
            consulta.setParameter("p1", nombreUsuario.toLowerCase().trim());
            consulta.setParameter("p2", encriptador.encriptar(claveAcceso.trim(), claveSecretaUsuario));
            usuarioRetorno = (Usuario) consulta.getSingleResult();
            usuarioRetorno.setUltimoLogin(new Date());
            this.edit(usuarioRetorno);
        } catch (Exception ex) {
            throw new Exception("Fallo de autenticación, la contraseña no es válida. Por favor, asegúrate de que el bloqueo de mayúsculas no está activado e inténtalo de nuevo.");
        }
        return usuarioRetorno;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Usuario> findAll(Boolean gestionarUsuariosServicios, List<Long> idGrupos, String apellidoNombre, String nombreUsuario, Long idGrupo, Long idServicio, Long dni, int first, int pageSize) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT  object(o) FROM Usuario AS o");

        if (gestionarUsuariosServicios) {
            String grupos = ("(4,5,12)");
            query.append(", IN (o.grupos) AS grup where grup.id in ").append(grupos).append(" AND o.estado = true");
        } else {
            if (!idGrupos.isEmpty()) {
                String idGruposConcatenados = "(";
                for (int i = 0; i < idGrupos.size(); i++) {
                    idGruposConcatenados = idGruposConcatenados + idGrupos.get(i).toString();
                    if ((Integer.sum(i, 1)) < idGrupos.size()) {
                        idGruposConcatenados = idGruposConcatenados + (",");
                    } else {
                        idGruposConcatenados = idGruposConcatenados + (")");
                    }
                }
                query.append(", IN (o.grupos) as grup where grup.id in ").append(idGruposConcatenados).append(" AND o.estado = true");
            } else if (idGrupo != null && idGrupo != 0L) {
                query.append(", IN (o.grupos) as grup where grup.id in (").append(idGrupo).append(") and o.estado = true");
            } else {
                query.append(" WHERE o.estado = true");
            }
        }

        if (apellidoNombre != null && !apellidoNombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.apellidoNombre)) LIKE '%");
            query.append(Utilidad.desacentuar(apellidoNombre).toUpperCase());
            query.append("%' ");
        }

        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombreUsuario)) LIKE '%");
            query.append(Utilidad.desacentuar(nombreUsuario).toUpperCase());
            query.append("%' ");
        }

        if (idServicio != null && idServicio != 0L) {
            query.append(" AND o.servicio.id = ").append(idServicio);
        }

        if (dni != null) {
            query.append(" AND o.dni = ").append(dni);
        }

        query.append(" ORDER BY o.apellidoNombre");
        Query consulta = em.createQuery(query.toString());
        consulta.setFirstResult(first);
        consulta.setMaxResults(pageSize);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Usuario> findAll(String apellidoNombre, String nombreUsuario) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Usuario as o WHERE o.estado = true ");
        if (apellidoNombre != null && !apellidoNombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.apellidoNombre)) LIKE '%");
            query.append(Utilidad.desacentuar(apellidoNombre).toUpperCase());
            query.append("%' ");
        }
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombreUsuario)) = '")
            .append(Utilidad.desacentuar(nombreUsuario).toUpperCase()).append("'");
           
            
        }        
        query.append(" ORDER BY o.apellidoNombre");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public Long countAll(Boolean gestionarUsuariosServicios, List<Long> idGrupos, String apellidoNombre, String nombreUsuario, Long idGrupo, Long idServicio, Long dni) {
        StringBuilder query = new StringBuilder();
        query.append("select COUNT(DISTINCT(o)) FROM Usuario as o");

        if (gestionarUsuariosServicios) {
            String grupos = ("(4,5,12)");
            query.append(", IN (o.grupos) AS grup where grup.id in ").append(grupos).append(" AND o.estado = true");
        } else {
            if (!idGrupos.isEmpty()) {
                String idGruposConcatenados = "(";
                for (int i = 0; i < idGrupos.size(); i++) {
                    idGruposConcatenados = idGruposConcatenados + idGrupos.get(i).toString();
                    if ((Integer.sum(i, 1)) < idGrupos.size()) {
                        idGruposConcatenados = idGruposConcatenados + (",");
                    } else {
                        idGruposConcatenados = idGruposConcatenados + (")");
                    }
                }
                query.append(", IN (o.grupos) as grup where grup.id in ").append(idGruposConcatenados).append(" AND o.estado = true");
            } else if (idGrupo != null && idGrupo != 0L) {
                query.append(", IN (o.grupos) as grup where grup.id in (").append(idGrupo).append(") and o.estado = true");
            } else {
                query.append(" WHERE o.estado = true");
            }
        }

        if (apellidoNombre != null && !apellidoNombre.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.apellidoNombre)) LIKE '%");
            query.append(Utilidad.desacentuar(apellidoNombre).toUpperCase());
            query.append("%' ");
        }

        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            query.append(" AND upper(FUNCTION('unaccent', o.nombreUsuario)) LIKE '%");
            query.append(Utilidad.desacentuar(nombreUsuario).toUpperCase());
            query.append("%' ");
        }

        if (idServicio != null && idServicio != 0L) {
            query.append(" AND o.servicio.id = ").append(idServicio);
        }

        if (dni != null) {
            query.append(" AND o.dni = ").append(dni);
        }
        Query consulta = em.createQuery(query.toString());
        return (Long) consulta.getSingleResult();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Usuario> findAll(boolean estado) {
        Query consulta = em.createQuery("select object(o) from Usuario as o WHERE o.estado = :p1 ORDER BY o.apellidoNombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Usuario> findAll(boolean estado, Long idServicio) {
        Query consulta = em.createQuery("select object(o) from Usuario as o WHERE o.estado = :p1 and o.servicio.id = :p2 ORDER BY o.apellidoNombre");
        consulta.setParameter("p1", estado);
        consulta.setParameter("p2", idServicio);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public boolean findExistsDNI(Long dni) {
        Query consulta = em.createQuery("select COUNT(o) from Usuario as o WHERE o.estado = true and o.dni = :p1");
        consulta.setParameter("p1", dni);
        return ((Long) consulta.getSingleResult()) != 0;
    }

    /**
     * @author Alvarenga Angel
     * @param estado
     * @return List Usuario
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Usuario> findAllNotServicio(boolean estado) {
        Query consulta = em.createQuery("select DISTINCT u from Usuario as u WHERE u.estado = :p1 AND  u.servicio IS NULL ORDER BY u.apellidoNombre");
        consulta.setParameter("p1", estado);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Usuario> findAllByGrupo(boolean estado, Long idGrupo) {
        Grupo grupoAux = this.grupoFacade.find(idGrupo);
        Query consulta = em.createQuery("select object(o) from Usuario as o WHERE o.estado = :p1 AND :p2 MEMBER OF (o.grupos)  ORDER BY o.apellidoNombre");
        consulta.setParameter("p1", estado);
        consulta.setParameter("p2", grupoAux);
        return consulta.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Usuario> findAll(String apellidoNombre) {
        return em.createQuery("select object(o) FROM Usuario as o WHERE o.estado = true AND o.apellidoNombre LIKE '%" + apellidoNombre + "%' ORDER BY o.apellidoNombre ").getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Usuario> findAll(List<Long> usuariosId) {
        String iDes = usuariosId.toString().substring(1, usuariosId.toString().length() - 1);
        return em.createQuery("select object (u) from Usuario as u where u.id in (" + iDes + ")").getResultList();
    }

    @Override
    public void create(String apellidoNombre, String nombreUss, String email, List<Grupo> lstGrupo, Long idUsuario, Long dni) throws Exception {
        try {
            boolean isNameEqual = false;
            List<Usuario> listUsuario = this.findAll(true);
            for (Usuario usuario : listUsuario) {
                if (usuario.getNombreUsuario().equalsIgnoreCase(nombreUss)) {
                    isNameEqual = true;
                    break;
                }
            }
            if (isNameEqual) {
                throw new Exception("El nombre del usuario ya existe");
            }
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombreUss.trim().toLowerCase());
            EncriptadorAES encriptador = new EncriptadorAES();
            String claveAux = Utilidad.generateRandomPassword();
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
            String urlEntorno = prop.getProperty("urlEntorno");
            usuario.setClaveAcceso(encriptador.encriptar(claveAux, claveSecretaUsuario));
            usuario.setEmail(email);
            usuario.setDni(dni);
            usuario.setEstado(true);
            usuario.setFechaAlta(new Date());
            usuario.setUpdatedPassword(true);
            usuario.setApellidoNombre(apellidoNombre.toUpperCase());
            usuario.setFoto("perfil-basico.png");
            this.create(usuario);
            //Mandar mail nueva clave
            String asunto = "Nuevo Usuario en SAFI (Sistema de Administración Financiera de Misiones)";
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Estimado/a ")
                    .append(apellidoNombre)
                    .append(" le informamos que se le ha creado un usuario en SAFI (Sistema de Administración Financiera de Misiones).")
                    .append("\n")
                    .append("\n")
                    .append("Deberá ingresar con las siguientes credenciales. Tenga en cuenta que la contraseña proporcionada es provisoria, deberá cambiarla.")
                    .append("\n")
                    .append("\n")
                    .append("Usuario: ").append(usuario.getNombreUsuario())
                    .append("\n")
                    .append("Contraseña: ").append(claveAux)
                    .append("\n")
                    .append("Link para ingresar: ").append(urlEntorno)
                    .append("\n")
                    .append("\n")
                    .append("Contaduría General de Misiones\n")
                    .append("Av. Polonia Nº1223 - CP 3300\n")
                    .append("TE (0376) 4447965 / 4447520");
            this.mailFacade.crear(usuario.getEmail(), asunto, mensaje.toString(), new Date(), idUsuario, false);
            MailSenderThread mailSender = new MailSenderThread(usuario.getEmail(), asunto, mensaje.toString(), new ArrayList(), false, false);
            mailSender.start();
            usuario.setGrupos(lstGrupo);
            this.edit(usuario);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new Exception("Error al intentar crear el usuario, " + ex.getMessage());
        }
    }

    @Override
    public void edit(Long id, String apellidoNombre,
            String nombreUsuario, String emailOriginal, String email, List<Long> listIdGrupos, Long dni) throws Exception {
        try {
            Usuario usuarioAux = this.find(id);
            usuarioAux.setNombreUsuario(nombreUsuario.toLowerCase());
            usuarioAux.setFechaModificacion(new Date());
            usuarioAux.setApellidoNombre(apellidoNombre.toUpperCase());
            if (!emailOriginal.trim().toUpperCase().equals(email.trim().toUpperCase())) {
                EncriptadorAES encriptador = new EncriptadorAES();
                String claveAux = Utilidad.generateRandomPassword();
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
                String claveSecretaUsuario = prop.getProperty("claveSecretaUsuario");
                String urlEntorno = prop.getProperty("urlEntorno");
                usuarioAux.setClaveAcceso(encriptador.encriptar(claveAux, claveSecretaUsuario));

                String asunto = "Edición Usuario en SAFI (Sistema de Administración Financiera de Misiones)";
                StringBuilder mensaje = new StringBuilder();
                mensaje.append("Estimado/a ")
                        .append(apellidoNombre)
                        .append(" le informamos que se ha editado su usuario en SAFI (Sistema de Administración Financiera de Misiones).")
                        .append("\n")
                        .append("\n")
                        .append("Deberá ingresar con las siguientes credenciales. Tenga en cuenta que la contraseña proporcionada es provisoria, deberá cambiarla.")
                        .append("\n")
                        .append("\n")
                        .append("Usuario: ").append(usuarioAux.getNombreUsuario())
                        .append("\n")
                        .append("Contraseña: ").append(claveAux)
                        .append("\n")
                        .append("Link para ingresar: ").append(urlEntorno)
                        .append("\n")
                        .append("\n")
                        .append("Contaduría General de Misiones\n")
                        .append("Av. Polonia Nº1223 - CP 3300\n")
                        .append("TE (0376) 4447965 / 4447520");
                this.mailFacade.crear(email, asunto, mensaje.toString(), new Date(), id, false);
                MailSenderThread mailSender = new MailSenderThread(email, asunto, mensaje.toString(), new ArrayList(), false, false);
                mailSender.start();
            }
            usuarioAux.setEmail(email);
            usuarioAux.setDni(dni);
            List<Grupo> lstGrupoAux = grupoFacade.finGrupos(listIdGrupos);
            usuarioAux.setGrupos(lstGrupoAux);
            this.edit(usuarioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el usuario");
        }
    }

    @Override
    public void editFotos(Long id, String foto) throws Exception {
        try {
            Usuario usuarioAux = this.find(id);
            usuarioAux.setFoto(foto);
            this.edit(usuarioAux);
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el usuario");
        }
    }

    @Override
    public void edit(Long id, String claveActual, String claveNueva,
            String claveConfirma) throws Exception {
        boolean var = true;
        Usuario usuarioAux = this.find(id);
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
        if (!claveNueva.trim().equals(claveActual.trim())) {
            if (claveNueva.trim().equals(claveConfirma.trim())) {
                usuarioAux.setClaveAcceso(encriptador.encriptar(claveNueva, claveSecretaUsuario));
                usuarioAux.setFechaModificacion(new Date());
                usuarioAux.setUpdatedPassword(false);
                this.edit(usuarioAux);
            } else {
                throw new Exception("La clave no es correcta");
            }
        } else {
            throw new Exception("La clave actual no es la correcta");
        }
    }

    @Override
    public void edit(Long idUsuario, List<Long> idMenu) throws Exception {
        try {
            Usuario usuario = this.find(idUsuario);
            if (!(idMenu.isEmpty())) {
                List<Menu> listaMenu = menuFacade.findAll(idMenu);
                usuario.setMenus(null);
                this.edit(usuario);
                if (!(listaMenu.isEmpty())) {
                    usuario.setMenus(listaMenu);
                    this.edit(usuario);
                }
            } else {
                usuario.setMenus(null);
                usuario.setAcciones(null);
                this.edit(usuario);
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar editar el usuario");
        }
    }

    /**
     *
     * @param idUsuario
     * @param idAcciones
     * @throws Exception
     */
    @Override
    public void remove(Long idUsuario, List<Long> idAcciones) throws Exception {
        try {
            Usuario usuario = this.find(idUsuario);
            if (!(idAcciones.isEmpty())) {
                usuario.getAcciones().clear();
                usuario.setAcciones(this.actionFacade.findAll(idAcciones));
                this.edit(usuario);
            } else {
                usuario.getAcciones().clear();
                this.edit(usuario);
            }
        } catch (Exception e) {
            throw new Exception("Error al intentar borrar acciones del usuario");
        }
    }

    @Override
    public void remove(Long idUsuario) throws Exception {
        try {
            Usuario usuarioAux = this.find(idUsuario);
            usuarioAux.setDni(null);
            usuarioAux.setEstado(false);
            this.edit(usuarioAux);
        } catch (Exception e) {
            throw new Exception("Error al intetar borrar el usuario");
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Usuario> findByEmailAndUsuario(String email, String usuario) {
        StringBuilder query = new StringBuilder();
        query.append("select object(o) FROM Usuario as o WHERE o.estado = true ");
        query.append("and o.email LIKE '").append(email).append("'");
        query.append("and o.nombreUsuario LIKE '").append(usuario).append("'");
        Query consulta = em.createQuery(query.toString());
        return consulta.getResultList();
    }

    @Override
    public List<Grupo> getGrupos(Long idUsuario) {
        Query consulta = em.createQuery("SELECT OBJECT (g) FROM Usuario AS u, IN (u.grupos) g where g.estado = true and u.id = :p1 ");
        consulta.setParameter("p1", idUsuario);
        return consulta.getResultList();
    }

    @Override
    public List<ActionItem> getAcciones(Long idUsuario) {
        Query consulta = em.createQuery("SELECT OBJECT (a) FROM Usuario AS u, IN (u.acciones) a where a.estado = true and u.id = :p1 ");
        consulta.setParameter("p1", idUsuario);
        return consulta.getResultList();
    }

    @Override
    public List<Menu> getMenus(Long idUsuario) {
        Query consulta = em.createQuery("SELECT OBJECT (m) FROM Usuario AS u, IN (u.menus) m where a.estado = true and u.id = :p1 ");
        consulta.setParameter("p1", idUsuario);
        return consulta.getResultList();
    }

    @Override
    public void restorePassword(Long idUsuario) throws Exception {
        try {
            Usuario usuarioAux = this.find(idUsuario);
            EncriptadorAES encriptador = new EncriptadorAES();
            String claveAux = Utilidad.generateRandomPassword();
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
            String urlEntorno = prop.getProperty("urlEntorno");
            usuarioAux.setClaveAcceso(encriptador.encriptar(claveAux, claveSecretaUsuario));
            usuarioAux.setUpdatedPassword(true);
            this.edit(usuarioAux);
            //Mandar mail nueva clave
            String asunto = "Contraseña de usuario cambiada en SAFI (Sistema de Administración Financiera de Misiones)";
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Estimado/a ")
                    .append(usuarioAux.getApellidoNombre())
                    .append(" le informamos que se ha cambiado la contraseña de su usuario de SAFI (Sistema de Administración Financiera de Misiones).")
                    .append("\n")
                    .append("\n")
                    .append("Deberá ingresar nuevamente con los siguientes datos. Tenga en cuenta que la contraseña proporcionada es provisoria, deberá cambiarla.")
                    .append("\n")
                    .append("\n")
                    .append("Usuario: ").append(usuarioAux.getNombreUsuario())
                    .append("\n")
                    .append("Contraseña: ").append(claveAux)
                    .append("\n")
                    .append("Link para ingresar: ").append(urlEntorno)
                    .append("\n")
                    .append("\n")
                    .append("Contaduría General de Misiones\n")
                    .append("Av. Polonia Nº1223 - CP 3300\n")
                    .append("TE (0376) 4447965 / 4447520");
            this.mailFacade.crear(usuarioAux.getEmail(), asunto, mensaje.toString(), new Date(), usuarioAux.getId(), false);
            MailSenderThread mailSender = new MailSenderThread(usuarioAux.getEmail(), asunto, mensaje.toString(), new ArrayList(), false, false);
            mailSender.start();
        } catch (Exception e) {
            throw new Exception("Error al intetar borrar el usuario");
        }
    }
}
