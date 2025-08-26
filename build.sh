#!/bin/bash

_TAG=${1:local}

mvn versions:set -DnewVersion=$_TAG

mvn clean install

mvn versions:revert
