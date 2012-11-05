
-- REF_METABOLITE TABLE, SEQ AND TRIGGER
--Create table (update when model is finish) TODO

TRUNCATE TABLE ISATAB.REF_METABOLITE;
DROP TABLE ISATAB.REF_METABOLITE; 
CREATE TABLE ISATAB.REF_METABOLITE 
   ("ID" NUMBER(*,0) NOT NULL, 
    "ACC" VARCHAR2(2000) NOT NULL, 
    "NAME" VARCHAR2(4000) NOT NULL, 
    "DESCRIPTION" VARCHAR2(4000), 
	 CONSTRAINT "PK_REF_METABOLITE" PRIMARY KEY ("ID"),
   CONSTRAINT UNIQUE_REF_MET_ACCESSION UNIQUE (ACC)
   );

ALTER TABLE ISATAB.REF_METABOLITE add CONSTRAINT UNIQUE_REF_MET_ACCESSION UNIQUE (ACC);

ALTER TABLE ISATAB.REF_METABOLITE add TEMP_ID VARCHAR2(20);
 
--Create the sequence for Metabolite ref id
DROP SEQUENCE ISATAB.REF_METABOLITE_SEQ;
CREATE SEQUENCE ISATAB.REF_METABOLITE_SEQ MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE ;

create or replace trigger REF_METABOLITE_TRIGGER
before insert or update on ISATAB.REF_METABOLITE    
for each row 
DECLARE
  new_id NUMBER(10);
BEGIN
  if inserting then
    if :NEW.ID is null then
      select REF_METABOLITE_SEQ.nextval into new_id from dual;
      :NEW.id := new_id;
    end if;
    if :NEW.ACC is null then
      :NEW.acc := 'MTBLC'||new_id;
    end if;

  end if;
end;
/
show errors


-- IMPORT FROM METABOLITE STUDIES
--select * from metabolite where 1=0
INSERT INTO REF_METABOLITE(NAME, DESCRIPTION, INCHI) 
SELECT IDENTIFIER as ACC, IDENTIFIER AS NAME, IDENTIFIER AS DESCRIPTION
FROM
(select distinct DESCRIPTION as NAME from metabolite where UPPER(identifer) like 'CHEBI:%');

--Run from ChEBI
select 'INSERT INTO REF_METABOLITE(NAME, DESCRIPTION, INCHI, TEMP_ID) values('''|| replace(c.ascii_name,'''','"') ||''','''|| replace(c.definition,'''','"') ||''','''|| replace(to_char(s.structure),'''','"') ||''','''|| c.chebi_accession || ''');'
from 
  compounds c, structures s
