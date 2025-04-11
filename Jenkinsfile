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

        stage('Deploy to Dev Environment') {
            steps {
                bat '''
                    echo APP_PORT=5173 > .env
                    echo STUDENT1_NAME="DEV VAN NGUYEN" >> .env
                    echo STUDENT1_ID=301289600 >> .env
                    echo STUDENT2_NAME="Seyeon Jo" >> .env
                    echo STAGE=DEV >> .env

                    docker build -t enterpriseapp-dev .
                    docker stop app-dev || echo No container running
                    docker rm app-dev || echo No container to remove
                    docker run -d -p 5173:5173 --name app-dev enterpriseapp-dev
                '''
            }
        }

        stage('Deploy to QAT Environment') {
            steps {
                bat '''
                    echo APP_PORT=5174 > .env
                    echo STUDENT1_NAME="QAT VAN NGUYEN" >> .env
                    echo STUDENT1_ID=301289600 >> .env
                    echo STUDENT2_NAME="Seyeon Jo" >> .env
                    echo STAGE=QAT >> .env

                    docker build -t enterpriseapp-qat .
                    docker stop app-qat || echo No container running
                    docker rm app-qat || echo No container to remove
                    docker run -d -p 5174:5174 --name app-qat enterpriseapp-qat
                '''
            }
        }

        stage('Deploy to Staging Environment') {
            steps {
                bat '''
                    echo APP_PORT=5175 > .env
                    echo STUDENT1_NAME="STAGING VAN NGUYEN" >> .env
                    echo STUDENT1_ID=301289600 >> .env
                    echo STUDENT2_NAME="Seyeon Jo" >> .env
                    echo STAGE=STAGING >> .env

                    docker build -t enterpriseapp-staging .
                    docker stop app-staging || echo No container running
                    docker rm app-staging || echo No container to remove
                    docker run -d -p 5175:5175 --name app-staging enterpriseapp-staging
                '''
            }
        }

        stage('Deploy to Production Environment') {
            steps {
                bat '''
                    echo APP_PORT=5000 > .env
                    echo STUDENT1_NAME="PRODUCTION VAN NGUYEN" >> .env
                    echo STUDENT1_ID=301289600 >> .env
                    echo STUDENT2_NAME="Seyeon Jo" >> .env
                    echo STAGE=PRODUCTION >> .env

                    docker build -t enterpriseapp-prod .
                    docker stop app-prod || echo No container running
                    docker rm app-prod || echo No container to remove
                    docker run -d -p 5000:5000 --name app-prod enterpriseapp-prod
                '''
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
