#!/usr/bin/env bash

for x in {1..10};do curl --header "Content-Type: text/xml;charset=UTF-8" --header "x-flow-id: test" --data @triggerMasterdata.xml http://z-devsn02.zalando:37001/ws/articleEventTriggerWebService >> eans_response; echo "" >> eans_response; done;
