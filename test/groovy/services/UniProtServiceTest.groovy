package services

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext

import models.Protein
import groovy.util.GroovyTestCase

/**
 *  Self explanatory I hope
 * 
 * @author den.konakov@gmail.com
 */
class UniProtServiceTest extends GroovyTestCase {
	
	private ApplicationContext textContext
	
	void setUp() {
		textContext = new FileSystemXmlApplicationContext("test\\groovy\\services\\test_beans.xml")
	}
	
	void testXml_ForQ99728() {
		def ups = (UniProtService) textContext.getBean(UniProtService.class)
		Protein protein = ups.getProteinInfo("Q99728")
		
		assert protein!=null

		assert protein.protein_id=="Q99728"
		assert protein.name=="BARD1_HUMAN"
		assert protein.full_name=="BRCA1-associated RING domain protein 1"
		assert protein.short_name=="BARD-1"
	}
	
	// This test is depends on <max-idle-seconds>5</max-idle-seconds> in hazelcast.xml
	void testMapPurge() {
		def ups = (UniProtService) textContext.getBean(UniProtService.class)
		
		// Should see as map is filled
		Protein protein = ups.getProteinInfo("Q99728")
		
		// Should be from the map
		protein = ups.getProteinInfo("Q99728")
		
		sleep(3000)
		
		// Should be again filling from the internet
		protein = ups.getProteinInfo("Q99728")
	}
	
}