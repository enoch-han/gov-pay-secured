terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 2.10.12"
    }
  }
}

provider "docker" {}

resource "docker_image" "database" {
  name         = "postgres:13.4"
  keep_locally = true
}

resource "docker_container" "database" {
  image = docker_image.database.postgres
  name  = "database_container"
  ports {
    internal = 5432
    external = 5432
  }
}

resource "docker_image" "backend" {
  name = ""
  keep_locally = true
}

resource "docker_container" "backend" {
  image = docker_image.backend
  name = "backend_container"
  ports {
      internal = 8080
      external = 8080
  }
}

resource "docker_image" "frontend" {
    name = ""
    keep_locally = true 
}

resource "docker_container" "frontend" {
  image = docker_image.frontend
  name = "frontend_container"
  ports {
      internal = 8080
      external = 9000
  }
}