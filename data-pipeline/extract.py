import os
import psycopg
import pandas as pd

connection = psycopg.connect(
    host="localhost",
    port=5432,
    dbname="energypulse",
    user="energypulse_user",
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

print(df)

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

print("\nCustomer summary:")
print(summary)

cursor.close()
connection.close()

summary.to_parquet("customer_summary.parquet", index=False)

print("\nSaved transformed data to customer_summary.parquet")

loaded_summary = pd.read_parquet("customer_summary.parquet")

print("\nData read back from Parquet:")
print(loaded_summary)