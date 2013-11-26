package uk.ac.ebi.metabolights.species.core.tools;

import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ebi.metabolights.species.model.Taxon;

/**
 * User: conesa
 * Date: 26/11/2013
 * Time: 10:12
 */
public class WoRMSParentSearcherTest {
	@Test
	public void testGetParentFromTaxon() throws Exception {

		WoRMSParentSearcher woRMSParentSearcher = new WoRMSParentSearcher();

		Taxon pontinus = new Taxon(woRMSParentSearcher.WoRMS_PREFIX + ":" + 127240, "Pontinus kuhlii (Bowdich, 1825)", "","");

		Taxon parent = woRMSParentSearcher.getParentFromTaxon(pontinus);

		assertEquals("Testing parent id of Pontinus kuhlii", "126170" , parent.getRecordIdentifier());

	}

	@Test
	public void testIsThisTaxonYours() throws Exception {

		WoRMSParentSearcher woRMSParentSearcher = new WoRMSParentSearcher();

		Taxon orphan = new Taxon(woRMSParentSearcher.WoRMS_PREFIX + ":" + 127240, "Pontinus kuhlii (Bowdich, 1825)", "","");

		assertTrue("Worms taxon must be accepted", woRMSParentSearcher.isThisTaxonYours(orphan));

		orphan.setPrefix("hsdWORMS");

		assertFalse("weird taxon must NOT be accepted", woRMSParentSearcher.isThisTaxonYours(orphan));

		orphan.setPrefix("NCBI");

		assertFalse("NCBI taxon must NOT be accepted", woRMSParentSearcher.isThisTaxonYours(orphan));




	}
}