where c.id = s.compound_id
  and s.type = 'InChI'
  and c.chebi_accession in ('CHEBI:18237','CHEBI:57595','CHEBI:16134','CHEBI:16414','CHEBI:17115','CHEBI:59760','CHEBI:6650','CHEBI:17050',
'CHEBI:46905','CHEBI:32365','CHEBI:17505','CHEBI:16680','CHEBI:35932','CHEBI:16530','CHEBI:18300','CHEBI:40410',
'CHEBI:507393','CHEBI:18089','CHEBI:30763','CHEBI:18344','CHEBI:16742','CHEBI:217069','CHEBI:16810','CHEBI:15740',
'CHEBI:17521','CHEBI:16259','CHEBI:16300','CHEBI:18107','CHEBI:17368','CHEBI:17489','CHEBI:36405','CHEBI:493263',
'CHEBI:17688','CHEBI:17509','CHEBI:52742','CHEBI:48300','CHEBI:16015','CHEBI:18261','CHEBI:57589','CHEBI:17148',
'CHEBI:32816','CHEBI:16643','CHEBI:22653','CHEBI:58359','CHEBI:33384','CHEBI:18012','CHEBI:30765','CHEBI:151',
'CHEBI:4167','CHEBI:16708','CHEBI:16856','CHEBI:60645','CHEBI:20067','CHEBI:30832','CHEBI:21547','CHEBI:16919',
'CHEBI:17786','CHEBI:41865','CHEBI:17884','CHEBI:17113','CHEBI:15366','CHEBI:16335',
'CHEBI:44811','CHEBI:17775','CHEBI:29073','CHEBI:19062','CHEBI:28832','CHEBI:18095','CHEBI:16765','CHEBI:27732',
'CHEBI:681857','CHEBI:21626','CHEBI:16199','CHEBI:16359','CHEBI:17203','CHEBI:16958','CHEBI:17345','CHEBI:17927',
'CHEBI:18333','CHEBI:15428','CHEBI:35621','CHEBI:28044','CHEBI:18257','CHEBI:17755','CHEBI:16189','CHEBI:16857',
'CHEBI:15741','CHEBI:17568','CHEBI:17084','CHEBI:16010','CHEBI:10642','CHEBI:18169','CHEBI:30794','CHEBI:60647',
'CHEBI:15849','CHEBI:16914','CHEBI:17645','CHEBI:15347','CHEBI:15595','CHEBI:64390','CHEBI:17381','CHEBI:18295',
'CHEBI:17126','CHEBI:17748','CHEBI:19289','CHEBI:63931','CHEBI:17561','CHEBI:17154','CHEBI:15354','CHEBI:15760',
'CHEBI:16695','CHEBI:30852','CHEBI:47693','CHEBI:57762','CHEBI:25017','CHEBI:26271','CHEBI:16109','CHEBI:28358',
'CHEBI:4208','CHEBI:37252','CHEBI:28842','CHEBI:16344','CHEBI:16168','CHEBI:18050','CHEBI:16737','CHEBI:17189',
'CHEBI:13705','CHEBI:18139','CHEBI:29806','CHEBI:32980','CHEBI:27637','CHEBI:32544','CHEBI:16388','CHEBI:9300',
'CHEBI:27823','CHEBI:27551','CHEBI:61509','CHEBI:17596','CHEBI:16464','CHEBI:17895','CHEBI:15971',
'CHEBI:28683','CHEBI:16797','CHEBI:16586','CHEBI:17794','CHEBI:16668','CHEBI:15603','CHEBI:48430','CHEBI:57742',
'CHEBI:16040','CHEBI:16750','CHEBI:16449','CHEBI:24898','CHEBI:32820','CHEBI:22660',
'CHEBI:16811','CHEBI:52682','CHEBI:26078','CHEBI:30746','CHEBI:30653','CHEBI:30915','CHEBI:30769','CHEBI:17533',
'CHEBI:16828','CHEBI:18147','CHEBI:30860','CHEBI:17622','CHEBI:28478','CHEBI:4194','CHEBI:15908','CHEBI:336244',
'CHEBI:17724','CHEBI:16320','CHEBI:16704','CHEBI:43355','CHEBI:14336','CHEBI:17385',
'CHEBI:9008','CHEBI:17710','CHEBI:7563','CHEBI:17069','CHEBI:16235','CHEBI:28790','CHEBI:17405','CHEBI:28867',
'CHEBI:17015','CHEBI:17687','CHEBI:10696','CHEBI:16977','CHEBI:17602','CHEBI:17821','CHEBI:45171','CHEBI:42593',
'CHEBI:35128','CHEBI:16467','CHEBI:30997','CHEBI:15756','CHEBI:15414','CHEBI:33198','CHEBI:35697','CHEBI:17992',
'CHEBI:29571','CHEBI:1148','CHEBI:17191','CHEBI:17597','CHEBI:48131','CHEBI:30089','CHEBI:15746','CHEBI:18019',
'CHEBI:480324','CHEBI:28835','CHEBI:15901','CHEBI:16410','CHEBI:28946','CHEBI:594277','CHEBI:17768','CHEBI:17141',
'CHEBI:30796','CHEBI:17924','CHEBI:13172','CHEBI:16112','CHEBI:17964','CHEBI:16995','CHEBI:17626','CHEBI:4139',
'CHEBI:27897','CHEBI:41308','CHEBI:16027','CHEBI:42111','CHEBI:36062','CHEBI:15940','CHEBI:16974','CHEBI:17053',
'CHEBI:46195','CHEBI:40813','CHEBI:35825','CHEBI:15611','CHEBI:29805','CHEBI:15676','CHEBI:41941','CHEBI:28177',
'CHEBI:16899','CHEBI:15724','CHEBI:16865','CHEBI:17310','CHEBI:27389','CHEBI:16973','CHEBI:17712','CHEBI:17296',
'CHEBI:8337','CHEBI:16207','CHEBI:37024','CHEBI:25094','CHEBI:18186','CHEBI:17522','CHEBI:32398','CHEBI:15584',
'CHEBI:15356','CHEBI:18127','CHEBI:4170','CHEBI:17733','CHEBI:16551','CHEBI:16236','CHEBI:50129','CHEBI:30768',
'CHEBI:15793','CHEBI:21077','CHEBI:16349','CHEBI:17968','CHEBI:30031','CHEBI:17170','CHEBI:104011','CHEBI:18123',
'CHEBI:18183','CHEBI:16814','CHEBI:15728','CHEBI:17784','CHEBI:48095','CHEBI:16610','CHEBI:15729','CHEBI:17151');


