pipeline {
    agent any
   environment {
        JAVA_HOME = tool name: 'JAVA_HOME', type: 'jdk'
        M2_HOME = tool name: 'M2_HOME', type: 'maven'
        PATH = "${JAVA_HOME}/bin:${M2_HOME}/bin:${PATH}"
        NEXUS_REPO_URL = "http://127.0.0.1:8081/repository/maven-releases/"
        MAVEN_SETTINGS = "/usr/share/maven/conf/settings.xml"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')

        DOCKER_IMAGE_NAME = 'malekswissi11/malekswissi4twin2'
        DOCKER_IMAGE_TAG = "${BUILD_NUMBER}"
    }
    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'SwissiMalek-4TWIN2-G3', url: 'https://github.com/itsMoetaz/4TWIN2_G3_kaddem.git' // Replace with your repository URL and branch
            }
        }
        stage('Java Version') {
            steps {
                sh 'java -version' // Verify Java setup
            }
        }
        stage('MAVEN') {
            steps {
                sh 'mvn --version' // Verify Maven setup
            }
        }
        stage('Compile') {
            steps {
                sh 'mvn clean compile' // Compile the project
            }
        }
   /*   stage('Test') {
            steps {
                sh 'mvn test' // Run tests
            }
        }*/
        stage('Install') {
            steps {
                sh 'mvn install' // Install the project
            }
        }


/*stage('Deploy to Nexus') {
    steps {
        script {
            withCredentials([usernamePassword(credentialsId: 'nexus-credentials-id', 
                                              usernameVariable: 'NEXUS_USERNAME', 
                                              passwordVariable: 'NEXUS_PASSWORD')]) {
                try {
                    sh """
                        mvn deploy \
                            --settings ${MAVEN_SETTINGS} \
                            -DskipTests \
                            -Dnexus.username=$NEXUS_USERNAME \
                            -Dnexus.password=$NEXUS_PASSWORD
                    """
                } catch (Exception e) {
                    echo "Deployment to Nexus failed: ${e.message}"
                    throw e
                }
            }
        }
    }
}*/

        stage('Docker Login') {
            steps {
                sh 'echo "$DOCKERHUB_CREDENTIALS_PSW" | docker login -u "$DOCKERHUB_CREDENTIALS_USR" --password-stdin'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."
                sh "docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ${DOCKER_IMAGE_NAME}:latest"
            }
        }

            stage('Push Docker Image to DockerHub') {
            steps {
                sh "docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                sh "docker push ${DOCKER_IMAGE_NAME}:latest"
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                script {
                    sh """
                    sed -i 's|image: saifmed/saifmeddeb4twin2:.*|image: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}|' docker-compose.yml
                    """

                    sh 'docker-compose down'
                    sh 'docker-compose up -d'
                }
            }
        }

        stage('Cleanup') {
            steps {
                sh 'docker-compose down'
                sh 'docker system prune -f'
            }
        }
    


        
        
    }
}
