pipeline {
    agent any

    environment {
        JAVA_HOME = tool name: 'JAVA_HOME', type: 'jdk'
        M2_HOME = tool name: 'M2_HOME', type: 'maven'
        PATH = "${JAVA_HOME}/bin:${M2_HOME}/bin:${PATH}"
        IMAGE_NAME = "itzmoetaz/4twin2-g3-kaddem"
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'BenKhedherMoetaz-4TWIN2-G3', url: 'https://github.com/itsMoetaz/4TWIN2_G3_kaddem.git'
            }
        }

        stage('Compile Stage') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test Stage') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Maven Install') {
            steps {
                sh 'mvn install'
            }
        }

        stage('MVN SONARQUBE') {
            steps {
                withSonarQubeEnv('sq') {
                    sh '''
                        mvn clean compile
                        mvn sonar:sonar -DskipTests -Dsonar.java.binaries=target/classes
                    '''
                }
            }
        }

        stage('Nexus Deployment') {
            steps {
                script {
                    def artifactExists = sh(
                        script: '''
                            curl -s -o /dev/null -w "%{http_code}" -u admin:admin "http://192.167.33.10:8081/repository/maven-public/tn/esprit/spring/4TWIN2-G3-kaddem/1.0/4TWIN2-G3-kaddem-1.0.jar"
                        ''',
                        returnStdout: true
                    ).trim()

                    if (artifactExists != '200') {
                        echo 'Deploying to Nexus...'
                        sh 'mvn deploy -Dmaven.test.skip=true'
                    } else {
                        echo 'Artifact already exists on Nexus; skipping deployment. '
                    }
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    sh "docker build -t ${IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $IMAGE_NAME:latest
                    '''
                }
            }
        }

        stage('Docker Compose Up') {
            steps {
                script {
                    echo '🛠️ Stopping any existing containers...'
                    sh 'docker compose down || true'

                    echo '🚀 Building and starting containers with Docker Compose...'
                    sh 'docker compose up -d --build'
                }
            }
        }
    }

            post {
                success {
                    mail to: 'moetaz.khedher2001@gmail.com',
                        subject: "✅ Jenkins SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                        body: """Good news! The build was successful.
                                Project: ${env.JOB_NAME}
                                Build #: ${env.BUILD_NUMBER}
                                View Build: ${env.BUILD_URL}"""
                }
                failure {
                    mail to: 'moetaz.khedher2001@gmail.com',
                        subject: "❌ Jenkins FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                        body: """Project: ${env.JOB_NAME}
                                Build #: ${env.BUILD_NUMBER}
                                Status: FAILED
                                View Build: ${env.BUILD_URL}"""
                }
            }
}