--Run from ChEBI
select 'INSERT INTO REF_ENTRY(DB_ID, ENTRY_ID, ENTRY_NAME) values(101,'''|| chebi_accession ||''','''|| replace(ascii_name,'''','"') || ''');'
from 
  compounds
where chebi_accession in ('CHEBI:18237','CHEBI:57595','CHEBI:16134','CHEBI:16414','CHEBI:17115','CHEBI:59760','CHEBI:6650','CHEBI:17050',
'CHEBI:46905','CHEBI:32365','CHEBI:17505','CHEBI:16680','CHEBI:35932','CHEBI:16530','CHEBI:18300','CHEBI:40410',
'CHEBI:507393','CHEBI:18089','CHEBI:30763','CHEBI:18344','CHEBI:16742','CHEBI:217069','CHEBI:16810','CHEBI:15740',
'CHEBI:17521','CHEBI:16259','CHEBI:16300','CHEBI:18107','CHEBI:17368','CHEBI:17489','CHEBI:36405','CHEBI:493263',
'CHEBI:17688','CHEBI:17509','CHEBI:52742','CHEBI:48300','CHEBI:16015','CHEBI:18261','CHEBI:57589','CHEBI:17148',
'CHEBI:32816','CHEBI:16643','CHEBI:22653','CHEBI:58359','CHEBI:33384','CHEBI:18012','CHEBI:30765','CHEBI:151',
'CHEBI:4167','CHEBI:16708','CHEBI:16856','CHEBI:60645','CHEBI:20067','CHEBI:30832','CHEBI:21547','CHEBI:16919',
'CHEBI:17786','CHEBI:41865','CHEBI:17884','CHEBI:17113','CHEBI:15366','CHEBI:16335',
'CHEBI:44811','CHEBI:17775','CHEBI:29073','CHEBI:19062','CHEBI:28832','CHEBI:18095','CHEBI:16765','CHEBI:27732',
'CHEBI:681857','CHEBI:21626','CHEBI:16199','CHEBI:16359','CHEBI:17203','CHEBI:16958','CHEBI:17345','CHEBI:17927',
'CHEBI:18333','CHEBI:15428','CHEBI:35621','CHEBI:28044','CHEBI:18257','CHEBI:17755','CHEBI:16189','CHEBI:16857',
'CHEBI:15741','CHEBI:17568','CHEBI:17084','CHEBI:16010','CHEBI:10642','CHEBI:18169','CHEBI:30794','CHEBI:60647',
'CHEBI:15849','CHEBI:16914','CHEBI:17645','CHEBI:15347','CHEBI:15595','CHEBI:64390','CHEBI:17381','CHEBI:18295',
'CHEBI:17126','CHEBI:17748','CHEBI:19289','CHEBI:63931','CHEBI:17561','CHEBI:17154','CHEBI:15354','CHEBI:15760',
'CHEBI:16695','CHEBI:30852','CHEBI:47693','CHEBI:57762','CHEBI:25017','CHEBI:26271','CHEBI:16109','CHEBI:28358',
'CHEBI:4208','CHEBI:37252','CHEBI:28842','CHEBI:16344','CHEBI:16168','CHEBI:18050','CHEBI:16737','CHEBI:17189',
'CHEBI:13705','CHEBI:18139','CHEBI:29806','CHEBI:32980','CHEBI:27637','CHEBI:32544','CHEBI:16388','CHEBI:9300',
'CHEBI:27823','CHEBI:27551','CHEBI:61509','CHEBI:17596','CHEBI:16464','CHEBI:17895','CHEBI:15971',
'CHEBI:28683','CHEBI:16797','CHEBI:16586','CHEBI:17794','CHEBI:16668','CHEBI:15603','CHEBI:48430','CHEBI:57742',
'CHEBI:16040','CHEBI:16750','CHEBI:16449','CHEBI:24898','CHEBI:32820','CHEBI:22660',
'CHEBI:16811','CHEBI:52682','CHEBI:26078','CHEBI:30746','CHEBI:30653','CHEBI:30915','CHEBI:30769','CHEBI:17533',
'CHEBI:16828','CHEBI:18147','CHEBI:30860','CHEBI:17622','CHEBI:28478','CHEBI:4194','CHEBI:15908','CHEBI:336244',
'CHEBI:17724','CHEBI:16320','CHEBI:16704','CHEBI:43355','CHEBI:14336','CHEBI:17385',
'CHEBI:9008','CHEBI:17710','CHEBI:7563','CHEBI:17069','CHEBI:16235','CHEBI:28790','CHEBI:17405','CHEBI:28867',
'CHEBI:17015','CHEBI:17687','CHEBI:10696','CHEBI:16977','CHEBI:17602','CHEBI:17821','CHEBI:45171','CHEBI:42593',
'CHEBI:35128','CHEBI:16467','CHEBI:30997','CHEBI:15756','CHEBI:15414','CHEBI:33198','CHEBI:35697','CHEBI:17992',
'CHEBI:29571','CHEBI:1148','CHEBI:17191','CHEBI:17597','CHEBI:48131','CHEBI:30089','CHEBI:15746','CHEBI:18019',
'CHEBI:480324','CHEBI:28835','CHEBI:15901','CHEBI:16410','CHEBI:28946','CHEBI:594277','CHEBI:17768','CHEBI:17141',
'CHEBI:30796','CHEBI:17924','CHEBI:13172','CHEBI:16112','CHEBI:17964','CHEBI:16995','CHEBI:17626','CHEBI:4139',
'CHEBI:27897','CHEBI:41308','CHEBI:16027','CHEBI:42111','CHEBI:36062','CHEBI:15940','CHEBI:16974','CHEBI:17053',
'CHEBI:46195','CHEBI:40813','CHEBI:35825','CHEBI:15611','CHEBI:29805','CHEBI:15676','CHEBI:41941','CHEBI:28177',
'CHEBI:16899','CHEBI:15724','CHEBI:16865','CHEBI:17310','CHEBI:27389','CHEBI:16973','CHEBI:17712','CHEBI:17296',
'CHEBI:8337','CHEBI:16207','CHEBI:37024','CHEBI:25094','CHEBI:18186','CHEBI:17522','CHEBI:32398','CHEBI:15584',
'CHEBI:15356','CHEBI:18127','CHEBI:4170','CHEBI:17733','CHEBI:16551','CHEBI:16236','CHEBI:50129','CHEBI:30768',
'CHEBI:15793','CHEBI:21077','CHEBI:16349','CHEBI:17968','CHEBI:30031','CHEBI:17170','CHEBI:104011','CHEBI:18123',
'CHEBI:18183','CHEBI:16814','CHEBI:15728','CHEBI:17784','CHEBI:48095','CHEBI:16610','CHEBI:15729','CHEBI:17151');
 

