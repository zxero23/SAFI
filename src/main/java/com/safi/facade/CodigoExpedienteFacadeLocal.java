package com.safi.facade;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import com.safi.entity.CodigoExpediente;

/**
 *
 * @author ccpm
 */
@Local
public interface CodigoExpedienteFacadeLocal {

    void create(CodigoExpediente codigoExpediente);

    void edit(CodigoExpediente codigoExpediente);

    void remove(CodigoExpediente codigoExpediente);

    CodigoExpediente find(Object id);

    List<CodigoExpediente> findAll();

    List<CodigoExpediente> findAll(boolean estado);

    List<CodigoExpediente> findAll(String codExpediente, int first, int pageSize);

    List<CodigoExpediente> findRange(int[] range);

    int count();

    public List<CodigoExpediente> findCodigos(List<Long> codigosId);

    public List<CodigoExpediente> findAll(List<Long> codigoId);

    public Long countAll(String codigoExpediente);
}
