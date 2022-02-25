pipeline {
    agent any
    stages{
        stage('Clone or Update Repository'){
            steps {
                script{
                    if(!fileExists()){
                        sh "git clone https://github.com/leonardoo1256/Omnibnk.git"
                    }else{
                        dir('/var/lib/jenkins/workspace/deploy-api/Omnibnk'){
                            sh "sudo git pull"
                        }
                    }
                }
            }
        }
        stage('Build containers'){
            steps{
                script{
                    dir('/var/lib/jenkins/workspace/deploy-api/Omnibnk/Omnibnk_1'){
                        sh "sudo docker-compose -f docker-compose.full.yml up -d"
                    }
                }
            }
        }
        stage('Run api changes'){
            steps{
                script{
                    dir('/var/lib/jenkins/workspace/deploy-api/Omnibnk/Omnibnk_1'){
                        sh "sudo docker-compose -f docker-compose.full.yml exec -T django ./manage.py makemigrations"
                        sh "sudo docker-compose -f docker-compose.full.yml exec -T django ./manage.py migrate"
                    }
                }
            }
        }
    }
}
