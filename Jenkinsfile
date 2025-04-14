pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'JendoubiMalek-4TWIN2-G3',
                    url: 'https://github.com/itsMoetaz/4TWIN2_G3_kaddem.git'
            }
        }

        stage('Compile Stage') {
            steps {
                sh 'mvn clean compile'
            }
        }


        stage('Build Docker Image') {
                    steps {
                        script {
                            // Construire l'image Docker
                            sh """
                            docker build -t malekjendoubi/kaddem:1.0.0 .
                            """
                        }
                    }
                }
         stage('Push Docker Image') {
                    steps {
                        script {
                            withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                                sh """
                                docker login -u ${DOCKER_USER} -p ${DOCKER_PASS}
                                docker push malekjendoubi/kaddem:1.0.0
                                """
                            }
                        }
                    }
                }
    }
}
