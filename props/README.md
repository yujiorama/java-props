# props

## Setup process

```bash
mvn package
sam local invoke PropsFunction
aws s3 mb s3://lambda-function-props
sam package \
    --template-file template.yaml \
    --output-template-file packaged.yaml \
    --s3-bucket lambda-function-props
stack_name="lambda-function-props"
sam deploy \
    --template-file packaged.yaml \
    --stack-name "${stack_name}" \
    --capabilities CAPABILITY_IAM
aws cloudformation describe-stacks \
    --stack-name "${stack_name}" \
    --query 'Stacks[].Outputs'
function_name=$(aws lambda list-functions | jq --arg stack_name "${stack_name}" -r '.Functions[] | .FunctionName | select(contains($stack_name)) | .')
aws lambda invoke --function-name "${function_name}" /dev/stdout
"java.awt.graphicsenv=sun.awt.X11GraphicsEnvironment|java.awt.printerjob=sun.print.PSPrinterJob|java.class.path=/var/runtime/lib/LambdaJavaRTEntry-1.0.jar|java.class.version=52.0|java.endorsed.dirs=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.141-1.b16.32.amzn1.x86_64/jre/lib/endorsed|java.ext.dirs=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.141-1.b16.32.amzn1.x86_64/jre/lib/ext:/usr/java/packages/lib/ext|java.home=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.141-1.b16.32.amzn1.x86_64/jre|java.io.tmpdir=/tmp|java.library.path=/lib64:/usr/lib64:/var/runtime:/var/runtime/lib:/var/task:/var/task/lib:/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib|java.net.preferIPv4Stack=true|java.runtime.name=OpenJDK Runtime Environment|java.runtime.version=1.8.0_141-b16|java.specification.name=Java Platform API Specification|java.specification.vendor=Oracle Corporation|java.specification.version=1.8|java.vendor.url.bug=http://bugreport.sun.com/bugreport/|java.vendor.url=http://java.oracle.com/|java.vendor=Oracle Corporation|java.version=1.8.0_141|java.vm.info=mixed mode, sharing|java.vm.name=OpenJDK 64-Bit Server VM|java.vm.specification.name=Java Virtual Machine Specification|java.vm.specification.vendor=Oracle Corporation|java.vm.specification.version=1.8|java.vm.vendor=Oracle Corporation|java.vm.version=25.141-b16"{
    "StatusCode": 200,
    "ExecutedVersion": "$LATEST"
}
aws cloudformation delete-stack --stack-name "${stack_name}"
```
