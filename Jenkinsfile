pipeline {
    agent { node { label 'fashion-be-ci' } }

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