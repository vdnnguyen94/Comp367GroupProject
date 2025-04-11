pipeline {
    agent any

    tools {
        maven "Maven"
    }

    environment {
        // Custom Project Variables
        STUDENT1_NAME = 'PRO VANNGUYEN'
        STUDENT1_ID = '301289600'
        STUDENT2_NAME = 'Seyeon Jo'
        STAGE = 'PRODUCTION'
        APP_PORT = '5174'

        // CI/CD and Docker Config
        SONAR_PROJECT_KEY = 'COMP367_Lab2'
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('SQToken')
        DOCKER_CREDENTIALS = 'dockerhubpat'
        DOCKER_IMAGE = 'vdnnguyen94/comp367project'
        DOCKER_TAG = 'latest'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/vdnnguyen94/Comp367GroupProject.git'
            }
        }

        stage('Build') {
            steps {
                bat "mvn -Dmaven.test.failure.ignore=true clean package"
            }
        }

        stage('Test & Coverage') {
            steps {
                bat 'mvn clean package jacoco:report'
            }
        }
        stage('Archive JaCoCo Report') {
            steps {
                archiveArtifacts artifacts: 'target/site/jacoco/**', fingerprint: true
            }
        }


        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('MySonarQubeServer') {
                    bat "mvn sonar:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY} -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_TOKEN}"
                }
            }
        }
        stage('Create .env for Docker & Archive') {
            steps {
                bat """
                echo APP_PORT=${APP_PORT} > .env
                echo STAGE=${STAGE} >> .env
                echo STUDENT1_NAME=${STUDENT1_NAME} >> .env
                echo STUDENT1_ID=${STUDENT1_ID} >> .env
                echo STUDENT2_NAME=${STUDENT2_NAME} >> .env
                """
            }
        }
        stage('Deliver Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/EnterpriseApp.jar', fingerprint: true
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        bat "echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin"
                    }
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat '''
                    echo === Docker Build Args ===
                    echo STUDENT1_NAME=%STUDENT1_NAME%
                    echo STUDENT2_NAME=%STUDENT2_NAME%
                    echo STAGE=%STAGE%
                    echo APP_PORT=%APP_PORT%

                    docker build -t %DOCKER_IMAGE%:%DOCKER_TAG% ^
                    --build-arg STUDENT1_NAME="%STUDENT1_NAME%" ^
                    --build-arg STUDENT1_ID="%STUDENT1_ID%" ^
                    --build-arg STUDENT2_NAME="%STUDENT2_NAME%" ^
                    --build-arg STAGE="%STAGE%" ^
                    --build-arg APP_PORT="%APP_PORT%" .
                '''
            }
        }

        stage('Docker Push') {
            steps {
                bat "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
            }
        }

        stage('Deploy to Production') {
            steps {
                echo "Deploying to ${STAGE}"
                echo "Name: ${STUDENT1_NAME}"
                echo "Student ID: ${STUDENT1_ID}"
                echo "Student 2: ${STUDENT2_NAME}"
                echo "App will be available at http://localhost:${APP_PORT}"
            }
        }
    }

    post {
        always {
            bat "docker logout"
        }
        success {
            echo "CI/CD completed for ${STUDENT1_NAME} and ${STUDENT2_NAME}"
        }
        failure {
            echo "Build failed! Check logs."
        }
    }
}
