pipeline {
    agent any

    tools {
        // Use the configured Maven version named "Maven" in Jenkins
        maven "Maven"
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/vdnnguyen94/COMP367Lab2MavenWebApp.git'
            }
        }

        stage('Build') {
            steps {
				// Run Maven on a Unix agent.
                //sh "mvn -Dmaven.test.failure.ignore=true clean package"
                
                // Run Maven build on Windows
                bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
    }

    post {
        // If the build is successful, archive the artifacts and test results
        success {
            junit '**/target/surefire-reports/TEST-*.xml'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }

        // If the build fails, mark it as unstable
        failure {
            echo "Build failed! Please check the logs."
        }
    }
}
