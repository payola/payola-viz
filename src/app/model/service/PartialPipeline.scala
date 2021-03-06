package model.service

import model.entity.ComponentInstance

case class PortMapping(sourceComponentInstance: ComponentInstance, targetComponentInstance: ComponentInstance, viaPortUri: String) {
  override def toString : String = sourceComponentInstance.toString + "<-" + targetComponentInstance.toString
}

case class PartialPipeline(componentInstances: Seq[ComponentInstance], portMappings: Seq[PortMapping], used : Boolean = false) {
  override def toString: String =
    """
      components: """ + componentInstances.toString() +
      """
      portMappings: """ + portMappings.toString() + """
      used: """ + used.toString

}
