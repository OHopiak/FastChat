pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'gradle build'
      }
    }
    stage('test') {
      agent any
      steps {
        sh 'gradle test'
      }
    }
    stage('deploy') {
      agent any
      steps {
        sh 'gradle assemble'
      }
    }
  }
}