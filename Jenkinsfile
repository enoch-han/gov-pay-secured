
node {
    stage('checkout') {
        checkout scm
    }
    stage('check java') {
        bat "java -version"
    }

    stage('clean') {
        bat "./gradlew clean --no-daemon"
    }
    stage('nohttp') {
        bat "./gradlew checkstyleNohttp --no-daemon --stacktrace"
    }

    stage('npm install') {
        bat "npm cache verify"
        bat "./gradlew npm_install -PnodeInstall --no-daemon"
    }
    stage('backend tests') {
        try {
            bat "./gradlew test integrationTest -PnodeInstall --no-daemon"
        } catch(err) {
            throw err
        } finally {
            junit '**/build/**/TEST-*.xml'
        }
    }

    stage('frontend tests') {
        try {
            bat "./gradlew npm_run_test -PnodeInstall --no-daemon"
        } catch(err) {
            throw err
        } finally {
            junit '**/build/test-results/TESTS-*.xml'
        }
    }

    stage('packaging') {
        bat "./gradlew bootJar -x test -Pprod -PnodeInstall --no-daemon"
        archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
    }

    stage('quality analysis') {
        bat "./gradlew -Pprod clean check jacocoTestReport sonarqube -Dsonar.host.url=http://localhost:9001"
    }
    
}
