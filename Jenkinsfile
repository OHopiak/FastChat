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
        junit(testResults: '**/build/test-results/**/*.xml', allowEmptyResults: true)
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