--compound.core:\
SELECT ID, ACC, NAME, DESCRIPTION, INCHI, TEMP_ID, IUPAC_NAMES, FORMULA, HAS_LITERATURE, HAS_REACTIONS, HAS_SPECIES, HAS_PATHWAYS, HAS_NMR, HAS_MS \
FROM REF_METABOLITE \
WHERE {0}
	
--where.compound.by.name:\
NAME = ?

--where.compound.by.accession:\
ACC = ?

--where.compound.by.id:\
ID = ?

--where.compound.all:\
1=1


--update.compound:\
UPDATE REF_METABOLITE \
	SET ACC = ?, NAME = ?, DESCRIPTION = ?, INCHI = ?, TEMP_ID = ?, IUPAC_NAMES = ?, FORMULA = ?, HAS_LITERATURE = ?, HAS_REACTIONS = ?, HAS_SPECIES = ?, HAS_PATHWAYS = ?, HAS_NMR = ?, HAS_MS = ? WHERE ID = ?
	
--insert.compound:\
INSERT INTO REF_METABOLITE \
	(ACC, NAME, DESCRIPTION, INCHI, TEMP_ID, IUPAC_NAMES, FORMULA, HAS_LITERATURE, HAS_REACTIONS, HAS_SPECIES, HAS_PATHWAYS, HAS_NMR, HAS_MS) \
	VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)
	
--delete.compound:\
DELETE FROM REF_METABOLITE \
WHERE {0}

--exist.compound:\
SELECT COUNT(*) FROM REF_METABOLITE WHERE {0}
	