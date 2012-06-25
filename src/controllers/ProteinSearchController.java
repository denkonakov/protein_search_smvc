package controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import services.ProteinServices;

/**
 * This is a central part of the project. It is the controller where we
 * read variables and trigger protein search and parsing.
 *
 * @author den.konakov@gmail.com
 */
@Controller
@RequestMapping("/protein")
public class ProteinSearchController {

	@Autowired
	private ProteinServices ps;
	
	/**
	 * Default action for showing search page
	 * 
	 * @return - name of the Velocity template for view
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showResults(Model model, HttpServletRequest request) {
		model.addAttribute("contextPath", request.getContextPath());
		return "showResults";
	}
	
	/**
	 * The main action for searching protein
	 * 
	 * @param proteinId - ProteinID for search
	 * @param model - model for passing protein structure to the page
	 * 
	 * @return - name of the Velocity template for view rendering
	 */
	@RequestMapping(value="/{proteinId}", method = RequestMethod.GET)
	public String searchForProtein(
			@PathVariable String proteinId, 
			Model model,
			HttpServletRequest request) {

		model.addAttribute("contextPath", request.getContextPath());
		model.addAttribute("protein", ps.proteinInfo(proteinId));
		
        return "showResults";
    }
	
}