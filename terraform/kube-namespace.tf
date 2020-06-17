resource "kubernetes_namespace" "graphql" {
  metadata {
    name = "graphql"
  }
}