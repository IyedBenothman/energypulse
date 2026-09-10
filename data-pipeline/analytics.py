import duckdb

result = duckdb.sql("""
    SELECT
        customer_id,
        customer_name,
        total_kwh,
        average_kwh,
        max_kwh,
        CASE
            WHEN average_kwh >= 15 THEN 'HIGH'
            WHEN average_kwh >= 10 THEN 'MEDIUM'
            ELSE 'LOW'
        END AS consumption_level
    FROM 'customer_summary.parquet'
    ORDER BY total_kwh DESC
""")

print(result)