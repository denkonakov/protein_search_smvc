package services

import models.Protein

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component

import services.GlobalCachingService.Caches

/**
 * Service for getting and parse data from PSICQUIC WS.
 * 
 * @author den.konakov@gmail.com
 */
@Component
class PSICQUICService {

	@Autowired
	private UniProtService ups
	
	@Autowired
	private GlobalCachingService cache
	
	private Logger log = Logger.getLogger(PSICQUICService.class)
	def URL_TEMPALTE = "http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/id:"

	// Regexp for getting two first tabs from text line
	def pattern = /^uniprotkb:([^|]+)[^\t]+\tuniprotkb:([^|]+)/
	
	def getInteractors(String proteinId) {
		
		def ids = null
		def me = cache.getInteractMap().getMapEntry(proteinId)
		if (me && me.isValid()) {
			log.info("{getInteractors} Cached value="+me+" valid="+me.isValid())
			ids = me.getValue()
		}

		if (!ids) {		
			def url = URL_TEMPALTE+proteinId
			log.info("{getInteractors} URL for fetch file: "+url)
			
			ids = []
			url.toURL().text.eachLine {
				log.info("{getInteractors} it="+it)
				def matcher = (it =~ pattern)
				log.info("{getInteractors} matcher="+matcher)
				// If we matched line with pattern, then we should find
				// interactor ID in two first columns
				if (matcher.size()>0) {
					matcher[0].eachWithIndex { item, index ->
						if (index > 0 && item != proteinId) {
							ids += [item]
						}
					}
				}
			}
			
			// As I find out, there can be duplicates...
			ids = ids.unique()
			log.info("{getInteractors} list of unique id: " + ids)
			
			cache.fillSafeMap(Caches.INTERACT, proteinId, ids)
		}
		
		def list = []
		ids.each {
			Protein itp = ups.getProteinInfo(it)
			log.info("{getInteractors} Interactor: Id="+itp.protein_id+" Name="+itp.name)
			list += [itp]
		}
		
		return list
	}
	
}