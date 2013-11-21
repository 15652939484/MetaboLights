

--species.core:\
SELECT COALESCE(RSF.ID,RS.ID) ID ,COALESCE(RSF.SPECIES, RS.SPECIES) SPECIES, COALESCE(RSF.DESCRIPTION,RS.DESCRIPTION) DESCRIPTION, COALESCE(RSF.TAXON,RS.TAXON) TAXON, COALESCE(RSF.SPECIES_MEMBER,RS.SPECIES_MEMBER) SPECIES_MEMBER, RS.FINAL_ID \
FROM REF_SPECIES RS LEFT JOIN REF_SPECIES RSF ON RS.FINAL_ID = RSF.ID \
WHERE {0}

--where.species.by.id:\
RS.ID = ?

--where.species.by.taxon:\
RS.TAXON = ?

--where.species.by.species:\
RS.SPECIES = ?

--where.species.all:\
1 = 1

--where.species.withoutspeciesmember:\
COALESCE(RSF.SPECIES_MEMBER,RS.SPECIES_MEMBER) IS NULL

--where.species.withspeciesmember:\
COALESCE(RSF.SPECIES_MEMBER,RS.SPECIES_MEMBER) IS NOT NULL


--update.species:\
UPDATE REF_SPECIES \
	SET SPECIES = ?, DESCRIPTION = ?, TAXON = ? , SPECIES_MEMBER = ? WHERE ID = ?
	
--insert.species:\
INSERT INTO REF_SPECIES \
	(SPECIES, DESCRIPTION, TAXON, SPECIES_MEMBER) \
	VALUES (?, ?, ?, ?)

--delete.species:\
DELETE FROM REF_SPECIES \
WHERE ID = ?


