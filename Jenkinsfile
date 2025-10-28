pipeline {
  agent { label 'manhtt' }



  environment {
    REGISTRY = "10.0.2.244:30500"
    IMAGE_NAME = "spring-demo"
    BRANCH_NAME = "main"
  }

  stages {
    stage('Checkout') {
      steps {
        git branch: "${BRANCH_NAME}", url: 'https://github.com/manhtthe/test_jenkins.git'
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          def shortCommit = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
          def tag = "v${shortCommit}"
          sh """
          docker build -t ${REGISTRY}/${IMAGE_NAME}:${tag} -t ${REGISTRY}/${IMAGE_NAME}:latest .
          """
          env.IMAGE_TAG = tag
        }
      }
    }

    stage('Push Image to Registry') {
      steps {
        sh """
        docker push ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
        docker push ${REGISTRY}/${IMAGE_NAME}:latest
        """
      }
    }

    stage('Deploy to Kubernetes') {
      steps {
        script {
          sh """
          kubectl set image deployment/${IMAGE_NAME} ${IMAGE_NAME}=${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG} -n default --kubeconfig ~/.kube/config
          kubectl rollout status deployment/${IMAGE_NAME} -n default --timeout=180s
          """
        }
      }
    }
  }

  post {
    success {
      echo "Build & Deploy thành công: ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}"
    }
    failure {
      echo "Build thất bại!"
    }
  }
}
