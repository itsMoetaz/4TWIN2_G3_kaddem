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
  stage('Deploy to Nexus') {
            steps {
                script {
                    sh """
                    mvn deploy -DaltDeploymentRepository=nexus::default::http://192.168.56.10:8081/repository/maven-releases/
                    """
                }
            }
        }
       

     

     

       
    }
}
