# props

## Local

```bash
env MAVEN_OPTS="--add-modules java.xml.bind" mvn exec:java -Dexec.mainClass=example.props.Props
[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ props ---
java.awt.graphicsenv=sun.awt.CGraphicsEnvironment|java.awt.printerjob=sun.lwawt.macosx.CPrinterJob|java.class.path=/Users/yujiorama/.sdkman/candidates/maven/current/boot/plexus-classworlds-2.5.2.jar|java.class.version=54.0|java.home=/Users/yujiorama/.sdkman/candidates/java/10.0.1-zulu|java.io.tmpdir=/var/folders/_p/wsr1n1f566l54j7528b8h0k80000gn/T/|java.library.path=/Users/yujiorama/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:.|java.runtime.name=OpenJDK Runtime Environment|java.runtime.version=10.0.1+9|java.specification.name=Java Platform API Specification|java.specification.vendor=Oracle Corporation|java.specification.version=10|java.vendor.url.bug=http://www.azulsystems.com/support/|java.vendor.url=http://www.azulsystems.com/|java.vendor.version=Zulu10.2+3|java.vendor=Azul Systems, Inc.|java.version.date=2018-04-17|java.version=10.0.1|java.vm.compressedOopsMode=Zero based|java.vm.info=mixed mode|java.vm.name=OpenJDK 64-Bit Server VM|java.vm.specification.name=Java Virtual Machine Specification|java.vm.specification.vendor=Oracle Corporation|java.vm.specification.version=10|java.vm.vendor=Azul Systems, Inc.|java.vm.version=10.0.1+9
```

## AWS Lambda

https://docs.aws.amazon.com/ja_jp/lambda/latest/dg/java-create-jar-pkg-maven-no-ide.html

```bash
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
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

## Azure Function

https://docs.microsoft.com/ja-jp/azure/azure-functions/functions-create-first-java-maven

```bash
brew tap azure/function
brew install azure-cli azure-functions-core-tools
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
env MAVEN_OPTS="--add-modules java.xml.bind" mvn clean package
env MAVEN_OPTS="--add-modules java.xml.bind" mvn azure-functions:deploy
[INFO] Authenticate with Azure CLI 2.0
[INFO] Updating the specified function app...
[INFO] Successfully updated the function app.props-20180922141301620
[INFO] Trying to deploy the function app...
[INFO] Successfully deployed the function app at https://props-20180922141301620.azurewebsites.net
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 45.674 s
[INFO] Finished at: 2018-09-22T15:44:58+09:00
[INFO] ------------------------------------------------------------------------

curl https://props-20180922141301620.azurewebsites.net/api/PropsFunction
https://portal.azure.com/
```
