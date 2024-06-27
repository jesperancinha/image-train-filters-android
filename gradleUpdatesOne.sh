#!/usr/bin/env bash

latestGradle=$(curl -s https://services.gradle.org/versions/current | jq -r '.version')

if [[ -n $latestGradle ]]; then
  #  YML pipeline files and more - Version
  echo "Scanning for yml files including GitHub action files..."
  for f in $(find . -name "*.yml"); do
      sed -E "s/gradle-version\: [0-9\.[a-z]*/gradle-version: $latestGradle/g" "$f" > "$f""01"
      mv "$f""01" "$f"
  done
#  ./gradlew build
else
  echo "Unable to read latest Gradle version!"
fi
