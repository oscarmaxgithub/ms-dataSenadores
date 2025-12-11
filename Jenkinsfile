pipeline {
    agent any

    environment {
        // Nombre de la imagen en DockerHub
        IMAGE_NAME = "oscarmax/ingestion-service"
        // Credenciales configuradas en Jenkins
        DOCKER_CREDS = credentials('docker-hub-creds')
        // Llave del proyecto definida en SonarQube
        SONAR_PROJECT_KEY = "microservicio-springboot"
    }

    tools {
        // Nombres tal cual los configuraste en "Global Tool Configuration"
        maven 'Maven-Local'
        jdk 'Java-17'
    }

    stages {
        stage('Checkout') {
            steps {
                // Descarga el código del repositorio que disparó el webhook
                checkout scm
            }
        }

        stage('Análisis de Calidad (SonarQube)') {
            steps {
                script {
                    // Escanea el código buscando bugs, code smells y duplicidad
                    def scannerHome = tool 'SonarQubeScanner'
                    withSonarQubeEnv('SonarQube-Server') {
                        sh "${scannerHome}/bin/sonar-scanner \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.java.binaries=target/classes"
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                // Espera a que SonarQube decida si el código aprueba o reprueba
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Análisis de Seguridad (OWASP)') {
            steps {
                // Busca vulnerabilidades en las librerías del pom.xml
                dependencyCheck additionalArguments: '--format HTML --out dependency-check-report.html', odometerAnalysisEnabled: false
            }
            post {
                always {
                    // Publica el reporte en Jenkins para que puedas verlo
                    publishHTML (target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: '.',
                        reportFiles: 'dependency-check-report.html',
                        reportName: 'OWASP Dependency Check'
                    ])
                }
            }
        }

        stage('Build & Test') {
            steps {
                // Compila el JAR
                sh 'mvn clean package -DskipTests=false'
            }
        }

        stage('Construir Imagen Docker') {
            steps {
                script {
                    // Construye la imagen etiquetada con el número de build
                    docker.build("${IMAGE_NAME}:${env.BUILD_NUMBER}")
                    docker.build("${IMAGE_NAME}:latest")
                }
            }
        }

        stage('Push a Docker Hub') {
            steps {
                script {
                    docker.withRegistry('', 'docker-hub-creds') {
                        docker.image("${IMAGE_NAME}:${env.BUILD_NUMBER}").push()
                        docker.image("${IMAGE_NAME}:latest").push()
                    }
                }
            }
        }

        stage('Desplegar en Kubernetes') {
            steps {
                script {
                    // Usamos sed para inyectar la versión correcta de la imagen en el YAML
                    sh "sed -i 's|IMAGE_PLACEHOLDER|${IMAGE_NAME}:${env.BUILD_NUMBER}|g' k8s-deployment.yaml"

                    // Aplicamos la configuración
                    sh "kubectl apply -f k8s-deployment.yaml"
                }
            }
        }
    }

    post {
        success {
            echo '¡Despliegue Exitoso!'
        }
        failure {
            echo 'El pipeline falló. Revisa los logs.'
        }
    }
}
