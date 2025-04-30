pipeline {
    agent any
    
    tools {
        maven 'Maven-3.8.6'
    }
    
    environment {
        NEXUS_URL = "nexus:8082"
        IMAGE_NAME = "product-app"
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        NEXUS_CREDENTIALS = credentials('nexus-credentials')
    }
    
    stages {
        stage('Checkout') {
    steps {
        git branch: 'master', url: 'https://github.com/MSBHAILA/product-app'
    }
}
        
        stage('Build Application') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Build and Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', passwordVariable: 'NEXUS_PASSWORD', usernameVariable: 'NEXUS_USERNAME')]) {
                    sh '''
                        docker login -u ${NEXUS_USERNAME} -p ${NEXUS_PASSWORD} http://${NEXUS_URL}
                        docker build -t ${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG} .
                        docker push ${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG}
                        docker tag ${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG} ${NEXUS_URL}/${IMAGE_NAME}:latest
                        docker push ${NEXUS_URL}/${IMAGE_NAME}:latest
                    '''
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
