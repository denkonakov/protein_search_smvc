package services

import org.springframework.context.ApplicationContext
import org.springframework.context.support.FileSystemXmlApplicationContext

import models.Protein
import groovy.util.GroovyTestCase;

/**
 *   Self explanatory I hope
 * 
 * @author den.konakov@gmail.com
 */
class PSICQUICServiceTest extends GroovyTestCase {

	private ApplicationContext textContext
	
	void setUp() {
		textContext = new FileSystemXmlApplicationContext("test\\groovy\\services\\test_beans.xml")
	}
	
	void testTabFile_testXml_ForQ99728() {
		def ups = (UniProtService) textContext.getBean(UniProtService.class)
		Protein protein = ups.getProteinInfo("Q99728")
		
		def psicq = (PSICQUICService) textContext.getBean(PSICQUICService.class)
		def list = psicq.getInteractors("Q99728")
		
		assertTrue (list!=null) && (list.size()==26)
	}
	
	// SHould not be IndexOutOfBound Exception
	void testTabFile_testXml_ForQ96RL1() {
		def ups = (UniProtService) textContext.getBean(UniProtService.class)
		Protein protein = ups.getProteinInfo("Q96RL1")
		
		def psicq = (PSICQUICService) textContext.getBean(PSICQUICService.class)
		def list = psicq.getInteractors("Q96RL1")

				assertTrue (list!=null)
		println list.size()
		
		sleep(3000)
		
		list = psicq.getInteractors("Q96RL1")
		
		assertTrue (list!=null)
		println list.size()
	}

	// Ids should be all unique
	void testTabFile_testXml_ForO95251() {
		def ups = (UniProtService) textContext.getBean(UniProtService.class)
		Protein protein = ups.getProteinInfo("O95251")
		
		def psicq = (PSICQUICService) textContext.getBean(PSICQUICService.class)
		def list = psicq.getInteractors("O95251")
		
		assertTrue (list!=null)
		println list.size()
	}
}