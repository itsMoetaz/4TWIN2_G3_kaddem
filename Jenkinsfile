
pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }



             stages {
              stages {
                    stage('GIT') {
                        steps {
                            git branch: 'AbdennebiSouhail-4TWIN2-G3', url: 'https://github.com/itsMoetaz/4TWIN2_G3_kaddem.git'
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

        stage('Nexus Deployment') {
                    steps {
                        script {
                            def artifactExists = sh(
                                script: '''
                                    curl -s -o /dev/null -w "%{http_code}" -u admin:admin "http://192.167.33.10:8081/repository/maven-public/tn/esprit/spring/kaddem/0.0.1-SNAPSHOT/kaddem-0.0.1-20250413.001931-1.jar"
                                ''',
                                returnStdout: true
                            ).trim()

                            if (artifactExists != '200') {
                                echo 'Artifact not found. Deploying to Nexus...'
                                sh 'mvn deploy -Dmaven.test.skip=true'
                            } else {
                                echo 'Artifact already exists on Nexus; skipping deployment.'
                            }
                        }
                    }
                }
        }



}
