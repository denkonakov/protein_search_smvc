package services

import models.Protein
import groovy.util.GroovyTestCase

/**
 *  Self explanatory I hope
 * 
 * @author den.konakov@gmail.com
 */
class UniProtServiceTest extends GroovyTestCase {
	
	void testXml_ForQ99728() {
		def ups = new UniProtService()
		Protein protein = ups.getProteinInfo("Q99728")
		
		assert protein!=null

		assert protein.protein_id=="Q99728"
		assert protein.name=="BARD1_HUMAN"
		assert protein.full_name=="BRCA1-associated RING domain protein 1"
		assert protein.short_name=="BARD-1"
	}
	
	// This test is depends on <max-idle-seconds>5</max-idle-seconds> in hazelcast.xml
	void testMapPurge() {
		def ups = new UniProtService()
		ups.cache = new GlobalCachingService()
		// Should see as map is filled
		Protein protein = ups.getProteinInfo("Q99728")
		// Should be from the map
		protein = ups.getProteinInfo("Q99728")
		
		sleep(1000)
		
		// Should be again filling from the internet
		protein = ups.getProteinInfo("Q99728")
	}
	
}