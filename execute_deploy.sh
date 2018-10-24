#!/bin/bash -e
cd cfn
chmod +x prepare_template.sh
#./prepare_template.sh
aws cloudformation package --template aws-resources.yml --s3-bucket ${S3_BUCKET_NAME} --output-template template-export.yml
aws cloudformation deploy  --template-file=template-export.yml --stack-name="${STACK_NAME}" --parameter-overrides ParamLambda=${STACK_PARAM} --capabilities=CAPABILITY_NAMED_IAM
