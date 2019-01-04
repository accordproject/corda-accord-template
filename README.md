![Corda](https://www.corda.net/wp-content/uploads/2016/11/fg005_corda_b.png)

# Corda + Accord Project Template

This repo contains an MVP integration between Corda and the Accord Project. The goal of this application is to demonstrate how a contract may be parsed using a Cicero and the issued as a Corda State onto the ledger.

This repository is divided into two parts: a Java reference solution (which is complete), and a Kotlin reference solutions (which is still a WIP). 

# Setup

### Tools 
* JDK 1.8 latest version
* IntelliJ latest version (2017.1 or newer)
* git

After installing the required tools, clone or download a zip of this repository, and place it in your desired 
location.

### IntelliJ setup
* From the main menu, click `open` (not `import`!) then navigate to where you placed this repository.
* Click `File->Project Structure`, and set the `Project SDK` to be the JDK you downloaded (by clicking `new` and 
nagivating to where the JDK was installed). Click `Okay`.
* Next, click `import` on the `Import Gradle Project` popup, leaving all options as they are. 
* If you do not see the popup: Navigate back to `Project Structure->Modules`, clicking the `+ -> Import` button,
navigate to and select the repository folder, select `Gradle` from the next menu, and finally click `Okay`, 
again leaving all options as they are.

# Instructions
* In this repo, a Java object has been precompiled from Cicero based on the promissory note example.
* No additional setup is required. To parse a state and issue a Promissory note on the ledger see either `Running The Tests` or `Deploying Locally`.

### Running the test
* Java: Select `Java - Unit tests` from the dropdown run configuration menu, and click the green play button.
* Note - there is currently only one test, which demonstrates the functionality. Other tests are a carry-over from another repo that must be adapted.
* Kotlin: N/A, still a WIP.
* Individual tests can be run by clicking the green arrow in the line number column next to each test.
* When running flow tests you must add the following to your run / debug configuration in the VM options field. This enables us to use
* Quasar - a library that provides high-performance, lightweight threads.
* "-javaagent: /PATH_TO_FILE_FROM_ROOT_DIR/quasar.jar"

# Template Files

### Accord
* Promissory Note Template: `java-source/src/main/java/AccordProject/cicero-template-library/src/promissory-note`
* Promissory Note Java Object & Associated Classes: `java-source/src/main/java/org`
* Cicero-Parse Shell Script: `java-source/src/main/resources/cicero-parse.sh`

### Java
State:

* Template: `java-source/src/main/java/net/corda/training/state/PromissoryNoteState.java`
* Tests: `java-source/src/test/java/net/corda/training/state/PromissoryNoteStateTests.java`

Contract:

* Template: `java-source/src/main/java/net/corda/training/contract/PromissoryNoteContract.java`
* Issue Tests: `java-source/src/test/java/net/corda/training/contract/PromissoryNoteIssueTests.java`
* Transfer Tests: `java-source/src/test/java/net/corda/training/contract/PromissoryNoteIssueTests.java`
* Settle Tests: `java-source/src/test/java/net/corda/training/contract/PromissoryNoteIssueTests.java`

Flow:

* Issue template: `java-source/src/main/java/net/corda/training/flow/PromissoryNoteIssueFlow.java`
* Issue tests: `java-source/src/test/java/net/corda/training/flow/PromissoryNoteIssueFlowTests.java`
* Transfer template: `java-source/src/main/java/net/corda/training/flow/PromissoryNoteTransferFlow.java`
* Transfer tests: `java-source/src/test/java/net/corda/training/flow/PromissoryNoteTransferFlowTests.java`
* Settle template: `java-source/src/main/java/net/corda/training/flow/PromissoryNoteSettleFlow.java`
* Settle tests: `java-source/src/test/java/net/corda/training/flow/PromissoryNoteSettleFlowTests.java`

# Running the CorDapp
Once your application passes all tests in `PromissoryNoteStateTests`, `PromissoryNoteIssueTests`, and `PromissoryNoteIssueFlowTests`, you can run the application and 
interact with it via a web browser. To run the finished application, you have two choices for each language: from the terminal, and from IntelliJ.