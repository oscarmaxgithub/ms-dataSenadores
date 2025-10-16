pipeline {
    agent any

    environment {
        // Usamos el nombre del servicio de Docker Compose como host
        SONARQUBE_URL = 'http://sonarqube:9000'
        // El nombre de la imagen Docker que vamos a construir
        DOCKER_IMAGE_NAME = 'mi-app-spring-boot'
        // Tag de la imagen, usando el número de build de Jenkins
        DOCKER_IMAGE_TAG = "v${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                // 'checkout scm' usa automáticamente la configuración SCM
                // del Job de Jenkins (URL de GitHub y credenciales).
                // ¡Esto es mucho más limpio y seguro!
                checkout scm
            }
        }

        stage('Build & Code Style') {
            steps {
                script {
                    echo 'Compilando y ejecutando Checkstyle con Maven...'
                    // El plugin de checkstyle se ejecuta en la fase 'verify'
                    sh 'mvn clean verify'
                }
            }
        }

        stage('Code Quality Analysis') {
            steps {
                script {
                    echo "Ejecutando análisis de SonarQube..."
                    // El sonar-scanner debe estar configurado en Jenkins
                    withSonarQubeEnv('SonarQubeLocal') {
                        sh '''
                            mvn sonar:sonar \
                              -Dsonar.host.url=${SONARQUBE_URL} \
                              -Dsonar.projectKey=mi-proyecto-java \
                              -Dsonar.projectName="Mi Proyecto Java"
                        '''
                    }
                }
            }
        }

        // Puedes agregar una etapa de seguridad aquí con Trivy o Snyk
        stage('Security Scan') {
            steps {
                script {
                    echo "Ejecutando scan de vulnerabilidades con Trivy..."
                    // Escanea el proyecto en busca de vulnerabilidades en las dependencias
                    sh 'docker run --rm -v $(pwd):/app aquasec/trivy fs /app'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Construyendo imagen Docker: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                    // Construye la imagen usando el Dockerfile del repositorio
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."

                    // Importante para Kubernetes local:
                    // Si tu cluster de K8s (Docker Desktop) no ve la imagen,
                    // es posible que necesites cargarla en el registro del cluster.
                    // Para Minikube sería: minikube image load ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}
                    // Para Docker Desktop, usualmente no es necesario si usan el mismo daemon.
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo "Desplegando en Kubernetes..."
                    // Primero, aplicamos los secretos de forma segura
                    sh 'kubectl apply -f k8s/secret.yml'

                    // Luego, actualizamos la imagen del deployment y aplicamos
                    // Usamos 'sed' para reemplazar el placeholder del tag en el manifiesto
                    sh "sed -i.bak 's|image: .*|image: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}|g' k8s/deployment.yml"
                    sh 'kubectl apply -f k8s/deployment.yml'
                    sh 'kubectl apply -f k8s/service.yml'

                    echo "Despliegue completado."
                }
            }
        }
    }

    post {
        always {
            echo 'Limpiando el workspace...'
            cleanWs()
        }
    }
}
