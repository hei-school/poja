# poja
Pay-as-you-go POstgres JAva: no activity then no payment.

The solution is based on AWS serverless: Aurora v1 Postgres-compatible, and Spring Boot deployed as a SAM application.
Thus, bring your own AWS.

## Git Workflow

poja is to be used à-la Gitflow but with only two branches/environments: prod and preprod.
The moment you push in one of these branches, CI/CD will be triggered.

## Requirements

Create first:
- Two subnets. They can be both public or both private or a mix of both, as long as they can communicate between each other. Reference their id in SSM under any name you want.
- A security group that allows HTTP and Postgres traffic. Put its id in SSM under any name you want.
- Two entries in SSM that stores the credentials of the database that will be created. The name MUST be as follows: `/<?app-name>/<?env>/db/username` and `/<?app-name>/<?env>/db/password` where <?app-name>` is any name you want and `<?app-name>` is either `prod` or `preprod`.

> **Warning**
> In case you provide private subnets, remind that the associated NAT Gateway for accessing Internet is __not__ serverless.
> Whether your POJA is used or not, the NAT Gateway will generate a fixed lower cost of around $35 per month.

## Usage
1. Clone this repository.
2. Run `$ ./poja.sh poja-ping eu-west-3 /poja/sg/id /poja/subnet/public1/id /poja/subnet/public2/id` if `poja-ping` is the name of your application, and `eu-west-3` the AWS region you want to deploy into, and the three remaining arguments the SSM parameters where you saved the id of your security group and subnets. We recommend prefixing your poja application names with `poja-`.
3. Commit changes and push them to Github.
4. Define the Github secrets for deploying into your AWS prod and preprod accounts: `PROD_AWS_ACCESS_KEY_ID`, `PROD_AWS_SECRET_ACCESS_KEY`, `PREPROD_AWS_ACCESS_KEY_ID`, and `PREPROD_AWS_SECRET_ACCESS_KEY`. If you use the same account for prod and preprod, just give the same values to the prod and preprod variables.
5. Run the `CD storage` action. This creates the serverless Postgres. The database URL is printed in the Github console.
6. Run the `CD compute` action. This creates the serverless Spring Boot. The API URL is printed in the Github console.
