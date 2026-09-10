import duckdb

con = duckdb.connect()

con.execute("""
    INSTALL aws;
    LOAD aws;

    CREATE OR REPLACE SECRET aws_secret (
        TYPE s3,
        PROVIDER credential_chain,
        CHAIN 'process',
        PROFILE 'duckdb',
        REGION 'eu-north-1'
    );
""")

result = con.sql("""
    SELECT
        customer_id,
        customer_name,
        total_kwh,
        average_kwh,
        max_kwh,
        extraction_date
    FROM 's3://energypulse-data-iyed-2026/customer-summaries/extraction_date=*/customer_summary.parquet'
    WHERE extraction_date = DATE '2026-09-10'
""")

print(result)