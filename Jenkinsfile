pipeline {
  agent {
    node {
      label 'gradle'
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
        sh 'gradle check jacocoTestReport'
        junit(testResults: '**/build/test-results/**/*.xml', allowEmptyResults: true)
        jacoco(execPattern: '**/build/**.exec')
      }
    }
    stage('deploy') {
      steps {
        sh 'gradle build'
        archiveArtifacts '**/build/libs/*.jar'
        sh './scripts/deploy'
      }
    }
  }
}