from extract import run_extract
from upload_to_s3 import upload_summary


def run_pipeline():
    print("Starting EnergyPulse data pipeline...")

    run_extract()
    upload_summary()

    print("Pipeline completed successfully!")


if __name__ == "__main__":
    run_pipeline()