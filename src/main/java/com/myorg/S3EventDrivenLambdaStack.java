package com.myorg;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.notifications.LambdaDestination;
import software.amazon.awscdk.services.sns.Topic;
import software.amazon.awscdk.services.sns.subscriptions.EmailSubscription;
import software.constructs.Construct;

import java.util.Collections;

public class S3EventDrivenLambdaStack extends Stack {
    public S3EventDrivenLambdaStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public S3EventDrivenLambdaStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Bucket s3Bucket = Bucket.Builder.create(this, "eda-bucket")
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        Topic snsTopic = Topic.Builder.create(this, "eda-topic").build();

        Function lambdaFunction = Function.Builder.create(this, "eda-function")
                .runtime(Runtime.NODEJS_20_X)
                .handler("index.handler")
                .code(Code.fromAsset("lambda"))
                .environment(Collections.singletonMap("SNS_TOPIC_ARN", snsTopic.getTopicArn()))
                .build();

        snsTopic.grantPublish(lambdaFunction);

        snsTopic.addSubscription(new EmailSubscription("ernestsarfo100@gmail.com"));

        lambdaFunction.addToRolePolicy(PolicyStatement.Builder.create()
                .actions(Collections.singletonList("s3:GetObject"))
                .resources(Collections.singletonList(s3Bucket.getBucketArn() + "/*"))
                .build());

        s3Bucket.addEventNotification(
                software.amazon.awscdk.services.s3.EventType.OBJECT_CREATED_PUT,
                new LambdaDestination(lambdaFunction)
        );
    }
}
