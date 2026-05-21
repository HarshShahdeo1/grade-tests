pipeline {
    agent any

    environment {
        RUNNER_IMAGE = 'csv302lpu/grade-runner:v1'
    }

    stages {
        stage('Checkout') {
            steps {
                // This automatically clones your GitHub repo securely into the Jenkins workspace
                checkout scm
            }
        }

        stage('Pull Image') {
            steps {
                bat "docker inspect %RUNNER_IMAGE% || echo Image verified locally"
            }
        }

        stage('Run Tests') {
            steps {
                // Ensure target directory structure exists before mounting
                bat "if not exist target\\surefire-reports mkdir target\\surefire-reports"

                // Run the containerized tests
                bat "docker run --rm -v %WORKSPACE%:/app -v %WORKSPACE%/target/surefire-reports:/app/target/surefire-reports -w /app %RUNNER_IMAGE% mvn test"
            }
        }

        stage('Publish Results') {
            steps {
                junit "target/surefire-reports/*.xml"
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: "target/surefire-reports/*", allowEmptyArchive: true
        }
        success {
            echo "SUCCESS: Build passed and all test assertions met successfully!"
        }
        failure {
            echo "FAILURE: Build failed due to compilation errors or test assertion mismatches."
        }
    }
}