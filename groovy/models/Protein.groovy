package models

/**
 *  Just model and that represents the following structure:
 *  
 *  a) Name (Xml Field: entry/name)
 *	b) Full Name (Xml Field: entry/protein/recommendedName/fullName)
 *	c) Short Name (Xml Field: entry/protein/recommendedName/shortName)
 *	d) Organism (Xml Field: entry/organism/name@type="scientific")
 *	e) Comment (Xml Field: entry/comment@type="function")
 *
 * @author den.konakov@gmail.com
 */
class Protein implements Serializable {
	def protein_id
	
	def name
	def full_name
	def short_name
	def organism
	def comment
	
	// The list of the interactions
	def transient proteins = []
}
