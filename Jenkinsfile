pipeline {
    environment {
        DOCKER_HUB_REGISTRY = "nguyencaominhnhut/fashion-be"
        DOCKER_HUB_CREDENTIALS = 'dockerhub_id'
    }

    agent any

    stages {

        stage("Build") {
            steps {
                sh """
                    docker-compose build
                    docker-compose up -d
                """
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