package uk.ac.ebi.metabolights.referencelayer.DAO.db;

import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import uk.ac.ebi.biobabel.util.db.DatabaseInstance;
import uk.ac.ebi.metabolights.referencelayer.domain.Species;

import java.sql.Connection;

public class ReaderTestSpeciesDB extends TestCase{
	
	protected static final Logger LOGGER = Logger.getLogger(ReaderTestSpeciesDB.class);
	
	private Connection con;
	protected SpeciesDAO speciesD;
	
	
	static String[] expected = new String[]{null,"SpeciesTestUpdated", "Desc updated", "Taxon updated"};
	
	
	@Override
	@BeforeClass
	protected void setUp() throws Exception {
	
		// Set up a simple configuration that logs on the console.
	    BasicConfigurator.configure();
	    
		DatabaseInstance dbi = DatabaseInstance.getInstance("metabolightsDEV");
		con = dbi.getConnection();
		speciesD = new SpeciesDAO(con);
		
	}
	 @Override
	 @AfterClass
	protected void tearDown() throws Exception {
		if (speciesD != null) speciesD.close();
		if (con != null) con.close();
		
	}

	 
	/*
	 */
	public void testSavingASpecies() throws Exception {
		
		Species sp = new Species();
		String[] newSpecie = new String[]{null,"New species","spec desc", "new taxon"};
		sp.setSpecies(newSpecie[1]);
        sp.setDescription(newSpecie[2]);
        sp.setTaxon(newSpecie[3]);
		
		speciesD.save(sp);
		assertSpecies(sp ,newSpecie);
		
		// at least check id is not 0
		assertTrue("Checking Species id is not 0", sp.getId() != 0 );
		
		// Update the id (it for now the expected id.
		expected[0] = Long.toString(sp.getId());
		sp.setSpecies(expected[1]);
        sp.setDescription(expected[2]);
        sp.setTaxon(expected[3]);

		speciesD.save(sp);
		assertSpecies(sp, expected);
		
		
	}

    public static Species newRandomSpecies() {
        Species species = new Species();
        species.setSpecies("Random SP");
        species.setTaxon("Random taxon");
        species.setDescription("Random desc");
        return species;

    }

	public void testFindASpeciesById() throws Exception {
		
		Species species = speciesD.findBySpeciesId(Long.parseLong(expected[0]));
		assertSpecies(species, expected);

        String change = "something different";

        // Test SpeciesIdentityMap usage
        species.setSpecies(change);

        species = speciesD.findBySpeciesId(Long.parseLong(expected[0]));

        assertEquals("Testing identity map usage", change, species.getSpecies());


	}	
	
	public void testDeleteASpecies() throws Exception {
		
		Species db = speciesD.findBySpeciesId(Long.parseLong(expected[0]));
		
		speciesD.delete(db);
		
		db = speciesD.findBySpeciesId(Long.parseLong(expected[0]));
		
		assertTrue("Deleted species must not be found" , db == null);
	}	

	
	private void assertSpecies(Species species, String[] expectedvalues){
		
		assertNotNull(species);
		// If the id is not null
		if (expectedvalues[0] != null) 	assertEquals("Checking " + expectedvalues[1] + " id", Long.parseLong(expectedvalues[0]), species.getId());
		assertEquals("Checking Species name" , expectedvalues[1] , species.getSpecies());
        assertEquals("Checking Species description" , expectedvalues[2] , species.getDescription());
        assertEquals("Checking Species taxon" , expectedvalues[3] , species.getTaxon());
		
		
	}
	
	
}