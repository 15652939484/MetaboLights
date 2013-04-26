package uk.ac.ebi.metabolights.referencelayer.DAO.db;


import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.util.db.SQLLoader;
import uk.ac.ebi.metabolights.referencelayer.IDAO.DAOException;
import uk.ac.ebi.metabolights.referencelayer.IDAO.ISpeciesDAO;
import uk.ac.ebi.metabolights.referencelayer.domain.Species;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class SpeciesDAO implements ISpeciesDAO{


	private Logger LOGGER = Logger.getLogger(SpeciesDAO.class);

	protected Connection con;
	protected SQLLoader sqlLoader;

	/**
	 * @param connection to the Species
	 * @throws java.io.IOException
	 */
	public SpeciesDAO(Connection connection) throws IOException{
		this.con = connection;
		this.sqlLoader = new SQLLoader(this.getClass(), con);
	}


    /**
     * Setter for Species connection. It also sets the same connection
     * for the underlying objects.<br>
     * This method should be used with pooled connections, and only when the
     * previous one and its prepared statements have been properly closed
     * (returned).
     * @param con
     * @throws java.sql.SQLException
     */
	public void setConnection(Connection con) throws SQLException{
		this.con = con;
		sqlLoader.setConnection(con);
	}

	@Override
	protected void finalize() throws Throwable {
        super.finalize();
		close();
	}

	/**
	 * Closes prepared statements, but not the connection.
	 * If you want to close the connection or return it to a pool,
	 * please call explicitly the method {@link java.sql.Connection#close()} or
	 * {@link #returnPooledConnection()} respectively.
	 */
	public void close() throws DAOException {
        try {
            sqlLoader.close();
        } catch (SQLException ex) {
            throw new DAOException(ex);
        }
	}

	/**
	 * Closes (returns to the pool) prepared statements and connection.
	 * This method should be called explicitly before finalising this object,
	 * in case its connection belongs to a pool.
	 * <br>
	 *      *
     * @throws uk.ac.ebi.metabolights.referencelayer.IDAO.DAOException while closing the compound reader.
     * @throws java.sql.SQLException while setting the compound reader connection to
     *      null.
     */
	public void returnPooledConnection() throws DAOException, SQLException{

		close();
		if (con != null){
			con.close();
			con = null;
		}
	}


	public Species findBySpeciesId(Long SpeciesId) throws DAOException {

        // Try to get it from the identity map...
        Species sp =  SpeciesIdentityMap.getSpecies(SpeciesId);

       // If not loaded yet
       if (sp == null){
           // It must return an array of one Species....get the first one and only.
           Collection<Species> Species = findBy("--where.species.by.id", SpeciesId);
           sp =  (Species ==null? null:Species.iterator().next());

       }

       return sp;
	}

    public Species findBySpeciesName(String speciesName) throws DAOException {

        // Try to get it from the identity map...
        // This can't be done as it is now since the map has a long as the key...change it to String? And add the species twice
        // Species sp =  SpeciesIdentityMap.getSpecies(SpeciesId);


        // It must return an array of one Species....get the first one and only.
        Collection<Species> Species = findBy("--where.species.by.species", speciesName);

        Species sp =  (Species ==null? null:Species.iterator().next());

        return sp;
    }


	public Set<Species> findAll() throws DAOException {

		return findBy("--where.species.all",1);
	}

	private Set <Species> findBy(String where, Object value)
	throws DAOException {
		ResultSet rs = null;
		try {

			PreparedStatement stm = sqlLoader.getPreparedStatement("--species.core", where);
			stm.clearParameters();

			// If can be casted as long
			if (value instanceof Long){
				stm.setLong(1, (Long) value);
			} else {
				stm.setString(1, (String) value);
			}


			rs = stm.executeQuery();

			// Load all Speciess
			return loadSpeciess(rs);

        } catch (SQLException e){
           throw new DAOException(e);
		} finally {
			if (rs != null) try {
                rs.close();
            } catch (SQLException ex) {
                LOGGER.error("Closing ResultSet", ex);
            }
		}
	}

	private Set<Species> loadSpeciess(ResultSet rs) throws SQLException{

		Set<Species> result = null;
		while (rs.next()){

			if (result == null) result = new HashSet<Species>();
			Species Species = loadSpecies(rs);
			result.add(Species);
		}
		return (result == null)? null : result;

	}

	private Species loadSpecies(ResultSet rs) throws SQLException {
		Species sp = null;

		// It should have a valid record
		sp = new Species();
		long id = rs.getLong("ID");
        String species = rs.getString("SPECIES");
		String description = rs.getString("DESCRIPTION");
        String taxon = rs.getString("TAXON");

		sp.setId(id);
        sp.setSpecies(species);
		sp.setDescription(description);
        sp.setTaxon(taxon);

        // Add the Species to the identity map
        SpeciesIdentityMap.addSpecies(sp);

		return sp;
	}

	public void save(Species sp) throws DAOException {

		// If its a new Species
		if (sp.getId() == 0) {
			insert (sp);
		} else {
			update(sp);
		}

	}

	/**
	 * Updates core data concerning only to the Species
	 * @param sp
	 * @throws uk.ac.ebi.metabolights.referencelayer.IDAO.DAOException
	 */
	private void update(Species sp ) throws DAOException {
		try {

			PreparedStatement stm = sqlLoader.getPreparedStatement("--update.species");
			stm.clearParameters();
			stm.setString(1, sp.getSpecies());
            stm.setString(2, sp.getDescription());
            stm.setString(3, sp.getTaxon());
			stm.setLong(4, sp.getId());
			stm.executeUpdate();

		} catch (SQLException ex) {
            throw new DAOException(ex);
		}
	}

	/**
	 * Inserts a new Species into the Species
	 * <br>
	 * @throws java.sql.SQLException
	 */
	private void insert(Species sp) throws DAOException {
		try {
			PreparedStatement stm = sqlLoader.getPreparedStatement("--insert.species", new String[]{"ID"}, null);
			stm.clearParameters();
			stm.setString(1, sp.getSpecies());
            stm.setString(2, sp.getDescription());
            stm.setString(3, sp.getTaxon());

			stm.executeUpdate();

			ResultSet keys = stm.getGeneratedKeys();

       		while (keys.next()) {
    			sp.setId(keys.getLong(1));  //Should only be one
    	        if (LOGGER.isDebugEnabled())
    	            LOGGER.debug("insertIntoSpecies: Species inserted with id:" +sp.getId());
    		}

       		keys.close();

            // Add Species to the identity map
            SpeciesIdentityMap.addSpecies(sp);

		} catch (SQLException ex) {
            throw new DAOException(ex);
		}
	}

	/**
	 * Deletes a Species from the Species
	 * <br>
	 * @throws java.sql.SQLException
	 */
	public void delete(Species sp) throws DAOException {

		// Delete the Species
		deleteSpecies(sp);
	}

	/**
	 * Deletes a Species from the Species
	 * <br>
	 * @throws java.sql.SQLException
	 */
	private void deleteSpecies(Species sp)	throws DAOException {
		try {
			PreparedStatement stm = sqlLoader.getPreparedStatement("--delete.species");
			stm.clearParameters();
			stm.setLong(1, sp.getId());
			stm.executeUpdate();
	
	        if (LOGGER.isDebugEnabled())
    	            LOGGER.debug("Species deleted with id:" +sp.getId());
    		
       		SpeciesIdentityMap.removeSpecies(sp);


		} catch (SQLException ex) {
            throw new DAOException(ex);
		}
	}
}