# See also
#  - https://bmuschko.github.io/gradle-docker-plugin/current/user-guide/#spring_boot_application_plugin
#
# ./gradlew dockerBuildImage


cd app-order-mgmt
docker build -f ./Dockerfile -t belaboros/eda-ecommerce-order-mgmt .

