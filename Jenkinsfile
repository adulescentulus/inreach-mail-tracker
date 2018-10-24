pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '15'))
    }
    environment {
        WHOAMI = sh(returnStdout: true, script: 'echo -n `id -u`:`id -g`')
    }
    stages {
    	stage ('Initialize') {
            agent {
                docker { 
                    image 'alpine'
                    reuseNode true 
                    args '-u root -v mvn_repo:/tmp/.m2'
                }
            }
            steps {
                sh "chown -R ${WHOAMI} /tmp/.m2"
            }
        }
        stage('Build') {
            agent {
                docker { 
                    image 'maven:3.5.3-jdk-8-alpine'
                    reuseNode true
                    args '-v mvn_repo:/tmp/.m2 -e MAVEN_CONFIG=/tmp/.m2'
                }
            }
            steps {
                sh '''
                    mvn package
                    '''
            }
        }
        stage('Deploy') {
            agent {
                docker { 
                    image 'grolland/aws-cli'
                    reuseNode true 
                }
            }
            environment {
                AWS_DEFAULT_REGION = 'eu-central-1'
                AWS_ACCESS_KEY_ID = credentials('AWS_KEY_DUMMYID')
                AWS_SECRET_ACCESS_KEY = credentials('AWS_KEY_DUMMY_KEY')
                STACK_NAME = 'example-stack-name'
                S3_BUCKET = 'example-bucket'
            }
            steps {
                s3Upload consoleLogLevel: 'INFO', dontWaitForConcurrentBuildCompletion: false, entries: [[bucket: 'nc-infra-cfn-jenkins-artifacts', excludedFile: '', flatten: false, gzipFiles: false, keepForever: false, managedArtifacts: true, noUploadOnFailure: true, selectedRegion: 'eu-central-1', showDirectlyInBrowser: false, sourceFile: '**/target/*.jar', storageClass: 'STANDARD', uploadFromSlave: false, useServerSideEncryption: false]], pluginFailureResultConstraint: 'FAILURE', profileName: 'ARTIFACTS', userMetadata: []
                sh '''
                    chmod +x execute_deploy.sh
                    ./execute_deploy.sh
                    '''
            }
        }
    }
    post { 
        always { 
            cleanWs()
        }
    }
}