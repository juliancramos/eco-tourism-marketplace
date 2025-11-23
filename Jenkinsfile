pipeline {
    agent any

    stages {
        stage('Ver workspace') {
            steps {
                sh '''
                  echo "Ruta actual:"
                  pwd
                  echo "Contenido del proyecto:"
                  ls -R
                '''
            }
        }
    }
}
