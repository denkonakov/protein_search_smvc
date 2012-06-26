package services

import models.Protein

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


/**
 * The final point of Protein class compilation.
 * But honestly, we could live without it. Just here for the 
 * 'future' possible expansion.
 * 
 * @author den.konakov@gmail.com
 */
@Component
class ProteinServices {
	
	@Autowired
	private UniProtService ups
	
	@Autowired
	private PSICQUICService psicq
	
	/**
	 * TODO: This is the perfect place to split two data lookups for different threads.
	 * 
	 * @param proteinId - protein ID for lookup
	 * 
	 * @return - filled Protein model. With list of interactors
	 */
	def proteinInfo(proteinId) {
		Protein prot = ups.getProteinInfo(proteinId)
		prot.proteins = psicq.getInteractors(proteinId)
		return prot
	}
}