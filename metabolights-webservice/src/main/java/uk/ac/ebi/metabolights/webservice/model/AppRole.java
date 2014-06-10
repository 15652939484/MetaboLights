package uk.ac.ebi.metabolights.webservice.model;

/**
 * User: conesa
 * Date: 09/06/2014
 * Time: 10:03
 */
/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * Last modified: 4/3/14 3:16 PM
 * Modified by:   kenneth
 *
 * Copyright 2014 - European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 */



/**
 * Represents a role
 */
public enum AppRole {

	ROLE_SUBMITTER (0),
	ROLE_SUPER_USER (1),
	ROLE_REVIEWER(2);

	private final int bit;

	/**
	 * Creates an authority with a specific bit representation. It's important that this doesn't
	 * change as it will be used in the database. The enum ordinal is less reliable as the enum may be
	 * reordered or have new roles inserted which would change the ordinal values.
	 *
	 * @param bit the permission bit which will represent this authority in the datastore.
	 */
	AppRole(int bit) {
		this.bit = bit;
	}

	public int getBit() {
		return bit;
	}

	public String getName() {
		return toString();
	}
}
