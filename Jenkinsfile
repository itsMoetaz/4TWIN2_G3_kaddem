pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

    stages {
        stage('Checkout Backend Code') {
            steps {
                dir('backend') {
                    git branch: 'AbdennebiSouhail-4TWIN2-G3',
                        url: 'https://github.com/itsMoetaz/4TWIN2_G3_kaddem.git'
                }
            }
        }

        stage('Maven Clean Compile') {
            steps {
                dir('backend') {
                    sh 'mvn clean'
                    echo 'Running Maven Compile'
                    sh 'mvn compile'
                }
            }
        }

        stage('Tests - JUnit/Mockito') {
            steps {
                dir('backend') {
                    sh 'mvn test'
                }
            }
        }
        
        stage('Build package') {
            steps {
                sh 'mvn package'
            }
        }
        
        stage('Maven Install') {
            steps {
                sh 'mvn install'
            }
        }

        stage('Backend - SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool 'scanner' // Make sure 'scanner' is configured in Jenkins
                    withSonarQubeEnv('sonar') { // Using your server ID 'sonar'
                        dir('backend') {
                            sh """
                            ${scannerHome}/bin/sonar-scanner \
                            -Dsonar.projectKey=kaddem \
                            -Dsonar.projectName='kaddem' \
                            -Dsonar.sources=src/main \
                            -Dsonar.java.binaries=target/classes \
                            -Dsonar.scm.provider=git
                            """
                        }
                    }
                }
            }
        }

          stage('Deploy on Nexus') {
              steps {
                  sh "mvn deploy -s /var/lib/jenkins/jobs/kaddem/workspace/maven-settings.xml -DskipTests"

              }
          }
    }


}