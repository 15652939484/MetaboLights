create or replace
PROCEDURE EBEYE (RELEASE_NUMBER NUMBER DEFAULT 0)
AS
  start_str VARCHAR2(2000) := '<database>
    <name>MetaboLights</name>
    <description>a database for metabolomics experiments and derived information</description>';

  release VARCHAR2(100) := '    <release>XXXXXX</release>';
  release_date_start VARCHAR2(100) := '    <release_date>';
  release_date VARCHAR2(20) default null;
  release_date_end VARCHAR2(15) := '</release_date>';
  entry_count_start VARCHAR2(30) := '     <entry_count>';
  entry_count NUMBER(10) default 0;
  entry_count_end VARCHAR2(20) := '</entry_count>';

  entries_start VARCHAR2(20) := '    <entries>';
  entries_end VARCHAR2(20)   := '    </entries>';

  entry_start VARCHAR2(30) := '        <entry id="XXXXXX">';
  entry_end VARCHAR2(30) := '        </entry>';

  name_start VARCHAR2(100) := '            <name>';
  name_end VARCHAR2(100) := '</name>';

  description_start VARCHAR2(200) := '            <description>';
  description_end VARCHAR2(200)   := '</description>';

  cross_ref_start VARCHAR2(100) := '            <cross_references>';
  cross_ref_end VARCHAR2(100)   := '            </cross_references>';

  ref_db_key VARCHAR2(200) := '                <ref dbkey="DBKEY_REPLACE" dbname="DBNAME_REPLACE" />';
  l_ref_db_key VARCHAR2(200) default null;

  dates_start VARCHAR2(100) := '            <dates>';
  dates_end VARCHAR2(100)   := '            </dates>';
  creation_date VARCHAR2(100)     := '                <date value="DATE_REPLACE" type="creation" />';
  modification_date VARCHAR2(100) := '                <date value="DATE_REPLACE" type="last_modification_date" />';

  add_fields_start VARCHAR2(100) := '            <additional_fields>';
  add_fields_entry VARCHAR2(200) := '                <field name="FIELD_NAME">FIELD_VALUE</field>';
  l_add_fields_entry VARCHAR2(200) default null;
  add_fields_end VARCHAR2(100)   := '            </additional_fields>';

  file_end VARCHAR2(100) := '</database>';


  cursor accession_c is
    select
      'STUDY' as source, s.id as entry_id, s.acc as name, TO_CHAR(dbms_lob.substr(s.title, 3999, 1 )) as title,
      s.submissiondate, s.releasedate, TO_CHAR(dbms_lob.substr(s.description, 3999, 1 )) as description, null as chebi_id, null as inchi,
      s.id as study_id
    from study s where status = 0
        ---and acc='MTBLS111'
     UNION
      select distinct 'COMPOUND' as source, rm.id as entry_id, rm.acc as name, name as title,
        rm.created_date as submissiondate, nvl(rm.updated_date,rm.created_date) as releasedate, rm.description as description, temp_id as chebi_id, inchi as inch,
        s.id as study_id
      from
        ref_entry re,
        ref_metabolite rm,
        ref_xref rx,
        study s
      where re.id = rx.to_entry
        and rx.from_entry = rm.id
        and re.db_id = 100 -- MetaboLights
        and re.entry_id = s.acc(+) --outher join
        --and rm.acc = 'MTBLC1402'
        ;


 -- Cursor for studies
 cursor metabo_c(study_id NUMBER, study_acc VARCHAR) is
    select distinct m.identifier,
      decode(substr(m.identifier,1,3),
              'CHE','chebi',
              'HMD','hmdb',
              'CID','pubchem',
              'MTB','metabolights',
              'LMP','Lipid Maps',
              'LMG','Lipid Maps',
              'C01','KEGG','C02','KEGG','C03','KEGG','C04','KEGG','C05','KEGG','C06','KEGG','C07','KEGG','C08','KEGG','C09','KEGG','C10','KEGG','C11','KEGG','C12','KEGG','C13','KEGG','C14','KEGG','C15','KEGG',
              'C16','KEGG','C17','KEGG','C18','KEGG','C19','KEGG','C20','KEGG',
              'OTHER') as database,
               replace(replace(replace(replace(trim(m.description),'/',' '),'|',' '),'?',' '),'  ',' ') as metabolite
    from
      metabolite m,
      assaygroup a
    where a.study_id = study_id
      and m.assaygroup_id = a.id
      and m.identifier like 'CHEBI:%'
    UNION
      select rm.acc as identifier, 'metabolights' as database, rm.name as metabolite
    from
      ref_entry re,
      ref_metabolite rm,
      ref_xref rx
    where re.id = rx.to_entry
      and rx.from_entry = rm.id
      and re.entry_id = study_acc;

