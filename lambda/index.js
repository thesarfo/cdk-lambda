import { SNSClient, PublishCommand } from '@aws-sdk/client-sns';
import { S3Client } from '@aws-sdk/client-s3';

const sns = new SNSClient();
const s3 = new S3Client();

export const handler = async (event) => {
    console.log("Event received:", JSON.stringify(event));

    const bucketName = event.Records[0].s3.bucket.name;
    const objectKey = event.Records[0].s3.object.key;

    const message = `A new file has been uploaded: ${objectKey} to bucket: ${bucketName}`;

    const snsTopicArn = process.env.SNS_TOPIC_ARN;

    const params = {
        Message: message,
        TopicArn: snsTopicArn
    };

    try {
        const command = new PublishCommand(params);
        await sns.send(command);
        console.log("Notification sent successfully.");
    } catch (error) {
        console.error("Error sending notification:", error);
        throw error;
    }
};
