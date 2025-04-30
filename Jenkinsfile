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
                checkout scm
            }
        }
        
        stage('Build Application') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', passwordVariable: 'NEXUS_PASSWORD', usernameVariable: 'NEXUS_USERNAME')]) {
                    sh '''
                        sudo docker login -u ${NEXUS_USERNAME} -p ${NEXUS_PASSWORD} http://${NEXUS_URL}
                        sudo docker build -t ${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG} .
                        sudo docker push ${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG}
                        sudo docker tag ${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG} ${NEXUS_URL}/${IMAGE_NAME}:latest
                        sudo docker push ${NEXUS_URL}/${IMAGE_NAME}:latest
                    '''
                }
            }
    }
    
    post {
        success {
            echo "Build and push successful! Image available at ${NEXUS_URL}/${IMAGE_NAME}:${IMAGE_TAG}"
        }
    }
}
