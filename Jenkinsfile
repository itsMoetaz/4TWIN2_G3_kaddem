pipeline {
    agent any

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
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
