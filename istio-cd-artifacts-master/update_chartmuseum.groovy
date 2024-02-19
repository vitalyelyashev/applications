    slave = "cr-dev-hp-1-us-east1-jenkins-2"


pipeline {
    triggers{
        pollSCM('* * * * *') //Polling SCM every minute, change first Asterix to H/2 to poll every 2 minutes
    }
    agent {node { label "${slave}" } }
    stages {
        stage('Update DEV by first time'){
            when {
                allOf {
                    branch "feature-*"
                    equals(actual: currentBuild.number, expected: 1) }
            }
            steps{
                sh """
                cd helm/
                #Uncomment line below to push to chartmuseum
                helm cm-push istio-operator/ chartmuseum-dev-gcp
                echo 'istio-operator was changed and pushed to Chartmuseum-Dev-GCP'

                #Uncomment line below to push to chartmuseum
                helm cm-push istio-resources/ chartmuseum-dev-gcp
                echo 'istio-resources was changed and pushed to Chartmuseum-Dev-GCP'

                echo 'Pushing kiali-resources chart to dev chartmuseum'
                #Uncomment line below to push to chartmuseum
                helm cm-push kiali-resources/ chartmuseum-dev-gcp
                echo 'kiali-resources was changed and pushed to Chartmuseum-Dev-GCP'
                """
                cleanWs()
            }
        }
        stage('Update DEV'){
            when {
                allOf {
                    branch "feature-*"
                    changeset "**/*.yaml" }
            }
            steps{
                sh """
                cd helm/
                #Uncomment line below to push to chartmuseum
                helm cm-push istio-operator/ chartmuseum-dev-gcp
                echo 'istio-operator was changed and pushed to Chartmuseum-Dev-GCP'

                #Uncomment line below to push to chartmuseum
                helm cm-push istio-resources/ chartmuseum-dev-gcp
                echo 'istio-resources was changed and pushed to Chartmuseum-Dev-GCP'

                echo 'Pushing kiali-resources chart to dev chartmuseum'
                #Uncomment line below to push to chartmuseum
                helm cm-push kiali-resources/ chartmuseum-dev-gcp
                echo 'kiali-resources was changed and pushed to Chartmuseum-Dev-GCP'
                """
                cleanWs()
            }
        }
        stage('Update PROD'){
            when { branch "master" }
            steps{
                sh """
                cd helm/
                #Uncomment line below to push to chartmuseum
                helm cm-push istio-operator/ chartmuseum
                echo 'istio-operator was changed and pushed to Chartmuseum-Prod'

                #Uncomment line below to push to chartmuseum
                helm cm-push istio-resources/ chartmuseum
                echo 'istio-resources was changed and pushed to Chartmuseum-Prod'

                echo 'Pushing kiali-resources chart to chartmuseum'
                #Uncomment line below to push to chartmuseum
                helm cm-push kiali-resources/ chartmuseum
                echo 'kiali-resources was changed and pushed to Chartmuseum-Prod'
                """
                cleanWs()
            }
        }
    }
}