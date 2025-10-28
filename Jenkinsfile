pipeline {
  agent {
    kubernetes {
      label 'docker-agent'
      yaml """
      apiVersion: v1
      kind: Pod
      metadata:
        labels:
          app: jenkins-docker-agent
      spec:
        serviceAccountName: jenkins
        containers:
        - name: docker
          image: docker:24-dind
          securityContext:
            privileged: true
          env:
          - name: DOCKER_TLS_CERTDIR
            value: ""                  # tắt TLS
          args:
          - --host=tcp://0.0.0.0:2375  # chỉ cần args, không cần command
          - --storage-driver=vfs
          - --insecure-registry=10.0.2.244:30500
          ports:
          - containerPort: 2375
            name: docker
        - name: docker-cli
          image: docker:24-cli
          command:
          - cat
          tty: true
          env:
          - name: DOCKER_HOST
            value: tcp://127.0.0.1:2375
          - name: DOCKER_TLS_CERTDIR
            value: ""
          volumeMounts:
          - name: docker-sock
            mountPath: /var/run
        volumes:
        - name: docker-sock
          emptyDir: {}
      """

    }
  }

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
        container('docker-cli') {
          script {
            sh 'git config --global --add safe.directory /home/jenkins/agent/workspace/spring-demo-build'

            def shortCommit = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
            def tag = "v${shortCommit}"

            sh """
              echo "Building image: ${REGISTRY}/${IMAGE_NAME}:${tag}"
              docker version
              docker build -t ${REGISTRY}/${IMAGE_NAME}:${tag} -t ${REGISTRY}/${IMAGE_NAME}:latest .
            """

            env.IMAGE_TAG = tag
          }
        }
      }
    }

    stage('Push Image to Registry') {
      steps {
        container('docker-cli') {
          sh """
            echo "Pushing images to ${REGISTRY}"
            echo '{ "insecure-registries": ["10.0.2.244:30500"] }' > /etc/docker/daemon.json
            dockerd &
            sleep 5
            docker login http://10.0.2.244:30500 || true
            docker push ${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
            docker push ${REGISTRY}/${IMAGE_NAME}:latest

          """
        }
      }
    }

    stage('Deploy to Kubernetes') {
      steps {
        container('docker-cli') {
          script {
            sh """
              echo "Deploying ${IMAGE_NAME} to Kubernetes..."
              kubectl set image deployment/${IMAGE_NAME} ${IMAGE_NAME}=${REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG} -n default --kubeconfig ~/.kube/config
              kubectl rollout status deployment/${IMAGE_NAME} -n default --timeout=180s
            """
          }
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
