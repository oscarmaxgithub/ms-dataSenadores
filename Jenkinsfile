pipeline {
    agent any

    environment {
        // Nombre de la imagen en DockerHub
        IMAGE_NAME = "oscarmax/ingestion-service"
        // Credenciales configuradas en Jenkins
        DOCKER_CREDS = credentials('docker-hub-creds')
        // Llave del proyecto definida en SonarQube
        SONAR_PROJECT_KEY = "microservicio_spring_boot"
    }

    tools {
        // Nombres tal cual los configuraste en "Global Tool Configuration"
        maven 'Maven-Local'
        jdk 'Java 17'
    }

    stages {
        stage('Checkout') {
            steps {
                // Descarga el código del repositorio que disparó el webhook
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // MEJOR PRÁCTICA: Inyección segura de credenciales.
                // 'postgres-db-creds' es el ID que creaste en Jenkins -> Credentials.
                withCredentials([usernamePassword(credentialsId: 'postgres-db-creds', usernameVariable: 'DB_USER', passwordVariable: 'DB_PASS')]) {
                    // Usamos comillas dobles "" para que Groovy interpole las variables ${DB_USER}
                    // Jenkins ocultará automáticamente los valores reales en los logs poniendo ****
                    sh """
                        mvn clean package -DskipTests=false \
                        -Dspring.datasource.url=jdbc:postgresql://nombre-contenedor-postgres:5432/nombre_bd \
                        -Dspring.datasource.username=${DB_USER} \
                        -Dspring.datasource.password=${DB_PASS}
                    """
                }
            }
        }

        stage('Análisis de Calidad (SonarQube)') {
            steps {
                script {
                    // Ahora sí encontrará 'target/classes' porque compilamos en el paso anterior
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
                timeout(time: 6, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

//         stage('Análisis de Seguridad (OWASP)') {
//             steps {
//                 // Busca vulnerabilidades en las librerías
//                 dependencyCheck additionalArguments: '--format HTML --out dependency-check-report.html', odcInstallation: 'dependency-check'
//             }
//             post {
//                 always {
//                     publishHTML (target: [
//                         allowMissing: false,
//                         alwaysLinkToLastBuild: false,
//                         keepAll: true,
//                         reportDir: '.',
//                         reportFiles: 'dependency-check-report.html',
//                         reportName: 'OWASP Dependency Check'
//                     ])
//                 }
//             }
//         }

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
                    // Actualiza el manifiesto y aplica cambios
                    sh "sed -i 's|IMAGE_PLACEHOLDER|${IMAGE_NAME}:${env.BUILD_NUMBER}|g' k8s/deployment.yml"
                    sh "kubectl apply -f k8s/"
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
