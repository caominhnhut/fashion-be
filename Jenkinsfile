pipeline {
    environment {
        DOCKER_HUB_REGISTRY = "nguyencaominhnhut/fashion-be"
        DOCKER_HUB_CREDENTIALS = 'dockerhub_id'
    }

    agent any

    stages {

        stage("Build") {
            steps {
                script {
                    docker.withRegistry('', env.DOCKER_HUB_CREDENTIALS) {
                        sh """
                             docker-compose -f docker-compose.yml up -d
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                deleteDir()
            }
        }
    }
}