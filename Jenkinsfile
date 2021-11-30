pipeline {
    environment {
        DOCKER_HUB_REGISTRY = "nguyencaominhnhut/fashion-be"
        DOCKER_HUB_CREDENTIALS = 'dockerhub_id'
    }

    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage("Build") {
            steps {
                sh """
                    chmod +x ./deploy.sh
                    ./deploy.sh
                """
            }
        }
    }
}