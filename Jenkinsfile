
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
                            url: 'https://github.com/itsMoetaz/DevopsFinal.git'
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


        }



}
