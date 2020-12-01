package com.safi.facade;

import java.util.List;
import javax.ejb.Local;
import com.safi.entity.ActionItem;
import com.safi.entity.Grupo;
import com.safi.entity.Menu;
import com.safi.entity.Usuario;

/**
 *
 * @author Matias Zakowicz
 */
@Local
public interface UsuarioFacadeLocal {

    void create(Usuario usuario);

    void edit(Usuario usuario);

    void remove(Usuario usuario);

    Usuario find(Object id);

    List<Usuario> findAll();

    List<Usuario> findRange(int[] range);

    int count();

    public Usuario find(String nombreUsuario, String claveAcceso) throws Exception;

    public List<Usuario> findAll(boolean estado);
    
    public List<Usuario> findAll(boolean estado, Long idServicio);

    public List<Usuario> findAllByGrupo(boolean estado, Long idGrupo);

    public List<Usuario> findAll(String apellidoNombre);

    public void create(String apellidoNombre, String nombreUss, String email, List<Grupo> lstGrupo,Long idUsuario, Long dni) throws Exception;

    public void edit(Long id, String apellidoNombre, String nombreUsuario,String emailOriginal, String email, List<Long> listIdGrupos, Long dni) throws Exception;

    public void editFotos(Long id, String foto) throws Exception;

    public void edit(Long id, String claveActual, String claveNueva, String claveConfirma) throws Exception;

    public void edit(Long idUsuario, List<Long> idMenu) throws Exception;

    public void remove(Long idUsuario, List<Long> idAcciones) throws Exception;

    public void remove(Long idUsuario) throws Exception;

    public List<Usuario> findAll(Boolean gestionarUsuariosServicios, List<Long> idGrupos, String apellidoNombre,
            String nombreUsuario, Long idGrupo, Long idServicio, Long dni, int first, int pageSize);

    public List<Usuario> findAll(List<Long> usuariosId);

    public List<Usuario> findAllNotServicio(boolean estado);

    public List<Usuario> findByEmailAndUsuario(String email, String usuario);

    public Long countAll(Boolean gestionarUsuariosServicios,  List<Long> idGrupos, String apellidoNombre, String nombreUsuario, Long idGrupo, Long idServicio, Long dni);

    public List<Usuario> findAll(String apellidoNombre, String nombreUsuario);

    public List<Grupo> getGrupos(Long idUsuario);

    public List<ActionItem> getAcciones(Long idUsuario);

    public List<Menu> getMenus(Long idUsuario);

    public void restorePassword(Long idUsuario) throws Exception;

    public boolean findExistsDNI(Long dni);

}
