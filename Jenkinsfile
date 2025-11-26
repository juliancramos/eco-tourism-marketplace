pipeline {
    agent any

    /*****************************************************
     * Variables de entorno para maven y docker compose.
     *
     * En EC2 estoy usando:
     *     mvn
     *     docker-compose
     *
     * En local usaba:
     *     /usr/local/bin/mvn
     *     /usr/local/bin/docker-compose
     *****************************************************/
    environment {
        MVN_CMD = 'mvn'
        DC_CMD  = 'docker-compose'
    }

    stages {

        stage('Workspace Info') {
            steps {
                sh '''
                    echo "Ruta actual:"
                    pwd
                '''
            }
        }

        stage('Build Maven') {
            steps {
                sh """
                    echo "Iniciando build con Maven..."
                    ${MVN_CMD} clean package -DskipTests
                """
            }
        }

        stage('Docker Compose Up') {
            steps {
                sh """
                    echo "Deteniendo contenedores previos (si existen)..."
                    ${DC_CMD} down || true

                    echo "Construyendo y levantando servicios..."
                    ${DC_CMD} up -d --build
                """
            }
        }
    }

    post {
        success {
            echo "Pipeline ejecutado con éxito."
        }
        failure {
            echo "Error durante el pipeline. Revisar logs."
        }
    }
}
