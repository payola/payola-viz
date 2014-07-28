package controllers.api

import data.models._

import play.api._
import play.api.libs.json.{JsPath, Writes, Json}
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.functional.syntax._


import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._


import scaldi.{Injectable, Injector}
import services.MD5
import services.data.rdf.{Node, LocalizedValue}
import services.data.rdf.sparql.datacube._


class DataCube(implicit inj: Injector) extends Controller with Injectable {

  val dataCubeService = inject[DataCubeService]

  def dataStructures(id: Long) = DBAction { implicit rs =>
    _withVisualizationAndDataSource(id) { (v, d) =>
      Ok(Json.toJson(dataCubeService.getDataStructures(d)))
    }
  }

  def values(id: Long) = DBAction(parse.json) { implicit rs =>

    val json: JsValue = rs.request.body
    val uris = json \ "uris"

    _withVisualizationAndDataSource(id) { (v, d) =>
      Ok(Json.toJson(dataCubeService.getValues(d, uris.as[List[String]])))
    }
  }

  def queryCube(id: Long) = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[DataCubeQueryData] match {
      case s: JsSuccess[DataCubeQueryData] => {

        val queryData: DataCubeQueryData = s.get
        _withVisualizationAndDataSource(id) { (v, d) =>
          val result = dataCubeService.processCubeQuery(d, queryData)
          Ok(Json.toJson(result))
        }

      }
      case e: JsError => {
        println(e)
        error("Query not valid")
      }
    }
  }

  private def _withVisualizationAndDataSource(id: Long)
                                             (func: (Visualization, DataSource) => Result)
                                             (implicit rs: play.api.db.slick.Config.driver.simple.Session): Result = {

    Visualizations.findByIdWithDataSource(id).map { case (visualization, datasource) =>
      func(visualization, datasource)
    }.getOrElse {
      NotFound
    }
  }

  implicit val localizedLiteralWrites = Json.writes[LocalizedValue]
  implicit val dataCubeDatasetWrites = Json.writes[DataCubeDataset]
  implicit val dataCubeDimensionPropertyWrites = Json.writes[DataCubeDimensionProperty]
  implicit val dataCubeMeasurePropertyWrites = Json.writes[DataCubeMeasureProperty]
  implicit val dataCubeAttributePropertyWrites = Json.writes[DataCubeAttributeProperty]
  implicit val dataCubeComponentWrites = Json.writes[DataCubeComponent]
  implicit val dataCubeDataStructureWrites = Json.writes[DataCubeDataStructure]
  implicit val dataCubeComponentValueWrites = Json.writes[DataCubeComponentValue]
  implicit val dataCubeQueryResultWrites = Json.writes[DataCubeQueryResult]


  implicit val cubeQueryValueFilter: Reads[DataCubeQueryValueFilter] = (
    (JsPath \ "label").readNullable[String] and
      (JsPath \ "uri").readNullable[String] and
      (JsPath \ "isActive").readNullable[Boolean]
    )(DataCubeQueryValueFilter.apply _)

  implicit val cubeQueryComponentFilter: Reads[DataCubeQueryComponentFilter] = (
    (JsPath \ "componentUri").read[String] and
      (JsPath \ "type").read[String] and
      (JsPath \ "values").read[Seq[DataCubeQueryValueFilter]]
    )(DataCubeQueryComponentFilter.apply _)

  implicit val cubeQueryFiltersReads: Reads[DataCubeQueryFilter] = (
    (JsPath \ "dsdUri").read[String] and
      (JsPath \ "components").read[Seq[DataCubeQueryComponentFilter]]
    )(DataCubeQueryFilter.apply _)

  implicit val cubeQueryReads: Reads[DataCubeQueryData] = (JsPath \ "filters").read[DataCubeQueryFilter].map(DataCubeQueryData(_))

  def datasets(id: Long) = DBAction { implicit rs =>
    _withVisualizationAndDataSource(id) { (v, d) =>
      Ok(Json.toJson(dataCubeService.getDatasets(d)))
    }
  }

}