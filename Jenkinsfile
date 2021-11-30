pipeline {
    environment {
        DOCKER_HUB_REGISTRY = "nguyencaominhnhut/fashion-be"
        DOCKER_HUB_CREDENTIALS = 'dockerhub_id'
    }

    agent any

    stages {

        stage("Build") {
            steps {
                sh '/usr/local/bin/docker-compose build'
                sh '/usr/local/bin/docker-compose up -d'
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