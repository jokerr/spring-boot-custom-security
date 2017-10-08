node {
    def projectProperties = [
        [$class: 'BuildDiscarderProperty', strategy: [$class: 'LogRotator', numToKeepStr: '5']],
        disableConcurrentBuilds(),
        pipelineTriggers([cron('@daily')])
    ]

    def SUCCESS = hudson.model.Result.SUCCESS.toString()
    currentBuild.result = SUCCESS

    stage 'Setup'
    deleteDir()


    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh "./gradlew clean build"

    stage 'Sonar'

    stage 'Archive'
    archiveArtifacts allowEmptyArchive: true, artifacts: 'build/libs/*.jar', onlyIfSuccessful: true
    junit allowEmptyResults: true, testResults: 'build/test-results/**/*.xml'
}