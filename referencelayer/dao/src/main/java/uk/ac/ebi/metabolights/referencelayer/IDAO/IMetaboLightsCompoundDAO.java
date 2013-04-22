package uk.ac.ebi.metabolights.referencelayer.IDAO;

import uk.ac.ebi.metabolights.referencelayer.domain.MetaboLightsCompound;

import java.util.Set;

/**
 * Reader for {@link MetaboLightsCompound} objects from MetaboLights reference layer.
 * @author Pablo Conesa
 */

public interface IMetaboLightsCompoundDAO {


    MetaboLightsCompound findByCompoundName(String name) throws DAOException;
    
    MetaboLightsCompound findByCompoundAccession(String accession) throws DAOException;

    MetaboLightsCompound findByCompoundId(Long compoundId) throws DAOException;

    Set<MetaboLightsCompound> getAllCompounds() throws DAOException;

    boolean doesCompoundExists(Long compoundId) throws DAOException;
    
    /**
     * Updates the MetaboLightsCompound.
     * @param compound
     * @throws DAOException
     */
    void save(MetaboLightsCompound compound) throws DAOException;


    /**
     * Closes any open system resources allocated to this reader.
     * @throws DAOException
     */
    void close() throws DAOException;
}
