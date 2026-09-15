import os
import psycopg
import pandas as pd

def run_extract():
    connection = psycopg.connect(
        host=os.getenv("DB_HOST", "localhost"),
        port=os.getenv("DB_PORT", "5432"),
        dbname=os.getenv("DB_NAME", "energypulse"),
        user=os.getenv("DB_USER", "energypulse_user"),
        password=os.environ["DB_PASSWORD"]
    )

    query = """
    SELECT
        mr.id,
        mr.customer_id,
        c.name AS customer_name,
        mr.meter_id,
        mr.consumption_kwh
    FROM meter_reading mr
    LEFT JOIN customer c
        ON mr.customer_id = c.customer_id
    ORDER BY mr.id;
    """

    cursor = connection.cursor()
    cursor.execute(query)

    rows = cursor.fetchall()
    columns = [description.name for description in cursor.description]

    df = pd.DataFrame(rows, columns=columns)

    summary = (
        df.groupby("customer_id")
        .agg(
            customer_name=("customer_name", "first"),
            total_kwh=("consumption_kwh", "sum"),
            average_kwh=("consumption_kwh", "mean"),
            max_kwh=("consumption_kwh", "max")
        )
        .reset_index()
    )

    summary.to_parquet("customer_summary.parquet", index=False)

    cursor.close()
    connection.close()

    print("Extract and transform completed successfully!")

if __name__ == "__main__":
    run_extract()