resource "kubernetes_service" "graphql" {
  metadata {
    name      = "graphql"
    namespace = kubernetes_namespace.graphql.metadata[0].name
  }
  spec {
    selector = {
      App = kubernetes_deployment.graphql.spec.0.template.0.metadata[0].labels.App
    }
    port {
      port = 8080
    }
    type = "ClusterIP"
  }
}