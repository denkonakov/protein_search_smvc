package services

import models.Protein
import groovy.util.GroovyTestCase;

/**
 *   Self explanatory I hope
 * 
 * @author den.konakov@gmail.com
 */
class PSICQUICServiceTest extends GroovyTestCase {

	void testTabFile_testXml_ForQ99728() {
		def ups = new UniProtService()
		def c = new GlobalCachingService()
		ups.cache = c
		Protein protein = ups.getProteinInfo("Q99728")
		
		def psicq = new PSICQUICService()
		psicq.cache = c
		// TODO: better solution needed
		psicq.ups = ups
		def list = psicq.getInteractors("Q99728")
		
		assertTrue (list!=null) && (list.size()==26)
	}
	
	// SHould not be IndexOutOfBound Exception
	void testTabFile_testXml_ForQ96RL1() {
		def ups = new UniProtService()
		def c = new GlobalCachingService()
		ups.cache = c
		Protein protein = ups.getProteinInfo("Q96RL1")
		
		def psicq = new PSICQUICService()
		psicq.cache = c
		// TODO: better solution needed
		psicq.ups = ups
		def list = psicq.getInteractors("Q96RL1")
		
		assertTrue (list!=null)
		println list.size()
		
		sleep(1000)
		
		list = psicq.getInteractors("Q96RL1")
		
		assertTrue (list!=null)
		println list.size()
	}

	// Ids should be all unique
	void testTabFile_testXml_ForO95251() {
		def ups = new UniProtService()
		def c = new GlobalCachingService()
		ups.cache = c
		Protein protein = ups.getProteinInfo("O95251")
		
		def psicq = new PSICQUICService()
		psicq.cache = c
		// TODO: better solution needed
		psicq.ups = ups
		def list = psicq.getInteractors("O95251")
		
		assertTrue (list!=null)
		println list.size()
	}
}
