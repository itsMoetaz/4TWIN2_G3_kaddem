pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'M2_HOME'
    }

     environment {
            DOCKER_IMAGE = 'souhail210301/4twin2-g3-kaddem:latest'
        }

    stages {
        stage('GIT') {
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

        stage('SonarQube Analysis') {
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

          stage('Build Docker Image') {
              steps {
                  script {
                      sh 'docker build -t souhail210301/4twin2-g3-kaddem:latest .'
                  }
              }
          }

          stage('Push to Docker Hub') {
              steps {
                  script {
                      def imageExists = sh(
                          script: 'curl -s -o /dev/null -w "%{http_code}" -u $DOCKER_USERNAME:$DOCKER_PASSWORD "https://hub.docker.com/v2/repositories/souhail210301/4twin2-g3-kaddem/tags/latest/"',
                          returnStdout: true
                      ).trim()

                      if (imageExists != '200') {
                          withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                              sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                              sh 'docker push souhail210301/4twin2-g3-kaddem:latest'
                          }
                      } else {
                          echo 'Docker image already exists on Docker Hub; skipping push.'
                      }
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

    }
     post {
             success {
                 echo '✅ Pipeline completed successfully!'
                 emailext(
                     subject: "✅ SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                     body: """Hello Souhail,

     🎉 Your Jenkins build succeeded!

     Job: ${env.JOB_NAME}
     Build Number: ${env.BUILD_NUMBER}
     URL: ${env.BUILD_URL}

     Regards,
     Jenkins CI
     """,
                     to: 'souhailabdennebi2@gmail.com'
                 )
             }
             failure {
                 echo '❌ Pipeline failed.'
                 emailext(
                     subject: "❌ FAILURE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                     body: """Hello Souha,

     😢 Your Jenkins build failed.

     Job: ${env.JOB_NAME}
     Build Number: ${env.BUILD_NUMBER}
     URL: ${env.BUILD_URL}

     Please check the logs for more details.

     Regards,
     Jenkins CI
     """,
                     to: 'souhailabdennebi2@gmail.com'
                 )
             }
         }
