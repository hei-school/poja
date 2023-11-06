# poja

**Your Java Infrawork. What is infrawork? It's like framework, but extended to the infrastructure running it!**

The Java framework is Spring Boot. The infrastructure on which it runs is AWS.
In the same way Spring Boot enforces a specific way of writing Java applications,
POJA enforces a specific way of running Spring Boot on AWS.

In particular, the whole stack is fully serverless: no activity then no payment.
Spring Boot itself is run on Lambda functions, with no cold start problem thanks to SnapStart.
The persistence is backed by Aurora Postgres v1, with no cold start problem if needed by maintaining it constantly hot through properly defined configuration variable.
An asynchronous stack is also embedded using EventBrige and SQS.

Additionally, POJA generates Github Actions for CI/CD, for health check, for formatting, for releases and for periodic database snapshoting.
More generally, POJA provides a way to operate the Spring Boot framework on AWS on a large scale, at the highest level of quality, and yet very simply through a simple CLI.

POJA stands for POstgres JAva as it only supported the generation of a simple Java Spring Boot connected to a POstgres in the beginning.

## Usage

Run a POJA stack in minutes using the [POJA CLI](https://github.com/hei-school/poja-cli).

## Features

### Git Workflow

poja is to be used à-la Gitflow but with only two branches/environments: prod and preprod.
The moment you push in one of these branches, CI/CD will be triggered.

### Code Quality

* A formatting script is embedded with POJA. Just run `./format.sh` and your whole code will be formatted using Google Java Format. To cite the GJF team: "There is no configurability as to the formatter's algorithm for formatting. This is a deliberate design decision to unify our code formatting on a single format."

* CI will fail whenever code coverage is below 80%. POJA code are not taken into account in the coverage computation, as they are marked by the annotation `@PojaGenerated`. More generally, any class annotated with `@...Generated` will be excluded from coverage computation.

### Asynchronous Tasks

POJA comes with a fully serverless asynchronous stack based on AWS Eventbrige events and on AWS SQS.
Creating a new asynchronous treatment is as simple as generating the corresponding event in `endpoint.event` using the code generation capabilities of Eventbrige,
say [UserCreated](https://github.com/hei-school/poja-base/blob/prod/src/main/java/com/company/base/endpoint/event/gen/UuidCreated.java),
then creating the service that will consume it by adding the `Service` suffix to the event name in `service.event`,
that is [UserCreatedService](https://github.com/hei-school/poja-base/blob/prod/src/main/java/com/company/base/service/event/UuidCreatedService.java) for the given example.

The link between the event and its consumer service is automatically done by POJA through means of reflection.
Note that unlike the synchronous stack, the asynchronous stack does _not_ benefit from SnapStart.
Hence it requires to launch a whole Spring Boot on each cold start.
We consider this acceptable as the treatment is asynchronous anyway.


## Requirements

Create first:
- Two subnets. They MUST be private, and access Internet through a NAT Gateway. Reference their id in SSM under any name you want.
- A security group that allows HTTP and Postgres traffic. Put its id in SSM under any name you want.
- Two entries in SSM that stores the credentials of the database that will be created. The name MUST be as follows: `/<?app-name>/<?env>/db/username` and `/<?app-name>/<?env>/db/password` where <?app-name>` is any name you want and `<?app-name>` is either `prod` or `preprod`.

> **Warning**
> Remind that the NAT Gateway associated to the subnets is __not__ serverless.
> Whether your POJA is used or not, the NAT Gateway will generate a fixed lower cost of around $35 per month.
> If you host 100 POJA in the same VPC, that makes $0.35 the fixed cost per POJA.

## Usage
1. Invoke the [POJA CLI](https://github.com/hei-school/poja-cli). We recommend prefixing your poja application names with `poja-`.
2. Commit changes and push them to Github.
3. Define the Github variable `PROD_DB_CLUSTER_TIMEOUT` that sets the prod database cluster scaling down timeout. Note that its value must be between 300 seconds (5 minutes) and 86_400 seconds (1 day). Due to the once-per-day health check action, the (serverless) prod database will always be hot if you set it to one day.
4. Define the Github secrets for deploying into your AWS prod and preprod accounts: `PROD_AWS_ACCESS_KEY_ID`, `PROD_AWS_SECRET_ACCESS_KEY`, `PREPROD_AWS_ACCESS_KEY_ID`, and `PREPROD_AWS_SECRET_ACCESS_KEY`. If you use the same account for prod and preprod, just give the same values to the prod and preprod variables.
5. Run the `CD storage` action. This creates the serverless Postgres. The database URL is printed in the Github console.
6. Run the `CD compute` action. This creates the serverless Spring Boot. The API URL is printed in the Github console.
