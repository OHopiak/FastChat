pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'gradle assemble'
      }
    }
    stage('test') {
      steps {
        sh 'gradle test'
      }
    }
    stage('deploy') {
      steps {
        sh 'gradle build'
        archiveArtifacts '**/build/libs/*.jar'
      }
    }
  }
}