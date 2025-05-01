pipeline {
    agent any
    
    tools {
        maven 'Maven-3.8.6'
    }
    
    environment {
        NEXUS_URL = "172.20.0.3:8082"
        IMAGE_NAME = "product-app"
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        NEXUS_CREDENTIALS = credentials('nexus-credentials')
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build Application') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Build and Push Docker Image') {
            steps {
                script {
                    docker.withServer('tcp://host.docker.internal:2375') {
                        def customImage = docker.build("${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG}")
                        docker.withRegistry("http://${NEXUS_URL}", 'nexus-credentials') {
                            customImage.push()
                            echo "Pushed image: ${customImage.imageName()}"
                        }
                    }
                }
            }
        }
    }
    
    post {
        success {
            echo "Build and push successful! Image available at ${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG}"
        }
    }
}
