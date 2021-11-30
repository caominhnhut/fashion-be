pipeline {
    environment {
        DOCKER_HUB_REGISTRY = "nguyencaominhnhut/fashion-be"
        DOCKER_HUB_CREDENTIALS = 'dockerhub_id'
    }

    agent any

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/YourGithubAccount/YourGithubRepository.git'
            }
        }

        stage("Build") {
            steps {
                sh './deploy.sh'
            }
        }
    }
}