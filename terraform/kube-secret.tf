resource "kubernetes_secret" "graphql" {
  metadata {
    name = "graphql"
    namespace = kubernetes_namespace.graphql.metadata[0].name
  }

  data = {
    AUTH0_CLIENT_ID = var.auth0_client_id
    AUTH0_CLIENT_SECRET = var.auth0_client_secret
    SPRING_DATASOURCE_URL = var.spring_datasource_url
    SPRING_DATASOURCE_USERNAME = var.spring_datasource_username
    SPRING_DATASOURCE_PASSWORD = var.spring_datasource_password
    GRAPHQL_S3_ACCESS_KEY = var.graphql_s3_access_key
    GRAPHQL_S3_SECRET_KEY = var.graphql_s3_secret_key
    GRAPHQL_S3_ENDPOINT = var.graphql_s3_endpoint
    GRAPHQL_S3_BUCKET = var.graphql_s3_bucket
  }
}
