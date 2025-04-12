pipeline {
    agent any

    environment {
        JAVA_HOME = tool name: 'jdk8', type: 'jdk'
        M2_HOME = tool name: 'Maven 3', type: 'maven'
        PATH = "${JAVA_HOME}/bin:${M2_HOME}/bin:${PATH}"
        SONAR_HOST_URL = "http://192.167.33.10:9000"
    }

    stages {
        stage('Checkout Backend Code') {
            steps {
                dir('backend') {
                    git branch: 'AbdennebiSouhail-4TWIN2-G3',
                        url: 'https://github.com/itsMoetaz/DevopsFinal.git'
                }
            }
        }

        stage('Clean, Build and Test') {
            steps {
                dir('backend') {
                    echo '🔧 Cleaning and testing project with Maven'
                    sh 'mvn clean test'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool 'sonar'
                    withSonarQubeEnv('sonar') {
                        dir('backend') {
                            sh """
                                ${scannerHome}/bin/sonar-scanner \
                                -Dsonar.projectKey=kaddem \
                                -Dsonar.projectName='Kaddem' \
                                -Dsonar.sources=src/main \
                                -Dsonar.tests=src/test \
                                -Dsonar.java.binaries=target/classes \
                                -Dsonar.scm.provider=git
                            """
                        }
                    }
                }
            }
        }
    }
}
