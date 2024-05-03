import boto3
from dotenv import load_dotenv, find_dotenv, dotenv_values 
import os

load_dotenv(find_dotenv())

ACCESS_ID = os.environ.get("ACCESS_ID")
ACCESS_KEY = os.environ.get("ACCESS_KEY")

def upload_file(file_name, bucket):
  object_name = file_name
  s3_client = boto3.client(
    's3',
    aws_access_key_id=ACCESS_ID,
    aws_secret_access_key=ACCESS_KEY
  )
  response = s3_client.upload_file(file_name, bucket, object_name)
  return response

def download_file(file_name, bucket, id):
  object_name = file_name
  s3_client = boto3.client(
    's3',
    aws_access_key_id="AKIAZQ3DQI4NOZOGJVN6",
    aws_secret_access_key="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"
  )
  to_save = "downloads/" + str(id) + ".pdf"
  response = s3_client.download_file(bucket, object_name, to_save)
  return response