select 'insert into ref_xref(from_entry, to_entry, rel_type_id) values('|| rm.id ||','|| re.id || ',2);'
from 
  ref_metabolite rm, ref_entry re
where
  rm.temp_id = re.entry_id;

-- Clean up before test run
truncate table ref_entry;
truncate table ref_metabolite;
truncate table ref_xref;

---------
-- Run the output from the 3 scripts above
---------

-- Tidy up a bit
update ref_metabolite set description = replace(description,'</stereo>') where description like '%</stereo>%';
update ref_metabolite set description = replace(description,'<stereo>') where description like '%<stereo>%';
update ref_metabolite set description = replace(description,'<element>') where description like '%<element>%';
update ref_metabolite set description = replace(description,'</element>') where description like '%</element>%';
update ref_metabolite set description = replace(description,'<locant>') where description like '%<locant>%';
update ref_metabolite set description = replace(description,'</locant>') where description like '%</locant>%';
update ref_metabolite set description = replace(description,'</bond>') where description like '%</bond>%';
update ref_metabolite set description = replace(description,'<bond>') where description like '%<bond>%';
update ref_metabolite set description = replace(description,'<ital>') where description like '%<ital>%';
update ref_metabolite set description = replace(description,'</ital>') where description like '%</ital>%';
update ref_metabolite set description = replace(description,'<smallsub>') where description like '%<smallsub>%';
update ref_metabolite set description = replace(description,'</smallsub>') where description like '%</smallsub>%';
update ref_metabolite set description = replace(description,'<smallsup>') where description like '%<smallsup>%';
update ref_metabolite set description = replace(description,'</smallsup>') where description like '%</smallsup>%';
update ref_metabolite set description = replace(description,'<minus/>') where description like '%<minus/>%';
update ref_metabolite set description = replace(description,'<degree/>') where description like '%<degree/>%';


