# eks-spring-hal-graphql
This repository is for my own educational purposes. 

The purpose is to define an API that serves data through both [HAL](https://spring.io/projects/spring-hateoas) and [GraphQL](https://www.howtographql.com/).

[Auth0](https://auth0.com/) provides authentication and authorization.

[GitHub Actions](https://github.com/features/actions) builds the Docker image. 

[GitHub Packages](https://github.com/features/packages) stores the Docker image.

[Terraform Cloud](https://www.terraform.io/) manages all infrastructure state.

[AWS Elastic Kubernetes Service](https://aws.amazon.com/eks/) runs the kubernetes cluster where the application is deployed.

Steps:
1. Create your [AWS Elastic Kubernetes Service](https://aws.amazon.com/eks/) cluster

2. [Configure access to your Docker registry from Kubernetes](https://kubernetes.io/docs/tasks/configure-pod-container/pull-image-private-registry/)
    - kubectl create secret docker-registry regcred --namespace=<your_namespace> --docker-server=docker.pkg.github.com --docker-username=<your_username> --docker-password=<your_password>

3. Create your version control repository

4. Configure your [Terraform Cloud](https://www.terraform.io/) workspace:
   - create account
   - create workspace
   - link the workspace to version control system repository
   - define variables

5. Push code to the version control repository to trigger a Terraform run which will:
    - create AWS resources
        - db subnet group
        - rds mysql database
    - create Kubernetes resources
        - namespace
        - graphql deployment and service
        - ingress
            
- TODO
    - kubernetes secret for github package access in terraform
    - ssl cert via terraform
    - route53 records via terraform https://www.terraform.io/docs/providers/aws/r/route53_record.html
    - database security group via terraform
    - subnet group via variable or reference to existing subnets
    - document usage of https://auth0.com/docs/extensions/authorization-extension/v2
    - document usage of spring acl
    - document usage of testcontainers
