
node {
    stage('checkout') {
        checkout scm
    }
    stage('check java') {
            sh "java -version"
            }

            stage('clean') {
                sh "chmod +x gradlew"
                sh "./gradlew clean --no-daemon"
            }
            stage('nohttp') {
                sh "./gradlew checkstyleNohttp --no-daemon --stacktrace"
            }

            stage('npm install') {
                sh "npm cache verify"
                sh "npm cache clean --force"
                sh "./gradlew npm_install -PnodeInstall --no-daemon"
            }
            stage('backend tests') {
                try {
                    sh "./gradlew test integrationTest -PnodeInstall --no-daemon"
                } catch(err) {
                    throw err
                } finally {
                    junit '**/build/**/TEST-*.xml'
                }
            }

            stage('frontend tests') {
                try {
                    sh "./gradlew npm_run_test -PnodeInstall --no-daemon"
                } catch(err) {
                    throw err
                } finally {
                    junit '**/build/test-results/TESTS-*.xml'
                }
            }

            stage('packaging') {
                sh "./gradlew bootJar -x test -Pprod -PnodeInstall --no-daemon"
                archiveArtifacts artifacts: '**/build/libs/*.jar', fingerprint: true
            }

            stage('quality analysis') {
                withSonarQubeEnv('govpaysecuredsonar') {
                    sh "./gradlew sonarqube --no-daemon"
                }
            }
    
}
