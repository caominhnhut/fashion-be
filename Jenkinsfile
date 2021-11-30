pipeline {
    agent any

    stages {

        stage("Build") {
            steps {
                sh "docker-compose up -d"
            }
        }
    }

    post {
      always {
          sh "docker-compose down || true"
      }
    }
}