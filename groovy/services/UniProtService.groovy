package services


import models.Protein

import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import services.GlobalCachingService.Caches

/** 
 * Service for getting and parse data from UniProt WS.
 * 
 * The WS is REST/XML based and have the following structure:	http://www.uniprot.org/uniprot/{proteinID}.xml
 * For example: http://www.uniprot.org/uniprot/Q99728.xml.
 * 
 *  a) Name (Xml Field: entry/name)
 *	b) Full Name (Xml Field: entry/protein/recommendedName/fullName)
 *	c) Short Name (Xml Field: entry/protein/recommendedName/shortName)
 *	d) Organism (Xml Field: entry/organism/name@type="scientific")
 *	e) Comment (Xml Field: entry/comment@type="function")
 * 
 * @author den.konakov@gmail.com
 */
@Component
class UniProtService {

	private Logger log = Logger.getLogger(UniProtService.class)	
	def URL_TEMPLATE = "http://www.uniprot.org/uniprot/"

	@Autowired
	private GlobalCachingService cache
		
	def getProteinInfo(String proteinId) {
		
		def me = cache.getProteinsMap().getMapEntry(proteinId)
		if (me && me.isValid()) {
			log.info("{getProteinInfo} Cached value="+me+" valid="+me.isValid())
			return me.getValue()
		}
		
		def url = URL_TEMPLATE+proteinId+".xml"
		log.info("{getProteinInfo} URL to call: " + url)
		def xml = new XmlSlurper().parseText(url.toURL().text)
		
		Protein res = new Protein(
			protein_id: proteinId,
			name:       xml.entry.name.toString(),
			full_name:  xml.entry.protein.recommendedName.fullName.toString(),
			short_name: xml.entry.protein.recommendedName.shortName.toString(),
			organism:   xml.entry.organism.name.find { it.attributes().type=="scientific" }.toString(),
			comment:    xml.entry.comment.find { it.attributes().type=="function" }.toString())
		
		cache.fillSafeMap(Caches.PROTEINS, proteinId, res)
		
		return res
	}	
	
}