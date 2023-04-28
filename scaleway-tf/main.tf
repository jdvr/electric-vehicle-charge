terraform {
  required_providers {
    scaleway = {
      source = "scaleway/scaleway"
      version = ">= 2.8.0"
    }
  }
  required_version = ">= 1.4.0"
}

provider "scaleway" {
  zone   = "fr-par-1"
  region = "fr-par"
  access_key = var.scw_access_key
  secret_key = var.scw_secret_key
}


resource "scaleway_container_namespace" "main" {
  project_id  = var.scw_project_id
  region      = "fr-par"
  name        = "ecc-namespace"
}

resource "scaleway_container" "ecc_container" {
  name           = "ecc-container"
  description    = "Container to run the app"
  namespace_id   = scaleway_container_namespace.main.id
  registry_image = "TODO"
  port           = 8080
  min_scale      = 1
  max_scale      = 1
  privacy        = "private"
  deploy         = true

  timeouts {
    create = "15m"
    update = "15m"
  }
}

output "domain" {
  value = scaleway_container.ecc_container.domain_name
}
