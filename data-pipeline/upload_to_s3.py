import boto3

s3 = boto3.client("s3")

bucket_name = "energypulse-data-iyed-2026"
file_name = "customer_summary.parquet"
object_key = "customer-summaries/customer_summary.parquet"

s3.upload_file(
    file_name,
    bucket_name,
    object_key
)

print("Uploaded customer_summary.parquet to S3 successfully!")