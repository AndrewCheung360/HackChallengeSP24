import boto3

def upload_file(file_name, bucket):
  object_name = file_name
  s3_client = boto3.client('s3')
  response = s3_client.upload_file(file_name, bucket, object_name)
  return response

def download_file(file_name, bucket, id):
  object_name = file_name
  s3_client = boto3.client('s3')
  to_save = "downloads/" + str(id) + ".pdf"
  response = s3_client.download_file(bucket, object_name, to_save)
  return response