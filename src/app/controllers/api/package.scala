package controllers

import data.models.{DataSource, Visualization, VisualizationEagerBox}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import services.data.rdf.LocalizedValue
import services.data.rdf.sparql.datacube._

package object api {

  implicit val localizedLiteralWrites = Json.writes[LocalizedValue]
  implicit val dataCubeDatasetWrites = Json.writes[DataCubeDataset]
  implicit val dataCubeDimensionPropertyWrites = Json.writes[DataCubeDimensionProperty]
  implicit val dataCubeMeasurePropertyWrites = Json.writes[DataCubeMeasureProperty]
  implicit val dataCubeAttributePropertyWrites = Json.writes[DataCubeAttributeProperty]
  implicit val dataCubeComponentWrites = Json.writes[DataCubeComponent]
  implicit val dataCubeDataStructureWrites = Json.writes[DataCubeDataStructure]
  implicit val dataCubeComponentValueWrites = Json.writes[DataCubeComponentValue]
  implicit val dataCubeKeyWrites = Json.writes[DataCubeKey]
  implicit val dataCubeCellWrites = Json.writes[DataCubeCell]
  implicit val dataCubeWrites = Json.writes[services.data.rdf.sparql.datacube.DataCube]
  implicit val dataCubeQueryResultWrites = Json.writes[DataCubeQueryResult]

  implicit val visualizationWrites = Json.writes[Visualization]
  implicit val dataSourceWrites = Json.writes[DataSource]

  implicit val visualizationEagerBoxWrites: Writes[VisualizationEagerBox] = Writes {
    visualizationEagerBox =>
      JsObject(
        Seq(
          ("visualization", Json.toJson(visualizationEagerBox.visualization)),
          ("datasource", Json.toJson(visualizationEagerBox.dataSource)),
          ("dsdDatasource", Json.toJson(visualizationEagerBox.dsdDataSource))
        )
      )
  }


  implicit val cubeQueryValueFilter: Reads[DataCubeQueryValueFilter] = (
    (JsPath \ "label").readNullable[String] and
      (JsPath \ "dataType").readNullable[String] and
      (JsPath \ "uri").readNullable[String] and
      (JsPath \ "isActive").readNullable[Boolean]
    )(DataCubeQueryValueFilter.apply _)
  implicit val cubeQueryComponentFilter: Reads[DataCubeQueryComponentFilter] = (
    (JsPath \ "componentUri").read[String] and
      (JsPath \ "type").read[String] and
      (JsPath \ "values").read[Seq[DataCubeQueryValueFilter]] and
      (JsPath \ "isActive").readNullable[Boolean]
    )(DataCubeQueryComponentFilter.apply _)
  implicit val cubeQueryFiltersReads: Reads[DataCubeQueryFilter] = (
    (JsPath \ "dsdUri").readNullable[String] and
      (JsPath \ "components").read[Seq[DataCubeQueryComponentFilter]]
    )(DataCubeQueryFilter.apply _)
  implicit val cubeQueryReads: Reads[DataCubeQueryData] = (JsPath \ "filters").read[DataCubeQueryFilter].map(DataCubeQueryData)

  def jsonCacheKey(id: Long, token: String) = "/visualizations/" + id + "/" + token
}
