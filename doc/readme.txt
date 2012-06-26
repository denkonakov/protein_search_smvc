MOTIVATION
==========

This document is intended to provide short overview for the "protein_serach_smvc" project. 
In addition, it proves in some way that as a developer I do care about documentation.

PROJECT DESCRIPTION
===================

The project provides users with the ability to search through UniProt open DB 
(http://www.uniprot.org/faq/28) protein description/attributes. It is basically one page
project, which shows results of that search on one HTML page, and joins additional 
information about other proteins which it can interact with. For the latter information 
it uses PSICQUIC DB (http://code.google.com/p/psicquic/)

Also I personally would like to use it as a proof of concept for the web development
project on JEE platform, using Gradle/Groovy/Spring MVC technologies all together.

FUNCTIONAL SPECIFICATION
========================

The project should give users the possibility to user for search an information about protein by its ID.
The typical ID -> Q99728.

The project should present a very basic web form with one field and search button for users.
JS validation of user input is not mandatory.

The project uses UniProt WS to get the following fields for proteins:
	
	a) Name (Xml Field: entry/name)
	b) Full Name (Xml Field: entry/protein/recommendedName/fullName)
	c) Short Name (Xml Field: entry/protein/recommendedName/shortName)
	d) Organism (Xml Field: entry/organism/name@type="scientific")
	e) Comment (Xml Field: entry/comment@type="function")
	
The WS is REST/XML based and has the following structure: http://www.uniprot.org/uniprot/{proteinID}.xml
For example: http://www.uniprot.org/uniprot/Q99728.xml. If there is no data - show label {No Data Available}

Project should obtain the list of interactors too, using the following PSICQUIC WS template: 
http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/current/search/query/id:{proteinID}

Using that tab separated file we should provide the table of the interactors with proteinID and their name.

	| ProteinID | Name | Full Name |
	
The project should use only those fields which start from "uniprotkb:" and ignore other ones.	
The project should allow us to search protein information for interactors by clicking on their proteinID in the table.

ARCHITECTURE
============

For this project I decided to use the following key technologies:

1) Spring MVC - after my experience with Grails I decided to look again at Spring MVC and see
how it compares with Grails. Probably later I will write tests for performance comparison.

2) Groovy - I personally believe that Syntax Sugar of Groovy is a great help in development. Not to 
mention its integration with XmlSlurper/Regexp for this project.

3) Gradle - as far as I leart it within this project it is a very nice and convenient tool for
Dependencies Manager and semi-automatic JAR/WAR build. 

4) Hazelcast - fast and reliable data cache.

5) Velocity - as a view technology.

The biggest concern for me at this moment is the performance. Both web-services are quite slow in comparison with
DB access. So, I decided to use a distributed map as a cache for this project.

The overall data flow looks like this:

1) Server gets a request with protein ID and finds appropriate Controller by URL mapping.

2) Controller uses an injected Groovy-based service to get protein data.

3) Service looks at Hazelcast, if there is NO valid data in Hazelcast - run 'fetch' processes for the protein WS.

	3a) Fetch process gets data, parses them and stores the results in Hazelcast.

4) Data are returned to the Controller. The latter one will fill Model and render View.

ASSUMPTIONS
===========

<max-idle-seconds>3600</max-idle-seconds> - the cache is valid for one hour

TESTING
=======

There is no proper testing for the project so far except a couple of Unit Tests I used for development.
Probably later, when I have time to write Grails based version for this project...

TODO
====

For the future it would be nice to have:

1) Statistics for amount of 'hit' protein ID (stored in DB). By using these statistics we could preload some
frequently used proteins to the cache.

2) We could (and SHOULD!) run two tasks for getting data in parallel synchronising them through AtomicRefs on Protein.

3) A short UML diagram for visual architecture explanation and data flow.

4) To make validation of user input (the one based on JS would be OK)

5) To ensure the project awareness of different Environments (dev, staging, prod)

6) Proper exception handling (do not show the exception, but a user-friendly message only)

25/06/2012 (c) den.konakov@gmail.com