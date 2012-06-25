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
	
	def proteinInfo(proteinId) {
		Protein prot = ups.getProteinInfo(proteinId)
		prot.proteins = psicq.getInteractors(proteinId)
		return prot
	}
}
