package model.rdf.sparql.datacube.query

import model.rdf.sparql.datacube.ObservationPattern
import model.rdf.sparql.query.SparqlQuery


class DataCubeCellQuery(pattern: ObservationPattern) extends SparqlQuery {

  def get: String = prefixes +
    """
      | CONSTRUCT {
      |   """.stripMargin + pattern.getConstructPattern + """
      | } WHERE {
      |   """.stripMargin + pattern.getWherePattern + """
      | }
    """.stripMargin

  private def prefixes =
    """
      | PREFIX qb:       <http://purl.org/linked-data/cube#>
      | PREFIX rdf:      <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
      | PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>
      | PREFIX xsd:      <http://www.w3.org/2001/XMLSchema#>
      | PREFIX dct:      <http://purl.org/dc/terms/>

    """.stripMargin

}
