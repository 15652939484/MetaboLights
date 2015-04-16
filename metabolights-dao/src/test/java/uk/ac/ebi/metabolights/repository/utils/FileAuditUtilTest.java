package uk.ac.ebi.metabolights.repository.utils;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FileAuditUtilTest extends TestCase {

	private File auditedFolder;
	private File i_file;
	private File a_file;
	private File s_file;
	private File m_file;
	private File ifile;
	private File u_file;
	private File testFolder;

	@Before
	public void setUp() throws IOException {

		// Create a temporary directory
		testFolder= new File(FileUtils.getTempDirectory(), "FileAuditTest");
		testFolder.mkdir();
		FileUtils.cleanDirectory(testFolder);

		// Create some files
		i_file = createAFile("i_fooo");
		a_file = createAFile("a_fooo");
		s_file = createAFile("s_fooo");
		m_file = createAFile("m_fooo");
		ifile = createAFile("ifooo");
		u_file = createAFile("u_fooo");

		auditedFolder  = new File(testFolder,"important_data");
		auditedFolder.mkdir();


	}

	public File createAFile(String fileName) throws IOException {
		File newFile = new File(testFolder,fileName);
		newFile.createNewFile();
		return newFile;
	}

	@Test
	public void testGetAuditFolder(){

		File auditFolder = FileAuditUtil.getAuditFolder(auditedFolder);

		assertEquals("Audit folder test", getAuditFolderName(),auditFolder.getAbsolutePath());
		assertTrue("Audit folder exists", auditFolder.exists());

	}

	private String getAuditFolderName(){

		// Compose the expected audit folder path
		return new File(auditedFolder, FileAuditUtil.AUDIT_FOLDER_NAME).getAbsolutePath();
	}

	@Test
	public void testGetBackUpFolder() {

		File backUpFolder = FileAuditUtil.getBackUpFolder(auditedFolder);

		assertTrue("backup folder under audit folder", backUpFolder.getAbsolutePath().startsWith(getAuditFolderName()));
		assertTrue("backUp folder exists", backUpFolder.exists());

	}

	@Test
	public void testMoveFileToAuditedFolder() throws IOException {

		File backUpFolder = FileAuditUtil.getBackUpFolder(auditedFolder);

		FileAuditUtil.moveFileToAuditedFolder(i_file,auditedFolder,backUpFolder);

		// Nothing there since audited folder was empty
		assertEquals("new auditable file moved, but nothing to backup", 0, backUpFolder.list().length);


		// Create it again since it has been moved
		i_file.createNewFile();
		FileAuditUtil.moveFileToAuditedFolder(i_file,auditedFolder,backUpFolder);

		// Back up should occur
		assertEquals("existing auditable file moved, backup happens", 1, backUpFolder.list().length);



		// Test a not audited file
		FileAuditUtil.moveFileToAuditedFolder(ifile,auditedFolder,backUpFolder);

		// Nothing there since audited folder was empty
		assertEquals("new not audited file moved, nothing to backup", 1, backUpFolder.list().length);


		// Create it again since it has been moved
		ifile.createNewFile();
		FileAuditUtil.moveFileToAuditedFolder(ifile,auditedFolder,backUpFolder);

		// Back up shouldn't occur
		assertEquals("existing not audited file replaced, backup does not happens", 1, backUpFolder.list().length);


		// Test one more auditable file
		FileAuditUtil.moveFileToAuditedFolder(a_file,auditedFolder,backUpFolder);

		// Nothing there since audited folder was empty
		assertEquals("new a_auditable file moved, but nothing to backup", 1, backUpFolder.list().length);

		// Create it again since it has been moved
		a_file.createNewFile();
		FileAuditUtil.moveFileToAuditedFolder(a_file,auditedFolder,backUpFolder);

		// Back up should occur
		assertEquals("existing a_auditable file moved, backup happens", 2, backUpFolder.list().length);




	}

	@Test
	public void testIdFileAudited() throws Exception {

		assertTrue("i_file should be audited",FileAuditUtil.idFileAudited(i_file));
		assertTrue("s_file should be audited",FileAuditUtil.idFileAudited(s_file));
		assertTrue("a_file should be audited",FileAuditUtil.idFileAudited(a_file));
		assertTrue("m_file should be audited",FileAuditUtil.idFileAudited(m_file));
		assertFalse("u_file should not be audited", FileAuditUtil.idFileAudited(u_file));
		assertFalse("ifile should not be audited", FileAuditUtil.idFileAudited(ifile));
	}
}