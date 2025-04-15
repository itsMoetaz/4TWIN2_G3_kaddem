pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    environment {
        BACKEND_IMAGE = 'souhail210301/4twin2-g3-kaddem:latest'
        FRONTEND_IMAGE = 'souhail210301/4twin2-g3-kaddem-front:latest'
    }

    stages {
        stage('Checkout Backend') {
            steps {
                dir('backend') {
                    git branch: 'AbdennebiSouhail-4TWIN2-G3',
                        url: 'https://github.com/itsMoetaz/4TWIN2_G3_kaddem.git'
                }
            }
        }

        stage('Checkout Frontend') {
            steps {
                dir('frontend') {
                    git branch: 'main',
                        url: 'https://github.com/itsMoetaz/4TWIN2_G3_kaddem.git'
                }
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool 'scanner'
                    withSonarQubeEnv('sonar') {
                        dir('backend') {
                            sh """
                                ${scannerHome}/bin/sonar-scanner \
                                -Dsonar.projectKey=kaddem \
                                -Dsonar.sources=src/main \
                                -Dsonar.java.binaries=target/classes
                            """
                        }
                    }
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                dir('backend') {
                    sh "mvn deploy -s /var/lib/jenkins/jobs/kaddem/workspace/maven-settings.xml -DskipTests"
                }
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                dir('backend') {
                    sh 'docker build -t $BACKEND_IMAGE .'
                }
            }
        }

        stage('Build Frontend Docker Image') {
            steps {
                dir('frontend') {
                    sh 'docker build -t $FRONTEND_IMAGE .'
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh '''
                        echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin
                        docker push $BACKEND_IMAGE
                        docker push $FRONTEND_IMAGE
                    '''
                }
            }
        }

        stage('Docker Compose Up') {
            steps {
                sh 'docker compose down || true'
                sh 'docker compose up -d --build'
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline completed successfully!'
        }
        failure {
            echo '❌ Pipeline failed!'
        }
    }
}
