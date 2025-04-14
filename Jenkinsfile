pipeline {
    agent any
    environment {
        JAVA_HOME = tool name: 'JAVA_HOME', type: 'jdk'
        M2_HOME = tool name: 'M2_HOME', type: 'maven'
        PATH = "${JAVA_HOME}/bin:${M2_HOME}/bin:${PATH}:/usr/local/bin"
        NEXUS_REPO_URL = "http://192.168.33.10:8081/repository/maven-snapshots/"
        MAVEN_SETTINGS = "/usr/share/maven/conf/settings.xml"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
        DOCKER_IMAGE_NAME = 'malekswissi11/malekswissi4twin2'
        DOCKER_IMAGE_TAG = "${BUILD_NUMBER}"
    }
    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'SwissiMalek-4TWIN2-G3', url: 'https://github.com/itsMoetaz/4TWIN2_G3_kaddem.git'
            }
        }
        stage('Java Version') {
            steps {
                sh 'java -version'
            }
        }
        stage('MAVEN') {
            steps {
                sh 'mvn --version'
            }
        }
        stage('Compile') {
            steps {
                sh 'mvn clean compile || exit 1'
            }
        }
        stage('Install') {
            steps {
                sh 'mvn install'
            }
        }
      /*  stage('Deploy to Nexus') {
            steps {
                script {
                    try {
                        sh '''
                            mvn deploy \
                                --settings ${MAVEN_SETTINGS} \
                                -DskipTests \
                                -DaltDeploymentRepository=nexus-snapshots::default::${NEXUS_REPO_URL}
                        '''
                    } catch (Exception e) {
                        echo "Deployment to Nexus failed: ${e.message}"
                        throw e 
                    }
                }
            }
        }*/
        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                    sh '''
                        echo "$DOCKERHUB_PASSWORD" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin
                    '''
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    // Si vous utilisez l'Option 1 (COPY), cette étape est suffisante
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."

                    // Si vous utilisez l'Option 2 (téléchargement depuis Nexus), ajoutez les identifiants Nexus
                    /*
                    withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        sh "docker build --build-arg NEXUS_USERNAME=${NEXUS_USERNAME} --build-arg NEXUS_PASSWORD=${NEXUS_PASSWORD} -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."
                    }
                    */

                    sh "docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ${DOCKER_IMAGE_NAME}:latest"
                }
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
                        sed -i 's|image: malekswissi11/malekswissi4twin2:.*|image: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}|' docker-compose.yml
                        docker-compose down
                        docker-compose up -d
                    """
                }
            }
        }
        stage('Cleanup') {
            steps {
                sh 'docker compose down || true'
                sh 'docker system prune -f'
            }
        }
    }
    post {
        always {
            sh 'docker logout'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
