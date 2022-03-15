
node {
    stage('checkout') {
        checkout scm
    }
    stage('check java') {
            pwsh "java -version"
            }

            stage('clean') {
                pwsh "./gradlew clean --no-daemon"
            }
            stage('nohttp') {
                pwsh "./gradlew checkstyleNohttp --no-daemon --stacktrace"
            }

            stage('npm install') {
                pwsh "./gradlew npm_install -PnodeInstall --no-daemon"
            }
            stage('backend tests') {
                try {
                    pwsh "./gradlew test integrationTest -PnodeInstall --no-daemon"
                } catch(err) {
                    throw err
                } finally {
                    junit '**/build/**/TEST-*.xml'
                }
            }

            stage('frontend tests') {
                try {
                    pwsh "./gradlew npm_run_test -PnodeInstall --no-daemon"
                } catch(err) {
                    throw err
                } finally {
                    junit '**/build/test-results/TESTS-*.xml'
                }
            }

            stage('packaging') {
                pwsh "./gradlew bootJar -x test -Pprod -PnodeInstall --no-daemon"
                archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
            }

            stage('quality analysis') {
                withSonarQubeEnv('govpaysecuredsonar') {
                    pwsh "./gradlew sonarqube --no-daemon"
                }
            }
    
}
