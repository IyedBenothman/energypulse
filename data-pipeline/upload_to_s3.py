import boto3
from datetime import datetime, timezone

s3 = boto3.client("s3")

bucket_name = "energypulse-data-iyed-2026"
file_name = "customer_summary.parquet"

extraction_date = datetime.now(timezone.utc).date().isoformat()

object_key = (
    f"customer-summaries/"
    f"extraction_date={extraction_date}/"
    f"customer_summary.parquet"
)

s3.upload_file(
    file_name,
    bucket_name,
    object_key
)

print("Uploaded customer_summary.parquet to S3 successfully!")