-- cursor for MTBLC records
  cursor metabo_comp(comp_acc VARCHAR) IS
      select rm.temp_id as entry_id, 'chebi' as database
      from
        ref_entry re, ref_metabolite rm, ref_xref rx, study s
      where re.id = rx.to_entry
        and rx.from_entry = rm.id
        and re.db_id = 100 -- MetaboLights
        and re.entry_id = s.acc(+) --outher join
        and rm.acc = comp_acc
      UNION
      select re.entry_id as entry_id, 'metabolights' as database
      from
        ref_entry re, ref_metabolite rm, ref_xref rx, study s
      where re.id = rx.to_entry
        and rx.from_entry = rm.id
        and re.db_id = 100 -- MetaboLights
        and re.entry_id = s.acc(+) --outher join
        and rm.acc = comp_acc
        ;

  cursor additional_c (l_study_id NUMBER) is
    select distinct d.value as design
    from design d
    where d.study_id = l_study_id;

  -- Assay information
  cursor assay_c(l_study_id NUMBER) is
    select distinct a.platform
    from assay a
    where a.study_id = l_study_id;


  cursor organism_c(p_study_id NUMBER) is
    SELECT DISTINCT PV.VALUE, S.ACC
    FROM PROPERTY_VALUE PV
      LEFT JOIN PROPERTY P ON PV.PROPERTY_ID = P.ID
      LEFT JOIN MATERIAL M ON PV.MATERIAL_ID = M.ID
      LEFT JOIN NODE N ON M.NODE_ID = N.ID
      LEFT JOIN STUDY S ON N.STUDY_ID = S.ID
    WHERE s.id = p_study_id
      AND LOWER(P.VALUE) = 'organism'
      AND PV.VALUE <> 'none';


  cursor factor_c(f_study_id NUMBER) IS
    SELECT distinct S.ACC, PV.VALUE AS FactorValue, P.VALUE AS Factor
    FROM ASSAYRESULT2PROPERTYVALUE AR2PV
      LEFT JOIN PROPERTY_VALUE PV ON AR2PV.PV_ID = PV.ID
      left join PROPERTY P ON PV.PROPERTY_ID = P.ID
      LEFT JOIN ASSAYRESULT AR ON AR2PV.AR_ID = AR.ID
      LEFT JOIN STUDY S ON AR.STUDY_ID = S.ID
    where
      PV.OBJ_TYPE = 'FactorValue'
      and s.id=f_study_id;



  -- Submitter(s)
  cursor submitter_c(sub_study_id NUMBER) IS
   select ud.firstname, ud.lastname
   from
    user_detail ud, study2user s
   where s.user_id = ud.id
    and s.study_id = sub_study_id;

  -- Technology
  cursor technology_c(o_study_id NUMBER)  IS
    select distinct oe.name
    from
      ontology_entry oe,
      assay a
    where  a.technology = oe.id
      and oe.obj_type = 'AssayTechnology'
      and a.study_id = o_study_id;




  -- IUPAC name
  cursor iupac_c(compound_id VARCHAR2) IS
    select distinct k.name as value
    from
      ken_test k,
      ref_metabolite rm
    where k.chebi_id = rm.temp_id
       and rm.acc = compound_id;

  -- formula
  cursor formula_c(compound_id VARCHAR2) IS
    select distinct  k.formula as value
    from
      ken_test k,
      ref_metabolite rm
    where k.chebi_id = rm.temp_id
       and rm.acc = compound_id;


