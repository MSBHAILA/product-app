pipeline {
    agent any
    
    tools {
        maven 'Maven 3.8.6'
    }
    
    environment {
        NEXUS_URL = "localhost:8082"
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
        
        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG} ."
                sh "docker tag ${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG} ${NEXUS_URL}/${IMAGE_NAME}:latest"
            }
        }
        
        stage('Push to Nexus') {
            steps {
                sh "echo ${NEXUS_CREDENTIALS_PSW} | docker login ${NEXUS_URL} -u ${NEXUS_CREDENTIALS_USR} --password-stdin"
                sh "docker push ${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker push ${NEXUS_URL}/${IMAGE_NAME}:latest"
                sh "docker logout ${NEXUS_URL}"
            }
        }
    }
    
    post {
        success {
            echo "Build and push successful! Image available at ${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG}"
        }
    }
}
