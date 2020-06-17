resource "kubernetes_ingress" "graphql" {
  metadata {
    name      = "graphql"
    namespace = kubernetes_namespace.graphql.metadata[0].name
    annotations = {
      "kubernetes.io/ingress.class"                = "alb"
      "alb.ingress.kubernetes.io/scheme"           = "internet-facing"
      "alb.ingress.kubernetes.io/target-type"      = "ip"
      "alb.ingress.kubernetes.io/success-codes"    = "200"
      "alb.ingress.kubernetes.io/healthcheck-path" = "/health"
      "alb.ingress.kubernetes.io/certificate-arn"  = var.ssl_cert_arn
      "alb.ingress.kubernetes.io/listen-ports"     = "[{\"HTTPS\":443}]"
    }
  }
  spec {
    rule {
      http {
        path {
          backend {
            service_name = kubernetes_service.graphql.metadata[0].name
            service_port = 8080
          }
        }
      }
    }
  }
}