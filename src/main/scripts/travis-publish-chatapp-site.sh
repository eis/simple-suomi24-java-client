#!/bin/bash
export PROJECT_GIT_ID=simple-suomi24-java-client

if [ "$TRAVIS_JDK_VERSION" == "oraclejdk7" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing...\n"

  echo -n $id_rsa_{00..30} >> ~/.ssh/id_rsa_base64
  base64 --decode --ignore-garbage ~/.ssh/id_rsa_base64 > ~/.ssh/id_rsa
  chmod 600 ~/.ssh/id_rsa
  echo -e "Host github.com\n\tStrictHostKeyChecking no\n" >> ~/.ssh/config
  git clone -b gh-pages --single-branch git@github.com:eis/${PROJECT_GIT_ID}.git ${PROJECT_GIT_ID}-push.docs
  cd ${PROJECT_GIT_ID}-push.docs
  git config user.email "eis+travis-pusher@iki.fi"
  git config user.name "eis-travis-pusher"
  git config push.default matching
  rm -f *.html
  rm -rf cobertura
  rm -rf css
  rm -rf images
  cp -R ../target/site/* .
  git add -u .
  git add .
  git commit -m "'mvn site' for ${PROJECT_NAME} ${PROJECT_VERSION}, build ${TRAVIS_BUILD_NUMBER}"
  git push

  echo -e "Published to gh-pages.\n"
fi
