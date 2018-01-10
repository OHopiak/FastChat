pipeline {
  agent {
    dockerfile {
      filename './deploy/Dockerfile'
    }
    
  }
  stages {
    stage('build') {
      steps {
        sh 'gradle assemble'
      }
    }
    stage('test') {
      steps {
        sh 'gradle check jacoco'
        junit(testResults: '**/build/test-results/**/*.xml', allowEmptyResults: true)
        jacoco(execPattern: '**/build/**.exec')
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