pipeline {
    agent any

    environment {
        // Defines the pipeline-level environment variables
        RUNNER_IMAGE = 'csv302lpu/grade-runner:v1'
        REPORT_DIR   = 'target/surefire-reports'
    }

    stages {
        stage('Checkout') {
            steps {
                // Stage 1: Retrieve source code from SCM
                checkout scm
            }
        }

        stage('Pull Image') {
            steps {
                // Stage 2: Pull/Verify the local runner image using the env variable
                sh "docker inspect ${RUNNER_IMAGE} || docker pull ${RUNNER_IMAGE}"
            }
        }

        stage('Run Tests') {
            steps {
                // Stage 3: Execute tests inside the container with dual mounts
                // ${WORKSPACE} dynamically references the Jenkins build directory
                sh "docker run --rm -v ${WORKSPACE}:/app -v ${WORKSPACE}/${REPORT_DIR}:/app/target/surefire-reports -w /app ${RUNNER_IMAGE} mvn test"
            }
        }

        stage('Publish Results') {
            steps {
                // Stage 4: Use the junit step to publish parsed test results
                junit "${REPORT_DIR}/*.xml"
            }
        }
    }

    post {
        always {
            // Archive the reports as permanent build artifacts
            archiveArtifacts artifacts: "${REPORT_DIR}/*", allowEmptyArchive: true
        }
        success {
            echo "SUCCESS: Build passed and all test assertions met successfully!"
        }
        failure {
            echo "FAILURE: Build failed due to compilation errors or test assertion mismatches."
        }
    }
}