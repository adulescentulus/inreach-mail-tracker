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
                AWS_DEFAULT_REGION = 'eu-west-1'
                AWS_ACCESS_KEY_ID = credentials('AWS_KEY_E2C_ID')
                AWS_SECRET_ACCESS_KEY = credentials('AWS_KEY_E2C_SECRET')
                STACK_NAME = 'Inreach-Mail-Tracker'
                S3_BUCKET = 'nc-projects-infrabucket'
            }
            steps {
                s3Upload consoleLogLevel: 'INFO', dontWaitForConcurrentBuildCompletion: false, entries: [[bucket: 'nc-infra-cfn-jenkins-artifacts', excludedFile: '', flatten: false, gzipFiles: false, keepForever: false, managedArtifacts: true, noUploadOnFailure: true, selectedRegion: 'eu-central-1', showDirectlyInBrowser: false, sourceFile: '**/target/*.jar', storageClass: 'STANDARD', uploadFromSlave: false, useServerSideEncryption: false]], pluginFailureResultConstraint: 'FAILURE', profileName: 'ARTIFACTS', userMetadata: []
                sh '''
                    cd cfn
                    chmod +x prepare_template.sh
                    #./prepare_template.sh
                    aws cloudformation package --template aws-resources.yml --s3-bucket $S3_BUCKET --s3-prefix inreach-mail-tracker --output-template template-export.yml
                    aws cloudformation deploy  --template-file=template-export.yml --stack-name="${STACK_NAME}" --parameter-overrides S3MarkersFile=markers.js --capabilities=CAPABILITY_NAMED_IAM
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