--Consistency check: SELECT Count(*) FROM (select distinct IDENTIFIER, DESCRIPTION from metabolite where instr(IDENTIFIER,'CHEBI:')=1) GROUP BY IDENTIFIER, DESCRIPTION HAVING COunt(*) >1
UPDATE REF_METABOLITE SET DESCRIPTION = DESCRIPTION || ' description';


--SELECT * FROM REF_METABOLITE
UPDATE REF_METABOLITE SET ACC = 'MTBLC' || ID WHERE instr(ACC,'MTBLC')=0;



--*******************************


-- DB TABLE, SEQ AND TRIGGER*****
CREATE TABLE ISATAB.DB
  ( ID NUMBER(*,0) NOT NULL,
  DB_NAME VARCHAR(50 BYTE) NOT NULL,
  CONSTRAINT PK_DB PRIMARY KEY (ID)
  );

 
 
--Create the sequence for DATABASE TABLE
CREATE SEQUENCE ISATAB.DB_SEQ MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE ;

--PURGE RECYCLEBIN;

-- CREATE THE TRIGGER
create or replace trigger DB_SEQ_TRIGGER
before insert or update on "ISATAB"."DB"    
for each row 
begin
  if inserting then
    if :NEW."ID" is null then
      select DB_SEQ.nextval into :NEW."ID" from dual;
    end if;
  end if;
end;

-- Select * from DB
INSERT INTO DB (DB_NAME) VALUES('METABLOLIGHTS');
INSERT INTO DB (DB_NAME) VALUES('CHEBI');




-- REL_TYPE TABLE, SEQ AND TRIGGER
--Create table (update when model is finish) TODO
CREATE TABLE ISATAB.REL_TYPE 
   (	"ID" NUMBER(*,0) NOT NULL, 
	"NAME" VARCHAR2(60 BYTE) NOT NULL, 
	 CONSTRAINT "PK_REL_TYPE" PRIMARY KEY (ID)
   );

 
