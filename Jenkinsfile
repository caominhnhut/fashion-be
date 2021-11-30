pipeline {
    agent any

    stages {

        stage("Build") {
            steps {
                checkout scm
                sh """
                pwd
                ls
                docker-compose up -d
                """
            }
        }
    }

    post {
      always {
          sh "docker-compose down || true"
      }
    }
}