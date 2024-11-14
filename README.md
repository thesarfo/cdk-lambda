This project demonstrates an event-driven architecture on AWS using AWS CDK. It triggers an AWS Lambda function in response to an S3 `PutObject` event, which then sends an email notification through Amazon SNS.

### Architecture Overview
- **S3 Bucket**: Stores files, triggers Lambda on `PutObject` event.
- **Lambda**: Executes logic upon file upload and sends an email.
- **SNS**: Sends an email notification to subscribed email addresses.

The CDK stack itself is written in Java(17) but the lambda function is written in Javascript(Node 20 Runtime).

---

Just head over to the `lambda` directory, run `npm install` - and then you can edit unique parameters in the stack(like emails) to your liking.


Then, from the root of the project, run:

```bash
cdk deploy
```

This command will deploy the CloudFormation stack with all the resources defined in the CDK application, including:

- An S3 bucket
- A Lambda function
- An SNS topic and subscription (email)


If you no longer need the resources, you can clean up by destroying the stack:

```bash
cdk destroy
```