--Create the sequence for Metabolite ref id
CREATE SEQUENCE ISATAB.REL_TYPE_SEQ MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE ;

create or replace trigger REL_TYPE_TRIGGER
before insert or update on "ISATAB"."REL_TYPE"    
for each row 
begin
  if inserting then
    if :NEW."ID" is null then
      select REL_TYPE_SEQ.nextval into :NEW."ID" from dual;
    end if;
  end if;
end;


INSERT INTO REL_TYPE (NAME) VALUES('identified in');
INSERT INTO REL_TYPE (NAME) VALUES('is related to');

--*******************************


-- REF_ENTRY TABLE, SEQ AND TRIGGER
--Create table (update when model is finish) 
CREATE TABLE ISATAB.REF_ENTRY 
   (	"ID" NUMBER(*,0) NOT NULL, 
      "DB_ID" NUMBER(*,0) NOT NULL,
      "ENTRY_ID" VARCHAR2(255 CHAR) NOT NULL,
      "ENTRY_NAME" VARCHAR2(1000 CHAR), 
	 CONSTRAINT "PK_REF_ENTRY" PRIMARY KEY (ID),
   CONSTRAINT "FK_REF_ENTRY_EL_TYPE" FOREIGN KEY (DB_ID) REFERENCES DB(ID)
   );


--alter table REF_ENTRY modify (ENTRY_NAME null);
 
--Create the sequence for Metabolite ref id
CREATE SEQUENCE ISATAB.REF_ENTRY_SEQ MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE ;

create or replace trigger REF_ENTRY_TRIGGER
before insert or update on "ISATAB"."REF_ENTRY"    
for each row 
begin
  if inserting then
    if :NEW."ID" is null then
      select REF_ENTRY_SEQ.nextval into :NEW."ID" from dual;
    end if;
  end if;
end;


-- INSERT CHEBI ENTRIES IN REF_ENTRY
-- SELECT * FROM DB
INSERT INTO REF_ENTRY SELECT NULL ID, 101 AS DB_ID, NAME AS ENTRY_ID, NULL  FROM REF_METABOLITE; 



--*******************************


-- REF_XREF TABLE, SEQ AND TRIGGER
--Create table
CREATE TABLE ISATAB.REF_XREF 
   (	"ID" NUMBER(*,0) NOT NULL, 
      "FROM_ENTRY" NUMBER(10,0) NOT NULL,
      "TO_ENTRY" NUMBER(10,0) NOT NULL,
      "REL_TYPE_ID" NUMBER(*,0)NOT NULL, 
	 CONSTRAINT "PK_REF_XREF" PRIMARY KEY (ID),
   CONSTRAINT "FK_REF_XREF_REL_TYPE" FOREIGN KEY (REL_TYPE_ID) REFERENCES REL_TYPE(ID)
   );

 
--Create the sequence for Metabolite ref id
CREATE SEQUENCE ISATAB.REF_XREF_SEQ MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE NOORDER NOCYCLE ;

create or replace trigger REF_XREF_TRIGGER
before insert or update on "ISATAB"."REF_XREF"    
for each row 
begin
  if inserting then
    if :NEW."ID" is null then
      select REF_XREF_SEQ.nextval into :NEW."ID" from dual;
    end if;   
  end if;
end;


-- Link REF_METABOLITE WITH CHEBI ENTRIES.
INSERT INTO REF_XREF
SELECT NULL AS ID ,REF_METABOLITE.ID AS FROM_ENTRY, REF_ENTRY.ID AS TO_ENTRY, 2 AS REL_TYPE_ID
FROM REF_METABOLITE 
LEFT JOIN REF_ENTRY ON REF_METABOLITE.NAME=REF_ENTRY.ENTRY_ID



--*******************************


SELECT * FROM REF_METABOLITE