BEGIN

  dbms_output.enable(null); --To enable unlimited print bugger for dbms_output
  -- Get the release date
  select to_char(SYSDATE,'YYYY-MM-DD') into release_date from dual;

  -- Get the number of entries to export. All public studies and reference metabolites
    select SUM(ids) into entry_count from (
      select count(*) as IDS from study s where status = 0
      UNION
      select distinct count(*) as IDS
        from ref_entry re, ref_metabolite rm, ref_xref rx, study s
      where re.id = rx.to_entry
        and rx.from_entry = rm.id
        and re.db_id = 100 -- MetaboLights
        and re.entry_id = s.acc(+)  --outher join
     );


  -- Output the header
  dbms_output.put_line(start_str);
  dbms_output.put_line(replace(release,'XXXXXX',nvl(RELEASE_NUMBER,1)));
  dbms_output.put_line(release_date_start || release_date ||release_date_end);
  dbms_output.put_line(entry_count_start || entry_count ||entry_count_end);
  dbms_output.put_line(entries_start);


  -- Loop throught the studies


    -- Start MTBLS and MTBLC loops here
    FOR study_cur IN accession_c LOOP
      dbms_output.put_line(replace(entry_start,'XXXXXX', study_cur.name));

      IF (study_cur.source = 'STUDY') THEN -- Get study data
        dbms_output.put_line(name_start || study_cur.name || name_end);
        dbms_output.put_line(description_start || study_cur.title || description_end);
      ELSE
        dbms_output.put_line(name_start || study_cur.title || name_end);
        dbms_output.put_line(description_start || study_cur.description || description_end);
      END IF;
      -- Xrefs loop
      l_ref_db_key := ref_db_key; -- Store the value l_ref_db_keybefore the loop starts


      dbms_output.put_line(cross_ref_start);

        IF (study_cur.source = 'STUDY') THEN -- Get study data
          FOR metabo_cur IN metabo_c(study_cur.entry_id, study_cur.name) LOOP
            dbms_output.put_line(replace(replace(ref_db_key, 'DBKEY_REPLACE', metabo_cur.identifier),'DBNAME_REPLACE', metabo_cur.database) );
            ref_db_key := l_ref_db_key; --Reset the loop replacer
          END LOOP;
        ELSE -- Get  data for MTBLC compounds
          FOR metabo_comp_cur IN metabo_comp(study_cur.name) LOOP
            dbms_output.put_line(replace(replace(ref_db_key,'DBKEY_REPLACE',metabo_comp_cur.entry_id),'DBNAME_REPLACE',metabo_comp_cur.database) );
            ref_db_key := l_ref_db_key;
          END LOOP;
        END IF;
      dbms_output.put_line(cross_ref_end);

      --Dates for the Study or metabolite
      dbms_output.put_line(dates_start);
        dbms_output.put_line(replace(creation_date,'DATE_REPLACE',study_cur.submissiondate));
        dbms_output.put_line(replace(modification_date,'DATE_REPLACE',study_cur.releasedate));
      dbms_output.put_line(dates_end);

      l_add_fields_entry := add_fields_entry;
      --Additional fields start
      dbms_output.put_line(add_fields_start);

      -- First "single" entries from the study

        -- Study description
        IF (study_cur.source = 'STUDY') THEN

          dbms_output.put_line(replace(replace(add_fields_entry,'FIELD_NAME','study_description'),'FIELD_VALUE',study_cur.description) );
          add_fields_entry := l_add_fields_entry;

          -- Study design loop
          FOR add_cur IN additional_c(study_cur.entry_id) LOOP
            dbms_output.put_line(replace(replace(add_fields_entry,'FIELD_NAME','study_design'),'FIELD_VALUE',add_cur.design) );
            add_fields_entry := l_add_fields_entry;
          END LOOP;

          -- Factors
          FOR factor_cur IN factor_c(study_cur.entry_id) LOOP
            dbms_output.put_line(replace(replace(add_fields_entry,'FIELD_NAME','study_factor'),'FIELD_VALUE',factor_cur.factor || ' ' || factor_cur.factorvalue) );
            add_fields_entry := l_add_fields_entry;
          END LOOP;

          -- Metabolite name loop
          FOR metabo_cur IN metabo_c(study_cur.entry_id, study_cur.name) LOOP
            dbms_output.put_line(replace(replace(add_fields_entry,'FIELD_NAME','metabolite_name'),'FIELD_VALUE',metabo_cur.metabolite) );
            add_fields_entry := l_add_fields_entry;
          END LOOP;

          -- Submitter
          FOR submitter_cur IN submitter_c(study_cur.study_id) LOOP
            dbms_output.put_line(replace(replace(add_fields_entry,'FIELD_NAME','submitter'),'FIELD_VALUE',submitter_cur.firstname ||' '||submitter_cur.lastname ) );
            add_fields_entry := l_add_fields_entry;
          END LOOP;

        ELSE  -- MTBLC (compound) information

          dbms_output.put_line(replace(replace(add_fields_entry,'FIELD_NAME','inchi'),'FIELD_VALUE',study_cur.inchi) );
          add_fields_entry := l_add_fields_entry;

          -- Study design loop
          FOR tech_cur IN technology_c(study_cur.study_id) LOOP
            dbms_output.put_line(replace(replace(add_fields_entry,'FIELD_NAME','technology_type'),'FIELD_VALUE',tech_cur.name) );
            add_fields_entry := l_add_fields_entry;
          END LOOP;

          -- iupac name
          FOR iupac_cur IN iupac_c(study_cur.name) LOOP
            dbms_output.put_line(replace(replace(add_fields_entry,'FIELD_NAME','iupac'),'FIELD_VALUE',iupac_cur.value) );
            add_fields_entry := l_add_fields_entry;
          END LOOP;

          -- formula
          FOR formula_cur IN formula_c(study_cur.name) LOOP
            dbms_output.put_line(replace(replace(add_fields_entry,'FIELD_NAME','formula'),'FIELD_VALUE',formula_cur.value) );
            add_fields_entry := l_add_fields_entry;
          END LOOP;

        END IF;


        -- COMMON between studies and compounds

        -- Platform
        FOR assay_cur IN assay_c(study_cur.study_id) LOOP
          dbms_output.put_line(replace(replace(add_fields_entry,'FIELD_NAME','platform'),'FIELD_VALUE',assay_cur.platform) );
          add_fields_entry := l_add_fields_entry;
        END LOOP;

        -- Organisms
        FOR organism_cur IN organism_c(study_cur.study_id) LOOP
          dbms_output.put_line(replace(replace(add_fields_entry,'FIELD_NAME','organism'),'FIELD_VALUE',organism_cur.value) );
          add_fields_entry := l_add_fields_entry;
        END LOOP;




      -- Then Loop throught other sets
      dbms_output.put_line(add_fields_end);

      dbms_output.put_line(entry_end);
    END LOOP; -- MTBLS and MTBLC loop


  -- End MTBLS loop here
  dbms_output.put_line(entries_end);

  -- Output the footer
  dbms_output.put_line(file_end);


END EBEYE;