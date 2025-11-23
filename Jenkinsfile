pipeline {
    agent any

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
                sh '''
                    echo "Iniciando build con Maven..."
                    /usr/local/bin/mvn clean package -DskipTests
                '''
            }
        }

        stage('Docker Compose Up') {
            steps {
                sh '''
                    echo "Deteniendo contenedores previos (por si ya existen)..."
                    /usr/local/bin/docker-compose down || true

                    echo "Construyendo y levantando servicios..."
                    /usr/local/bin/docker-compose up -d --build
                '''
            }
        }
    }
}
