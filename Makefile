b: buildw
clean:
	rm -r image-train-filters-app/build &
buildw: clean
	./gradlew clean build test jacocoTestReport -i
	./gradlew -i
debug: clean
	./gradlew clean build test jacocoTestReport -i --stacktrace --debug
	./gradlew --stacktrace --debug
install-jacococli:
	wget https://search.maven.org/remotecontent\?filepath\=org/jacoco/jacoco/0.8.7/jacoco-0.8.7.zip
	unzip remotecontent\?filepath=org%2Fjacoco%2Fjacoco%2F0.8.7%2Fjacoco-0.8.7.zip
upgrade:
	gradle wrapper --gradle-version 8.0.1
coverage:
	./gradlew clean build test jacocoTestReport
	./gradlew -i
